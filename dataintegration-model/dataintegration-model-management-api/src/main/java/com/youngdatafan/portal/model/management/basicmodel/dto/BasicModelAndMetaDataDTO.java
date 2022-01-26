package com.youngdatafan.portal.model.management.basicmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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

    @ApiModelProperty(value = "模型中文名")
    private String cName;

    @ApiModelProperty(value = "schema名称")
    private String schemaName;

    @ApiModelProperty(value = "模型id")

    private String modelName;

    @ApiModelProperty(value = "模型类型")

    private String modelType;

    @ApiModelProperty(value = "是否有效")

    private String enabled;

    @ApiModelProperty(value = "模型排序")

    private String modelSort;

    @ApiModelProperty(value = "数据库名字")
    private String dsName;

    @ApiModelProperty(value = "表名")

    private String tableName;
    @ApiModelProperty(value = "描述")

    private String description;
    @ApiModelProperty(value = "字段名称")

    private String columnName;
    @ApiModelProperty(value = "字段类型")

    private String columnType;
    @ApiModelProperty(value = "字段长度")

    private String columnLength;

    @ApiModelProperty(value = "字段精度")

    private String columnPrecision;

    @ApiModelProperty(value = "字段中文名")

    private String columnChineseName;

    @ApiModelProperty(value = "维度度量")

    private String dimensionMetric;

    @ApiModelProperty(value = "是否统计")

    private String statistics;
    @ApiModelProperty(value = "模型字段排序")

    private String modelDataSort;

    @ApiModelProperty(value = "字段描述")
    private String columnDescription;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
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
