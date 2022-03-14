package com.youngdatafan.di.run.management.util;

import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;

/**
 * sql编码处理
 *
 * @author gavin
 * @since 2020/5/7 5:03 下午
 */
public class SqlEncoderUtil {
    public static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    /**
     * 加密
     *
     * @param sql
     * @return
     */
    public static String encode(String sql) {
        return PASSWORD_ENCODER.encode(sql, false);
    }

    /**
     * 解密
     *
     * @param sql
     * @return
     */
    public static String decode(String sql) {
        return PASSWORD_ENCODER.decode(sql, false);
    }
}
