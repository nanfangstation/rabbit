/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package hrecord;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

import lombok.Data;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-07-19 20:19
 */
@Data
public class MetadataWrapperBO {

    /**
     * 响应码状态
     */
    @JSONField(name = "code")
    private int code;

    /**
     * 总结果集数目
     */
    @JSONField(name = "totalCount")
    private int totalCount;

    /**
     * 返回结果列表
     */
    @JSONField(name = "data")
    private List<MetadataResultBO> data;

    /**
     * 响应码状态说明.
     */
    @JSONField(name = "message")
    private String message;
}
