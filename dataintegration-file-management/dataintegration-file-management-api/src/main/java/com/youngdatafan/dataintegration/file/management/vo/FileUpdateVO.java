package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 文件更新接受信息类.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/8 15:41
 */
@ApiModel("文件更新接受信息类")
@Data
public class FileUpdateVO {

    /**
     * 文件自增id.
     */
    @ApiModelProperty(value = "文件id", required = true)
    @NotBlank
    private String fileId;

    /**
     * 文件pid.
     */
    @ApiModelProperty(value = "pid", required = true)
    private String pid;

    /**
     * 文件名称.
     */
    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank
    private String fileName;

    /**
     * 文件类型.
     */
    @ApiModelProperty(value = "文件类型", required = true)
    private String fileType;

    /**
     * 排序.
     */
    @ApiModelProperty(value = "排序", required = true)
    @NotNull
    private Integer order;

    /**
     * 备注.
     */
    @ApiModelProperty(value = "排序", required = false)
    private String notes;

    /**
     * 分组名称.
     */
    @ApiModelProperty(value = "分组名称", required = true)
    @NotBlank
    private String isValid;

}
