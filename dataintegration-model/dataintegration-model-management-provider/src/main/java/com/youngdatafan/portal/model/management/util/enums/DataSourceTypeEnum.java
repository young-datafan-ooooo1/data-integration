package com.youngdatafan.portal.model.management.util.enums;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/7/2 5:17 下午</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public enum DataSourceTypeEnum {
    MYSQL("MYSQL", "MYSQL"),

    CLICKHOUSE("CLICKHOUSE", "CLICKHOUSE"),

    ORACLE("ORACLE", "ORACLE");


    private final String code;
    private final String desc;

    DataSourceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String code() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

}
