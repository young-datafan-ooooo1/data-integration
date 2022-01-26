package com.youngdatafan.portal.model.management.modelgrant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/13 4:46 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel("新增用户授权信息表")
public class UpdateModelGroupGrantUserVO {

    @ApiModelProperty(value = "用户授权id")
    private String userGrantId;
    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "模型授权组")
    private List<String> modelGrantGroupNames;

    @ApiModelProperty(value = "模型名称")
    private List<String> modelNames;

    public String getUserName() {
        return userName;
    }

    public String getUserGrantId() {
        return userGrantId;
    }

    public void setUserGrantId(String userGrantId) {
        this.userGrantId = userGrantId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getModelGrantGroupNames() {
        return modelGrantGroupNames;
    }

    public void setModelGrantGroupNames(List<String> modelGrantGroupNames) {
        this.modelGrantGroupNames = modelGrantGroupNames;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getModelNames() {
        return modelNames;
    }

    public void setModelNames(List<String> modelNames) {
        this.modelNames = modelNames;
    }
}
