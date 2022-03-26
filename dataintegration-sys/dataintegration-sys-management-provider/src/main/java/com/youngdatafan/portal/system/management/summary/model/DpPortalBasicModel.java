package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;
import lombok.Data;

/**
 * 基础模型信息表.
 */
@Data
public class DpPortalBasicModel {
    /**
     * 模型名称做ID(PK).
     */
    private String modelName;

    /**
     * 模型描述.
     */
    private String description;

    /**
     * 模型类型.
     */
    private String modelType;

    /**
     * 关联数据源(FK).
     */
    private String dsName;

    /**
     * 关联表schema.
     */
    private String tableSchema;

    /**
     * 关联表名.
     */
    private String tableName;

    /**
     * 表中文名称.
     */
    private String tableChineseName;

    /**
     * 表描述.
     */
    private String tableDescription;

    /**
     * 统计信息收集时间.
     */
    private Date statisticsTime;

    /**
     * 表记录行数.
     */
    private Integer rowCnt;

    /**
     * 是否启用.
     */
    private String enabled;

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
     * 排序.
     */
    private String modelSort;

    /**
     * 模型中文名.
     */
    private String cName;

}
