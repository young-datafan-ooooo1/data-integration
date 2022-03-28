package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础模型组.
 */
@Data
@ApiModel(description = "基础模型组")
public class OutinterfaceModelGroupDTO {

    @ApiModelProperty(value = "模型组id")
    private String modelGroupId;

    @ApiModelProperty(value = "模型组名称")
    private String modelGroupName;

}
