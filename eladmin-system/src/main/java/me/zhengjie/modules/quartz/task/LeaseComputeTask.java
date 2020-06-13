package me.zhengjie.modules.quartz.task;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.business.lease.domain.LeaseMaterials;
import me.zhengjie.business.lease.domain.LeaseProject;
import me.zhengjie.business.lease.domain.LeaseProjectDetail;
import me.zhengjie.business.lease.service.LeaseMaterialsService;
import me.zhengjie.business.lease.service.LeaseProjectDetailService;
import me.zhengjie.business.lease.service.LeaseProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算租赁费用
 */
@Slf4j
@Component
public class LeaseComputeTask {

    @Autowired
    private LeaseProjectService leaseProjectService;

    @Autowired
    private LeaseProjectDetailService leaseProjectDetailService;

    @Autowired
    private LeaseMaterialsService leaseMaterialsService;

    public void run(){
        // 获取项目列表
        List<LeaseProject> leaseProjectList = leaseProjectService.findByStatus("PROCESS");

        // 获取项目详情处理成map
        List<LeaseProjectDetail> leaseProjectDetailAll = leaseProjectDetailService.findByStatus("PROCESS");
        Map<Long, List<LeaseProjectDetail>> leaseProjectDetailMap = new HashMap<>();
        for (LeaseProjectDetail leaseProjectDetail : leaseProjectDetailAll) {
            List<LeaseProjectDetail> list = new ArrayList<>();
            if (leaseProjectDetailMap.containsKey(leaseProjectDetail.getLeaseProjectId())) {
                list = leaseProjectDetailMap.get(leaseProjectDetail.getLeaseProjectId());
            }
            list.add(leaseProjectDetail);
            leaseProjectDetailMap.put(leaseProjectDetail.getLeaseProjectId(), list);
        }

        // 获取物料信息处理成map
        List<LeaseMaterials> leaseMaterials = leaseMaterialsService.findAll();
        Map<Long, LeaseMaterials> leaseMaterialsMap = new HashMap<>();
        for (LeaseMaterials leaseMaterial : leaseMaterials) {
            leaseMaterialsMap.put(leaseMaterial.getId(), leaseMaterial);
        }

        for (LeaseProject leaseProject : leaseProjectList) {
            // 获取项目租赁详情
            BigDecimal totalPrice = BigDecimal.ZERO;
            List<LeaseProjectDetail> leaseProjectDetails = leaseProjectDetailMap.get(leaseProject.getId());
            // 计算价格
            for (LeaseProjectDetail leaseProjectDetail : leaseProjectDetails) {
                // 获取物料并计算价格
                BigDecimal todayPrice;
                LeaseMaterials materials = leaseMaterialsMap.get(leaseProjectDetail.getId());
                if (materials != null) {
                    // 单价 * 数量
                    todayPrice = leaseProjectDetail.getNumber().multiply(materials.getPrice());
                } else {
                    todayPrice = BigDecimal.ZERO;
                }

                // 价格相加
                BigDecimal nowMoney = leaseProjectDetail.getNowMoney() == null ? BigDecimal.ZERO : leaseProjectDetail.getNowMoney();
                leaseProjectDetail.setNowMoney(nowMoney.add(todayPrice));

                // 计算项目总费用
                totalPrice = leaseProjectDetail.getNowMoney().add(totalPrice);
            }
            // 保存数据
            leaseProjectDetailService.saveAll(leaseProjectDetails);

            // 设置总费用
            leaseProject.setTotalMoney(totalPrice);

            log.info("[{}] 项目总费用：[{}]", leaseProject.getName(), leaseProject.getTotalMoney());
        }

        // 保存数据
        leaseProjectService.saveAll(leaseProjectList);


    }

}
