package com.youngdatafan.dataintegration.file.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件定时清理映射类.
 *
 * @author songxiaolang
 * @date 2022-01-04 14:39:25
 */
@ApiModel
@Data
public class DpFileRegularClean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物理主键自动递增.
     */
    @ApiModelProperty("定时清理任务主键")
    private Integer fileRegularCleanId;

    /**
     * 文件保存范围天.
     */
    @ApiModelProperty("文件保存范围天")
    private Integer effectiveDays;

    /**
     * 是否同时按照业务设置保留时长.
     */
    @ApiModelProperty("是否同时按照业务设置保留时长")
    private Integer isUseBusiness;

    /**
     * 创建人Id.
     */
    @ApiModelProperty("创建人Id")
    private Integer createUserId;

    /**
     * 修改人Id.
     */
    @ApiModelProperty("修改人Id")
    private Integer updateUserId;

    /**
     * 创建时间.
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间.
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

}
