package com.youngdatafan.portal.system.management.summary.model;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/18 5:26 下午
 */
public class FileSummary {
    private String fileType;
    private String createChannel;
    private int cnt;
    private String mon;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getCreateChannel() {
        return createChannel;
    }

    public void setCreateChannel(String createChannel) {
        this.createChannel = createChannel;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }
}
