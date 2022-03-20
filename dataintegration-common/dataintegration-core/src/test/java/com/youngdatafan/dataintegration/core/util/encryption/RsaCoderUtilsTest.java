package com.youngdatafan.dataintegration.core.util.encryption;

import com.youngdatafan.dataintegration.core.util.Base64Utils;
import org.junit.Test;

import java.util.Arrays;

/**
 * {@link RsaCoderUtils} 测试代码
 * Created by hua on 2017/10/10.
 */
public class RsaCoderUtilsTest {

    @Test
    public void test() throws Exception {

        String eaglefacePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2SiEX+bRAA5o87yjAdScPLk3oO3TejYyDb4Rqk8YA37cvruXnA08IlkFJyaw0WwCa5UR/8m/tzL0SybYwdIyfvtuG9hEqXPQiLEk+Pauu6YE3EXUQ4dnZQs+YqIFazMb5oY/NDKoq90+A+Bcq8VSLT0hvh2U157/yYAX2hx0thQIDAQAB";
        String eaglefacePrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALZKIRf5tEADmjzvKMB1Jw8uTeg7dN6NjINvhGqTxgDfty+u5ecDTwiWQUnJrDRbAJrlRH/yb+3MvRLJtjB0jJ++24b2ESpc9CIsST49q67pgTcRdRDh2dlCz5iogVrMxvmhj80Mqir3T4D4FyrxVItPSG+HZTXnv/JgBfaHHS2FAgMBAAECgYBOKj9CjppSELlkg5DP5exXwB08BbK5c6za90iqgq8W1NcyJScEBhEvt25Le6TR1mvN/51917FTIMW6drAe2vspEtQLl055J6RdMeWWfX8TYREReyV8luwhaotwsmkVlg1T8VN9uW2DvLtaBxVhRmaYZ/hzsx34eEwqRu0+XhBYgQJBAOmR37LhbtSuaa48xRRj0M+HceX0k2UZvEqGCtfT1wLrngR81aE4GSVpu38HGiixTVK04KU7BFc13juviFAY7dUCQQDHy5JaQGYnyX5aIZqsyrqp3jMH1nzmiyonjwf+1FJwrt3EQDa5OxwFP3n8d0+yd0WBjTPdEQABa67/FVuCjCjxAkEAlb4PzH76rx+iYTJUp13W4TXUr7i9RnPZY23MRdJqZlToWbguvc1ZUcMXkbjb7Hq/N+b+nu/XOJIwh9NhcPiKgQJAfsgUR8cDzA2VazZVw34lszM36KpXuafniRC/PxqWccDVIvqofyQeJMph6y3+2b3L0mi613Y5La2ab22rEw1D0QJAKeEQIPIptLfL5lb6iHf1Z1zb7bsnoFJX7qXNnCqtfWxsTnI1PKozSCJhDtMwRiOrfflHkeKfQPgSsCAQUjpZxg==";

        String str = "abc";
        String encrypt = RsaCoderUtils.encryptByPublicKey(str, "UTF-8", eaglefacePublicKey);
        System.out.println("public encrypt, encrypt decrypt");
        System.out.println("source: " + str);
        System.out.println("encrypt:" + encrypt);
        System.out.println("decrypt: " + RsaCoderUtils.decryptByPrivateKey(encrypt, "UTF-8", eaglefacePrivateKey));


        System.out.println("##########################################################");
        encrypt = RsaCoderUtils.encryptByPrivateKey(str, "UTF-8", eaglefacePrivateKey);
        System.out.println("private encrypt, public decrypt");
        System.out.println("source: " + str);
        System.out.println("encrypt:" + encrypt);
        System.out.println("decrypt: " + RsaCoderUtils.decryptByPublicKey(encrypt, "UTF-8", eaglefacePublicKey));

    }

    @Test
    public void encryptByPublicKey() throws Exception {
        String eaglefacePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2SiEX+bRAA5o87yjAdScPLk3oO3TejYyDb4Rqk8YA37cvruXnA08IlkFJyaw0WwCa5UR/8m/tzL0SybYwdIyfvtuG9hEqXPQiLEk+Pauu6YE3EXUQ4dnZQs+YqIFazMb5oY/NDKoq90+A+Bcq8VSLT0hvh2U157/yYAX2hx0thQIDAQAB";

        String data = "{\n" +
                "    \t\"service_code\":\"example1\"\n" +
                "    }";

        String encryptedData = RsaCoderUtils.encryptByPublicKey(data, "UTF-8", eaglefacePublicKey);
        System.out.println(encryptedData);

    }

