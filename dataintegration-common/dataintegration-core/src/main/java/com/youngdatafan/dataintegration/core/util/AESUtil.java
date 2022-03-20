package com.youngdatafan.dataintegration.core.util;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.util.StringUtils;

/**
 * AES加密解密.
 *
 * @author songxiaolang
 */
public class AESUtil {

    /**
     * 密文前缀.
     */
    public static final String PASSWORD_ENCRYPTED_PREFIX = "Encrypted ";

    /**
     * AES密钥 .
     */
    public static final String KEY = "aesKey";

    /**
     * IV向量 .
     */
    public static final String IV = "aesIv";

    /**
     * 默认值 length: must be 16 bytes long.
     */
    public static final String DEF = "4CE4E6E9B10496A5";

    /**
     * AES算法.
     */
    public static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 获取环境变量.
     * cas java -DaesKey=indeed
     *
     * @param key 环境变量key
     * @param def 环境变量默认值
     * @return 环境变量
     */
    private static String getEnv(String key, String def) {
        return System.getProperty(key, def);
    }

    /**
     * AES加密.
     *
     * @param data 原文/明文
     * @return 返回密文字符串
     */
    public static String encrypt(String data) {
        if (StringUtils.isEmpty(data)) {
            throw new IllegalArgumentException("'Data' must be specified and not be null!");
        }
        try {

            byte[] dataByte = data.getBytes(StandardCharsets.UTF_8);
            byte[] keyByte = getEnv(KEY, DEF).getBytes(StandardCharsets.UTF_8);
            byte[] ivByte = getEnv(IV, DEF).getBytes(StandardCharsets.UTF_8);
            // 使用CBC模式创建密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            //创建向量/偏移量iv，增加加密算法的强度
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
            // 配置加密模式并初始化
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyByte, "AES"), ivParameterSpec);
            // 密码器加密数据
            byte[] valueByte = cipher.doFinal(dataByte);
            // Base64加密并转成字符串
            String encrypt = new String(Base64.encode(valueByte));
            return PASSWORD_ENCRYPTED_PREFIX + encrypt;
        } catch (Exception e) {
            throw new RuntimeException("Encrypt Fail!", e);
        }
    }

    /**
     * AES解密.
     *
     * @param encryptCode 秘文
     * @return 明文
     */
    public static String decrypt(String encryptCode) {
        if (StringUtils.isEmpty(encryptCode) || !encryptCode.startsWith(PASSWORD_ENCRYPTED_PREFIX)) {
            throw new IllegalArgumentException("'encryptCode' must be specified and not be null! and must contain Encrypted !");
        }
        String relEncryptCode = encryptCode.substring(PASSWORD_ENCRYPTED_PREFIX.length());
        try {
            //对密文进行Base64解密
            byte[] encryptCodeByte = Base64Utils.decodeFromString(relEncryptCode);
            //转为utf8字节数组
            byte[] keyByte = KEY.getBytes(StandardCharsets.UTF_8);
            //转为utf8字节数组
            byte[] ivByte = IV.getBytes(StandardCharsets.UTF_8);
            // 使用CBC模式创建密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            //创建向量/偏移量iv，增加加密算法的强度
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
            // 配置加密模式并初始化
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyByte, "AES"), ivParameterSpec);
            // 密码器解密数据
            byte[] decryptBytes = cipher.doFinal(encryptCodeByte);
            // 转为字符串
            return new String(decryptBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decrypt Fail!", e);
        }
    }

}
