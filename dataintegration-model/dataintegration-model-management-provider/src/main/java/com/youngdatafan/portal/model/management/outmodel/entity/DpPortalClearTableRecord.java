package com.youngdatafan.portal.model.management.outmodel.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 清空临时表记录表(DpPortalClearTableRecord)实体类
 *
 * @author makejava
 * @since 2020-08-03 15:05:47
 */
public class DpPortalClearTableRecord implements Serializable {
    private static final long serialVersionUID = -34504909271285660L;
    /**
    * 模型id
    */
    private String modelId;
    /**
    * 创建人id
    */
    private String createUserId;
    /**
    * 数据库id
    */
    private String datasourceId;
    /**
    * 表名
    */
    private String tableName;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 模型类型
    */
    private String modelType;


    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

}