package com.youngdatafan.portal.system.management.gateway.entity;

import java.util.Date;
import lombok.Data;

/**
 * DpGatewayRatelimit.
 */
@Data
public class DpGatewayRatelimit {
    /**
     * id.
     */
    private String id;

    /**
     * 资源.
     */
    private String resource;

    /**
     * 资源类型（1：api请求uri）.
     */
    private String type;

    /**
     * 命名空间.
     */
    private String namespace;

    /**
     * 流控模式(1:直接).
     */
    private String mode;

    /**
     * 阈值类型(1:QPS).
     */
    private String thresholdType;

    /**
     * 阈值取值.
     */
    private String thresholdValue;

    /**
     * 超时时间（毫秒）.
     */
    private Integer timeOut;

    /**
     * 失败处置方式（1:快速失败）.
     */
    private String fallBack;

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
