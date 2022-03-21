package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件系统信息返回.
 *
 * @Author liuhao
 * @Date 2022/1/19 16:26
 */
@Data
@ApiModel("文件系统信息返回")
public class FileSystemInfo {

    /**
     * 文件系统基础路径.
     */
    @ApiModelProperty("文件系统基础路径")
    private String fsBaseDir;

    /**
     * 文件系统用户名.
     */
    @ApiModelProperty("文件系统用户名")
    private String fsUserName;

    /**
     * 文件系统密码.
     */
    @ApiModelProperty("文件系统密码")
    private String fsPassword;
}
