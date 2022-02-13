package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/3/7 2:22 下午
 */

@Data
@ApiModel(description = "字段信息")
public class FieldsInfoVO {

    @ApiModelProperty(value = "字段名", required = true)
    @NotBlank
    private String name;
    @ApiModelProperty(value = "字段中文名", required = false)
    private String nameCn;
    @ApiModelProperty(value = "字段类型", required = true)
    @NotBlank
    private String type;
    @ApiModelProperty(value = "字段长度", required = true)
    @NotBlank
    private int length;
    @ApiModelProperty(value = "字段精度", required = true)
    @NotBlank
    private int precision;
}
