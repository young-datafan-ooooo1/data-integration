package com.youngdatafan.portal.system.management.common.utils;

import com.youngdatafan.dataintegration.core.util.encryption.sm.Sm3Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * SM3Utils.
 */
public class SM3Utils {

    /**
     * 加密.
     *
     * @param s parameter
     * @return String
     */
    public static String encode(String s) {
        byte[] md = new byte[32];
        byte[] msg1 = s.getBytes();
        Sm3Digest sm3 = new Sm3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        return new String(Hex.encode(md));
    }
}
