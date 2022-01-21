package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/5/28 2:28 下午
*/
/**
    * 业务模型信息表
    */
public class DpPortalBusinessModel {
    /**
    * 模型名称(PK)
    */
    private String modelName;

    /**
    * 模型中文名
    */
    private String chineseName;

    /**
    * 关联基础模型名称
    */
    private String basicModelName;

    /**
    * 模型描述
    */
    private String description;

    /**
    * 数据过滤条件
    */
    private String filterRule;

    /**
    * 是否启用
    */
    private String enabled;

    /**
    * 查询SQL
    */
    private String querySql;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    /**
    * 创建者
    */
    private String createUserId;

    /**
    * 业务模型排序
    */
    private String modelSort;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getBasicModelName() {
        return basicModelName;
    }

    public void setBasicModelName(String basicModelName) {
        this.basicModelName = basicModelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilterRule() {
        return filterRule;
    }

    public void setFilterRule(String filterRule) {
        this.filterRule = filterRule;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
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
        this.createUserId = createUserId;
    }

    public String getModelSort() {
        return modelSort;
    }

    public void setModelSort(String modelSort) {
        this.modelSort = modelSort;
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
        sb.append(", querySql=").append(querySql);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", modelSort=").append(modelSort);
        sb.append("]");
        return sb.toString();
    }
}