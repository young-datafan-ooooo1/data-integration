package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/5/28 2:28 下午
*/
/**
    * 报表看板表
    */
public class DpPortalDashboard {
    /**
    * 看板id
    */
    private String dashboardId;

    /**
    * 看板名称
    */
    private String dashboardName;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 看板是否公开
    */
    private String dashboardIsPubulic;

    /**
    * 创建人
    */
    private String createUserId;

    /**
    * 数据格式
    */
    private String dataJson;

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDashboardIsPubulic() {
        return dashboardIsPubulic;
    }

    public void setDashboardIsPubulic(String dashboardIsPubulic) {
        this.dashboardIsPubulic = dashboardIsPubulic;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dashboardId=").append(dashboardId);
        sb.append(", dashboardName=").append(dashboardName);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", dashboardIsPubulic=").append(dashboardIsPubulic);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", dataJson=").append(dataJson);
        sb.append("]");
        return sb.toString();
    }
}