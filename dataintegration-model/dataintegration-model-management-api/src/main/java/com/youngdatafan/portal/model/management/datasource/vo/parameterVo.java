package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Echo_Sxl on 2020/8/6 14:33
 * @version 1.0
 */
@ApiModel(description = "数据源参数")
public class parameterVo {

    @ApiModelProperty(value = "code_key")
    private   String  code;
    @ApiModelProperty(value = "code_value")
    private   String attribute;
    @ApiModelProperty(value = "code_中文")
    private  String  label;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "parameterVo{" +
                "code='" + code + '\'' +
                ", attribute='" + attribute + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
