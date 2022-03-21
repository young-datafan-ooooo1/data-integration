package com.youngdatafan.dataintegration.core.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64工具类.
 *
 * @author gavin
 * @Date 2020-01-09 13:50
 */
public class Base64Utils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    /**
     * Base64对给定的字节数组进行编码.
     *
     * @param src 原始字节数组
     * @return 编码字节数组
     */
    public static byte[] encode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getEncoder().encode(src);
    }

    /**
     * Base64解码给定的字节数组.
     *
     * @param src 编码字节数组
     * @return 原始字节数组
     */
    public static byte[] decode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getDecoder().decode(src);
    }

    /**
     * Base64使用RFC 4648对给定的字节数组进行编码.
     *
     * @param src 原始字节数组
     * @return 编码字节数组
     */
    public static byte[] encodeUrlSafe(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getUrlEncoder().encode(src);
    }

    /**
     * Base64使用RFC 4648对给定字节数组进行解码.
     *
     * @param src 编码字节数组
     * @return 原始字节数组
     */
    public static byte[] decodeUrlSafe(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getUrlDecoder().decode(src);
    }

    /**
     * Base64将给定的字节数组编码为字符串.
     *
     * @param src 原始字节数组
     * @return 作为UTF-8字符串的编码字节数组
     */
    public static String encodeToString(byte[] src) {
        if (src.length == 0) {
            return "";
        }
        return new String(encode(src), DEFAULT_CHARSET);
    }

    /**
     * Base64从UTF-8字符串解码给定的字节数组.
     *
     * @param src 编码的UTF-8字符串
     * @return 原始字节数组
     */
    public static byte[] decodeFromString(String src) {
        if (src.isEmpty()) {
            return new byte[0];
        }
        return decode(src.getBytes(DEFAULT_CHARSET));
    }

    /**
     * Base64使用RFC 4648将给定字节数组编码为字符串.
     *
     * @param src 原始字节数组
     * @return 作为UTF-8字符串的编码字节数组
     */
    public static String encodeToUrlSafeString(byte[] src) {
        return new String(encodeUrlSafe(src), DEFAULT_CHARSET);
    }

    /**
     * Base64使用RFC 4648从UTF-8字符串解码给定的字节数组.
     *
     * @param src 编码的UTF-8字符串
     * @return 原始字节数组
     */
    public static byte[] decodeFromUrlSafeString(String src) {
        return decodeUrlSafe(src.getBytes(DEFAULT_CHARSET));
    }

}
