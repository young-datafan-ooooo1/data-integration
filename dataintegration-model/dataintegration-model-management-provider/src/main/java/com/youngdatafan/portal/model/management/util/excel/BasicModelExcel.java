package com.youngdatafan.portal.model.management.util.excel;

public class BasicModelExcel {
  //模型名称
  private String name;

  //模型描述
  private String description;

  //关联数据源名称
  private String datasource;

  //关联表schema
  private String tableSchema;

  //关联表名
  private String tableName;

  //表中文名称
  private String tableChineseName;

  //表描述
  private String tableDescription;

  //字段名
  private String columnName;

  //字段类型
  private String columnType;

  //字段长度
  private int columnLength;

  //字段精度
  private int columnPrecision;

  //字段中文名称
  private String columnChineseName;

  //字段描述
  private String columnDescription;

  //字段转换SQL
  private String columnEtlSql;

  //维度度量
  private String dimensionMetric;

  //度量分组
  private String metricGroup;

  //是否统计
  private String statistics;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDatasource() {
    return datasource;
  }

  public void setDatasource(String datasource) {
    this.datasource = datasource;
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

  public int getColumnLength() {
    return columnLength;
  }

  public void setColumnLength(int columnLength) {
    this.columnLength = columnLength;
  }

  public int getColumnPrecision() {
    return columnPrecision;
  }

  public void setColumnPrecision(int columnPrecision) {
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
}
