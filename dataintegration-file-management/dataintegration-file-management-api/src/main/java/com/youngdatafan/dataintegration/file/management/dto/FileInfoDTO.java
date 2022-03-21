package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 文件信息返回类.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/14 18:19
 */
@Data
@ApiModel("文件信息返回类")
public class FileInfoDTO {
    /**
     * 文件id.
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 文件名.
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 文件夹id.
     */
    @ApiModelProperty("文件夹id")
    private String folderId;

    /**
     * 文件夹名称.
     */
    @ApiModelProperty("文件夹名称")
    private String folderName;

    /**
     * 文件类型.
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 相对路径.
     */
    @ApiModelProperty("相对路径")
    private String fileRelativPath;

    /**
     * 创建用户id.
     */
    @ApiModelProperty("创建用户Id")
    private String createUserId;

    /**
     * 创建用户名称.
     */
    @ApiModelProperty("上传用户")
    private String createUserName;


    /**
     * 用户名.
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 密码.
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 文件管理记录的用户账号.不保证是最小的.
     */
    @ApiModelProperty("文件管理所记录的用户账号不保证是最小")
    private String accountOld;

    /**
     * 是否是文件夹.
     */
    @ApiModelProperty("是否文件夹")
    private String isFolder;

    /**
     * 文件存储类型.
     */
    @ApiModelProperty("文件存储类型")
    private String fileServerType;

    /**
     * 文件大小.
     */
    @ApiModelProperty("文件大小")
    private String fileSize;

    /**
     * 文件来源系统.
     */
    @ApiModelProperty("来源系统")
    private String sourceSystem;

    /**
     * 文件来源方式.
     */
    @ApiModelProperty("来源方式")
    private String sourceWay;

    /**
     * 文件来源项目.
     */
    @ApiModelProperty("来源项目")
    private String sourceProject;

    /**
     * 文件上传时间.
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 文件最近修改时间.
     */
    @ApiModelProperty("最近修改时间")
    private Date lastModifiedTime;

    /**
     * 文件部门名称.
     */
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 文件部门编码.
     */
    @ApiModelProperty("部门编码")
    private Integer deptId;

    /**
     * 业务文件保留的有效天数.
     */
    @ApiModelProperty("业务文件保留有效天数")
    private Integer effectiveDays;
}
