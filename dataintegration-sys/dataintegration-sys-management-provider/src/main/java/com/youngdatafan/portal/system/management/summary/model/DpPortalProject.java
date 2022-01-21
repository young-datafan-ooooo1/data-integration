package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/5/28 2:28 下午
*/
/**
    * 项目信息表
    */
public class DpPortalProject {
    /**
    * 项目编号(PK)
    */
    private String projectId;

    /**
    * 项目名称
    */
    private String projectName;

    /**
    * 项目描述
    */
    private String description;

    /**
    * 项目类型
    */
    private String projectType;

    /**
    * 项目状态
    */
    private String projectStatus;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    /**
    * 创建者
    */
    private String createUserId;

    /**
    * 创建者名称
    */
    private String createUserName;

    /**
    * 最后修改者
    */
    private String updateUserId;

    /**
    * 最后修改者名称
    */
    private String updateUserName;

    /**
    * 分组编号(FK)
    */
    private String groupId;

    /**
    * 分组名称
    */
    private String groupName;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", projectId=").append(projectId);
        sb.append(", projectName=").append(projectName);
        sb.append(", description=").append(description);
        sb.append(", projectType=").append(projectType);
        sb.append(", projectStatus=").append(projectStatus);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", createUserName=").append(createUserName);
        sb.append(", updateUserId=").append(updateUserId);
        sb.append(", updateUserName=").append(updateUserName);
        sb.append(", groupId=").append(groupId);
        sb.append(", groupName=").append(groupName);
        sb.append("]");
        return sb.toString();
    }
}