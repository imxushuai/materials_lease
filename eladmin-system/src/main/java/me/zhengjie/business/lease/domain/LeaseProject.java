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
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://docs.auauz.net
* @description /
* @author imxushuai
* @date 2020-05-26
**/
@Entity
@Data
@Table(name="lease_project")
public class LeaseProject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    @ApiModelProperty(value = "项目名称")
    private String name;

    @Column(name = "start_time",nullable = false)
    @NotNull
    @ApiModelProperty(value = "项目开始时间")
    private Timestamp startTime;

    @Column(name = "status")
    @ApiModelProperty(value = "项目状态")
    private String status;

    @Column(name = "end_time")
    @ApiModelProperty(value = "项目结束时间")
    private Timestamp endTime;

    @Column(name = "now_money")
    @ApiModelProperty(value = "截至目前费用")
    private BigDecimal nowMoney;

    @Column(name = "total_money")
    @ApiModelProperty(value = "截至结束时费用")
    private BigDecimal totalMoney;

    @Column(name = "remark")
    @ApiModelProperty(value = "项目描述")
    private String remark;

    public void copy(LeaseProject source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}