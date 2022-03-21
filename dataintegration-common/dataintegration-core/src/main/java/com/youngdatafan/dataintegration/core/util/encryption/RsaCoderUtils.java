package com.youngdatafan.dataintegration.core.util.encryption;

import com.youngdatafan.dataintegration.core.util.Base64Utils;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/**
 * 加解密工具类.
 *
 * @author gavin
 */
public class RsaCoderUtils {

    /**
     * 加密算法.
     */
    public static final String KEY_ALGORTHM = "RSA";

    /**
     * RSA最大加密明文大小.
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小.
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 用RSA算法进行加密.
     *
     * @param data      要加密的字符串
     * @param charset   字符集
     * @param publicKey 加密使用的公钥
     * @return 加密结果
     * @throws Exception 异常
     */
    public static String encryptByPublicKey(final String data, final String charset, final String publicKey)
        throws Exception {
        byte[] encryptedResult = RsaCoderUtils.encryptByPublicKey(data.getBytes(charset),
            publicKey);

        return Base64Utils.encodeToString(encryptedResult);
    }


    /**
     * 用公钥加密.
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @return 加密后的字节数组
     * @throws Exception 异常
     */
    public static byte[] encryptByPublicKey(final byte[] data, final String publicKey) throws Exception {
        //对公钥解密
        byte[] keyBytes = Base64Utils.decodeFromString(publicKey);

        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKeyObj = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyObj);

        // RSA单词最大加密长度为117。为此采用分段加密的办法来加密
        byte[] dataReturn = null;
        for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
            byte[] doFinal = cipher.doFinal(RsaCoderUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
            dataReturn = RsaCoderUtils.addAll(dataReturn, doFinal);
        }

