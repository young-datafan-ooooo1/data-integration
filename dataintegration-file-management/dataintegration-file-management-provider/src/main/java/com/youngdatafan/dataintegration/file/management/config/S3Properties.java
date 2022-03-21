package com.youngdatafan.dataintegration.file.management.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * s3文件服务器.
 *
 * @author liuhao
 * @since 2022/1/19 17:14
 */

@Data
@ApiModel("s3文件服务器")
public class S3Properties {

    /**
     * Amazon S3 ip.
     */
    @ApiModelProperty("s3服务器ip")
    private String server;

    /**
     * Amazon S3 端口.
     */
    @ApiModelProperty("s3服务器端口")
    private int port;

    /**
     * Amazon S3用户名.
     */
    @ApiModelProperty("s3服务器用户名")
    private String userName;

    /**
     * Amazon S3密码.
     */
    @ApiModelProperty("s3服务器密码")
    private String password;

    /**
     * 根路径.
     */
    @ApiModelProperty("s3服务器文件根路径")
    private String rootPath;

    /**
     * 是否创建文件夹.
     */
    @ApiModelProperty("是否创建文件夹")
    private Boolean createDir;

    /**
     * 桶路径.
     */
    @ApiModelProperty("桶路径")
    private String bucket;
}
