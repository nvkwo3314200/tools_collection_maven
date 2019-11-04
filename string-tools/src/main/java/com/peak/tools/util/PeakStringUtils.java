package com.peak.tools.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 *  包含字符串相关的一些基本操作
 */
public class PeakStringUtils {

    /**
     * 传入java属性， 得到jdbc属性
     * 方式在不连续的大写字母前加上下划线， 并把最终的结果大写
     * @param prop
     * @return
     */
    public static String getJdbcProp(String prop) {
        return getJdbcProp(prop, null);
    }

    /**
     * 传入java属性， 得到jdbc属性
     * 方式在不连续的大写字母前加上下划线， 并把最终的结果大写
     * @param prop
     * @return
     */
    public static String getJdbcProp(String prop, String prefix) {
        String tempPrefix = prefix == null? "": prefix.trim();
        tempPrefix = (tempPrefix.lastIndexOf("_") > -1 || "".equals(tempPrefix))? tempPrefix: tempPrefix + "_";
        char[] arr = prop.toCharArray();
        StringBuffer sb = new StringBuffer();
        char pre = 1;
        for(int i = 0 ; i < arr.length; i++) {
            if(arr[i] < 91 && arr[i] > 64 && pre > 96 && pre < 123) { // 如果当前字母为大写，且前一个字符为小写，则加上下划线
                sb.append("_");
            }
            sb.append(arr[i]);
            pre = arr[i];
        }
        sb.insert(0, tempPrefix);
        return sb.toString().toUpperCase();
    }

    /**
     * 把文本描述转化为java属性并去掉其中的特殊字符, 以空格做为分隔符
     * @param item
     * @return
     */
    public static String getJavaProperty(String item) {
        StringBuffer sb = new StringBuffer();
        String tempItem = item.replaceAll("\\&|\\(|\\)|\\_|\\?|\\-|\\.", "");
        Arrays.asList(tempItem.split(" ")).forEach(a -> {
            if(StringUtils.isNotEmpty(a)) {
                char[] arr = a.toCharArray();
                arr[0] = new Character(arr[0]).toString().toUpperCase().toCharArray()[0];
                sb.append(arr);
            }
        });
        sb.replace(0, 1, sb.substring(0,1).toLowerCase());
        return sb.toString();
    };

}
