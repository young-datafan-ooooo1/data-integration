package com.youngdatafan.portal.model.management.superset.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/3/3 3:52 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel("superset跳转参数")
public class SupersetVO {
    @ApiModelProperty(value = "字段信息")
    private String columns;

    @ApiModelProperty(value = "业务模型id")
    private String businessModelName;

    @ApiModelProperty(value = "查询sql")
    private String sql;

    @ApiModelProperty(value = "数据源id")
    private String datasourceId;

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getBusinessModelName() {
        return businessModelName;
    }

    public void setBusinessModelName(String businessModelName) {
        this.businessModelName = businessModelName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "SupersetVO{" +
                "columns='" + columns + '\'' +
                ", businessModelName='" + businessModelName + '\'' +
                ", sql='" + sql + '\'' +
                '}';
    }
}
