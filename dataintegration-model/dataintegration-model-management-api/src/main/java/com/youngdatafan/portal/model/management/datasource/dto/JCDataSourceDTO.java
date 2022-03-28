package com.youngdatafan.portal.model.management.datasource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * JCDataSourceDTO.
 */
@Data
public class JCDataSourceDTO {

    @ApiModelProperty(value = "数据源名称", required = true)
    @Length(max = 30, min = 2)
    private String dsName;

    @ApiModelProperty(value = "数据源描述", required = true)
    private String describe;

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dsType;

    @ApiModelProperty(value = "数据源用户名", required = true)
    private String dsUsername;

    @ApiModelProperty(value = "数据源密码", required = true)
    private String dsPassword;

    @ApiModelProperty(value = "来源平台", required = true)
    private String sourcePlatform;

    @ApiModelProperty(value = "HOST", required = true)
    private String dsHost;

    @ApiModelProperty(value = "端口")
    private String dsPort;

    @ApiModelProperty(value = "access", required = true)
    private String access;

    @ApiModelProperty(value = "高级参数")
    private String dsConnectorSetting;

    @ApiModelProperty(value = "数据库名称")
    private String database;

    @ApiModelProperty(value = "数据库id")
    private String datasourceId;

    @JsonProperty("CUSTOM_DRIVER_CLASS")
    private String CUSTOM_DRIVER_CLASS;

    @JsonProperty("CUSTOM_URL")
    private String CUSTOM_URL;

    @JsonProperty("DATABASE_DIALECT_ID")
    private String DATABASE_DIALECT_ID;

    private List<DatasourceParamsDTO> paramsDTOS;

}
