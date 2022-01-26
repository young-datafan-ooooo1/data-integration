package com.youngdatafan.portal.model.management.datasource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.util.List;


public class JCDataSourceDTO {

    @ApiModelProperty(value = "数据源名称", required = true)
    @Length(max = 30, min = 2)
    private String dsName;

    @ApiModelProperty(value = "数据源描述", required = true)
    private String describe;

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dsType;

    @ApiModelProperty(value = "数据源用户名", required = true)
    private String dsUsername;

    @ApiModelProperty(value = "数据源密码", required = true)
    private String dsPassword;

    @ApiModelProperty(value = "来源平台", required = true)
    private String sourcePlatform;

    @ApiModelProperty(value = "HOST", required = true)
    private String dsHost;


    @ApiModelProperty(value = "端口")
    private String dsPort;


    @ApiModelProperty(value = "access", required = true)
    private String access;

    @ApiModelProperty(value = "高级参数")
    private String dsConnectorSetting;

    @ApiModelProperty(value = "数据库名称")
    private String database;


    @ApiModelProperty(value = "数据库id")
    private String datasourceId;

    @JsonProperty("CUSTOM_DRIVER_CLASS")
    private String  CUSTOM_DRIVER_CLASS;

    @JsonProperty("CUSTOM_URL")
    private String  CUSTOM_URL;

    @JsonProperty("DATABASE_DIALECT_ID")
    private String  DATABASE_DIALECT_ID;

    private List<DatasourceParamsDTO> paramsDTOS;

    public List<DatasourceParamsDTO> getParamsDTOS() {
        return paramsDTOS;
    }

    public void setParamsDTOS(List<DatasourceParamsDTO> paramsDTOS) {
        this.paramsDTOS = paramsDTOS;
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

    public String getDsUsername() {
        return dsUsername;
    }

    public void setDsUsername(String dsUsername) {
        this.dsUsername = dsUsername;
    }

    public String getDsPassword() {
        return dsPassword;
    }

    public void setDsPassword(String dsPassword) {
        this.dsPassword = dsPassword;
    }

    public String getSourcePlatform() {
        return sourcePlatform;
    }

    public void setSourcePlatform(String sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public String getDsHost() {
        return dsHost;
    }

    public void setDsHost(String dsHost) {
        this.dsHost = dsHost;
    }

    public String getDsPort() {
        return dsPort;
    }

    public void setDsPort(String dsPort) {
        this.dsPort = dsPort;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }


    public String getDsConnectorSetting() {
        return dsConnectorSetting;
    }

    public void setDsConnectorSetting(String dsConnectorSetting) {
        this.dsConnectorSetting = dsConnectorSetting;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    @Override
    public String toString() {
        return "JCDataSourceDTO{" +
                "dsName='" + dsName + '\'' +
                ", describe='" + describe + '\'' +
                ", dsType='" + dsType + '\'' +
                ", dsUsername='" + dsUsername + '\'' +
                ", dsPassword='" + dsPassword + '\'' +
                ", sourcePlatform='" + sourcePlatform + '\'' +
                ", dsHost='" + dsHost + '\'' +
                ", dsPort='" + dsPort + '\'' +
                ", access='" + access + '\'' +
                ", dsConnectorSetting='" + dsConnectorSetting + '\'' +
                ", database='" + database + '\'' +
                ", datasourceId='" + datasourceId + '\'' +
                '}';
    }
}
