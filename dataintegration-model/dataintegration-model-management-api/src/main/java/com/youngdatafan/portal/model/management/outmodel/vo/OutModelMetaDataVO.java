package com.youngdatafan.portal.model.management.outmodel.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "输出类模型元数据")
public class OutModelMetaDataVO implements Serializable {
    @ApiModelProperty(value = "模型id")
    private String modelName;

    @ApiModelProperty(value = "字段名称")

    private String columnName;

    @ApiModelProperty(value = "字段类型")

    private String columnType;

    @ApiModelProperty(value = "字段长度")

    private Integer columnLength;
    @ApiModelProperty(value = "字段精度")

    private Integer columnPrecision;
    @ApiModelProperty(value = "字段中文名")

    private String columnChineseName;
    @ApiModelProperty(value = "字段描述")

    private String columnDescription;
    @ApiModelProperty(value = "字段过滤sql")

    private String columnEtlSql;
    @ApiModelProperty(value = "维度度量")

    private String dimensionMetric;
    @ApiModelProperty(value = "度量组")
    private String metricGroup;

    @ApiModelProperty(value = "统计")
    private String statistics;

    @ApiModelProperty(value = "模型字段排序")
    private String modelDataSort;

    private String enabled;

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    private static final long serialVersionUID = 1L;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType == null ? null : columnType.trim();
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
        this.columnChineseName = columnChineseName == null ? null : columnChineseName.trim();
    }

    public String getColumnDescription() {
        return columnDescription;
    }

    public void setColumnDescription(String columnDescription) {
        this.columnDescription = columnDescription == null ? null : columnDescription.trim();
    }

    public String getColumnEtlSql() {
        return columnEtlSql;
    }

    public void setColumnEtlSql(String columnEtlSql) {
        this.columnEtlSql = columnEtlSql == null ? null : columnEtlSql.trim();
    }

    public String getDimensionMetric() {
        return dimensionMetric;
    }

    public void setDimensionMetric(String dimensionMetric) {
        this.dimensionMetric = dimensionMetric == null ? null : dimensionMetric.trim();
    }

    public String getMetricGroup() {
        return metricGroup;
    }

    public void setMetricGroup(String metricGroup) {
        this.metricGroup = metricGroup == null ? null : metricGroup.trim();
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics == null ? null : statistics.trim();
    }

    public String getModelDataSort() {
        return modelDataSort;
    }

    public void setModelDataSort(String modelDataSort) {
        this.modelDataSort = modelDataSort == null ? null : modelDataSort.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", modelName=").append(modelName);
        sb.append(", columnName=").append(columnName);
        sb.append(", columnType=").append(columnType);
        sb.append(", columnLength=").append(columnLength);
        sb.append(", columnPrecision=").append(columnPrecision);
        sb.append(", columnChineseName=").append(columnChineseName);
        sb.append(", columnDescription=").append(columnDescription);
        sb.append(", columnEtlSql=").append(columnEtlSql);
        sb.append(", dimensionMetric=").append(dimensionMetric);
        sb.append(", metricGroup=").append(metricGroup);
        sb.append(", statistics=").append(statistics);
        sb.append(", modelDataSort=").append(modelDataSort);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}