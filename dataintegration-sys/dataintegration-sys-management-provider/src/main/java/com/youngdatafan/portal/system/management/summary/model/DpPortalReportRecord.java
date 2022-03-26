package com.youngdatafan.portal.system.management.summary.model;

import java.util.Date;
import lombok.Data;


/**
 * 报表分析记录表.
 */
@Data
public class DpPortalReportRecord {
    /**
     * 保存报表id.
     */
    private String reportId;

    /**
     * 数据库id.
     */
    private String datasourceId;

    /**
     * 查询sql.
     */
    private String querySql;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 创建人id.
     */
    private String createUserId;

    /**
     * 图表id.
     */
    private String chartId;

    /**
     * 图表名称.
     */
    private String reportTittle;

    /**
     * 图表对应的表名称.
     */
    private String reportTable;

    /**
     * 报表数据.
     */
    private String dataJson;

}
