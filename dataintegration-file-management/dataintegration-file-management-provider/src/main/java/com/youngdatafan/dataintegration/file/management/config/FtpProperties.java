package com.youngdatafan.dataintegration.file.management.config;

import lombok.Data;

/**
 * ftp配置类.
 */
@Data
public class FtpProperties {

    /**
     * ftp服务器ip.
     */
    private String server;

    /**
     * ftp端口.
     */
    private int port;

    /**
     * ftp登陆用户名.
     */
    private String userName;

    /**
     * ftp登录密码.
     */
    private String password;

    /**
     * 是否被动模式.
     */
    private boolean passiveMode;

    /**
     * 文件编码.
     */
    private String ftpEncoding;

    /**
     * 是否创建文件夹.
     */
    private Boolean createDir;

    /**
     * ftp文件根路径.
     */
    private String rootPath;
}
