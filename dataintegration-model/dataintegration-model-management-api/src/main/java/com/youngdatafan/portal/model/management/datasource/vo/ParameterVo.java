package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源参数.
 */
@Data
@ApiModel(description = "数据源参数")
public class ParameterVo {

    @ApiModelProperty(value = "code_key")
    private String code;

    @ApiModelProperty(value = "code_value")
    private String attribute;

    @ApiModelProperty(value = "code_中文")
    private String label;

}
