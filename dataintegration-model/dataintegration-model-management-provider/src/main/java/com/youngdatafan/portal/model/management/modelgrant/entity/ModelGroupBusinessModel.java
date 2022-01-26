package com.youngdatafan.portal.model.management.modelgrant.entity;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(description = "授权组关联业务模型对象")
public class ModelGroupBusinessModel implements Serializable {
    private String groupName;

    private String modelName;

    private static final long serialVersionUID = 1L;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", groupName=").append(groupName);
        sb.append(", modelName=").append(modelName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}