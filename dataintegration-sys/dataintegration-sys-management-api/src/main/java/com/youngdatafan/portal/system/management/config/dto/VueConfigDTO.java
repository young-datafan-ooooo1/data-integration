package com.youngdatafan.portal.system.management.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author; jeremychen
 * @Descripition;
 * @Date;2020/5/14 5;27 下午
 */
@Data
@ApiModel(description = "前端地址配置")
public class VueConfigDTO {
    @ApiModelProperty("id")
    private String baseUrl;

    @ApiModelProperty("id")
    private String ssoUrl;

    @ApiModelProperty("id")
    private String systemUrl;

    @ApiModelProperty("id")
    private String groupUrl;
    @ApiModelProperty("id")
    private String projectUrl;

    @ApiModelProperty("id")
    private String runManagerUrl;

    @ApiModelProperty("id")
    private String modelMangerUrl;

    @ApiModelProperty("id")
    private String wsUrl;

    @ApiModelProperty("id")
    private String onLineUrl;

    @ApiModelProperty("id")
    private String fileUrl;

    @ApiModelProperty("id")
    private String recordUrl;
}
