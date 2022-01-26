package com.youngdatafan.portal.model.management.businessmodel.entity;

import java.io.Serializable;

public class BusinessModelMetadata implements Serializable {
    private String businessModelName;

    private String columnName;

    private String columnSerial;

    private String customColumnName;

    private String columnEtlSql;

    private static final long serialVersionUID = 1L;

    public String getBusinessModelName() {
        return businessModelName;
    }

    public void setBusinessModelName(String businessModelName) {
        this.businessModelName = businessModelName == null ? null : businessModelName.trim();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getColumnSerial() {
        return columnSerial;
    }

    public void setColumnSerial(String columnSerial) {
        this.columnSerial = columnSerial == null ? null : columnSerial.trim();
    }

    public String getCustomColumnName() {
        return customColumnName;
    }

    public void setCustomColumnName(String customColumnName) {
        this.customColumnName = customColumnName == null ? null : customColumnName.trim();
    }

    public String getColumnEtlSql() {
        return columnEtlSql;
    }

    public void setColumnEtlSql(String columnEtlSql) {
        this.columnEtlSql = columnEtlSql == null ? null : columnEtlSql.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", businessModelName=").append(businessModelName);
        sb.append(", columnName=").append(columnName);
        sb.append(", columnSerial=").append(columnSerial);
        sb.append(", customColumnName=").append(customColumnName);
        sb.append(", columnEtlSql=").append(columnEtlSql);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}