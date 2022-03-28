package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;
import lombok.Data;

/**
 * 文件管理表.
 */
@Data
public class DpPortalFileManager {
    /**
     * 文件id.
     */
    private String fileId;

    /**
     * 文件名.
     */
    private String fileName;

    /**
     * 文件类型.
     */
    private String fileType;

    /**
     * 排序.
     */
    private Integer order;

    /**
     * 备注.
     */
    private String notes;

    /**
     * 是否有效.
     */
    private String isValid;

    /**
     * 上传时间.
     */
    private Date uploadTime;

    /**
     * 创建人id.
     */
    private String createUserId;

    /**
     * 分组编号(FK).
     */
    private String groupId;

    /**
     * 分组名称.
     */
    private String groupName;

    /**
     * 是否文件夹.
     */
    private String isFolder;

    /**
     * 创建渠道.
     */
    private String createChannel;

}
