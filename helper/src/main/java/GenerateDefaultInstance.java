/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import lombok.Data;

/**
 * 生成默认实例
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-06-19 23:58
 */
public class GenerateDefaultInstance {

    @Test
    public void testPrint() {
        print(new People());
    }
    /* output:
    people.setName("name");
    people.setId(1);
    * */

    public static <T> void print(T target) {
        Field[] fields = target.getClass().getDeclaredFields();
        Set<String> fieldSet = new HashSet<String>();
        for (Field field : fields) {
            fieldSet.add(field.getName());
        }
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            String fieldName = methodName.substring(3, methodName.length());
            char[] fieldChar = fieldName.toCharArray();
            fieldChar[0] += 32;
            String lowerFieldName = String.valueOf(fieldChar);
            if (methodName.startsWith("set") && fieldSet.contains(lowerFieldName)) {
                Type[] parameterTypes = method.getGenericParameterTypes();
                Type type = parameterTypes[0];
                //                System.out.println(type.toString());
                StringBuilder stringBuilder = new StringBuilder();
                char[] nameChar = target.getClass().getSimpleName().toCharArray();
                nameChar[0] += 32;
                stringBuilder.append(String.valueOf(nameChar));
                stringBuilder.append(".");
                stringBuilder.append(methodName);
                stringBuilder.append("(");
                if (!type.toString().contains("Void")) {
                    if (type.toString().contains("String")) {
                        stringBuilder.append("\"");
                        stringBuilder.append(lowerFieldName);
                        stringBuilder.append("\"");
                    }
                    if (type.toString().contains("Integer")) {
                        stringBuilder.append(1);
                    }
                    if (type.toString().contains("Long")) {
                        stringBuilder.append(1L);
                    }
                    if (type.toString().contains("Date")) {
                        stringBuilder.append(new Date());
                    }
                    if (type.toString().contains("Boolean")) {
                        stringBuilder.append(true);
                    }
                }
                stringBuilder.append(");");
                System.out.println(stringBuilder.toString());
            }
        }
    }
}

@Data
class People {
    private Integer id;
    private String name;
}
