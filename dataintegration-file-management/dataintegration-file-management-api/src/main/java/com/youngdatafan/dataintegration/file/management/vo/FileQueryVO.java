package com.youngdatafan.dataintegration.file.management.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 文件查询接受信息类.
 *
 * @author liuhao
 * @since 2022/1/19 16:40
 */

@Data
@ApiModel("文件查询接受信息类")
public class FileQueryVO {


    /**
     * 查询关键字.
     */
    @ApiModelProperty("关键字")
    private String keyword;

    /**
     * 文件最小条件.
     */
    @ApiModelProperty("文件最小")
    private Integer fileSizeMin;

    /**
     * 文件最大条件.
     */
    @ApiModelProperty("文件最大")
    private Integer fileSizeMax;

    /**
     * 查询起始时间.
     */
    @ApiModelProperty("起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 查询结束时间.
     */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 文件夹id.
     */
    @ApiModelProperty("文件夹id")
    private String folderId;

    /**
     * 是否文件管理.
     */
    @ApiModelProperty(value = "是否文件管理", hidden = true)
    private String isAdmin;

    /**
     * 用户id.
     */
    @ApiModelProperty(value = "用户id", hidden = true)
    private String myUserId;

    /**
     * 部门id.
     */
    @ApiModelProperty("部门id")
    private Integer deptId;

    /**
     * 用户id.
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 文件类型.
     */
    @ApiModelProperty("文件类型{我的文件:my；他人文件：grant}")
    private String fileType;
}
