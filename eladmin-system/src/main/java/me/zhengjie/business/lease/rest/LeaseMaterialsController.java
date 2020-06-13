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
import me.zhengjie.business.lease.domain.LeaseMaterials;
import me.zhengjie.business.lease.service.LeaseMaterialsService;
import me.zhengjie.business.lease.service.dto.LeaseMaterialsQueryCriteria;
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
* @date 2020-06-06
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "物料管理管理")
@RequestMapping("/api/leaseMaterials")
public class LeaseMaterialsController {

    private final LeaseMaterialsService leaseMaterialsService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('leaseMaterials:list')")
    public void download(HttpServletResponse response, LeaseMaterialsQueryCriteria criteria) throws IOException {
        leaseMaterialsService.download(leaseMaterialsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询物料管理")
    @ApiOperation("查询物料管理")
    @PreAuthorize("@el.check('leaseMaterials:list')")
    public ResponseEntity<Object> query(LeaseMaterialsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(leaseMaterialsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增物料管理")
    @ApiOperation("新增物料管理")
    @PreAuthorize("@el.check('leaseMaterials:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody LeaseMaterials resources){
        return new ResponseEntity<>(leaseMaterialsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改物料管理")
    @ApiOperation("修改物料管理")
    @PreAuthorize("@el.check('leaseMaterials:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody LeaseMaterials resources){
        leaseMaterialsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除物料管理")
    @ApiOperation("删除物料管理")
    @PreAuthorize("@el.check('leaseMaterials:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        leaseMaterialsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("获取所有项目(下拉选择框使用)")
    @ApiOperation("获取所有项目(下拉选择框使用)")
    @GetMapping("findSelectData")
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(leaseMaterialsService.findSelectDataAll(), HttpStatus.OK);
    }
}