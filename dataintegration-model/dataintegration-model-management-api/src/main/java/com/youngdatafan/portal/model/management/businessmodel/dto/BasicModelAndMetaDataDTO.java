package com.youngdatafan.portal.model.management.businessmodel.dto;

import io.swagger.annotations.ApiModel;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/22 9:50 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "模型信息和所关联模型元数据")
public class BasicModelAndMetaDataDTO {

    private String cName;

    private String modelName;

    private String modelType;

    private String enabled;

    private String modelSort;
    private String dsName;
    private String tableName;
    private String description;
    private String columnName;
    private String columnType;
    private String columnLength;
    private String columnPrecision;
    private String columnChineseName;
    private String dimensionMetric;
    private String statistics;
    private String modelDataSort;
    private String columnDescription;

    private String columnEtlSql;


    private String metricGroup;

    public String getColumnEtlSql() {
        return columnEtlSql;
    }

    public void setColumnEtlSql(String columnEtlSql) {
        this.columnEtlSql = columnEtlSql;
    }

    public String getMetricGroup() {
        return metricGroup;
    }

    public void setMetricGroup(String metricGroup) {
        this.metricGroup = metricGroup;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getModelSort() {
        return modelSort;
    }

    public void setModelSort(String modelSort) {
        this.modelSort = modelSort;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public String getColumnPrecision() {
        return columnPrecision;
    }

    public void setColumnPrecision(String columnPrecision) {
        this.columnPrecision = columnPrecision;
    }

    public String getColumnChineseName() {
        return columnChineseName;
    }

    public void setColumnChineseName(String columnChineseName) {
        this.columnChineseName = columnChineseName;
    }

    public String getDimensionMetric() {
        return dimensionMetric;
    }

    public void setDimensionMetric(String dimensionMetric) {
        this.dimensionMetric = dimensionMetric;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public String getModelDataSort() {
        return modelDataSort;
    }

    public void setModelDataSort(String modelDataSort) {
        this.modelDataSort = modelDataSort;
    }

    public String getColumnDescription() {
        return columnDescription;
    }


    @Override
    public String toString() {
        return "BasicModelAndMetaDataDTO{" +
                "cName='" + cName + '\'' +
                ", modelName='" + modelName + '\'' +
                ", modelType='" + modelType + '\'' +
                ", enabled='" + enabled + '\'' +
                ", modelSort='" + modelSort + '\'' +
                ", dsName='" + dsName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", description='" + description + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnLength='" + columnLength + '\'' +
                ", columnPrecision='" + columnPrecision + '\'' +
                ", columnChineseName='" + columnChineseName + '\'' +
                ", dimensionMetric='" + dimensionMetric + '\'' +
                ", statistics='" + statistics + '\'' +
                ", modelDataSort='" + modelDataSort + '\'' +
                ", columnDescription='" + columnDescription + '\'' +
                '}';
    }

    public void setColumnDescription(String columnDescription) {
        this.columnDescription = columnDescription;
    }
}
