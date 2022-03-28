package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模型id和模型名称对象.
 */
@Data
@ApiModel(description = "模型id和模型名称对象")
public class OutinterfaceModelNameAndCnameDTO {

    @ApiModelProperty(value = "模型id")

    private String modelName;

    @ApiModelProperty(value = "模型名称")

    private String cName;

}
