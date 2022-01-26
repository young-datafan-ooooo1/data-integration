package com.youngdatafan.portal.model.management.util.enums;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/22 11:26 AM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public enum DimensionMetricEnum {

    DIMENSION("D", "维度"),
    NULL("N", "无"),

    METRIC("M", "度量");
    private final String code;
    private final String desc;

    DimensionMetricEnum(String code, String desc) {
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
