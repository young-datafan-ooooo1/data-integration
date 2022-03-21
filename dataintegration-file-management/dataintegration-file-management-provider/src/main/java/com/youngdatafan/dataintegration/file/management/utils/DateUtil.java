package com.youngdatafan.dataintegration.file.management.utils;

import io.swagger.annotations.ApiModel;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具转化时间类.
 *
 * @author liuhao
 * @since 2022/1/21 10:47
 */

@ApiModel("日期时间工具转换类")
public class DateUtil {

    /**
     * 比较两个日期，忽略毫秒单位.
     * @param date1 第一个时间点.
     * @param date2 第二个时间点.
     * @return 是否一致.
     */
    public static boolean equalsIgnoreMillisecond(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return getDateSeconds(date1) == getDateSeconds(date2);
    }

    /**
     * 获取当前毫秒数.
     * @param date 日期时间.
     * @return 日期毫秒数.
     */
    public static long getDateSeconds(Date date) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTimeInMillis();
    }
}
