package com.youngdatafan.portal.model.management.basicmodel.vo;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/23 7:09 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class BatchInserModelVO {

    private String dataSourceName;

    private String group;

    private String modelType;

    private String schemaName;

    private List<TableNameAndModelNameVO> tableNameAndModelNameVOS;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }


    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<TableNameAndModelNameVO> getTableNameAndModelNameVOS() {
        return tableNameAndModelNameVOS;
    }

    public void setTableNameAndModelNameVOS(List<TableNameAndModelNameVO> tableNameAndModelNameVOS) {
        this.tableNameAndModelNameVOS = tableNameAndModelNameVOS;
    }
}
