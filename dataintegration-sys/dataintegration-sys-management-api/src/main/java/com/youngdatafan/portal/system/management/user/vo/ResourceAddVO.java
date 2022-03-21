package com.youngdatafan.portal.system.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 资源返回对象.
 */
@Data
@ApiModel(description = "资源返回对象")
public class ResourceAddVO {


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
     * 资源描述.
     */
    @ApiModelProperty(value = "资源描述", required = false)
    @NotBlank
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
     * 资源级别.
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
     * 资源排序.
     */
    @ApiModelProperty(value = "路由url", required = true)
    private String routeUrl;

    @ApiModelProperty(value = "状态", required = true)
    private String status;

}
