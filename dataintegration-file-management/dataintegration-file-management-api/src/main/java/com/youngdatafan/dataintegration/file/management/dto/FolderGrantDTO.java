package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件夹权限信息类.
 *
 * @author liuhao
 * @since 2022/1/19 16:17
 */

@ApiModel("文件夹权限信息类")
@Data
public class FolderGrantDTO {

    /**
     * 文件夹id.
     */
    @ApiModelProperty("文件夹id")
    private String pid;

    /**
     * 文件夹名称.
     */
    @ApiModelProperty("文件夹名称")
    private String folderName;
}
