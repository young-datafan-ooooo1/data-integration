package com.youngdatafan.portal.model.management.modelgrant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import lombok.Data;

/**
 * 更新接口模型授权组对象.
 */
@Data
@ApiModel(description = "更新接口模型授权组对象")
public class UpdateModelGroupVO {

    @ApiModelProperty(value = "模型授权组Id", required = true)
    private String groupId;

    @ApiModelProperty(value = "模型授权组名称", required = true)
    private String groupName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enabled;

    @ApiModelProperty(value = "关联业务模型名称", required = true)
    private List<String> modelId;

}
