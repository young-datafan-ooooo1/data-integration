package com.youngdatafan.portal.model.management.common.enums;

/**
 * GroupTypeEnum.
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

    /**
     * code.
     *
     * @return code
     */
    public String code() {
        return this.code;
    }

    /**
     * getDesc.
     *
     * @return desc
     */
    public String getDesc() {
        return this.desc;
    }
}
