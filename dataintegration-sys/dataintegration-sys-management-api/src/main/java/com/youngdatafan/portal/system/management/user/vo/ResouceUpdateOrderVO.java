package com.youngdatafan.portal.system.management.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 资源层级顺序.
 */
@Data
@ApiModel(description = "资源层级顺序")
public class ResouceUpdateOrderVO {

    /**
     * 资源id.
     */
    @ApiModelProperty(value = "资源id", required = true)
    @NotBlank
    private String resId;

    /**
     * 资源顺序.
     */
    @ApiModelProperty(value = "父资源名称", required = true)
    @NotBlank
    private Integer order;

    /**
     * 父资源名称.
     */
    @ApiModelProperty(value = "父资源名称", required = true)
    @NotBlank
    private String resPid;


    /**
     * 资源级别.
     */
    @ApiModelProperty(value = "资源级别", required = true)
    @NotBlank
    private Integer resLevel;
}
