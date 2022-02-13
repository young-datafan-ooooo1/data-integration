package com.youngdatafan.di.run.management.server.entity;

import java.util.Date;

public class DpDeSavemodelHistory {
    /**
     * ID
     */
    private Long id;


    /**
     * 保存为模型的模型id，外键
     */
    private String modelId;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 状态（1运行中、2成功、3终止）
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

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
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

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
