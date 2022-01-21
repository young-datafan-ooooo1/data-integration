package com.youngdatafan.portal.system.management.summary.model;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/18 3:12 下午
 */
public class TaskRunSummary {

    private String startDate;
    private String status;
    private String projectType;
    private int cnt;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}
