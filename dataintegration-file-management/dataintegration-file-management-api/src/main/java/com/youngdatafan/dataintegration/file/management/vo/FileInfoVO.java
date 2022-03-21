package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 文件基础信息类.
 *
 * @author liuhao
 * @since 2022/1/19 16:36
 */

@Data
@ApiModel("文件基础信息类")
public class FileInfoVO {

    /**
     * 文件id.
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 文件名称.
     */
    @ApiModelProperty("文件名称")
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
     * 用户名称.
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 用户id.
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 部门id.
     */
    @ApiModelProperty("部门id")
    private Integer deptId;

    /**
     * 部门名称.
     */
    @ApiModelProperty("部门名称")
    private String deptName;

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
     * 文件来源平台.
     */
    @ApiModelProperty("来源平台")
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
     * 文件更新时间.
     */
    @ApiModelProperty("更新时间")
    private Date lastModifiedTime;

    /**
     * 文件继续保留时长.
     */
    @ApiModelProperty("剩余时长")
    private Integer remainingDay;

    /**
     * 文件总计保留时长.
     */
    @ApiModelProperty("保留时长")
    private Integer effectiveDay;

    /**
     * 文件路径.
     */
    @ApiModelProperty("文件路径")
    private String filePath;
}
