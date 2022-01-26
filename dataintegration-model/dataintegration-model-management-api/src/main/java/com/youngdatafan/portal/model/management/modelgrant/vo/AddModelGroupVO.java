package com.youngdatafan.portal.model.management.modelgrant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/13 12:07 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "新增接口模型授权组对象")
public class AddModelGroupVO {

    @ApiModelProperty(value = "模型授权组名称", required = true)
    private String groupName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enabled;

    @ApiModelProperty(value = "关联业务模型名称", required = true)
    private List<String> modelId;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public List<String> getModelId() {
        return modelId;
    }

    public void setModelId(List<String> modelId) {
        this.modelId = modelId;
    }
}
