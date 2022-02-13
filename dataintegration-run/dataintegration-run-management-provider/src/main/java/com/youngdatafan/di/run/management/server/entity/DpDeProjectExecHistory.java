package com.youngdatafan.di.run.management.server.entity;

import java.util.Date;

public class DpDeProjectExecHistory {
    /**
    * 流水号
    */
    private Long id;

    /**
    * 项目编号
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
    * 执行环境（TSPT、DDPT）
    */
    private String execEnv;

    /**
    * 执行状态
    */
    private String status;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    /**
    * 执行时间秒
    */
    private Integer execSecond;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getExecEnv() {
        return execEnv;
    }

    public void setExecEnv(String execEnv) {
        this.execEnv = execEnv;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getExecSecond() {
        return execSecond;
    }

    public void setExecSecond(Integer execSecond) {
        this.execSecond = execSecond;
    }
}