    @Test
    public void decryptByPublicKey() throws Exception {
        String eaglefacePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2SiEX+bRAA5o87yjAdScPLk3oO3TejYyDb4Rqk8YA37cvruXnA08IlkFJyaw0WwCa5UR/8m/tzL0SybYwdIyfvtuG9hEqXPQiLEk+Pauu6YE3EXUQ4dnZQs+YqIFazMb5oY/NDKoq90+A+Bcq8VSLT0hvh2U157/yYAX2hx0thQIDAQAB";
        String responseBody = "YInDlIKQ0M+OzDRfmEL/Scl6dl4uXaxfZANxhvppsMo9BopUuh82sU5LQf8BoXWdIkBy25IRlEWlyctzsLx4XaUsGCd+4hwoaeMSJ9Wh/ZBP5ZgfRFtS0WOgebSTXq4+BAXMNx81saZ4CICMlmH4bwLX3pc1UrqGo3aarPWn6wk=";
        String s = RsaCoderUtils.decryptByPublicKey(responseBody, "UTF-8", eaglefacePublicKey);
        System.out.println(s);

    }


    @Test
    public void base64() {
        System.out.println(Base64Utils.encodeToString("YInDlIKQ0M+OzDRfmEL/Scl6dl4uXaxfZANxhvppsMo9BopUuh82sU5LQf8BoXWdIkBy25IRlEWlyctzsLx4XaUsGCd+4hwoaeMSJ9Wh/ZBP5ZgfRFtS0WOgebSTXq4+BAXMNx81saZ4CICMlmH4bwLX3pc1UrqGo3aarPWn6wk=".getBytes()));
        System.out.println(Arrays.toString(Base64Utils.encode("YInDlIKQ0M+OzDRfmEL/Scl6dl4uXaxfZANxhvppsMo9BopUuh82sU5LQf8BoXWdIkBy25IRlEWlyctzsLx4XaUsGCd+4hwoaeMSJ9Wh/ZBP5ZgfRFtS0WOgebSTXq4+BAXMNx81saZ4CICMlmH4bwLX3pc1UrqGo3aarPWn6wk=".getBytes())));
    }

    @Test
    public void rsa() throws Exception {
        String eaglefacePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2SiEX+bRAA5o87yjAdScPLk3oO3TejYyDb4Rqk8YA37cvruXnA08IlkFJyaw0WwCa5UR/8m/tzL0SybYwdIyfvtuG9hEqXPQiLEk+Pauu6YE3EXUQ4dnZQs+YqIFazMb5oY/NDKoq90+A+Bcq8VSLT0hvh2U157/yYAX2hx0thQIDAQAB";
        String eaglefacePrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALZKIRf5tEADmjzvKMB1Jw8uTeg7dN6NjINvhGqTxgDfty+u5ecDTwiWQUnJrDRbAJrlRH/yb+3MvRLJtjB0jJ++24b2ESpc9CIsST49q67pgTcRdRDh2dlCz5iogVrMxvmhj80Mqir3T4D4FyrxVItPSG+HZTXnv/JgBfaHHS2FAgMBAAECgYBOKj9CjppSELlkg5DP5exXwB08BbK5c6za90iqgq8W1NcyJScEBhEvt25Le6TR1mvN/51917FTIMW6drAe2vspEtQLl055J6RdMeWWfX8TYREReyV8luwhaotwsmkVlg1T8VN9uW2DvLtaBxVhRmaYZ/hzsx34eEwqRu0+XhBYgQJBAOmR37LhbtSuaa48xRRj0M+HceX0k2UZvEqGCtfT1wLrngR81aE4GSVpu38HGiixTVK04KU7BFc13juviFAY7dUCQQDHy5JaQGYnyX5aIZqsyrqp3jMH1nzmiyonjwf+1FJwrt3EQDa5OxwFP3n8d0+yd0WBjTPdEQABa67/FVuCjCjxAkEAlb4PzH76rx+iYTJUp13W4TXUr7i9RnPZY23MRdJqZlToWbguvc1ZUcMXkbjb7Hq/N+b+nu/XOJIwh9NhcPiKgQJAfsgUR8cDzA2VazZVw34lszM36KpXuafniRC/PxqWccDVIvqofyQeJMph6y3+2b3L0mi613Y5La2ab22rEw1D0QJAKeEQIPIptLfL5lb6iHf1Z1zb7bsnoFJX7qXNnCqtfWxsTnI1PKozSCJhDtMwRiOrfflHkeKfQPgSsCAQUjpZxg==";

        int count = 1000;
        byte[] data = "fdafkjdskfjdksljafkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkmldioufoiewufioFdasfjlfjfdakfjslkj大健康路附近的撒娇了开发的就是了框架付了款jl fdjasfjslkfaj4u89fhdofjasf".getBytes();

        // 公钥加密
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            RsaCoderUtils.encryptByPublicKey(data, eaglefacePublicKey);
        }
        System.out.println("公钥加密： " + (System.currentTimeMillis() - currentTimeMillis));

        // 私钥加密
        currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            RsaCoderUtils.encryptByPrivateKey(data, eaglefacePrivateKey);
        }
        System.out.println("私钥加密： " + (System.currentTimeMillis() - currentTimeMillis));

        // 加签
        currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            RsaCoderUtils.sign(data, eaglefacePrivateKey, "MD5WithRSA");
        }
        System.out.println("数字签名： " + (System.currentTimeMillis() - currentTimeMillis));

    }

}
