package com.youngdatafan.portal.model.management.common.entity;

import lombok.Data;

/**
 * BasicModelMetaDataVO.
 */
@Data
public class BasicModelMetaDataVO {

    private String modelName;

    private String columnName;

    private String columnType;

    private Integer columnLength;

    private Integer columnPrecision;

    private String columnChineseName;

    private String columnDescription;

    private String dimensionMetric;

    private String metricGroup;

    private String statistics;

    private String modelDataSort;

}

