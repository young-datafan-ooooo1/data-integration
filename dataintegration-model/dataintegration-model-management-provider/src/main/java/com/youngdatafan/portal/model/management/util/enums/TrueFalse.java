package com.youngdatafan.portal.model.management.util.enums;

public enum TrueFalse {
    T("T", "是"),
    F("F", "否");
    private final String code;
    private final String desc;

    TrueFalse(String code, String desc) {
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
