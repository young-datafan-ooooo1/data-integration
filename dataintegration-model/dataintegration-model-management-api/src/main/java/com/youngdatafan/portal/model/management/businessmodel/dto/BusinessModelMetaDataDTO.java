package com.youngdatafan.portal.model.management.businessmodel.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 业务模型数据.
 */
@Data
@ApiModel(description = "业务模型数据")
public class BusinessModelMetaDataDTO {

    private static final long serialVersionUID = 1L;

    private String modelName;

    private String columnName;

    private String columnType;

    private Integer columnLength;

    private Integer columnPrecision;

    private String columnChineseName;

    private String columnDescription;

    private String columnEtlSql;

    private String dimensionMetric;

    private String metricGroup;

    private String statistics;

    private String modelDataSort;

}
