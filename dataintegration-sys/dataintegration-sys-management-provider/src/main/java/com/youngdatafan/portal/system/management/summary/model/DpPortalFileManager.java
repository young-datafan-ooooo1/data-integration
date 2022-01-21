package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/5/28 2:28 下午
*/
/**
    * 文件管理表
    */
public class DpPortalFileManager {
    /**
    * 文件id
    */
    private String fileId;

    /**
    * 文件名
    */
    private String fileName;

    /**
    * 文件类型
    */
    private String fileType;

    /**
    * 排序
    */
    private Integer order;

    /**
    * 备注
    */
    private String notes;

    /**
    * 是否有效
    */
    private String isValid;

    /**
    * 上传时间
    */
    private Date uploadTime;

    /**
    * 创建人id
    */
    private String createUserId;

    /**
    * 分组编号(FK)
    */
    private String groupId;

    /**
    * 分组名称
    */
    private String groupName;

    /**
    * 是否文件夹
    */
    private String isFolder;

    /**
    * 创建渠道
    */
    private String createChannel;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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

    public String getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(String isFolder) {
        this.isFolder = isFolder;
    }

    public String getCreateChannel() {
        return createChannel;
    }

    public void setCreateChannel(String createChannel) {
        this.createChannel = createChannel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fileId=").append(fileId);
        sb.append(", fileName=").append(fileName);
        sb.append(", fileType=").append(fileType);
        sb.append(", order=").append(order);
        sb.append(", notes=").append(notes);
        sb.append(", isValid=").append(isValid);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", groupId=").append(groupId);
        sb.append(", groupName=").append(groupName);
        sb.append(", isFolder=").append(isFolder);
        sb.append(", createChannel=").append(createChannel);
        sb.append("]");
        return sb.toString();
    }
}