package com.youngdatafan.portal.model.management.basicmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模型信息和所关联模型元数据.
 */
@ApiModel(description = "模型信息和所关联模型元数据")
@Data
public class BasicModelAndMetaDataDTO {

    @ApiModelProperty(value = "模型中文名")
    private String cName;

    @ApiModelProperty(value = "schema名称")
    private String schemaName;

    @ApiModelProperty(value = "模型id")

    private String modelName;

    @ApiModelProperty(value = "模型类型")

    private String modelType;

    @ApiModelProperty(value = "是否有效")

    private String enabled;

    @ApiModelProperty(value = "模型排序")

    private String modelSort;

    @ApiModelProperty(value = "数据库名字")
    private String dsName;

    @ApiModelProperty(value = "表名")

    private String tableName;

    @ApiModelProperty(value = "描述")

    private String description;

    @ApiModelProperty(value = "字段名称")

    private String columnName;

    @ApiModelProperty(value = "字段类型")

    private String columnType;

    @ApiModelProperty(value = "字段长度")

    private String columnLength;

    @ApiModelProperty(value = "字段精度")

    private String columnPrecision;

    @ApiModelProperty(value = "字段中文名")

    private String columnChineseName;

    @ApiModelProperty(value = "维度度量")

    private String dimensionMetric;

    @ApiModelProperty(value = "是否统计")

    private String statistics;

    @ApiModelProperty(value = "模型字段排序")

    private String modelDataSort;

    @ApiModelProperty(value = "字段描述")
    private String columnDescription;

}
