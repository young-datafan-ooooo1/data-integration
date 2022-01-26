package com.youngdatafan.portal.model.management.businessmodel.entity;

import java.io.Serializable;

/**
 * 业务模型关联sql表(DpPortalBusinessModelQuerysql)实体类
 *
 * @author makejava
 * @since 2020-07-15 11:22:11
 */
public class DpPortalBusinessModelQuerysql implements Serializable {
    private static final long serialVersionUID = -45859823690560256L;
    /**
    * 业务模型id
    */
    private String businessModelId;
    /**
    * 业务模型sql
    */
    private Object businessModelSql;


    public DpPortalBusinessModelQuerysql(String businessModelId, Object businessModelSql) {
        this.businessModelId = businessModelId;
        this.businessModelSql = businessModelSql;
    }

    public String getBusinessModelId() {
        return businessModelId;
    }

    public void setBusinessModelId(String businessModelId) {
        this.businessModelId = businessModelId;
    }

    public Object getBusinessModelSql() {
        return businessModelSql;
    }

    public void setBusinessModelSql(Object businessModelSql) {
        this.businessModelSql = businessModelSql;
    }

}