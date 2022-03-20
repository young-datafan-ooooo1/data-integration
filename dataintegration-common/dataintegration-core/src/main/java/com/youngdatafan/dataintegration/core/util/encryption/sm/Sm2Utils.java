package com.youngdatafan.dataintegration.core.util.encryption.sm;

import com.youngdatafan.dataintegration.core.util.Pair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * sm2加密工具类.
 *
 * @author gavin
 */
public class Sm2Utils {

    /**
     * 生成随机秘钥对.
     *
     * @return sm2 key pair
     */
    public static Pair<String, String> generateKeyPair() {
        Sm2 sm2 = Sm2.getInstance();
        AsymmetricCipherKeyPair key = sm2.getEccKeyPairGenerator().generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();

        return new Pair<>(Utils.byteToHex(publicKey.getEncoded(false)),
            Utils.byteToHex(privateKey.toByteArray()));
    }

    /**
     * 数据加密.
     *
     * @param publicKey 公钥
     * @param data      数据
     * @return 加密后字符串
     */
    public static String encrypt(final byte[] publicKey, final byte[] data) {
        if (publicKey == null || publicKey.length == 0) {
            return null;
        }

        if (data == null || data.length == 0) {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        Sm2 sm2 = Sm2.getInstance();
        ECPoint userKey = sm2.getEccCurve().decodePoint(publicKey);

        ECPoint c1 = cipher.initEnc(sm2, userKey);
        cipher.encrypt(source);
        byte[] c3 = new byte[32];
        cipher.doFinal(c3);

        //C1 C2 C3拼装成加密字串
        return Utils.byteToHex(c1.getEncoded(false)) + Utils.byteToHex(source) + Utils.byteToHex(c3);
    }

    /**
     * 数据解密.
     *
     * @param privateKey    私钥
     * @param encryptedData 加密数据
     * @return 解密后的数据
     */
    public static byte[] decrypt(final byte[] privateKey, final byte[] encryptedData) {
        if (privateKey == null || privateKey.length == 0) {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0) {
            return null;
        }
        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = Utils.byteToHex(encryptedData);
        /*分解加密字串
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
         */
        byte[] c1Bytes = Utils.hexToByteArray(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Utils.hexToByteArray(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = Utils.hexToByteArray(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

        Sm2 sm2 = Sm2.getInstance();
        BigInteger userD = new BigInteger(1, privateKey);

        //通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.getEccCurve().decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.initDec(userD, c1);
        cipher.decrypt(c2);
        cipher.doFinal(c3);

        //返回解密结果
        return c2;
    }

}
