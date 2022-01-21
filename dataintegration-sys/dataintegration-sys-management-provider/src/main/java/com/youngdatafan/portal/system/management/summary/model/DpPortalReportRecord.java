package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/5/28 2:28 下午
*/
/**
    * 报表分析记录表
    */
public class DpPortalReportRecord {
    /**
    * 保存报表id
    */
    private String reportId;

    /**
    * 数据库id
    */
    private String datasourceId;

    /**
    * 查询sql
    */
    private String querySql;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 创建人id
    */
    private String createUserId;

    /**
    * 图表id
    */
    private String chartId;

    /**
    * 图表名称
    */
    private String reportTittle;

    /**
    * 图表对应的表名称
    */
    private String reportTable;

    /**
    * 报表数据
    */
    private String dataJson;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String getReportTittle() {
        return reportTittle;
    }

    public void setReportTittle(String reportTittle) {
        this.reportTittle = reportTittle;
    }

    public String getReportTable() {
        return reportTable;
    }

    public void setReportTable(String reportTable) {
        this.reportTable = reportTable;
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
        sb.append(", reportId=").append(reportId);
        sb.append(", datasourceId=").append(datasourceId);
        sb.append(", querySql=").append(querySql);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", chartId=").append(chartId);
        sb.append(", reportTittle=").append(reportTittle);
        sb.append(", reportTable=").append(reportTable);
        sb.append(", dataJson=").append(dataJson);
        sb.append("]");
        return sb.toString();
    }
}