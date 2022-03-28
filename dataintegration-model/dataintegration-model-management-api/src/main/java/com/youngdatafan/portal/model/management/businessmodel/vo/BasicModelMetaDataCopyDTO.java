package com.youngdatafan.portal.model.management.businessmodel.vo;

import lombok.Data;

/**
 * BasicModelMetaDataCopyDTO.
 */
@Data
public class BasicModelMetaDataCopyDTO {

    private String cName;

    private String modelName;

    private String modelType;

    private String enabled;

    private String businessModelName;

    private String modelSort;

    private String dsName;

    private String tableName;

    private String description;

    private String columnName;

    private String columnType;

    private String columnLength;

    private String columnPrecision;

    private String columnChineseName;

    private String dimensionMetric;

    private String statistics;

    private String modelDataSort;

    private String columnDescription;

    private String columnEtlSql;

    private String metricGroup;
}
