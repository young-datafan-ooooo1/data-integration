package com.youngdatafan.di.run.management.steps.connect.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "字段信息")
public class FieldDTO {

    @ApiModelProperty(value = "字段名")
    private String fieldName;
    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    @ApiModelProperty(value = "字段长度")
    private Integer fieldLength;

    @ApiModelProperty(value = "字段精度")
    private Integer fieldPrecision;
}
