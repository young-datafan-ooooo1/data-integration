package com.youngdatafan.portal.model.management.modelgrant.entity;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

public class UserGrant implements Serializable {
    private String userGrantId;

    private String userName;

    private String description;

    private String enabled;

    private Date createTime;

    private String createUserId;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getUserGrantId() {
        return userGrantId;
    }

    public void setUserGrantId(String userGrantId) {
        this.userGrantId = userGrantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userGrantId=").append(userGrantId);
        sb.append(", userName=").append(userName);
        sb.append(", description=").append(description);
        sb.append(", enabled=").append(enabled);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}