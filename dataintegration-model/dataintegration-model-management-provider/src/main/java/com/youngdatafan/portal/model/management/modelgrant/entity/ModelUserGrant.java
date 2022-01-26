package com.youngdatafan.portal.model.management.modelgrant.entity;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(description = "用户关联模型对象")
public class ModelUserGrant implements Serializable {
    private String modelName;

    private String userId;

    private static final long serialVersionUID = 1L;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modelName=").append(modelName);
        sb.append(", userId=").append(userId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}