package com.youngdatafan.portal.model.management.superset.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * superset跳转参数.
 */
@Data
@ApiModel("superset跳转参数")
public class SupersetVO {
    @ApiModelProperty(value = "字段信息")
    private String columns;

    @ApiModelProperty(value = "业务模型id")
    private String businessModelName;

    @ApiModelProperty(value = "查询sql")
    private String sql;

    @ApiModelProperty(value = "数据源id")
    private String datasourceId;

}
