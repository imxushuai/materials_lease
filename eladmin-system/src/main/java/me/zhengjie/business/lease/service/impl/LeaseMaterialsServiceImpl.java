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

import me.zhengjie.business.lease.domain.LeaseMaterials;
import me.zhengjie.business.lease.service.vo.LeaseMaterialsSelectVO;
import me.zhengjie.business.lease.service.vo.LeaseProjectSelectVO;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.business.lease.repository.LeaseMaterialsRepository;
import me.zhengjie.business.lease.service.LeaseMaterialsService;
import me.zhengjie.business.lease.service.dto.LeaseMaterialsDto;
import me.zhengjie.business.lease.service.dto.LeaseMaterialsQueryCriteria;
import me.zhengjie.business.lease.service.mapstruct.LeaseMaterialsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://docs.auauz.net
* @description 服务实现
* @author imxushuai
* @date 2020-06-06
**/
@Service
@RequiredArgsConstructor
public class LeaseMaterialsServiceImpl implements LeaseMaterialsService {

    private final LeaseMaterialsRepository leaseMaterialsRepository;
    private final LeaseMaterialsMapper leaseMaterialsMapper;

    @Override
    public Map<String,Object> queryAll(LeaseMaterialsQueryCriteria criteria, Pageable pageable){
        Page<LeaseMaterials> page = leaseMaterialsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(leaseMaterialsMapper::toDto));
    }

    @Override
    public List<LeaseMaterialsDto> queryAll(LeaseMaterialsQueryCriteria criteria){
        return leaseMaterialsMapper.toDto(leaseMaterialsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public LeaseMaterialsDto findById(Long id) {
        LeaseMaterials leaseMaterials = leaseMaterialsRepository.findById(id).orElseGet(LeaseMaterials::new);
        ValidationUtil.isNull(leaseMaterials.getId(),"LeaseMaterials","id",id);
        return leaseMaterialsMapper.toDto(leaseMaterials);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LeaseMaterialsDto create(LeaseMaterials resources) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        resources.setCreateBy(currentUserId);
        resources.setUpdateBy(currentUserId);

        return leaseMaterialsMapper.toDto(leaseMaterialsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LeaseMaterials resources) {
        LeaseMaterials leaseMaterials = leaseMaterialsRepository.findById(resources.getId()).orElseGet(LeaseMaterials::new);
        ValidationUtil.isNull( leaseMaterials.getId(),"LeaseMaterials","id",resources.getId());
        leaseMaterials.copy(resources);
        leaseMaterialsRepository.save(leaseMaterials);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            leaseMaterialsRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<LeaseMaterialsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LeaseMaterialsDto leaseMaterials : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", leaseMaterials.getName());
            map.put("描述", leaseMaterials.getRemark());
            map.put("创建人", leaseMaterials.getCreateBy());
            map.put("更新人", leaseMaterials.getUpdateBy());
            map.put("创建时间", leaseMaterials.getCreateTime());
            map.put("更新时间", leaseMaterials.getUpdateTime());
            map.put("单位", leaseMaterials.getUnit());
            map.put("单价/天", leaseMaterials.getPrice());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<LeaseMaterialsSelectVO> findSelectDataAll() {
        List<LeaseMaterialsSelectVO> list = new ArrayList<>();
        for (Object o : leaseMaterialsRepository.findLeaseMaterialsSelectVOAll()) {
            Object[] objects = (Object[]) o;
            list.add(new LeaseMaterialsSelectVO((Long) objects[0], (String) objects[1]));
        }
        return list;
    }

    @Override
    public List<LeaseMaterials> findAll() {
        return leaseMaterialsRepository.findAll();
    }
}