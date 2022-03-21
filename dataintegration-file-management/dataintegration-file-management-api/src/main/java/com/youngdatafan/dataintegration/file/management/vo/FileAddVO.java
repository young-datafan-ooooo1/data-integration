package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 文件增加接受信息类.
 *
 * @Author jeremychen
 * @Descripition:
 * @Date 2020/4/8 15:41
 */
@ApiModel("新增文件接受信息类")
@Data
public class FileAddVO {
    /**
     * 文件id.
     */
    @ApiModelProperty(value = "文件id", required = true)
    private String fileId;

    /**
     * 父目录id.
     */
    @ApiModelProperty(value = "父目录id", required = true)
    private String pid;

    /**
     * 文件名称.
     */
    @ApiModelProperty(value = "文件名称", required = true)
    private String fileName;

    /**
     * 文件夹名称.
     */
    @ApiModelProperty(value = "文件夹名称")
    private String fileFolderName;

    /**
     * 文件类型.
     */
    @ApiModelProperty(value = "文件类型", required = true)
    private String fileType;

    /**
     * 排序.
     */
    @ApiModelProperty(value = "排序", required = true)
    private Integer order;

    /**
     * 备注.
     */
    @ApiModelProperty(value = "备注", required = false)
    private String notes;

    /**
     * 是否文件夹.
     */
    @ApiModelProperty(value = "是否文件夹", required = true)
    @NotBlank
    private String isFolder;

    @ApiModelProperty(value = "保留天数")
    private Integer effectiveDays;

}
