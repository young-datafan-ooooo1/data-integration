package com.youngdatafan.portal.common.project.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 项目.
 */
@Setter
@Getter
public class DpPortalProjectUserGrant {
    /**
    * 项目编号(PK).
    */
    private String projectId;

    /**
    * 用户编号(PK).
    */
    private String userId;

    /**
    * 操作模式.
    */
    private String opModel;
}
