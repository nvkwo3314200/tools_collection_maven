package com.peak.tools;


import com.peak.core.ApplicationContext;
import com.peak.core.Constant;
import com.peak.tools.util.PeakStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 有关于生成报表属性，表名，以及数据库字段的相关
 */
@Slf4j
public class ReportUtils {

    private static final String MYBATIS_TEMPLATE = "<result column=\"%s\" jdbcType=\"%s\" property=\"%s\" />";

    public static List<String> convertDesc2JavaProp(String filename) {
        List<String> propList = new ArrayList<>();
        String path = (ApplicationContext.getRootClassPath() + "/" + filename);
        StringBuffer sb = new StringBuffer();
        try {
            sb.delete(0, sb.length());
            List<String> list = FileUtils.readLines(new File(path), Constant.DEFAULT_CHARSET);
            list.forEach(item -> {
                sb.append("private String ").append(PeakStringUtils.getJavaProperty(item)).append("; //").append(item).append("\n");
            });
            propList.add(sb.toString());
        } catch (IOException e) {
            log.error("文件{}处理失败,原因{}", filename, e.getMessage());
        }
        return propList;
    }

    public static List<String> converJavaProp2MybatisColumn(String filename) {
        List<String> columnList = new ArrayList<>();
        String path = (ApplicationContext.getRootClassPath() + "/" + filename);
        List<String> list = null;
        List<MyType> typeList = new ArrayList<>();
        try {
            list = FileUtils.readLines(new File(path), Constant.DEFAULT_CHARSET);
            list.forEach(item -> {
                String temp = item;
                if(item.indexOf("//") > -1) {
                    temp = item.substring(0, item.indexOf("//"));
                }
                temp = temp.replaceAll("private\\W", "").replaceAll(";\\W?", "");
                String[] arr2 = temp.trim().split(" ");
                typeList.add(new MyType(arr2[0], arr2[1]));
            });

            typeList.forEach(item -> columnList.add(String.format(MYBATIS_TEMPLATE, getJdbcProp(item.prop), getType(item.type), item.prop)));
        } catch (IOException e) {
            log.error("文件{}处理失败,原因{}", filename, e.getMessage());
        }
        return columnList;
    }

    private static Object getType(String type) {
        if("String".equals(type)) {
            return "VARCHAR";
        }
        if("Date".equals(type)) {
            return "TIMESTAMP";
        }
        if("int".equals(type) || "Integer".equals(type) || "BigDecimal".equals(type)) {
            return "DECIMAL";
        }
        return null;
    }

    private static Object getJdbcProp(String prop) {
        char[] arr = prop.toCharArray();
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ; i < arr.length; i++) {
            if(arr[i] < 97) {
                sb.append("_");
            }
            sb.append(arr[i]);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * java对象的属性名与类型
     */
    static class MyType {
        String type;
        String prop;
        public MyType(String type, String prop) {
            this.type = type;
            this.prop = prop;
        }
    }
}