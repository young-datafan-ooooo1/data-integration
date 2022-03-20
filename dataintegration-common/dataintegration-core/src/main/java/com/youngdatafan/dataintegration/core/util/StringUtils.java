package com.youngdatafan.dataintegration.core.util;

/**
 * string工具类.
 *
 * @author renhua.zhang
 * @since 2017-12-25 13:40
 **/
public class StringUtils {

    /**
     * 字符串拼接.
     *
     * @param strs 字符串数组
     * @return 拼接之后的字符串
     */
    public static String append(Object... strs) {
        if (strs == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (Object s : strs) {
            sb.append(String.valueOf(s));
        }
        return sb.toString();
    }
}
