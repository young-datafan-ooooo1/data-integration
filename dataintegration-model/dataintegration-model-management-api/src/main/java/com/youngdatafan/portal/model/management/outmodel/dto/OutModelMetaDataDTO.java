package com.youngdatafan.portal.model.management.outmodel.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * OutModelMetaDataDTO.
 */
@Data
public class OutModelMetaDataDTO implements Serializable {

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
