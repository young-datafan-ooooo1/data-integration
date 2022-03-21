package com.youngdatafan.portal.system.management.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 资源返回对象.
 */
@Data
@ApiModel(description = "资源返回对象")
public class ResourceDTO {
    /**
     * 资源编号(PK).
     */
    @ApiModelProperty("资源编号(PK)")
    private String resId;

    /**
     * 资源名称.
     */
    @ApiModelProperty("资源名称")
    private String resName;

    /**
     * 资源描述.
     */
    @ApiModelProperty("资源描述")
    private String describe;

    /**
     * 资源url地址.
     */
    @ApiModelProperty("资源url地址")
    private String resUrl;

    /**
     * 资源父id.
     */
    @ApiModelProperty("资源父id")
    private String resPid;

    /**
     * 资源级别/.
     */
    @ApiModelProperty("资源级别")
    private Integer resLevel;

    /**
     * 资源类型.
     */
    @ApiModelProperty("资源类型 0-菜单 1-按钮 2-控件 3-跳转地址")
    private String resType;

    /**
     * 资源排序.
     */
    @ApiModelProperty("资源排序")
    private Integer resOrder;

    /**
     * 资源状态.
     */
    @ApiModelProperty("资源状态")
    private String status;

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

    @ApiModelProperty("前端路由地址")
    private String routeUrl;

    @ApiModelProperty("子菜单清单")
    private List<ResourceDTO> childResources;

}
