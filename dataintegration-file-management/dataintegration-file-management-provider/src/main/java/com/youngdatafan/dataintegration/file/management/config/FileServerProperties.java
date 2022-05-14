package com.youngdatafan.dataintegration.file.management.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件服务器.
 *
 * @author liuhao
 * @since 2022/1/19 17:13
 */

@Component
@ConfigurationProperties(prefix = "file.server")
@Data
@ApiModel("文件服务器竖向")
public class FileServerProperties {

    /**
     * 采用的服务器.
     */
    @ApiModelProperty("采用的服务器")
    private String useServer;

    @ApiModelProperty("继承服务类型")
    private String extendsFileType;

    /**
     * ftp服务器相关属性.
     */
    @ApiModelProperty("ftp服务器")
    private FtpProperties ftp;

    /**
     * s3服务器相关属性.
     */
    @ApiModelProperty("s3服务器")
    private S3Properties s3;

    /**
     * s3服务器相关属性.
     */
    @ApiModelProperty("s3服务器")
    private MinIoProperties minIos3;
}
