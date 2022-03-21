package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件权限类.
 *
 * @author liuhao
 * @since 2022/1/19 16:08
 */

@Data
@ApiModel("文件权限类")
public class FileGrantDTO {

    /**
     * 文件id.
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 当前文件夹名称.
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 父级文件夹id.
     */
    @ApiModelProperty("父文件夹id")
    private String pid;

    /**
     * 父级文件夹名称.
     */
    @ApiModelProperty("父文件夹名称")
    private String folderName;


}
