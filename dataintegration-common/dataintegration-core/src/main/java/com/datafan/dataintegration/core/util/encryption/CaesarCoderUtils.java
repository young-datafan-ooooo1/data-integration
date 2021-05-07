package com.datafan.dataintegration.core.util.encryption;

/**
 * 凯撒加密解密工具类
 *
 * @author renhua.zhang
 * @create 2018-05-30 12:25
 **/
public class CaesarCoderUtils {

    /**
     * 加密字符串
     *
     * @param str 字符串
     * @param k   系数
     * @return 加密后的内容
     */
    public static String encrypt(String str, int k) {
        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            // 如果字符串中的某个字符是小写字母
            if (c >= 'a' && c <= 'z') {
                // 移动key%26位
                c += k % 25;
                if (c < 'a') {
                    // 向左超界
                    c += 25;
                }
                if (c > 'z') {
                    // 向右超界
                    c -= 25;
                }
            }
            // 如果字符串中的某个字符是大写字母
            else if (c >= 'A' && c <= 'Z') {
                // 移动key%26位
                c += k % 25;
                if (c < 'A') {
                    // 同上
                    c += 25;
                }
                if (c > 'Z') {
                    // 同上
                    c -= 25;
                }
            }
            result.append(c);
        }
        return result.toString();
    }

    /**
     * 解密字符串
     *
     * @param str 字符串
     * @param k   系数
     * @return 解密后的内容
     */
    public static String decrypt(String str, int k) {
        // 取相反数
        k = 0 - k;
        return encrypt(str, k);
    }

}
