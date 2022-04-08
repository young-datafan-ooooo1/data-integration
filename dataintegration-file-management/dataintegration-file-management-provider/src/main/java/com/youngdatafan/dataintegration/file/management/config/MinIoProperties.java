package com.youngdatafan.dataintegration.file.management.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * MinIoProperties.
 *
 * @author songxiaolang
 * @since 2022-03-29 14:47
 */
@Data
@ApiModel("MinIo文件服务器")
public class MinIoProperties {

    /**
     * minio地址+端口号.
     */
    private String url;

    /**
     * minio用户名.
     */
    private String accessKey;

    /**
     * minio密码.
     */
    private String secretKey;

    /**
     * 文件桶的名称.
     */
    private String bucketName;

    /**
     * 文件根路径名称.
     */
    private String rootPath;

}
