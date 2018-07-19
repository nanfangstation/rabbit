/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package hrecord;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-07-19 20:20
 */
@Data
public class MetadataResultBO {
    @JSONField(name = "name_en")
    private String enName;
    /**
     * 元数据标识id
     */
    @JSONField(name = "health_record_id")
    private String id;

    /**
     * 元数据名称
     */
    @JSONField(name = "health_record_name")
    private String name;

    /**
     * 元数据类型
     */
    @JSONField(name = "data_type")
    private String dataType;
}
