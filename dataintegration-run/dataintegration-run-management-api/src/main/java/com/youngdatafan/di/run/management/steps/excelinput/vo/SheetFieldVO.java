package com.youngdatafan.di.run.management.steps.excelinput.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/7 1:24 下午
 */
@Data
@ApiModel(description = "excel sheet 字段")
public class SheetFieldVO {

    @ApiModelProperty(value = "字段名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "字段类型", required = true)
    @NotBlank
    private String type;

    @ApiModelProperty(value = "长度", required = true)
    @NotBlank
    private Integer length;

    @ApiModelProperty(value = "精度", required = true)
    @NotBlank
    private Integer precision;

    @ApiModelProperty(value = "去除空格类型", required = true)
    @NotBlank
    private Integer trimType;

    @ApiModelProperty(value = "货币符号", required = true)
    @NotBlank
    private String currencySymbol;

    @ApiModelProperty(value = "小数", required = true)
    @NotBlank
    private String decimalSymbol;

    @ApiModelProperty(value = "分组", required = true)
    @NotBlank
    private String groupingSymbol;


}
