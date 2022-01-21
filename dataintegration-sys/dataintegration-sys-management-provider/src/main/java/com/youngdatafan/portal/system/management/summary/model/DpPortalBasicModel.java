package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/5/28 2:28 下午
*/
/**
    * 基础模型信息表
    */
public class DpPortalBasicModel {
    /**
    * 模型名称做ID(PK)
    */
    private String modelName;

    /**
    * 模型描述
    */
    private String description;

    /**
    * 模型类型
    */
    private String modelType;

    /**
    * 关联数据源(FK)
    */
    private String dsName;

    /**
    * 关联表schema
    */
    private String tableSchema;

    /**
    * 关联表名
    */
    private String tableName;

    /**
    * 表中文名称
    */
    private String tableChineseName;

    /**
    * 表描述
    */
    private String tableDescription;

    /**
    * 统计信息收集时间
    */
    private Date statisticsTime;

    /**
    * 表记录行数
    */
    private Integer rowCnt;

    /**
    * 是否启用
    */
    private String enabled;

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
    * 排序
    */
    private String modelSort;

    /**
    * 模型中文名
    */
    private String cName;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableChineseName() {
        return tableChineseName;
    }

    public void setTableChineseName(String tableChineseName) {
        this.tableChineseName = tableChineseName;
    }

    public String getTableDescription() {
        return tableDescription;
    }

    public void setTableDescription(String tableDescription) {
        this.tableDescription = tableDescription;
    }

    public Date getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Date statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    public Integer getRowCnt() {
        return rowCnt;
    }

    public void setRowCnt(Integer rowCnt) {
        this.rowCnt = rowCnt;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
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

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modelName=").append(modelName);
        sb.append(", description=").append(description);
        sb.append(", modelType=").append(modelType);
        sb.append(", dsName=").append(dsName);
        sb.append(", tableSchema=").append(tableSchema);
        sb.append(", tableName=").append(tableName);
        sb.append(", tableChineseName=").append(tableChineseName);
        sb.append(", tableDescription=").append(tableDescription);
        sb.append(", statisticsTime=").append(statisticsTime);
        sb.append(", rowCnt=").append(rowCnt);
        sb.append(", enabled=").append(enabled);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", modelSort=").append(modelSort);
        sb.append(", cName=").append(cName);
        sb.append("]");
        return sb.toString();
    }
}