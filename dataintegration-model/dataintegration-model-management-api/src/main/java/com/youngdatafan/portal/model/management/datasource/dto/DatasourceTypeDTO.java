package com.youngdatafan.portal.model.management.datasource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源类型返回对象.
 */
@Data
@ApiModel(description = "数据源类型返回对象")
public class DatasourceTypeDTO {

    @ApiModelProperty(value = "数据源类型名称", required = true)
    private String datasourceTypeName;

}
