package com.youngdatafan.portal.common.project.entity;

public class DpPortalProjectUserGrant {
    /**
    * 项目编号(PK)
    */
    private String projectId;

    /**
    * 用户编号(PK)
    */
    private String userId;

    /**
    * 操作模式
    */
    private String opModel;

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

    public String getOpModel() {
        return opModel;
    }

    public void setOpModel(String opModel) {
        this.opModel = opModel;
    }
}