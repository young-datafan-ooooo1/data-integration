package com.youngdatafan.dataintegration.file.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 文件平台管理映射类.
 *
 * @Author: jeremychen
 * @Date 2020/4/8 3:41 下午
 */
@ApiModel("文件管理映射类")
@Data
@ToString
public class DpPortalFileManager {
    /**
     * 文件id.
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 文件pid.
     */
    @ApiModelProperty("文件pid")
    private String pid;

    /**
     * 文件名.
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 文件真实名.
     */
    @ApiModelProperty("文件真实名称")
    private String fileRealName;

    /**
     * 文件类型.
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 排序.
     */
    @ApiModelProperty("排序")
    private Integer order;

    /**
     * 备注.
     */
    @ApiModelProperty("备注")
    private String notes;

    /**
     * 是否有效.
     */
    @ApiModelProperty("是否有效")
    private String isValid;

    /**
     * 上传时间.
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 最新修改时间.
     */
    @ApiModelProperty("文件最新修改时间")
    private Date lastModifiedTime;

    /**
     * 创建人id.
     */
    @ApiModelProperty("文件创建人id")
    private String createUserId;

    /**
     * 创建人用户名.
     */
    @ApiModelProperty("创建人用户名")
    private String createUserName;

    /**
     * 是否是文件夹.
     */
    @ApiModelProperty("是否是文件夹")
    private String isFolder;

    /**
     * 创建渠道.
     */
    @ApiModelProperty("创建渠道")
    private String createChannel;

    /**
     * 文件服务类型.
     */
    @ApiModelProperty("文件服务器类型")
    private String fileServerType;

    /**
     * 文件路径.
     */
    @ApiModelProperty("文件路径")
    private String filePath;

    /**
     * 文件真实大小.
     */
    @ApiModelProperty("文件真实大小")
    private String fileSize;

    /**
     * 文件大小前端展示.
     */
    @ApiModelProperty("文件前端展示")
    private String fileSizeShow;

    /**
     * 来源系统.
     */
    @ApiModelProperty("来源系统")
    private String sourceSystem;

    /**
     * 来源方式.
     */
    @ApiModelProperty("来源方式")
    private String sourceWay;

    /**
     * 来源项目.
     */
    @ApiModelProperty("来源项目")
    private String sourceProject;

    /**
     * 文件保存时间, 以天为单位.
     */
    @ApiModelProperty("文件保存范围天")
    private Integer effectiveDays;

    /**
     * 用户账号.
     */
    @ApiModelProperty("用户账号")
    private String userName;

}
