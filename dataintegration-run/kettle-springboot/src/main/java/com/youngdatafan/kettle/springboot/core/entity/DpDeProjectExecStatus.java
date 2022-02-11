package com.youngdatafan.kettle.springboot.core.entity;

import com.youngdatafan.kettle.springboot.core.bean.TableType;

import java.util.Date;

public class DpDeProjectExecStatus {
    /**
     * 项目编号(PK)
     */
    private String projectId;

    /**
     * 执行用户编号
     */
    private String userId;

    /**
     * 执行用户名
     */
    private String userName;

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 临时表名称
     */
    private String tempTableName;

    /**
     * 临时表类型
     */
    private TableType tableType;

    /**
     * 数据查询sql
     */
    private String dataQuerySql;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 执行时间
     */
    private Date lastExecTime;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getTempTableName() {
        return tempTableName;
    }

    public void setTempTableName(String tempTableName) {
        this.tempTableName = tempTableName;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public String getDataQuerySql() {
        return dataQuerySql;
    }

    public void setDataQuerySql(String dataQuerySql) {
        this.dataQuerySql = dataQuerySql;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastExecTime() {
        return lastExecTime;
    }

    public void setLastExecTime(Date lastExecTime) {
        this.lastExecTime = lastExecTime;
    }
}
