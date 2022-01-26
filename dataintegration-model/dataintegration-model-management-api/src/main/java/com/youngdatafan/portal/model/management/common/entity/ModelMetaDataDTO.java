package com.youngdatafan.portal.model.management.common.entity;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/3/19 2:58 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class ModelMetaDataDTO {


    private String modelName;

    private String columnName;

    private String columnType;

    private String columnLength;

    private String columnPrecision;

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
        this.modelDataSort = modelDataSort;
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
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
