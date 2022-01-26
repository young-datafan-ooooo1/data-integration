package com.youngdatafan.portal.model.management.util.enums;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/24 10:35 AM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public enum ModelTypeEnum {

    BASICMODEL("basicmodel", "基础模型组"),

    BUSINESSMODEL("businessmodel", "业务模型组"),

    OUTMODEL("outmodel", "输出模型组"),

    CUSTOMMODEL("custommodel", "自定义是模型组");

    private final String code;
    private final String desc;

    ModelTypeEnum(String code, String desc) {
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
