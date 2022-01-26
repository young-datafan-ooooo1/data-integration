package com.youngdatafan.portal.model.management.datasource.entity;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "数据源对象")
public class Datasource implements Serializable {
    private String dsName;

    private String describe;

    private String dsType;

    private String dsUrl;

    private String driverClassName;

    private String dsUsername;

    private String dsPassword;

    private String enabled;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String datasourceId;

    private String dsConectorSetting;

    private String sourcePlatform;


    private static final long serialVersionUID = 1L;


    public String getSourcePlatform() {
        return sourcePlatform;
    }

    public void setSourcePlatform(String sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName == null ? null : dsName.trim();
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public String getDsType() {
        return dsType;
    }

    public void setDsType(String dsType) {
        this.dsType = dsType == null ? null : dsType.trim();
    }

    public String getDsUrl() {
        return dsUrl;
    }

    public void setDsUrl(String dsUrl) {
        this.dsUrl = dsUrl == null ? null : dsUrl.trim();
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName == null ? null : driverClassName.trim();
    }

    public String getDsUsername() {
        return dsUsername;
    }

    public void setDsUsername(String dsUsername) {
        this.dsUsername = dsUsername == null ? null : dsUsername.trim();
    }

    public String getDsPassword() {
        return dsPassword;
    }

    public void setDsPassword(String dsPassword) {
        this.dsPassword = dsPassword == null ? null : dsPassword.trim();
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId == null ? null : datasourceId.trim();
    }

    public String getDsConectorSetting() {
        return dsConectorSetting;
    }

    public void setDsConectorSetting(String dsConectorSetting) {
        this.dsConectorSetting = dsConectorSetting == null ? null : dsConectorSetting.trim();
    }

    @Override
    public String toString() {
        return "Datasource{" +
                "dsName='" + dsName + '\'' +
                ", describe='" + describe + '\'' +
                ", dsType='" + dsType + '\'' +
                ", dsUrl='" + dsUrl + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", dsUsername='" + dsUsername + '\'' +
                ", dsPassword='" + dsPassword + '\'' +
                ", enabled='" + enabled + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUserId='" + createUserId + '\'' +
                ", datasourceId='" + datasourceId + '\'' +
                ", dsConectorSetting='" + dsConectorSetting + '\'' +
                ", sourcePlatform='" + sourcePlatform + '\'' +
                '}';
    }
}