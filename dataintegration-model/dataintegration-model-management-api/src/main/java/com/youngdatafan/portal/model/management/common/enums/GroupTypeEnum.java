package com.youngdatafan.portal.model.management.common.enums;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/24 1:59 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public enum GroupTypeEnum {
    JBMX("JBMX", "基本模型"), YWMX("YWMX", "业务模型"), SCJK("SCJK", "输出接口"), SCLMX("SCLMX", "输出类模型"),
    ZDYMX("ZDYMX", "自定义模型"), TSJB("TSJB", "探索脚本"),
    JSJB("JSJB", "集成脚本"), BB("BB", "报表"), XM("XM", "项目");

    private final String code;
    private final String desc;

    GroupTypeEnum(String code, String desc) {
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
