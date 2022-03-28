package com.youngdatafan.portal.model.management.basicmodel.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源id和名称对象.
 */
@Data
@ApiModel(description = "数据源id和名称对象")
public class BasicModelDatasourceDTO {

    @ApiModelProperty(value = "数据源id")
    private String datasourceId;

    @ApiModelProperty(value = "数据源名称")
    private String datasourceName;

    @ApiModelProperty(value = "数据源类型")
    private String datasourceType;

}
