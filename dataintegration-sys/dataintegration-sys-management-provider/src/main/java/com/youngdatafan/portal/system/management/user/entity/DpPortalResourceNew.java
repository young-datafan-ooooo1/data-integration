package com.youngdatafan.portal.system.management.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;


/**
 * 资源菜单表.
 */
@ApiModel(value = "com-dp-portal-system-management-user-entity-DpPortalResourceNew")
@Data
public class DpPortalResourceNew {
    /**
     * 资源编号(PK).
     */
    @ApiModelProperty(value = "资源编号(PK)")
    private String resId;

    /**
     * 资源名称.
     */
    @ApiModelProperty(value = "资源名称")
    private String resName;

    /**
     * 资源描述.
     */
    @ApiModelProperty(value = "资源描述")
    private String describe;

    /**
     * 资源url地址.
     */
    @ApiModelProperty(value = "资源url地址")
    private String resUrl;

    /**
     * 资源父id.
     */
    @ApiModelProperty(value = "资源父id")
    private String resPid;

    /**
     * 资源级别.
     */
    @ApiModelProperty(value = "资源级别")
    private String resLevel;

    /**
     * 资源类型.
     */
    @ApiModelProperty(value = "资源类型")
    private String resType;

    /**
     * 资源排序.
     */
    @ApiModelProperty(value = "资源排序")
    private Integer resOrder;

    /**
     * 资源状态.
     */
    @ApiModelProperty(value = "资源状态")
    private String status;

    /**
     * 创建时间.
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改时间.
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
