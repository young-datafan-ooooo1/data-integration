package com.youngdatafan.portal.system.management.config.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 前端地址信息配置表.
 */
@ApiModel(value = "com-dp-portal-system-management-config-entity-DpPortalWebConfig")
@Data
public class DpPortalWebConfig {
    /**
     * key.
     */
    @ApiModelProperty(value = "key")
    private String key;

    /**
     * value.
     */
    @ApiModelProperty(value = "value")
    private String value;
}
