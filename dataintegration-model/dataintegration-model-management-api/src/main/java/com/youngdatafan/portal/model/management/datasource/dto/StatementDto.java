package com.youngdatafan.portal.model.management.datasource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 数据源.
 */
@Data
@ApiModel(description = "数据源 Explorer")
public class StatementDto {

    @ApiModelProperty(value = "Schemas")
    private List<String> schemas;

    @ApiModelProperty(value = "所有表")
    private List<String> tables;

    @ApiModelProperty(value = "所有视图")

    private List<String> views;

    @ApiModelProperty(value = "所有同义词")
    private List<String> synonyms;

}
