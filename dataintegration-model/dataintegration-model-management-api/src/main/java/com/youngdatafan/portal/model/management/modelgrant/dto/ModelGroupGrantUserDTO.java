package com.youngdatafan.portal.model.management.modelgrant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/13 6:30 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel("用户授权信息")
public class ModelGroupGrantUserDTO {

    @ApiModelProperty(value = "用户授权id")
    private String userGrantId;

    @ApiModelProperty(value = "用户名称", required = true)
    private String userName;

    @ApiModelProperty(value = "用户名称", required = true)
    private String userId;


    @ApiModelProperty(value = "模型授权分组", required = true)
    private List<GroupIdAndGroupNamesDTO> grantGroups;

    @ApiModelProperty(value = "模型分组", required = true)
    private List<BusinessModelListDTO> models;

    @ApiModelProperty(value = "模型数量", required = true)
    private Integer modelCount = 0;

    @ApiModelProperty(value = "描述", required = true)
    private String description;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enabled;

    @ApiModelProperty(value = "创建人", required = true)
    private String createUserName;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<BusinessModelListDTO> getModels() {
        return models;
    }

    public void setModels(List<BusinessModelListDTO> models) {
        this.models = models;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGrantId() {
        return userGrantId;
    }

    public void setUserGrantId(String userGrantId) {
        this.userGrantId = userGrantId;
    }

    public List<GroupIdAndGroupNamesDTO> getGrantGroups() {
        return grantGroups;
    }

    public void setGrantGroups(List<GroupIdAndGroupNamesDTO> grantGroups) {
        this.grantGroups = grantGroups;
    }

    public Integer getModelCount() {
        return modelCount;
    }

    public void setModelCount(Integer modelCount) {
        this.modelCount = modelCount;
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

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}

