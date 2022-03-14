package com.youngdatafan.di.run.management.server.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author gavin
 * @since 2020/2/22 12:04 下午
 */
@Slf4j
public class DateUtils {

    /**
     * datetime日期格式匹配
     */
    public static final Pattern DATE_TIME_PATTERN = Pattern.compile("\\d{1,4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}");

    /**
     * date日期格式匹配
     */
    public static final Pattern DATE_PATTERN = Pattern.compile("\\d{1,4}-\\d{1,2}-\\d{1,2}");

    /**
     * 转换date对象
     *
     * @param date 日期字符串
     * @return Date
     */
    public static Date parseDate(String date) {
        try {
            if (date.matches(DATE_TIME_PATTERN.pattern())) {
                return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(date);
            } else if (date.matches(DATE_PATTERN.pattern())) {
                return FastDateFormat.getInstance("yyyy-MM-dd").parse(date);
            }
        } catch (ParseException e) {
            log.error("日期转换失败,date = {}", date);
        }

        return null;
    }

    /**
     * 转换Timestamp对象
     *
     * @param date 日期字符串
     * @return Timestamp
     */
    public static Timestamp parseTimestamp(String date) {
        try {
            if (date.matches(DATE_TIME_PATTERN.pattern())) {
                return new Timestamp(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(date).getTime());
            } else if (date.matches(DATE_PATTERN.pattern())) {
                return new Timestamp(FastDateFormat.getInstance("yyyy-MM-dd").parse(date).getTime());
            }
        } catch (ParseException e) {
            log.error("日期转换失败,date = {}", date);
        }

        return null;
    }

}
