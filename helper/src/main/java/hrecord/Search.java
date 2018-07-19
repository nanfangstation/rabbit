/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package hrecord;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import util.HttpClientUtils;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-07-19 20:28
 */
public class Search {

    private String searchUrl = "http://192.168.1.46:2000";
    private static final String SUFFIX = "/health_record_search";
    public static final String SEARCH_CHAR_SET = "UTF-8";
    //搜索服务超时时间
    public static final int SEARCH_TIME_OUT = 1000;
    //医生UUID，支持多值，逗号分隔，最多30个
    public static final String EXPERT_ID = "doctor";
    //元数据
    public static final String META_ID = "keyword";

    public MetadataWrapperBO search(String q) {
        Map<String, String> params = new HashMap<>();
        params.put(META_ID, q);
        String result = HttpClientUtils.doGet(searchUrl + SUFFIX, params, SEARCH_CHAR_SET, SEARCH_TIME_OUT);
        MetadataWrapperBO metadataWrapperBO = JSON.parseObject(result, MetadataWrapperBO.class);
        return metadataWrapperBO;
    }
}
