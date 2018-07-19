/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package std;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import hrecord.Element;
import hrecord.MetadataResultBO;
import hrecord.MetadataWrapperBO;
import hrecord.Search;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-07-18 16:55
 */
public class Hello {
    public static void main(String[] args) throws Exception {
        String filePath = "/Users/nanfang/Desktop/1.txt";
        read(filePath);
    }

    public static void read(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineContent = "";
        int i = 0;
        Integer a = 0;
        List<Element> elementList = new ArrayList<>();
        while ((lineContent = reader.readLine()) != null) {
            Pattern patternThree = Pattern
                .compile("(\\S+)(\\s+)(\\S+)(\\s+)(\\S+)(\\s+)(\\S+)(\\s+)(\\S+)(\\s+)(\\S+)(\\s+)(\\S+)");
            Matcher matcherThree = patternThree.matcher(lineContent);
            if (matcherThree.find()) {
                i++;
                Element element = new Element();
                element.setId(Integer.valueOf(matcherThree.group(1)));
                element.setTitle(matcherThree.group(3));
                element.setDataTypeName(matcherThree.group(5));
                element.setInputTypeName(matcherThree.group(7));
                element.setCategory(matcherThree.group(9));
                element.setTagId(matcherThree.group(11));
                element.setTagName(matcherThree.group(13));
                elementList.add(element);
                //                writeFileByFileWriter("/Users/nanfang/Desktop/3.txt", content.toString());
            }
        }
        Map<Integer, List<Element>> map = elementList.stream().collect(Collectors.groupingBy(Element::getId));
        List<Map.Entry<Integer, List<Element>>> list2 = new ArrayList<>();
        list2.addAll(map.entrySet());
        Collections.sort(list2, (o1, o2) -> o1.getKey() - o2.getKey());

        for (Map.Entry<Integer, List<Element>> entry : map.entrySet()) {
            Integer key = entry.getKey();
            List<Element> elementList1 = entry.getValue();
            Element element = elementList1.get(0);
            String title = element.getTitle();
            StringBuilder content = new StringBuilder();
            content.append(key);
            content.append("|");
            content.append(element.getTitle());
            content.append("|");
            content.append(element.getDataTypeName());
            content.append("|");
            content.append(element.getInputTypeName());
            content.append("|");
            content.append(element.getCategory());
            content.append("|");
            content.append(element.getTagId());
            content.append("|");
            for (Element tag : elementList1) {
                content.append(tag.getTagName() + "、");
            }
            content.append("|");
            Search search = new Search();
            MetadataWrapperBO metadataWrapperBO = search.search(title);
            if (metadataWrapperBO != null && !CollectionUtils.isEmpty(metadataWrapperBO.getData())) {
                List<MetadataResultBO> metadataResultBOS = metadataWrapperBO.getData();
                if (metadataResultBOS.size() > a)
                    a = metadataResultBOS.size();
                for (MetadataResultBO metadataResultBO : metadataResultBOS) {
                    if (metadataResultBO.getId().contains("DE")) {
                        content.append(metadataResultBO.getEnName());
                        content.append("|");
                        content.append(metadataResultBO.getName());
                        content.append("|");
                        content.append(metadataResultBO.getId());
                        content.append("|");
                    }
                    if (metadataResultBO.getId().contains("CV")) {
                        content.append(metadataResultBO.getId());
                        content.append("|");
                    }
                }
            }
            System.out.println(content.toString());
        }
        System.out.println(a);
        System.out.println("总：" + i);
    }

    public static void writeFileByFileWriter(String filePath, String content) {
        try {
            File file = new File(filePath);
            synchronized (file) {
                FileWriter fw = new FileWriter(filePath, true);
                fw.write(content + System.getProperty("line.separator"));
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
