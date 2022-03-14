package com.youngdatafan.di.run.management.util;

/**
 * 字段类型
 *
 * @author gavin
 * @since 2020/2/21 2:44 下午
 */
public enum KettleFieldType {
    /**
     * 字符串
     */
    STRING,

    /**
     * 精度数值
     */
    NUMBER,

    /**
     * 大精度数值
     */
    BIGNUMBER,

    /**
     * int or long
     */
    INTEGER,
    /**
     * 日期
     */
    DATE,

    /**
     * 时间
     */
    TIMESTAMP;

}
