package com.youngdatafan.portal.system.management.summary.model;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/18 6:17 下午
 */
public class ProjectStatusSum {

    private String projecType;
    private int total;
    private int ONLINE;
    private int OFFLINE;
    private int CHECKING;
    private int REFUSE;
    private int CANCEL;
    private int CANCELING;
    private int wfb;

    public String getProjecType() {
        return projecType;
    }

    public void setProjecType(String projecType) {
        this.projecType = projecType;
    }

    public int getONLINE() {
        return ONLINE;
    }

    public void setONLINE(int ONLINE) {
        this.ONLINE = ONLINE;
    }

    public int getOFFLINE() {
        return OFFLINE;
    }

    public void setOFFLINE(int OFFLINE) {
        this.OFFLINE = OFFLINE;
    }

    public int getCHECKING() {
        return CHECKING;
    }

    public void setCHECKING(int CHECKING) {
        this.CHECKING = CHECKING;
    }

    public int getREFUSE() {
        return REFUSE;
    }

    public void setREFUSE(int REFUSE) {
        this.REFUSE = REFUSE;
    }

    public int getCANCEL() {
        return CANCEL;
    }

    public void setCANCEL(int CANCEL) {
        this.CANCEL = CANCEL;
    }

    public int getCANCELING() {
        return CANCELING;
    }

    public void setCANCELING(int CANCELING) {
        this.CANCELING = CANCELING;
    }

    public int getWfb() {
        return wfb;
    }

    public void setWfb(int wfb) {
        this.wfb = wfb;
    }
}
