package com.youngdatafan.portal.model.management.outmodel.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 输出类模型元数据.
 */
@Data
@ApiModel(description = "输出类模型元数据")
public class OutModelMetaDataVO implements Serializable {
    @ApiModelProperty(value = "模型id")
    private String modelName;

    @ApiModelProperty(value = "字段名称")

    private String columnName;

    @ApiModelProperty(value = "字段类型")

    private String columnType;

    @ApiModelProperty(value = "字段长度")

    private Integer columnLength;

    @ApiModelProperty(value = "字段精度")

    private Integer columnPrecision;

    @ApiModelProperty(value = "字段中文名")

    private String columnChineseName;

    @ApiModelProperty(value = "字段描述")

    private String columnDescription;

    @ApiModelProperty(value = "字段过滤sql")

    private String columnEtlSql;

    @ApiModelProperty(value = "维度度量")

    private String dimensionMetric;

    @ApiModelProperty(value = "度量组")
    private String metricGroup;

    @ApiModelProperty(value = "统计")
    private String statistics;

    @ApiModelProperty(value = "模型字段排序")
    private String modelDataSort;

    private String enabled;

}
