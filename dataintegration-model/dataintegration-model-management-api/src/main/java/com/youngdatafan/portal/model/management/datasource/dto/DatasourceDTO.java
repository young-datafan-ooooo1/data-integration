package com.youngdatafan.portal.model.management.datasource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 5:02 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */

@ApiModel(description = "数据源返回对象")
public class DatasourceDTO {

    @ApiModelProperty(value = "数据源名称", required = true)
    @Length(max = 30, min = 2)
    private String dsName;

    @ApiModelProperty(value = "数据源描述", required = true)
    private String describe;

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dsType;

    @ApiModelProperty(value = "数据源地址", required = true)
    private String dsUrl;

    @ApiModelProperty(value = "端口", required = true)
    private String port;

    @ApiModelProperty(value = "数据源schema", required = true)
    private String databse;

    @ApiModelProperty(value = "host", required = true)
    private String dbhost;

    @ApiModelProperty(value = "数据源驱动类", required = true)
    private String driverClassName;

    @ApiModelProperty(value = "数据源用户名", required = true)
    private String dsUsername;

    @ApiModelProperty(value = "是否启用", required = true)
    private String enabled;

    @ApiModelProperty(value = "数据源创建时间")
    private Date createTime;

    @ApiModelProperty(value = "数据源更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "数据源创建人id", required = true)
    private String createUserId;

    @ApiModelProperty(value = "数据源密码", required = true)
    private String dsPassword;

    @ApiModelProperty(value = "数据源id")
    private String datasourceId;

    @ApiModelProperty(value = "数据源链接配置", required = true)
    private String dsConectorSetting;

    @ApiModelProperty(value = "来源平台")
    private String sourcePlatform;

    @ApiModelProperty(value = "access", required = true)
    private String access;

    @JsonProperty("CUSTOM_DRIVER_CLASS")
    private String  CUSTOM_DRIVER_CLASS;

    @JsonProperty("CUSTOM_URL")
    private String  CUSTOM_URL;

    @JsonProperty("DATABASE_DIALECT_ID")
    private String  DATABASE_DIALECT_ID;


    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getCUSTOM_DRIVER_CLASS() {
        return CUSTOM_DRIVER_CLASS;
    }

    public void setCUSTOM_DRIVER_CLASS(String CUSTOM_DRIVER_CLASS) {
        this.CUSTOM_DRIVER_CLASS = CUSTOM_DRIVER_CLASS;
    }

    public String getCUSTOM_URL() {
        return CUSTOM_URL;
    }

    public void setCUSTOM_URL(String CUSTOM_URL) {
        this.CUSTOM_URL = CUSTOM_URL;
    }

    public String getDATABASE_DIALECT_ID() {
        return DATABASE_DIALECT_ID;
    }

    public void setDATABASE_DIALECT_ID(String DATABASE_DIALECT_ID) {
        this.DATABASE_DIALECT_ID = DATABASE_DIALECT_ID;
    }

    private List<DatasourceParamsDTO> paramsDTOS;

    public String getDbhost() {
        return dbhost;
    }

    public void setDbhost(String dbhost) {
        this.dbhost = dbhost;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabse() {
        return databse;
    }

    public void setDatabse(String databse) {
        this.databse = databse;
    }

    public List<DatasourceParamsDTO> getParamsDTOS() {
        return paramsDTOS;
    }

    public void setParamsDTOS(List<DatasourceParamsDTO> paramsDTOS) {
        this.paramsDTOS = paramsDTOS;
    }

    public String getSourcePlatform() {
        return sourcePlatform;
    }

    public void setSourcePlatform(String sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public String getDatasourceId() {
        return datasourceId;
    }


    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDsConectorSetting() {
        return dsConectorSetting;
    }

    public void setDsConectorSetting(String dsConectorSetting) {
        this.dsConectorSetting = dsConectorSetting;
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

    public String getDsUsername() {
        return dsUsername;
    }

    public void setDsUsername(String dsUsername) {
        this.dsUsername = dsUsername;
    }


    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
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
        this.createUserId = createUserId;
    }

    public String getDsPassword() {
        return dsPassword;
    }

    public void setDsPassword(String dsPassword) {
        this.dsPassword = dsPassword;
    }

    @Override
    public String toString() {
        return "DatasourceDTO{" +
                "dsName='" + dsName + '\'' +
                ", describe='" + describe + '\'' +
                ", dsType='" + dsType + '\'' +
                ", dsUrl='" + dsUrl + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", dsUsername='" + dsUsername + '\'' +
                ", enabled='" + enabled + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUserId='" + createUserId + '\'' +
                ", dsPassword='" + dsPassword + '\'' +
                ", datasourceId='" + datasourceId + '\'' +
                ", dsConectorSetting='" + dsConectorSetting + '\'' +
                '}';
    }
}
