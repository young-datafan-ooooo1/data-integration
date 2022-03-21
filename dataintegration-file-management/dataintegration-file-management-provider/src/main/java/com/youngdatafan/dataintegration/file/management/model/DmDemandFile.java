package com.youngdatafan.dataintegration.file.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 项目需求表样文件表.
 *
 * @author liuhao
 * @since 2022/1/20 10:40
 */
@ApiModel(value = "com-sensesai-escat-file-management-model-DmDemandFile")
@Data
public class DmDemandFile {
    /**
     * 主键Id.
     */
    @ApiModelProperty(value = "主键Id")
    private Integer id;

    /**
     * 任务id.
     */
    @ApiModelProperty(value = "任务id")
    private Integer taskId;

    /**
     * 文件名.
     */
    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * 文件大小.
     */
    @ApiModelProperty(value = "文件大小")
    private Integer fileSize;

    /**
     * 文件路径.
     */
    @ApiModelProperty(value = "文件路径")
    private String filePath;

    /**
     * 创建人.
     */
    @ApiModelProperty(value = "创建人")
    private Integer createUser;

    /**
     * 创建时间.
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人.
     */
    @ApiModelProperty(value = "修改人")
    private Integer updateUser;

    /**
     * 修改时间.
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
