package com.youngdatafan.portal.common.project.entity;

import java.util.Date;

public class DpPortalProjectFile {
    /**
     * 项目编号(PK)
     */
    private String projectId;

    /**
     * 项目文件内容
     */
    private String projectFile;

    /**
     * 项目版本号
     */
    private String projectVersion;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(String projectFile) {
        this.projectFile = projectFile;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}