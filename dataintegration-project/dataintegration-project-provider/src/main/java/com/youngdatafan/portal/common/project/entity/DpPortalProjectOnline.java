package com.youngdatafan.portal.common.project.entity;

import java.util.Date;
import lombok.Data;

/**
 * 项目.
 */
@Data
public class DpPortalProjectOnline {
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
    * 审核状态.
    */
    private String status;

    /**
    * 提交时间.
    */
    private Date createTime;

    /**
    * 频率id.
    */
    private String frequencyId;

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
    * 审核人.
    */
    private String reviewUserId;

    /**
    * 审核人名称.
    */
    private String reviewUserName;

    /**
    * 审核备注.
    */
    private String reviewNotes;

    /**
    * 运行开始时间.
    */
    private String runStartTime;
}
