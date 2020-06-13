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
package me.zhengjie.business.lease.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://docs.auauz.net
* @description /
* @author imxushuai
* @date 2020-06-06
**/
@Entity
@Data
@Table(name="lease_project_detail")
public class LeaseProjectDetail implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "create_by")
    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @Column(name = "update_by")
    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "详情")
    private String name;

    @Column(name = "remark")
    @ApiModelProperty(value = "描述")
    private String remark;

    @Column(name = "start_time")
    @ApiModelProperty(value = "开始时间")
    private Timestamp startTime;

    @Column(name = "end_time")
    @ApiModelProperty(value = "结束时间")
    private Timestamp endTime;

    @Column(name = "materials_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "物料")
    private Long materialsId;

    @Column(name = "now_money")
    @ApiModelProperty(value = "截至目前时费用")
    private BigDecimal nowMoney;

    @Column(name = "lease_project_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "所属项目")
    private Long leaseProjectId;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @Column(name = "number",nullable = false)
    @NotNull
    @ApiModelProperty(value = "数量")
    private BigDecimal number;

    public void copy(LeaseProjectDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}