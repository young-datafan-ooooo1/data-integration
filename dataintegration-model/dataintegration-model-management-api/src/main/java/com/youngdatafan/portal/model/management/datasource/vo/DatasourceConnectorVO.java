package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源输入接口对象.
 */
@Data
@ApiModel(description = "数据源输入接口对象")
public class DatasourceConnectorVO {

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dsType;

    @ApiModelProperty(value = "数据源地址", required = true)
    private String dsUrl;

    @ApiModelProperty(value = "数据源用户名", required = true)
    private String dsUsername;

    @ApiModelProperty(value = "数据源密码", required = true)
    private String dsPassword;

    @ApiModelProperty(value = "是否加密", required = true)
    private Boolean encryption;

    @ApiModelProperty(value = "数据源驱动", required = true)
    private String driverClassName;

}
