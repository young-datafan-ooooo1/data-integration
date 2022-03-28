package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 数据源输入接口对象.
 */
@Data
@ApiModel(description = "数据源输入接口对象")
public class DatasourceVO {

    @ApiModelProperty(value = "数据源名称", required = true)
    @Length(max = 30, min = 2)
    private String dsName;

    @ApiModelProperty(value = "数据源描述", required = true)
    private String describe;

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dsType;

    @ApiModelProperty(value = "数据源地址", required = true)
    private String dsUrl;

    @ApiModelProperty(value = "数据源驱动类", required = true)
    private String driverClassName;

    @ApiModelProperty(value = "数据源用户名", required = true)
    private String dsUsername;

    @ApiModelProperty(value = "数据源密码", required = true)
    private String dsPassword;

    @ApiModelProperty(value = "是否启用", required = true)
    private String enabled;

    @ApiModelProperty(value = "数据源创建时间")
    private Date createTime;

    @ApiModelProperty(value = "数据源更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "数据源创建人id", required = true)
    private String createUserId;

    @ApiModelProperty(value = "高级连接配置", required = true)
    private String dsConectorSetting;

    @ApiModelProperty(value = "数据库id", required = true)
    private String datasourceId;

    @ApiModelProperty(value = "来源平台")
    private String sourcePlatform;

    @ApiModelProperty(value = "高级参数")
    private AdvancedParametersVO advancedParametersVO;

}
