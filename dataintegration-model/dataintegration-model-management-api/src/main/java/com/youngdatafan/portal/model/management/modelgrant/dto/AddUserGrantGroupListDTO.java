package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import lombok.Data;

/**
 * 授权组对象.
 */
@Data
@ApiModel(description = "授权组对象")
public class AddUserGrantGroupListDTO {

    @ApiModelProperty(value = "授权组id")
    private String grantId;

    @ApiModelProperty(value = "授权组名称")
    private String grantGroupName;

    @ApiModelProperty(value = "关联模型名称")
    private List<String> businessModelName;

    @ApiModelProperty(value = "模型数量")
    private Integer modelCount;

}
