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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 匹配标准科室分类
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-07-18 09:35
 */
public class DealDeptCategory {
    public static void main(String[] args) throws Exception {
        String filePath = "/Users/nanfang/Desktop/1.txt";
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            file.createNewFile();
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String lineContent = "";
        Map<String, LinkedList<String>> categoryMap = new HashMap<String, LinkedList<String>>();
        while ((lineContent = reader.readLine()) != null) {
            Pattern patternThree = Pattern.compile("(\\S+)(\\s+)(\\S+)(\\s+)(\\S+)(\\s+)(\\S+)");
            Matcher matcherThree = patternThree.matcher(lineContent);
            Pattern patternTwo = Pattern.compile("(\\S+)(\\s+)(\\S+)(\\s+)(\\S+)");
            Matcher matcherTwo = patternTwo.matcher(lineContent);
            if (matcherThree.find()) {
                LinkedList<String> category = new LinkedList();
                category.add(matcherThree.group(7));
                category.add(matcherThree.group(3));
                category.add(matcherThree.group(5));
                categoryMap.put(matcherThree.group(1), category);
            } else if (matcherTwo.find()) {
                LinkedList<String> category = new LinkedList();
                category.add(matcherTwo.group(5));
                category.add(matcherTwo.group(3));
                categoryMap.put(matcherTwo.group(1), category);
            }

        }

        String filePath2 = "/Users/nanfang/Desktop/2.txt";
        File file2 = new File(filePath2);
        FileInputStream fileInputStream2 = new FileInputStream(file2);
        InputStreamReader inputStreamReader2 = new InputStreamReader(fileInputStream2);
        BufferedReader reader2 = new BufferedReader(inputStreamReader2);
        String lineContent2 = "";
        int i = 0;
        int j = 0;
        List<String> all = new ArrayList<String>();
        List<String> in = new ArrayList<String>();
        while ((lineContent2 = reader2.readLine()) != null) {
            j++;
            Pattern pattern = Pattern.compile("(\\S+)(\\s+)(\\S+)");
            Matcher matcher = pattern.matcher(lineContent2);
            if (matcher.find()) {
                all.add(matcher.group(3));
                for (Map.Entry<String, LinkedList<String>> entry : categoryMap.entrySet()) {
                    LinkedList<String> linkedList = entry.getValue();
                    if (linkedList.size() == 3) {
                        String content = null;
                        if (linkedList.get(0).equals(matcher.group(3))) {
                            in.add(matcher.group(3));
                            content = matcher.group(3) + " |全匹配|" + linkedList.get(0);
                            System.out.println(content);
                            writeFileByFileWriter("/Users/nanfang/Desktop/3.txt", content);
                            i++;
                        } else if (matcher.group(3).contains(linkedList.get(1))) {
                            in.add(matcher.group(3));
                            content = matcher.group(3) + " |半匹配1|" + linkedList.get(1);
                            System.out.println(content);
                            writeFileByFileWriter("/Users/nanfang/Desktop/3.txt", content);
                            i++;
                        } else if (matcher.group(3).contains(linkedList.get(2))) {
                            in.add(matcher.group(3));
                            content = matcher.group(3) + " |半匹配2|" + linkedList.get(2);
                            System.out.println(content);
                            writeFileByFileWriter("/Users/nanfang/Desktop/3.txt", content);
                            i++;
                        }
                    } else if (linkedList.size() == 2) {
                        String content = null;
                        if (linkedList.get(0).equals(matcher.group(3))) {
                            in.add(matcher.group(3));
                            content = matcher.group(3) + " |全匹配|" + linkedList.get(0);
                            System.out.println(content);
                            writeFileByFileWriter("/Users/nanfang/Desktop/3.txt", content);
                            i++;
                        } else if (matcher.group(3).contains(linkedList.get(1))) {
                            in.add(matcher.group(3));
                            content = matcher.group(3) + " |半匹配1|" + linkedList.get(1);
                            System.out.println(content);
                            writeFileByFileWriter("/Users/nanfang/Desktop/3.txt", content);
                            i++;
                        }
                    } else {
                        String content = matcher.group(3) + "|无匹配|";
                        System.out.println(content);
                        writeFileByFileWriter("/Users/nanfang/Desktop/3.txt", content);
                        i++;
                    }
                }
            } else {
                System.out.println("未匹配: " + lineContent2);
            }
        }
        all.stream().filter(line -> !in.contains(line)).collect(Collectors.toList()).forEach(content -> {
            System.out.println(content + "|无匹配|");
            writeFileByFileWriter("/Users/nanfang/Desktop/3.txt", content + "|无匹配|");
        });
        System.out.println("总i: " + i);
        System.out.println("总j: " + j);
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
