package com.youngdatafan.dataintegration.core.util.encryption.sm;

import com.youngdatafan.dataintegration.core.util.Pair;
import org.junit.Test;

/**
 * sm2测试
 *
 * @author renhua.zhang
 * @since 2017-12-06 14:07
 **/
public class Sm2UtilsTest {

    @Test
    public void test() throws Exception {
        String plainText = "你好";
        byte[] sourceData = plainText.getBytes();

        //下面的秘钥可以使用generateKeyPair()生成的秘钥内容
        // 国密规范正式私钥
        String prik = "3690655E33D5EA3D9A4AE1A1ADD766FDEA045CDEAA43A9206FB8C430CEFE0D94";
        // 国密规范正式公钥
        String pubk = "04F6E0C3345AE42B51E06BF50B98834988D54EBC7460FE135A48171BC0629EAE205EEDE253A530608178A98F1E19BB737302813BA39ED3FA3C51639D7A20C7391A";

//        System.out.println("加密: ");
        String cipherText = Sm2Utils.encrypt(Utils.hexToByteArray(pubk), sourceData);
        System.out.println(cipherText);
//        System.out.println("解密: ");
        long currentTimeMillis = System.currentTimeMillis();
        plainText = new String(Sm2Utils.decrypt(Utils.hexToByteArray(prik), Utils.hexToByteArray(cipherText)));
//        System.out.println(plainText);


        System.out.println(System.currentTimeMillis() - currentTimeMillis);
    }

    @Test
    public void generateKeyPair() {
        Pair<String, String> x = Sm2Utils.generateKeyPair();
        System.out.println(x.getK());
        System.out.println(x.getV());
    }

    @Test
    public void test1() throws Exception {
        String eaglefacePublicKey = "043D0CF2A448F605D5889445380F7EB960940C6184053FE660CAC0B93072A8ECA9DB968E96BDD3816442FABBC43B45173B9B05C15F7C78EF186C85CEAE193CFF61";
        String eaglefacePrivateKey = "00A84C69187B5A399126444DC94E61FA45654233E0C6692AB740BA9B6A18C96902";

        int count = 1;
        byte[] data = "fdafkjdskfjdksljafkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkmldioufoiewufioFdasfjlfjfdakfjslkj大健康路附近的撒娇了开发的就是了框架付了款jl fdjasfjslkfaj4u89fhdofjasf".getBytes();


        // 公钥加密
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Sm2Utils.encrypt(Utils.hexStringToBytes(eaglefacePublicKey), data);
        }
        System.out.println("公钥加密： " + (System.currentTimeMillis() - currentTimeMillis));


        // 私钥解密
        currentTimeMillis = System.currentTimeMillis();

        data = Utils.hexStringToBytes("0475096EE76D083D7B7A48937214AB7F504BEC936850662DF91D54C1610C5F2BB0D79E673138E84C0D25CD24A7713F15EE466891487C47B07A78605FCB35B323C91EB780ABC90ECD3E54464E9379923F1ABF0ABF60119E1AB8B52C0D52FCC60325572FFAC39EF13276584F5F2F5BC90970CB47EE6CE0B88C764F6264C646CD4FCE015AD94D9DFA4D691C4535F697CCEB049D883B6C114B465671F441D7548813F0676C0804968B820D220E6FC8E91EE2D3FA6AC7A78AD9CEFEC7032244608D007FE5378766BB679A8B6E96241D9B632C25D093819BBC9C4C7699958E28E26D2B8E55D6109F4E211DFCBA3770043AA1830116F5C82DB9C9691B00CF96E6344831577E58F2A9906FA5D441047EE368CA75155CDFDD4F8701B5");
        for (int i = 0; i < count; i++) {
            Sm2Utils.decrypt(Utils.hexToByteArray(eaglefacePrivateKey), data);
        }
        System.out.println("私钥解密： " + (System.currentTimeMillis() - currentTimeMillis));
    }

}
