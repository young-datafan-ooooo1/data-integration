package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 集成数据源输入接口对象.
 */
@Data
@ApiModel(description = "集成数据源输入接口对象")
public class JCDataSourceVO {

    @ApiModelProperty(value = "数据库id", required = true)
    private String datasourceId;

    @ApiModelProperty(value = "数据源名称", required = true)
    @Length(max = 30, min = 2)
    private String dsName;

    @ApiModelProperty(value = "数据源描述")
    private String describe;

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dsType;

    @ApiModelProperty(value = "数据源用户名", required = true)
    private String dsUsername;

    @ApiModelProperty(value = "数据源密码")
    private String dsPassword;

    @ApiModelProperty(value = "来源平台", required = true)
    private String sourcePlatform;

    @ApiModelProperty(value = "HOST")
    private String dsHost;

    @ApiModelProperty(value = "数据库名称")
    private String database;

    @ApiModelProperty(value = "端口")
    private String dsPort;

    @ApiModelProperty(value = "access", required = true)
    private String access;

    @ApiModelProperty(value = "高级参数")
    private AdvancedParametersVO dsConnectorSetting;

    @ApiModelProperty(value = "CUSTOM_DRIVER_CLASS")
    private String customDriverClass;

    @ApiModelProperty(value = "CUSTOM_URL")
    private String customUrl;

    @ApiModelProperty(value = "DATABASE_DIALECT_ID")
    private String databaseDialectId;

}
