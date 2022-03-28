package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;
import lombok.Data;

/**
 * 报表看板表.
 */
@Data
public class DpPortalDashboard {
    /**
     * 看板id.
     */
    private String dashboardId;

    /**
     * 看板名称.
     */
    private String dashboardName;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 更新时间.
     */
    private Date updateTime;

    /**
     * 看板是否公开.
     */
    private String dashboardIsPubulic;

    /**
     * 创建人.
     */
    private String createUserId;

    /**
     * 数据格式.
     */
    private String dataJson;

}
