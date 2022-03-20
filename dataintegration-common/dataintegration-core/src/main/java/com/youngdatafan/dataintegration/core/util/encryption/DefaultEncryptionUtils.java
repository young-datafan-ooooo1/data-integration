package com.youngdatafan.dataintegration.core.util.encryption;

import com.youngdatafan.dataintegration.core.util.encryption.sm.Sm4Utils;

/**
 * 默认加密工具类.
 *
 * @author gavin
 * @since 2020/9/3 5:34 下午
 */
public class DefaultEncryptionUtils {

    private static final String SECRET_KEY = "pIfTnhWjkZqiyHIK";

    /**
     * 加密.
     *
     * @param text 字符串
     * @return 字符串
     */
    public static String encrypt(String text) {
        try {
            return Sm4Utils.encryptDataCBC(SECRET_KEY, false, text, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密.
     *
     * @param text 字符串
     * @return 字符串
     */
    public static String decrypt(String text) {
        try {
            return Sm4Utils.decryptDataCBC(SECRET_KEY, false, text, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
