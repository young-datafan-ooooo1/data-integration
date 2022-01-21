package com.youngdatafan.portal.system.management.summary.model;

/**
 * @Author: jeremychen
 * @Descripition:项目的
 * @Date:2020/5/27 1:53 下午
 */
public class ProjectSumMonth {
    private int cnt;
    private String mon;
    private String projectType;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}
