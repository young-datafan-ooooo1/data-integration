package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;


@ApiModel(description = "集成数据源输入接口对象")
public class JCDataSourceVO {

    @ApiModelProperty(value = "数据库id", required = true)
    private String datasourceId;

    @ApiModelProperty(value = "数据源名称", required = true)
    @Length(max = 30, min = 2)
    private String dsName;

    @ApiModelProperty(value = "数据源描述")
    private String describe;

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dsType;

    @ApiModelProperty(value = "数据源用户名", required = true)
    private String dsUsername;

    @ApiModelProperty(value = "数据源密码" )
    private String dsPassword;

    @ApiModelProperty(value = "来源平台", required = true)
    private String sourcePlatform;

    @ApiModelProperty(value = "HOST")
    private String dsHost;

    @ApiModelProperty(value = "数据库名称")
    private String database;


    @ApiModelProperty(value = "端口")
    private String dsPort;


    @ApiModelProperty(value = "access", required = true)
    private String access;

    @ApiModelProperty(value = "高级参数")
    private  AdvancedParametersVO  dsConnectorSetting;



    @ApiModelProperty(value = "CUSTOM_DRIVER_CLASS")
    private String  customDriverClass;

    @ApiModelProperty(value = "CUSTOM_URL")
    private String  customUrl;

    @ApiModelProperty(value = "DATABASE_DIALECT_ID")
    private String  databaseDialectId;



    public String getCustomDriverClass() {
        return customDriverClass;
    }

    public void setCustomDriverClass(String customDriverClass) {
        this.customDriverClass = customDriverClass;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getDatabaseDialectId() {
        return databaseDialectId;
    }

    public void setDatabaseDialectId(String databaseDialectId) {
        this.databaseDialectId = databaseDialectId;
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

    public AdvancedParametersVO getDsConnectorSetting() {
        return dsConnectorSetting;
    }

    public void setDsConnectorSetting(AdvancedParametersVO dsConnectorSetting) {
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
        return "JCDataSourceVO{" +
                "datasourceId='" + datasourceId + '\'' +
                ", dsName='" + dsName + '\'' +
                ", describe='" + describe + '\'' +
                ", dsType='" + dsType + '\'' +
                ", dsUsername='" + dsUsername + '\'' +
                ", dsPassword='" + dsPassword + '\'' +
                ", sourcePlatform='" + sourcePlatform + '\'' +
                ", dsHost='" + dsHost + '\'' +
                ", database='" + database + '\'' +
                ", dsPort='" + dsPort + '\'' +
                ", access='" + access + '\'' +
                ", dsConnectorSetting=" + dsConnectorSetting +
                ", customDriverClass='" + customDriverClass + '\'' +
                ", customUrl='" + customUrl + '\'' +
                ", databaseDialectId='" + databaseDialectId + '\'' +
                '}';
    }
}
