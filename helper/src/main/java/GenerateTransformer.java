/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import lombok.Data;

/**
 * 根据相同属性生成转换类(暂不支持嵌套类)
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-06-19 23:55
 */
public class GenerateTransformer {

    @Test
    public void testTransform() {
        transform(new To(), new From());
    }
    /* output:
    to.setName(from.getName());
    to.setId(from.getId());
    * */

    public static <T, S> void transform(T target, S source) {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Set<String> sourceNameSet = new HashSet<String>();
        for (Field field : sourceFields) {
            sourceNameSet.add(field.getName());
        }
        Field[] targetFields = target.getClass().getDeclaredFields();
        Set<String> targetNameSet = new HashSet<String>();
        for (Field field : targetFields) {
            targetNameSet.add(field.getName());
        }
        Method[] declaredTargetMethods = target.getClass().getDeclaredMethods();
        for (Method method : declaredTargetMethods) {
            String methodName = method.getName();
            String fieldName = methodName.substring(3, methodName.length());
            char[] fieldChar = fieldName.toCharArray();
            fieldChar[0] += 32;
            String lowerFieldName = String.valueOf(fieldChar);
            if (methodName.startsWith("set") && sourceNameSet.contains(lowerFieldName)) {
                String sourceSimpleName = source.getClass().getSimpleName();
                char[] sourceChar = sourceSimpleName.toCharArray();
                sourceChar[0] += 32;
                String sourceName = String.valueOf(sourceChar);
                String targetSimpleName = target.getClass().getSimpleName();
                char[] targetChar = targetSimpleName.toCharArray();
                targetChar[0] += 32;
                String targetName = String.valueOf(targetChar);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(targetName);
                stringBuffer.append(".set");
                stringBuffer.append(fieldName);
                stringBuffer.append("(");
                stringBuffer.append(sourceName);
                stringBuffer.append(".get");
                stringBuffer.append(fieldName);
                stringBuffer.append("());");
                System.out.println(stringBuffer.toString());
            }
        }
    }
}

@Data
class From {
    private Integer id;
    private String name;
}

@Data
class To {
    private Integer id;
    private String name;
}
