package com.youngdatafan.gateway.ratelimit.bean;

/**
 * @author gavin
 * 环境
 */
public enum EnvironmentEnum {

    SANDBOX("SAND_BOX", "沙箱环境"),
    PROD("PROD", "线上环境");

    private String value;
    private String desc;

    EnvironmentEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static EnvironmentEnum getEnvironmentEnumByValue(String value) {
        EnvironmentEnum[] values = EnvironmentEnum.values();
        for (EnvironmentEnum environmentEnum : values) {
            if (value.equals(environmentEnum.value)) {
                return environmentEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
