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
package me.zhengjie.business.lease.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
* @website https://docs.auauz.net
* @description /
* @author imxushuai
* @date 2020-06-06
**/
@Data
public class LeaseProjectDetailDto implements Serializable {

    /** ID */
    /** 防止精度丢失 */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /** 创建者 */
    private Long createBy;

    /** 更新者 */
    private Long updateBy;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 详情 */
    private String name;

    /** 描述 */
    private String remark;

    /** 开始时间 */
    private Timestamp startTime;

    /** 结束时间 */
    private Timestamp endTime;

    /** 物料 */
    private Long materialsId;

    /** 截至目前时费用 */
    private BigDecimal nowMoney;

    /** 所属项目 */
    private Long leaseProjectId;

    /** 状态 */
    private String status;

    /** 数量 */
    private BigDecimal number;
}