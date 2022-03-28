package com.youngdatafan.portal.model.management.basicmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 模型分组对象.
 */
@ApiModel(description = "模型分组对象")
public class GroupDTO {

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "分组id")
    private String groupId;

}
