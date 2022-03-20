package com.youngdatafan.dataintegration.core.util.encryption.sm;

import com.youngdatafan.dataintegration.core.util.Base64Utils;

/**
 * sm4 工具类.
 *
 * @author gavin
 */
public class Sm4Utils {
    private static final String IV = "UISwD5fW1cFh8SNS";

    /**
     * ecb加密.
     *
     * @param secretKey 加密密码
     * @param hexString 加密密码是否是16进制
     * @param plainText 加密文本
     * @param charset   字符编码
     * @return 加密后的字符串
     * @throws Exception error.
     */
    public static String encryptDataECB(final String secretKey, final boolean hexString, final String plainText, final String charset) throws Exception {
        Sm4Context ctx = new Sm4Context();
        ctx.setPadding(true);
        ctx.setMode(Sm4.SM4_ENCRYPT);

        byte[] keyBytes;
        if (hexString) {
            keyBytes = Utils.hexStringToBytes(secretKey);
        } else {
            keyBytes = secretKey.getBytes();
        }

        Sm4 sm4 = new Sm4();
        sm4.sm4SetkeyEnc(ctx, keyBytes);
        byte[] encrypted = sm4.sm4CryptEcb(ctx, plainText.getBytes(charset));
        return new String(Base64Utils.encode(encrypted), charset);
    }

    /**
     * ecb解密.
     *
     * @param secretKey  解密密码
     * @param hexString  解密密码是否是16进制
     * @param cipherText 加密后的文本
     * @param charset    字编码
     * @return 解密后字符串
     * @throws Exception error.
     */
    public static String decryptDataECB(final String secretKey, final boolean hexString, final String cipherText, final String charset) throws Exception {
        Sm4Context ctx = new Sm4Context();
        ctx.setPadding(true);
        ctx.setMode(Sm4.SM4_DECRYPT);

        byte[] keyBytes;
        if (hexString) {
            keyBytes = Utils.hexStringToBytes(secretKey);
        } else {
            keyBytes = secretKey.getBytes();
        }

        Sm4 sm4 = new Sm4();
        sm4.sm4SetkeyDec(ctx, keyBytes);
        byte[] decrypted = sm4.sm4CryptEcb(ctx, Base64Utils.decode(cipherText.getBytes(charset)));
        return new String(decrypted, charset);
    }

    /**
     * cbc加密.
     *
     * @param secretKey 加密密码
     * @param hexString 加密密码是否是16进制
     * @param plainText 加密文本
     * @param charset   字符编码
     * @return 加密后的字符串
     * @throws Exception error.
     */
    public static String encryptDataCBC(final String secretKey, final boolean hexString, final String plainText, final String charset) throws Exception {
        Sm4Context ctx = new Sm4Context();
        ctx.setPadding(true);
        ctx.setMode(Sm4.SM4_ENCRYPT);

        byte[] keyBytes;
        byte[] ivBytes;
        if (hexString) {
            keyBytes = Utils.hexStringToBytes(secretKey);
            ivBytes = Utils.hexStringToBytes(IV);
        } else {
            keyBytes = secretKey.getBytes();
            ivBytes = IV.getBytes();
        }

        Sm4 sm4 = new Sm4();
        sm4.sm4SetkeyEnc(ctx, keyBytes);
        byte[] encrypted = sm4.sm4CryptCbc(ctx, ivBytes, plainText.getBytes(charset));
        return new String(Base64Utils.encode(encrypted), charset);
    }

    /**
     * cbc解密.
     *
     * @param secretKey  解密密码
     * @param hexString  解密密码是否是16进制
     * @param cipherText 加密后的文本
     * @param charset    字编码
     * @return 解密后字符串
     * @throws Exception error.
     */
    public static String decryptDataCBC(final String secretKey, final boolean hexString, final String cipherText, final String charset) throws Exception {
        Sm4Context ctx = new Sm4Context();
        ctx.setPadding(true);
        ctx.setMode(Sm4.SM4_DECRYPT);

        byte[] keyBytes;
        byte[] ivBytes;
        if (hexString) {
            keyBytes = Utils.hexStringToBytes(secretKey);
            ivBytes = Utils.hexStringToBytes(IV);
        } else {
            keyBytes = secretKey.getBytes();
            ivBytes = IV.getBytes();
        }

        Sm4 sm4 = new Sm4();
        sm4.sm4SetkeyDec(ctx, keyBytes);
        byte[] decrypted = sm4.sm4CryptCbc(ctx, ivBytes, Base64Utils.decode(cipherText.getBytes(charset)));
        return new String(decrypted, charset);
    }
}
