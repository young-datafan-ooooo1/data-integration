package com.youngdatafan.portal.model.management.util.model;

import java.io.Serializable;

public class ExcelColumns implements Serializable {
    private String tableId;

    private Integer columnIndex;

    private String columnName;

    private String columnDesc;

    private String dataType;

    private String regexpCheck;

    private static final long serialVersionUID = 1L;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId == null ? null : tableId.trim();
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc == null ? null : columnDesc.trim();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    public String getRegexpCheck() {
        return regexpCheck;
    }

    public void setRegexpCheck(String regexpCheck) {
        this.regexpCheck = regexpCheck == null ? null : regexpCheck.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tableId=").append(tableId);
        sb.append(", columnIndex=").append(columnIndex);
        sb.append(", columnName=").append(columnName);
        sb.append(", columnDesc=").append(columnDesc);
        sb.append(", dataType=").append(dataType);
        sb.append(", regexpCheck=").append(regexpCheck);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}