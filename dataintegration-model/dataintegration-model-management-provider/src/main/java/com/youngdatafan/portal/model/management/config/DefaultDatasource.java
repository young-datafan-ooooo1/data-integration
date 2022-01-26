package com.youngdatafan.portal.model.management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/28 6:17 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "default.clickhouse")
public class DefaultDatasource {

    private String dsName;

    private String describe;

    private String dsType;

    private String dsUrl;
    private String driverClassName;
    private String dsUserName;
    private String dsPassword;
    private String createUserId;

    private String sourcePlatform;
    private String datasourceId;

    private String enabled;

    private Date createTime;

    private Date updateTime;

    private String dsConectorSetting;

    public String getSourcePlatform() {
        return sourcePlatform;
    }

    public void setSourcePlatform(String sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public String getDsConectorSetting() {
        return dsConectorSetting;
    }

    public void setDsConectorSetting(String dsConectorSetting) {
        this.dsConectorSetting = dsConectorSetting;
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

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDsType() {
        return dsType;
    }

    public void setDsType(String dsType) {
        this.dsType = dsType;
    }

    public String getDsUrl() {
        return dsUrl;
    }

    public void setDsUrl(String dsUrl) {
        this.dsUrl = dsUrl;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDsUserName() {
        return dsUserName;
    }

    public void setDsUserName(String dsUserName) {
        this.dsUserName = dsUserName;
    }

    public String getDsPassword() {
        return dsPassword;
    }

    public void setDsPassword(String dsPassword) {
        this.dsPassword = dsPassword;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public String toString() {
        return "DefaultDatasource{" +
                "dsName='" + dsName + '\'' +
                ", describe='" + describe + '\'' +
                ", dsType='" + dsType + '\'' +
                ", dsUrl='" + dsUrl + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", dsUserName='" + dsUserName + '\'' +
                ", dsPassword='" + dsPassword + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", datasourceId='" + datasourceId + '\'' +
                '}';
    }
}
