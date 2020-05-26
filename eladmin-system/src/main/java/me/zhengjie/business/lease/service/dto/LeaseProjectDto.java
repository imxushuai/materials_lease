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

/**
* @website https://docs.auauz.net
* @description /
* @author imxushuai
* @date 2020-05-26
**/
@Data
public class LeaseProjectDto implements Serializable {

    /** ID */
    private Long id;

    /** 创建者 */
    private Long createBy;

    /** 更新者 */
    private Long updateBy;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 项目名称 */
    private String name;

    /** 项目开始时间 */
    private Timestamp startTime;

    /** 项目状态 */
    private String status;

    /** 项目结束时间 */
    private Timestamp endTime;

    /** 截至目前费用 */
    private BigDecimal nowMoney;

    /** 截至结束时费用 */
    private BigDecimal totalMoney;

    /** 项目描述 */
    private String remark;
}