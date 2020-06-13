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

import lombok.RequiredArgsConstructor;
import me.zhengjie.business.lease.domain.LeaseProject;
import me.zhengjie.business.lease.repository.LeaseProjectRepository;
import me.zhengjie.business.lease.service.vo.LeaseProjectSelectVO;
import me.zhengjie.business.lease.service.LeaseProjectService;
import me.zhengjie.business.lease.service.dto.LeaseProjectDto;
import me.zhengjie.business.lease.service.dto.LeaseProjectQueryCriteria;
import me.zhengjie.business.lease.service.mapstruct.LeaseProjectMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://docs.auauz.net
* @description 服务实现
* @author imxushuai
* @date 2020-05-26
**/
@Service
@RequiredArgsConstructor
public class LeaseProjectServiceImpl implements LeaseProjectService {

    private final LeaseProjectRepository leaseProjectRepository;
    private final LeaseProjectMapper leaseProjectMapper;

    @Override
    public Map<String,Object> queryAll(LeaseProjectQueryCriteria criteria, Pageable pageable){
        Page<LeaseProject> page = leaseProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(leaseProjectMapper::toDto));
    }

    @Override
    public List<LeaseProjectDto> queryAll(LeaseProjectQueryCriteria criteria){
        return leaseProjectMapper.toDto(leaseProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public LeaseProjectDto findById(Long id) {
        LeaseProject leaseProject = leaseProjectRepository.findById(id).orElseGet(LeaseProject::new);
        ValidationUtil.isNull(leaseProject.getId(),"LeaseProject","id",id);
        return leaseProjectMapper.toDto(leaseProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LeaseProjectDto create(LeaseProject resources) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        resources.setCreateBy(currentUserId);
        resources.setUpdateBy(currentUserId);
        resources.setTotalMoney(BigDecimal.ZERO);
        resources.setNowMoney(BigDecimal.ZERO);
        resources.setStatus("PROCESS");

        return leaseProjectMapper.toDto(leaseProjectRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LeaseProject resources) {
        LeaseProject leaseProject = leaseProjectRepository.findById(resources.getId()).orElseGet(LeaseProject::new);
        ValidationUtil.isNull( leaseProject.getId(),"LeaseProject","id",resources.getId());
        leaseProject.copy(resources);
        leaseProjectRepository.save(leaseProject);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            leaseProjectRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<LeaseProjectDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LeaseProjectDto leaseProject : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建者", leaseProject.getCreateBy());
            map.put("更新者", leaseProject.getUpdateBy());
            map.put("创建时间", leaseProject.getCreateTime());
            map.put("更新时间", leaseProject.getUpdateTime());
            map.put("项目名称", leaseProject.getName());
            map.put("项目开始时间", leaseProject.getStartTime());
            map.put("项目状态", leaseProject.getStatus());
            map.put("项目结束时间", leaseProject.getEndTime());
            map.put("截至目前费用", leaseProject.getNowMoney());
            map.put("截至结束时费用", leaseProject.getTotalMoney());
            map.put("项目描述", leaseProject.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List findSelectDataAll() {
        List<LeaseProjectSelectVO> list = new ArrayList<>();
        for (Object o : leaseProjectRepository.findLeaseProjectSelectVOAll()) {
            Object[] objects = (Object[]) o;
            list.add(new LeaseProjectSelectVO((Long) objects[0], (String) objects[1]));
        }
        return list;
    }

    @Override
    public List<LeaseProject> findAll() {
        return leaseProjectRepository.findAll();
    }

    @Override
    public void saveAll(List<LeaseProject> leaseProjectList) {
        leaseProjectRepository.saveAll(leaseProjectList);
    }

    @Override
    public List<LeaseProject> findByStatus(String status) {
        return leaseProjectRepository.findByStatus(status);
    }
}