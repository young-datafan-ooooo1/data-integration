package com.youngdatafan.portal.model.management.basicmodel.entity;

import java.io.Serializable;

/**
 * 基础模型关联sql表(DpPortalBasicModelQuerysql)实体类
 *
 * @author makejava
 * @since 2020-07-15 11:20:54
 */
public class DpPortalBasicModelQuerysql implements Serializable {
    private static final long serialVersionUID = -43919030018805044L;
    /**
    * 基础模型id
    */
    private String basicModelId;
    /**
    * 基础模型sql
    */
    private String basicModelSql;

    public DpPortalBasicModelQuerysql(String basicModelId, String basicModelSql) {
        this.basicModelId = basicModelId;
        this.basicModelSql = basicModelSql;
    }

    public String getBasicModelId() {
        return basicModelId;
    }

    public void setBasicModelId(String basicModelId) {
        this.basicModelId = basicModelId;
    }

    public String getBasicModelSql() {
        return basicModelSql;
    }

    public void setBasicModelSql(String basicModelSql) {
        this.basicModelSql = basicModelSql;
    }
}