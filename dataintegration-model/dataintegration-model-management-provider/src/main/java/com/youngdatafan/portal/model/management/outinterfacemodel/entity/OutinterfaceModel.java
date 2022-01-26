package com.youngdatafan.portal.model.management.outinterfacemodel.entity;

import java.io.Serializable;
import java.util.Date;

public class OutinterfaceModel implements Serializable {
    private String modelName;

    private String chineseName;

    private String basicModelName;

    private String description;

    private String filterRule;

    private String enabled;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String modelSort;

    private String querySql;

    private static final long serialVersionUID = 1L;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName == null ? null : chineseName.trim();
    }

    public String getBasicModelName() {
        return basicModelName;
    }

    public void setBasicModelName(String basicModelName) {
        this.basicModelName = basicModelName == null ? null : basicModelName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getFilterRule() {
        return filterRule;
    }

    public void setFilterRule(String filterRule) {
        this.filterRule = filterRule == null ? null : filterRule.trim();
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getModelSort() {
        return modelSort;
    }

    public void setModelSort(String modelSort) {
        this.modelSort = modelSort == null ? null : modelSort.trim();
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql == null ? null : querySql.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modelName=").append(modelName);
        sb.append(", chineseName=").append(chineseName);
        sb.append(", basicModelName=").append(basicModelName);
        sb.append(", description=").append(description);
        sb.append(", filterRule=").append(filterRule);
        sb.append(", enabled=").append(enabled);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", modelSort=").append(modelSort);
        sb.append(", querySql=").append(querySql);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
