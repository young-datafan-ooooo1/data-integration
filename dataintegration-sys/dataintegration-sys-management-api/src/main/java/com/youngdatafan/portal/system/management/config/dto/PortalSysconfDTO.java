package com.youngdatafan.portal.system.management.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 系统配置表.
 */
@ApiModel(value = "com-dp-portal-system-management-config-entity-DpPortalSysconf")
@Data
public class PortalSysconfDTO {
    /**
     * 记录系统相关的参数关键字.
     */
    @ApiModelProperty(value = "记录系统相关的参数关键字")
    private String paramKey;

    /**
     * 记录参数值信息.
     */
    @ApiModelProperty(value = "记录参数值信息")
    private String paramValue;

    /**
     * 记录参数描述信息.
     */
    @ApiModelProperty(value = "记录参数描述信息")
    private String paramDescribe;

    @ApiModelProperty(value = "")
    private Date createTime;

    @ApiModelProperty(value = "")
    private Date updateTime;

    /**
     * 参数所属平台.
     */
    @ApiModelProperty(value = "参数所属平台")
    private String paramType;
}
