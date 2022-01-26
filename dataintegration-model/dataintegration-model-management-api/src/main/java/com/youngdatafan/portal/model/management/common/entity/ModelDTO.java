package com.youngdatafan.portal.model.management.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

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
public class ModelDTO {

    @ApiModelProperty(value = "中文名称")
    private String cName;

    @ApiModelProperty(value = "名称即id")
    private String name;

    @ApiModelProperty(value = "数据源id")
    private String datasourceName;

    @ApiModelProperty(value = "表名称")
    private String tableName;

    @ApiModelProperty(value = "排序")
    private String sortNum;

    @ApiModelProperty(value = "schema名称")
    private String schemaName;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建时间")
    private String querySql;

    @ApiModelProperty(value = "关联元数据集合")
    private List<ModelMetaDataDTO> modelMetaDataDTOS;

    @ApiModelProperty(value = "基础模型sql")
    private String basicModelSql;

    @ApiModelProperty(value = "业务模型sql")
    private String businessModelSql;

    public String getBasicModelSql() {
        return basicModelSql;
    }

    public void setBasicModelSql(String basicModelSql) {
        this.basicModelSql = basicModelSql;
    }

    public String getBusinessModelSql() {
        return businessModelSql;
    }

    public void setBusinessModelSql(String businessModelSql) {
        this.businessModelSql = businessModelSql;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ModelMetaDataDTO> getModelMetaDataDTOS() {
        return modelMetaDataDTOS;
    }

    public void setModelMetaDataDTOS(List<ModelMetaDataDTO> modelMetaDataDTOS) {
        this.modelMetaDataDTOS = modelMetaDataDTOS;
    }
}
