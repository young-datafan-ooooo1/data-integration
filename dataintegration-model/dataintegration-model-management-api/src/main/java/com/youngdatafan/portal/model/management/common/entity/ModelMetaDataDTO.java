package com.youngdatafan.portal.model.management.common.entity;

import lombok.Data;

/**
 * ModelMetaDataDTO.
 */
@Data
public class ModelMetaDataDTO {

    private static final long serialVersionUID = 1L;

    private String modelName;

    private String columnName;

    private String columnType;

    private String columnLength;

    private String columnPrecision;

    private String columnChineseName;

    private String columnDescription;

    private String columnEtlSql;

    private String dimensionMetric;

    private String metricGroup;

    private String statistics;

    private String modelDataSort;

}