        return dataReturn;
    }

    /**
     * 用RSA算法进行加密.
     *
     * @param data       要加密的字符串
     * @param charset    字符集
     * @param privateKey 加密使用的公钥
     * @return 加密结果
     * @throws Exception 异常
     */
    public static String encryptByPrivateKey(final String data, final String charset, final String privateKey)
        throws Exception {
        byte[] encryptedResult = RsaCoderUtils.encryptByPrivateKey(data.getBytes(charset),
            privateKey);

        return Base64Utils.encodeToString(encryptedResult);
    }


    /**
     * 用私钥加密.
     *
     * @param data 加密数据
     * @param key  密钥
     * @return 加密后的字节数组
     * @throws Exception 异常
     */
    public static byte[] encryptByPrivateKey(final byte[] data, final String key) throws Exception {
        //解密密钥
        byte[] keyBytes = Base64Utils.decodeFromString(key);
        //取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        // 加密时超过117字节就报错。为此采用分段加密的办法来加密
        byte[] dataReturn = null;
        for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
            byte[] doFinal = cipher.doFinal(RsaCoderUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
            dataReturn = RsaCoderUtils.addAll(dataReturn, doFinal);
        }

        return dataReturn;
    }

    /**
     * 把参数通过私钥进行加签.
     *
     * @param data               要加密的字符串
     * @param charset            字符串的编码
     * @param privateKey         商户的私钥
     * @param signatureAlgorithm 签名算法
     * @return 加签后的数据
     * @throws Exception 异常
     */
    public static String sign(final String data, final String charset, final String privateKey, final String signatureAlgorithm) throws Exception {
        byte[] dataInBytes = data.getBytes(charset);
        // 用应用的私钥加签.
        return RsaCoderUtils.sign(dataInBytes, privateKey, signatureAlgorithm);
    }

    /**
     * 用私钥对信息生成数字签名.
     *
     * @param data               加密数据
     * @param privateKey         私钥
     * @param signatureAlgorithm 签名算法
     * @return 签名后的字符串
     * @throws Exception 异常
     */
    public static String sign(final byte[] data, final String privateKey, final String signatureAlgorithm) throws Exception {
        //解密私钥
        byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initSign(privateKey2);
        signature.update(data);

        return Base64Utils.encodeToString(signature.sign());
    }

    /**
     * 解密数据.
     *
     * @param data    base64格式的加密数据
     * @param charset 编码
     * @param key     私钥
     * @return 解密之后的内容
     * @throws Exception 异常
     */
    public static String decryptByPrivateKey(final String data, final String charset, final String key) throws Exception {
        byte[] byte64 = Base64Utils.decode(data.getBytes(charset));
        byte[] encryptedBytes = decryptByPrivateKey(byte64, key);
        return new String(encryptedBytes, charset);
    }


    /**
     * 用私钥解密.
     *
     * @param data 加密数据
     * @param key  密钥
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static byte[] decryptByPrivateKey(final byte[] data, final String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = Base64Utils.decodeFromString(key);

        // key
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // Cipher
        Cipher cp = Cipher.getInstance(keyFactory.getAlgorithm());
        cp.init(Cipher.DECRYPT_MODE, privateKey);

        // 分段解密的办法来解密
        int keyLength = ((RSAPrivateKey) privateKey).getModulus().bitLength();
        int maxDecryptBlock = keyLength / 8;
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxDecryptBlock) {
                cache = cp.doFinal(data, offSet, maxDecryptBlock);
            } else {
                cache = cp.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxDecryptBlock;
        }

        return out.toByteArray();
    }

    /**
     * 解密数据.
     *
     * @param data    base64格式的加密数据
     * @param charset 编码
     * @param key     公钥
     * @return 解密之后的内容
     * @throws Exception 异常
     */
    public static String decryptByPublicKey(final String data, final String charset, final String key) throws Exception {
        byte[] byte64 = Base64Utils.decode(data.getBytes(charset));
        byte[] encryptedBytes = decryptByPublicKey(byte64, key);
        return new String(encryptedBytes, charset);
    }

    /**
     * 用公钥解密.
     *
     * @param data 加密数据
     * @param key  密钥
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static byte[] decryptByPublicKey(final byte[] data, final String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = Base64Utils.decodeFromString(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        // 解密时超过128字节就报错。为此采用分段解密的办法来解密
        byte[] dataReturn = null;
        for (int i = 0; i < data.length; i += MAX_DECRYPT_BLOCK) {
            byte[] doFinal = cipher.doFinal(RsaCoderUtils.subarray(data, i, i + MAX_DECRYPT_BLOCK));
            dataReturn = RsaCoderUtils.addAll(dataReturn, doFinal);
        }

        return dataReturn;
    }

    /**
     * 校验数字签名.
     *
     * @param data               加密数据
     * @param publicKey          公钥
     * @param sign               数字签名
     * @param signatureAlgorithm 签名算法
     * @return 验签结果
     * @throws Exception 异常
     */
    public static boolean verify(final byte[] data, final String publicKey, final String sign, final String signatureAlgorithm) throws Exception {
        //解密公钥
        byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
        //构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(Base64Utils.decodeFromString(sign));
    }


    /**
     * 对返回结果进行验签.
     *
     * @param signSource         签名值
     * @param signResult         签名内容
     * @param publicKey          公钥
     * @param charset            字符集
     * @param signatureAlgorithm 签名算法
     * @return true verify success,false verify error.
     * @throws Exception 验签异常
     */
    public static boolean verifySign(final String signSource, final String signResult, final String publicKey, final String charset, final String signatureAlgorithm) throws Exception {
        return verify(signSource.getBytes(charset), publicKey, signResult, signatureAlgorithm);
    }

    /**
     * <p>Produces a new <code>byte</code> array containing the elements
     * between the start and end indices.</p>
     * <p>The start index is inclusive, the end index exclusive.
     * Null array input produces null output.</p>
     *
     * @param array               the array
     * @param startIndexInclusive the starting index. Undervalue (&lt;0)
     *                            is promoted to 0, overvalue (&gt;array.length) results
     *                            in an empty array.
     * @param endIndexExclusive   elements up to endIndex-1 are present in the
     *                            returned subarray. Undervalue (&lt; startIndex) produces
     *                            empty array, overvalue (&gt;array.length) is demoted to
     *                            array length.
     * @return a new array containing the elements between the start and end indices.
     * @since 2.1
     */
    public static byte[] subarray(final byte[] array, int startIndexInclusive, int endIndexExclusive) {
        int endIndexExclusiveTmp = endIndexExclusive;
        int startIndexInclusiveTmp = startIndexInclusive;
        if (array == null) {
            return null;
        }
        if (startIndexInclusiveTmp < 0) {
            startIndexInclusiveTmp = 0;
        }
        if (endIndexExclusiveTmp > array.length) {
            endIndexExclusiveTmp = array.length;
        }
        int newSize = endIndexExclusiveTmp - startIndexInclusiveTmp;
        if (newSize <= 0) {
            return new byte[0];
        }

        byte[] subarray = new byte[newSize];
        System.arraycopy(array, startIndexInclusiveTmp, subarray, 0, newSize);
        return subarray;
    }

    /**
     * <p>Adds all the elements of the given arrays into a new array.</p>
     * <p>The new array contains all of the element of <code>array1</code> followed
     * by all of the elements <code>array2</code>. When an array is returned, it is always
     * a new array.</p>
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
     * </pre>
     *
     * @param array1 the first array whose elements are added to the new array.
     * @param array2 the second array whose elements are added to the new array.
     * @return The new byte[] array.
     * @since 2.1
     */
    public static byte[] addAll(final byte[] array1, final byte[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * <code>null</code>.</p>
     * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
     *
     * @param array the array to clone, may be <code>null</code>
     * @return the cloned array, <code>null</code> if <code>null</code> input
     */
    public static byte[] clone(final byte[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

}
