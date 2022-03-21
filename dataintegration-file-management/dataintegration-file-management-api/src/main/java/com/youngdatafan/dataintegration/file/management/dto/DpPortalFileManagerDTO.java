package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 文件信息返回类.
 *
 * @Author jeremychen
 * @Descripition:
 * @Date 2020/4/8 15:41
 */
@Data
@ApiModel("文件信息返回类")
public class DpPortalFileManagerDTO {
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
     * 真实的文件名.
     */
    @ApiModelProperty("文件真实名")
    private String fileRealName;

    /**
     * 文件类型.
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 文件大小.
     */
    @ApiModelProperty("文件大小")
    private String fileSize;


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
     * 文件路径.
     */
    @ApiModelProperty("文件路径")
    private String filePath;

    /**
     * 上传时间.
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 创建人id.
     */
    @ApiModelProperty("创建人id")
    private String createUserId;


    /**
     * 创建人名称.
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 是否是文件夹.
     */
    @ApiModelProperty("是否文件夹")
    private String isFolder;

    /**
     * 文件前段展示大小.
     */
    @ApiModelProperty("文件大小前端展示")
    private String fileSizeShow;

    /**
     * 父级目录pid.
     */
    @ApiModelProperty("父id")
    private String pid;


    /**
     * 文件创建渠道.
     */
    @ApiModelProperty("创建渠道")
    private String createChannel;

    /**
     * 文件系统来源.
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
     * 文件保存期限,默认以天为单位.
     */
    @ApiModelProperty("文件保存范围天")
    private Integer effectiveDays;

    /**
     * 用户名.
     */
    @ApiModelProperty("用户账号")
    private String userName;

}
