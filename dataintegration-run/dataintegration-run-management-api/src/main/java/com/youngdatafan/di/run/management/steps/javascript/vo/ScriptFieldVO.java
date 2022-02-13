package com.youngdatafan.di.run.management.steps.javascript.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/7 1:24 下午
 */
@Data
@ApiModel(description = "javascript 变量")
public class ScriptFieldVO {

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "改名为")
    private String reName;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "长度")
    private String len;

    @ApiModelProperty(value = "精度")
    private String precision;

    @ApiModelProperty(value = "货币符号")
    private String replace;

}
