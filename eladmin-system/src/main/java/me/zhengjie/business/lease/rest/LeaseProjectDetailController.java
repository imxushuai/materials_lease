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
import me.zhengjie.business.lease.domain.LeaseProjectDetail;
import me.zhengjie.business.lease.service.LeaseProjectDetailService;
import me.zhengjie.business.lease.service.dto.LeaseProjectDetailQueryCriteria;
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
@Api(tags = "租赁项目明细管理")
@RequestMapping("/api/leaseProjectDetail")
public class LeaseProjectDetailController {

    private final LeaseProjectDetailService leaseProjectDetailService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('leaseProjectDetail:list')")
    public void download(HttpServletResponse response, LeaseProjectDetailQueryCriteria criteria) throws IOException {
        leaseProjectDetailService.download(leaseProjectDetailService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询租赁项目明细")
    @ApiOperation("查询租赁项目明细")
    @PreAuthorize("@el.check('leaseProjectDetail:list')")
    public ResponseEntity<Object> query(LeaseProjectDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(leaseProjectDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增租赁项目明细")
    @ApiOperation("新增租赁项目明细")
    @PreAuthorize("@el.check('leaseProjectDetail:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody LeaseProjectDetail resources){
        return new ResponseEntity<>(leaseProjectDetailService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改租赁项目明细")
    @ApiOperation("修改租赁项目明细")
    @PreAuthorize("@el.check('leaseProjectDetail:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody LeaseProjectDetail resources){
        leaseProjectDetailService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除租赁项目明细")
    @ApiOperation("删除租赁项目明细")
    @PreAuthorize("@el.check('leaseProjectDetail:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        leaseProjectDetailService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("endLeaseDetail")
    @Log("结束租赁明细")
    @ApiOperation("结束租赁明细")
    public ResponseEntity<Object> endLeaseDetail(@RequestBody Long[] ids){
        leaseProjectDetailService.endLeaseDetail(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}