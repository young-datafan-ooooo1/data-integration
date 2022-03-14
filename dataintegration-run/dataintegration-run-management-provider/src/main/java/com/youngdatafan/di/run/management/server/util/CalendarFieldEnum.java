package com.youngdatafan.di.run.management.server.util;

/**
 * @author gavin
 * @since 2021/11/11 10:44
 */
public enum CalendarFieldEnum {
    /**
     * 年.
     */
    YEAR(1),
    /**
     * 月.
     */
    MONTH(2),
    /**
     * 当前年份的周.
     */
    WEEK_OF_YEAR(3),
    /**
     * 当前月份的周.
     */
    WEEK_OF_MONTH(4),
    /**
     * 当前月份的天.
     */
    DAY_OF_MONTH(5),
    /**
     * 当前年份的天.
     */
    DAY_OF_YEAR(6),
    /**
     * 当前周的天.
     */
    DAY_OF_WEEK(7),
    /**
     * 当月的当前周的天.
     */
    DAY_OF_WEEK_IN_MONTH(8),
    /**
     * 小时.
     */
    HOUR(10),
    /**
     * 当前天的小时.
     */
    HOUR_OF_DAY(11),
    /**
     * 分钟.
     */
    MINUTE(12),
    /**
     * 秒.
     */
    SECOND(13),
    /**
     * 毫秒.
     */
    MILLISECOND(14);

    /**
     * 字段对应的值.
     */
    private int value;

    CalendarFieldEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
