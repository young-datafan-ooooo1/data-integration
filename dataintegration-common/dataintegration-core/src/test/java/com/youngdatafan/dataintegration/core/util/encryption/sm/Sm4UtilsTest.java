package com.youngdatafan.dataintegration.core.util.encryption.sm;

import org.junit.Test;

/**
 * sm4测试
 *
 * @author renhua.zhang
 * @since 2017-12-06 15:35
 **/
public class Sm4UtilsTest {

    @Test
    public void testCbc() throws Exception {
        String encrypt = Sm4Utils.encryptDataCBC("xxxxxxxxxxxxxxx1", false, "nihao", "UTF-8");
        System.out.println(encrypt);

        String decrypt = Sm4Utils.decryptDataCBC("xxxxxxxxxxxxxxx1", false, encrypt, "UTF-8");
        System.out.println(decrypt);
    }

    @Test
    public void testEcb() throws Exception {
        String encrypt = Sm4Utils.encryptDataECB("xxxxxxxxxxxxxxx1", false, "nihao", "UTF-8");
        System.out.println(encrypt);

        String decrypt = Sm4Utils.decryptDataECB("xxxxxxxxxxxxxxx1", false, encrypt, "UTF-8");
        System.out.println(decrypt);
    }

}
