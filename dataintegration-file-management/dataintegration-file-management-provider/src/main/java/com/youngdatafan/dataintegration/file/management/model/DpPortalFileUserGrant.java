package com.youngdatafan.dataintegration.file.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件管理表.
 *
 * @author lizihao
 * @date 2021-12-27 14:53:37
 */
@ApiModel
@Data
public class DpPortalFileUserGrant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id.
     */
    @ApiModelProperty("主键id")
    private Integer id;

    /**
     * 文件id.
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 父目录id.
     */
    @ApiModelProperty("父目录id")
    private String pid;

    /**
     * 文件名.
     */
    @ApiModelProperty("文件名")
    private Integer userId;

    /**
     * 0文件1文件夹.
     */
    @ApiModelProperty("0文件1文件夹")
    private Integer grantType;

    /**
     * 创建人.
     */
    @ApiModelProperty("创建人")
    private Integer createUser;

    /**
     * 修改人.
     */
    @ApiModelProperty("修改人")
    private Integer updateUser;

    /**
     * 创建时间.
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间.
     */
    @ApiModelProperty("修改时间")
    private Date uploadTime;

}
