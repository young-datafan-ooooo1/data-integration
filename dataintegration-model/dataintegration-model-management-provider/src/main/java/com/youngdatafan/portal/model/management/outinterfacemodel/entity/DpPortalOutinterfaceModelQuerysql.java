package com.youngdatafan.portal.model.management.outinterfacemodel.entity;

import java.io.Serializable;

/**
 * 业务模型关联sql表(DpPortalBusinessModelQuerysql)实体类
 *
 * @author makejava
 * @since 2020-07-15 11:22:11
 */
public class DpPortalOutinterfaceModelQuerysql implements Serializable {
    private static final long serialVersionUID = -45859823690560256L;
    /**
    * 业务模型id
    */
    private String outinterfaceModelId;
    /**
    * 业务模型sql
    */
    private Object outinterfaceModelSql;


    public DpPortalOutinterfaceModelQuerysql(String outinterfaceModelId, Object outinterfaceModelSql) {
        this.outinterfaceModelId = outinterfaceModelId;
        this.outinterfaceModelSql = outinterfaceModelSql;
    }

    public String getOutinterfaceModelId() {
        return outinterfaceModelId;
    }

    public void setOutinterfaceModelId(String outinterfaceModelId) {
        this.outinterfaceModelId = outinterfaceModelId;
    }

    public Object getOutinterfaceModelSql() {
        return outinterfaceModelSql;
    }

    public void setOutinterfaceModelSql(Object outinterfaceModelSql) {
        this.outinterfaceModelSql = outinterfaceModelSql;
    }
}
