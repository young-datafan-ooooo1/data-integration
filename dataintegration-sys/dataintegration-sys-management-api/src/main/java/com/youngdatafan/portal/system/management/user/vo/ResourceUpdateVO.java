package com.youngdatafan.portal.system.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 资源更新对象.
 */
@Data
@ApiModel(description = "资源更新对象")
public class ResourceUpdateVO {
    /**
     * 资源编号(PK).
     */
    @ApiModelProperty(value = "资源编号(PK)", required = true)
    @NotBlank
    private String resId;

    /**
     * 资源名称.
     */
    @ApiModelProperty(value = "资源名称", required = true)
    @NotBlank
    private String resName;

    /**
     * 资源描述,.
     */
    @ApiModelProperty(value = "资源描述", required = false)
    private String describe;

    /**
     * 资源url地址.
     */
    @ApiModelProperty(value = "资源url地址", required = false)
    private String resUrl;

    /**
     * 资源父id.
     */
    @ApiModelProperty(value = "资源父id", required = false)
    private String resPid;

    /**
     * 资源级别/.
     */
    @ApiModelProperty(value = "资源级别", required = true)
    @NotBlank
    private Integer resLevel;

    /**
     * 资源级别.
     */
    @ApiModelProperty(value = "资源类型", required = true)
    @NotBlank
    private String resType;

    /**
     * 资源排序.
     */
    @ApiModelProperty(value = "资源排序", required = true)
    @NotBlank
    private Integer resOrder;

    /**
     * 资源状态.
     */
    @ApiModelProperty(value = "资源状态", required = true)
    @NotBlank
    private String status;

    /**
     * 前端路由地址.
     */
    @ApiModelProperty(value = "前端路由地址", required = true)
    private String routeUrl;

}
