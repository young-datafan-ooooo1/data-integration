package com.youngdatafan.portal.model.management.businessmodel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 模型信息和所关联模型元数据.
 */
@Data
@ApiModel(description = "模型信息和所关联模型元数据")
public class BasicModelAndMetaDataDTO {

    private String cName;

    private String modelName;

    private String modelType;

    private String enabled;

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
