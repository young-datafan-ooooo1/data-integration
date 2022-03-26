package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;
import lombok.Data;

/**
 * 项目信息表.
 */
@Data
public class DpPortalProject {
    /**
     * 项目编号(PK).
     */
    private String projectId;

    /**
     * 项目名称.
     */
    private String projectName;

    /**
     * 项目描述.
     */
    private String description;

    /**
     * 项目类型.
     */
    private String projectType;

    /**
     * 项目状态.
     */
    private String projectStatus;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 修改时间.
     */
    private Date updateTime;

    /**
     * 创建者.
     */
    private String createUserId;

    /**
     * 创建者名称.
     */
    private String createUserName;

    /**
     * 最后修改者.
     */
    private String updateUserId;

    /**
     * 最后修改者名称.
     */
    private String updateUserName;

    /**
     * 分组编号(FK).
     */
    private String groupId;

    /**
     * 分组名称.
     */
    private String groupName;

}
