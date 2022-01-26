package com.youngdatafan.portal.model.management.datasource.entity;

import java.io.Serializable;

/**
 * (DpPortalUserDatasource)实体类
 *
 * @author makejava
 * @since 2020-11-30 18:25:06
 */
public class DpPortalUserDatasource implements Serializable {
    private static final long serialVersionUID = -10979399037099124L;
    
    private String userId;
    
    private String dsId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDsId() {
        return dsId;
    }

    public void setDsId(String dsId) {
        this.dsId = dsId;
    }

}