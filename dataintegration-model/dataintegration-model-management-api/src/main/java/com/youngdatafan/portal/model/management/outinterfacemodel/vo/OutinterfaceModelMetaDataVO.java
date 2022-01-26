package com.youngdatafan.portal.model.management.outinterfacemodel.vo;

import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/18 10:31 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel("业务模型元数据")
public class OutinterfaceModelMetaDataVO {
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

