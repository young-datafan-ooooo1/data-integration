package com.youngdatafan.dataintegration.core.util;

import java.util.Random;
import java.util.UUID;

/**
 * UUID工具类，支持生成32位uuid.
 *
 * @author renhua.zhang
 * @since 2017-10-20 10:23
 **/
public class UUIDUtils {

    private static final int SHORT_MAX = 65536;

    private static int counter = -1;

    /**
     * 根据jdk生成默认uuid.
     *
     * @return uuid字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 根据jdk生成32位uuid，不带分隔符(-).
     *
     * @return uuid字符串
     */
    public static String generateUUID32() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        return digits(mostSigBits >> 32, 8)
            + digits(mostSigBits >> 16, 4)
            + digits(mostSigBits, 4)
            + digits(leastSigBits >> 48, 4)
            + digits(leastSigBits, 12);
    }

    /**
     * 生成唯一的long类型的id.
     *
     * @return uuid string
     */
    public static synchronized long nextId() {
        long now = System.currentTimeMillis();
        if (counter == -1) {
            long seed = now ^ Thread.currentThread().getId();
            Random rnd = new Random(Long.hashCode(seed));
            counter = rnd.nextInt(SHORT_MAX);
        }
        long id = (now << 16) | counter;
        counter = (counter + 1) % SHORT_MAX;
        return id;
    }

    /**
     * Returns val represented by the specified number of hex digits.
     */
    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

}
