package com.youngdatafan.portal.system.management.user.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * DpPortalResource.
 */
@Data
public class DpPortalResource {
    /**
     * 资源编号(PK).
     */
    private String resId;

    /**
     * 资源名称.
     */
    private String resName;

    /**
     * 资源描述.
     */
    private String describe;

    /**
     * 资源url地址.
     */
    private String resUrl;

    /**
     * 资源父id.
     */
    private String resPid;

    /**
     * 资源级别.
     */
    private Integer resLevel;

    /**
     * 资源类型.
     */
    private String resType;

    /**
     * 资源排序.
     */
    private Integer resOrder;

    /**
     * 资源状态.
     */
    private String status;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 修改时间.
     */
    private Date updateTime;

    /**
     * 前端路由地址.
     */
    private String routeUrl;

    private List<DpPortalResource> childResources;

}
