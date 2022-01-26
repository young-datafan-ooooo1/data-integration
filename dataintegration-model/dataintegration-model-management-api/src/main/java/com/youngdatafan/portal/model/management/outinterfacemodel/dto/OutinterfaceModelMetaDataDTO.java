package com.youngdatafan.portal.model.management.outinterfacemodel.dto;

import io.swagger.annotations.ApiModel;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/20 2:17 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "业务模型数据")
public class OutinterfaceModelMetaDataDTO {

    private String modelName;

    private String columnName;

    private String columnType;

    private Integer columnLength;

    private Integer columnPrecision;

    private String columnChineseName;

    private String columnDescription;

    private String columnEtlSql;

    private String dimensionMetric;

    private String metricGroup;

    private String statistics;

    private String modelDataSort;




    private static final long serialVersionUID = 1L;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    public Integer getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(Integer columnLength) {
        this.columnLength = columnLength;
    }

    public Integer getColumnPrecision() {
        return columnPrecision;
    }

    public void setColumnPrecision(Integer columnPrecision) {
        this.columnPrecision = columnPrecision;
    }

    public String getColumnChineseName() {
        return columnChineseName;
    }

    public void setColumnChineseName(String columnChineseName) {
        this.columnChineseName = columnChineseName;
    }

    public String getColumnDescription() {
        return columnDescription;
    }

    public void setColumnDescription(String columnDescription) {
        this.columnDescription = columnDescription;
    }

    public String getColumnEtlSql() {
        return columnEtlSql;
    }

    public void setColumnEtlSql(String columnEtlSql) {
        this.columnEtlSql = columnEtlSql;
    }

    public String getDimensionMetric() {
        return dimensionMetric;
    }

    public void setDimensionMetric(String dimensionMetric) {
        this.dimensionMetric = dimensionMetric;
    }

    public String getMetricGroup() {
        return metricGroup;
    }

    public void setMetricGroup(String metricGroup) {
        this.metricGroup = metricGroup;
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
}
