package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;
import lombok.Data;

/**
 * 业务模型信息表.
 */
@Data
public class DpPortalBusinessModel {
    /**
     * 模型名称(PK).
     */
    private String modelName;

    /**
     * 模型中文名.
     */
    private String chineseName;

    /**
     * 关联基础模型名称.
     */
    private String basicModelName;

    /**
     * 模型描述.
     */
    private String description;

    /**
     * 数据过滤条件.
     */
    private String filterRule;

    /**
     * 是否启用.
     */
    private String enabled;

    /**
     * 查询SQL.
     */
    private String querySql;

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
     * 业务模型排序.
     */
    private String modelSort;

}
