package com.youngdatafan.portal.system.management.gateway.entity;

import java.util.Date;
import lombok.Data;

/**
 * DpGatewayRoute.
 */
@Data
public class DpGatewayRoute {
    /**
     * 路由id.
     */
    private String id;

    /**
     * 路由转发地址.
     */
    private String uri;

    /**
     * 排序.
     */
    private Integer order;

    /**
     * 预处理规则.
     */
    private String predicates;

    /**
     * 过滤器.
     */
    private String filters;

    /**
     * 元数据.
     */
    private String metadata;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 更新时间.
     */
    private Date updateTime;

    /**
     * 状态（1启用、2失效）.
     */
    private Integer status;

}
