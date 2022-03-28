package com.youngdatafan.portal.system.management.log.entity;

import java.util.Date;
import lombok.Data;

/**
 * DpPortalLog.
 */
@Data
public class DpPortalLog {
    /**
     * (PK).
     */
    private Long logId;

    /**
     * 服务名称.
     */
    private String serverName;

    /**
     * 请求uri.
     */
    private String reqUri;

    /**
     * 远程ip地址.
     */
    private String reqIp;

    /**
     * 请求时间.
     */
    private Date reqTime;

    /**
     * 接收字节数.
     */
    private Integer reqSize;

    /**
     * 请求用户id.
     */
    private String reqUserId;

    /**
     * 请求用户名.
     */
    private String reqUserName;

    /**
     * 执行时间.
     */
    private Integer excTime;

}
