package com.youngdatafan.dataintegration.file.management.enums;

import lombok.Getter;

@Getter
public enum GrantTypeEnum {

    FILE(0, "文件"), FOLDER(1, "文件夹");

    private final int code;
    private final String desc;

    GrantTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
