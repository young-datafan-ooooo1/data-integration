package com.youngdatafan.portal.model.management.basicmodel.dto;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/18 7:20 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class BasicModelMetaDataDTO extends BasicModelMetaDataVO {

//    private String modelName;
//
//    private String columnName;
//
//    private String columnType;

//    private String columnLength;
//
//    private String columnPrecision;
//
//    private String columnChineseName;
//
//    private String columnDescription;
//
    private String columnEtlSql;
//
//    private String dimensionMetric;
//
//    private String metricGroup;
//
//    private String statistics;
//
//    private String modelDataSort;

    public String getColumnEtlSql() {
        return columnEtlSql;
    }

    public void setColumnEtlSql(String columnEtlSql) {
        this.columnEtlSql = columnEtlSql;
    }
}
