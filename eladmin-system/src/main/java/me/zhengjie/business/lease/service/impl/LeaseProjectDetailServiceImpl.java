/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.business.lease.service.impl;

import me.zhengjie.business.lease.domain.LeaseProjectDetail;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.business.lease.repository.LeaseProjectDetailRepository;
import me.zhengjie.business.lease.service.LeaseProjectDetailService;
import me.zhengjie.business.lease.service.dto.LeaseProjectDetailDto;
import me.zhengjie.business.lease.service.dto.LeaseProjectDetailQueryCriteria;
import me.zhengjie.business.lease.service.mapstruct.LeaseProjectDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description 服务实现
* @author imxushuai
* @date 2020-06-06
**/
@Service
@RequiredArgsConstructor
public class LeaseProjectDetailServiceImpl implements LeaseProjectDetailService {

    private final LeaseProjectDetailRepository leaseProjectDetailRepository;
    private final LeaseProjectDetailMapper leaseProjectDetailMapper;

    @Override
    public Map<String,Object> queryAll(LeaseProjectDetailQueryCriteria criteria, Pageable pageable){
        Page<LeaseProjectDetail> page = leaseProjectDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(leaseProjectDetailMapper::toDto));
    }

    @Override
    public List<LeaseProjectDetailDto> queryAll(LeaseProjectDetailQueryCriteria criteria){
        return leaseProjectDetailMapper.toDto(leaseProjectDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public LeaseProjectDetailDto findById(Long id) {
        LeaseProjectDetail leaseProjectDetail = leaseProjectDetailRepository.findById(id).orElseGet(LeaseProjectDetail::new);
        ValidationUtil.isNull(leaseProjectDetail.getId(),"LeaseProjectDetail","id",id);
        return leaseProjectDetailMapper.toDto(leaseProjectDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LeaseProjectDetailDto create(LeaseProjectDetail resources) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        resources.setCreateBy(currentUserId);
        resources.setUpdateBy(currentUserId);
        resources.setStatus("PROCESS");

        return leaseProjectDetailMapper.toDto(leaseProjectDetailRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LeaseProjectDetail resources) {
        LeaseProjectDetail leaseProjectDetail = leaseProjectDetailRepository.findById(resources.getId()).orElseGet(LeaseProjectDetail::new);
        ValidationUtil.isNull( leaseProjectDetail.getId(),"LeaseProjectDetail","id",resources.getId());
        leaseProjectDetail.copy(resources);
        leaseProjectDetailRepository.save(leaseProjectDetail);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            leaseProjectDetailRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<LeaseProjectDetailDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LeaseProjectDetailDto leaseProjectDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建者", leaseProjectDetail.getCreateBy());
            map.put("更新者", leaseProjectDetail.getUpdateBy());
            map.put("创建时间", leaseProjectDetail.getCreateTime());
            map.put("更新时间", leaseProjectDetail.getUpdateTime());
            map.put("详情", leaseProjectDetail.getName());
            map.put("描述", leaseProjectDetail.getRemark());
            map.put("开始时间", leaseProjectDetail.getStartTime());
            map.put("结束时间", leaseProjectDetail.getEndTime());
            map.put("物料", leaseProjectDetail.getMaterialsId());
            map.put("截至目前时费用", leaseProjectDetail.getNowMoney());
            map.put("所属项目", leaseProjectDetail.getLeaseProjectId());
            map.put("状态", leaseProjectDetail.getStatus());
            map.put("数量", leaseProjectDetail.getNumber());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void endLeaseDetail(Long[] ids) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<LeaseProjectDetail> list = new ArrayList<>();
        for (Long id : ids) {
            Optional<LeaseProjectDetail> leaseProjectDetailOptional = leaseProjectDetailRepository.findById(id);
            if (leaseProjectDetailOptional.isPresent()) {
                LeaseProjectDetail leaseProjectDetail = leaseProjectDetailOptional.get();
                if (leaseProjectDetail.getStatus().equals("PROCESS")) {
                    leaseProjectDetail.setStatus("END");
                    leaseProjectDetail.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    leaseProjectDetail.setUpdateBy(currentUserId);

                    list.add(leaseProjectDetail);
                }
            }
        }
        if (list.size() > 0) {
            leaseProjectDetailRepository.saveAll(list);
        }
    }

    @Override
    public List<LeaseProjectDetail> findByLeaseProjectId(Long id) {
        return leaseProjectDetailRepository.findByLeaseProjectId(id);
    }

    @Override
    public List<LeaseProjectDetail> findAll() {
        return leaseProjectDetailRepository.findAll();
    }

    @Override
    public void saveAll(List<LeaseProjectDetail> leaseProjectDetails) {
        leaseProjectDetailRepository.saveAll(leaseProjectDetails);
    }

    @Override
    public List<LeaseProjectDetail> findByStatus(String status) {
        return leaseProjectDetailRepository.findByStatus(status);
    }
}