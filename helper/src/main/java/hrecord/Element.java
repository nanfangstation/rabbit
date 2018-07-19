/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package hrecord;

import lombok.Data;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-07-19 21:10
 */
@Data
public class Element {
    private Integer id;
    private String title;
    private Integer dataType;
    private String dataTypeName;
    private Integer inputType;
    private String inputTypeName;
    private String category;
    private String tagId;
    private String tagName;
}
