package com.dp.de.run.management.plugin.service.csvinput;

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
@ApiModel(description = "csv 字段")
public class FieldVO {

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "格式")
    private String format;

    @ApiModelProperty(value = "长度")
    private String len;

    @ApiModelProperty(value = "精度")
    private String precision;

    @ApiModelProperty(value = "货币符号")
    private String dollarSign;

    @ApiModelProperty(value = "小数点符号")
    private String pointSymbol;

    @ApiModelProperty(value = "分组符号")
    private String classSymbol;

    @ApiModelProperty(value = "去掉空格类型")
    private String spaceTypeRemoved;

}
