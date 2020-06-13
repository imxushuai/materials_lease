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
package me.zhengjie.business.lease.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.business.lease.domain.LeaseProject;
import me.zhengjie.business.lease.service.LeaseProjectService;
import me.zhengjie.business.lease.service.dto.LeaseProjectQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @author imxushuai
* @date 2020-05-26
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "租赁项目管理")
@RequestMapping("/api/leaseProject")
public class LeaseProjectController {

    private final LeaseProjectService leaseProjectService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('leaseProject:list')")
    public void download(HttpServletResponse response, LeaseProjectQueryCriteria criteria) throws IOException {
        leaseProjectService.download(leaseProjectService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询租赁项目")
    @ApiOperation("查询租赁项目")
    @PreAuthorize("@el.check('leaseProject:list')")
    public ResponseEntity<Object> query(LeaseProjectQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(leaseProjectService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增租赁项目")
    @ApiOperation("新增租赁项目")
    @PreAuthorize("@el.check('leaseProject:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody LeaseProject resources){
        return new ResponseEntity<>(leaseProjectService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改租赁项目")
    @ApiOperation("修改租赁项目")
    @PreAuthorize("@el.check('leaseProject:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody LeaseProject resources){
        leaseProjectService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除租赁项目")
    @ApiOperation("删除租赁项目")
    @PreAuthorize("@el.check('leaseProject:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        leaseProjectService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("获取所有项目(下拉选择框使用)")
    @ApiOperation("获取所有项目(下拉选择框使用)")
    @GetMapping("findSelectData")
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(leaseProjectService.findSelectDataAll(), HttpStatus.OK);
    }
}