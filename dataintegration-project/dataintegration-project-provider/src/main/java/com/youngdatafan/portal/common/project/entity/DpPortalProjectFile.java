package com.youngdatafan.portal.common.project.entity;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目.
 */
@Setter
@Getter
public class DpPortalProjectFile {
    /**
     * 项目编号(PK).
     */
    private String projectId;

    /**
     * 项目文件内容.
     */
    private String projectFile;

    /**
     * 项目版本号.
     */
    private String projectVersion;

    /**
     * 创建时间.
     */
    private Date createTime;

}
