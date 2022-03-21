package com.youngdatafan.portal.system.management.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/7/13 6:38 下午
*/

/**
 * 前端地址信息配置表.
 */
@ApiModel(value = "com-dp-portal-system-management-config-entity-DpPortalWebConfig")
@Data
public class PortalWebConfigDTO {
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
