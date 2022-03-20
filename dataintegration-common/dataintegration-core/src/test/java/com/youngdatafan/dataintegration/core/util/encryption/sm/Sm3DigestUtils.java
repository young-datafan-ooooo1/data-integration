package com.youngdatafan.dataintegration.core.util.encryption.sm;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

/**
 * sm3测试
 *
 * @author renhua.zhang
 * @since 2017-12-06 14:43
 **/
public class Sm3DigestUtils {

    @Test
    public void test() {
        byte[] md = new byte[32];
        byte[] msg1 = "你好".getBytes();
        Sm3Digest sm3 = new Sm3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        String s = new String(Hex.encode(md));
        System.out.println(s.toUpperCase());
    }
}
