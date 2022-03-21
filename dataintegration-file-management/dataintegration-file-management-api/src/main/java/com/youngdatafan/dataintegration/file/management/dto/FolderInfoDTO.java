package com.youngdatafan.dataintegration.file.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 文件夹信息返回类.
 *
 * @author liuhao
 * @since 2022/1/19 16:19
 */

@Data
@ApiModel("文件夹信息返回类")
public class FolderInfoDTO {

    /**
     * 用户id.
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 文件管理表记录的用户账号信息.
     */
    @ApiModelProperty("文件管理表记录的用户账号信息")
    private String accountOld;

    /**
     * 用户账号.
     */
    @ApiModelProperty("用户账号")
    private String account;

    /**
     * 用户名称.
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 所属部门.
     */
    @ApiModelProperty("所属部门")
    private String deptName;

    /**
     * 所属部门编码.
     */
    @ApiModelProperty("所属部门编码")
    private Integer deptId;

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
     * 备注信息.
     */
    @ApiModelProperty("备注")
    private String notes;

    /**
     * 排序.
     */
    @ApiModelProperty("排序")
    private String order;

    /**
     * 文件夹系统来源.
     */
    @ApiModelProperty("来源平台")
    private String sourceSystem;

    /**
     * 文件夹来源方式.
     */
    @ApiModelProperty("来源方式")
    private String sourceWay;

    /**
     * 文件夹来源项目.
     */
    @ApiModelProperty("来源项目")
    private String sourceProject;

    /**
     * 文件夹路径.
     */
    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("上传时间")
    private Date uploadTime;

    @ApiModelProperty("修改时间")
    private Date lastModifiedTime;
}
