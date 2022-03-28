package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加模型返回对象.
 */
@Data
@ApiModel(description = "添加模型返回对象")
public class BusinessModelListDTO {

    @ApiModelProperty(value = "模型id")
    private String modelName;

    @ApiModelProperty(value = "模型中文名称")
    private String modelChineseName;

    @ApiModelProperty(value = "模型分组名称")
    private String modelGroupName;

    @ApiModelProperty(value = "模型分组类型")
    private String modelGroupType;

}
