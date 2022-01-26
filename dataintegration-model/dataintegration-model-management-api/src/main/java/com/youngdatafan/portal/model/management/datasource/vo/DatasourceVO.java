package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : 数据源接口输入参数</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 5:13 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "数据源输入接口对象")
public class DatasourceVO {

    @ApiModelProperty(value = "数据源名称", required = true)
    @Length(max = 30, min = 2)
    private String dsName;

    @ApiModelProperty(value = "数据源描述", required = true)
    private String describe;

    @ApiModelProperty(value = "数据源类型", required = true)
    private String dsType;

    @ApiModelProperty(value = "数据源地址", required = true)
    private String dsUrl;

    @ApiModelProperty(value = "数据源驱动类", required = true)
    private String driverClassName;

    @ApiModelProperty(value = "数据源用户名", required = true)
    private String dsUsername;

    @ApiModelProperty(value = "数据源密码", required = true)
    private String dsPassword;

    @ApiModelProperty(value = "是否启用", required = true)
    private String enabled;

    @ApiModelProperty(value = "数据源创建时间")
    private Date createTime;

    @ApiModelProperty(value = "数据源更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "数据源创建人id", required = true)
    private String createUserId;

    @ApiModelProperty(value = "高级连接配置", required = true)
    private String dsConectorSetting;

    @ApiModelProperty(value = "数据库id", required = true)
    private String datasourceId;

    @ApiModelProperty(value = "来源平台")
    private String sourcePlatform;

    @ApiModelProperty(value = "高级参数")
    private  AdvancedParametersVO  advancedParametersVO;

    public AdvancedParametersVO getAdvancedParametersVO() {
        return advancedParametersVO;
    }

    public void setAdvancedParametersVO(AdvancedParametersVO advancedParametersVO) {
        this.advancedParametersVO = advancedParametersVO;
    }

    public void setSourcePlatform(String sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }

    public String getSourcePlatform() {
        return sourcePlatform;
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

    public String getDsPassword() {
        return dsPassword;
    }

    public void setDsPassword(String dsPassword) {
        this.dsPassword = dsPassword;
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

    @Override
    public String toString() {
        return "DatasourceVO{" +
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
                ", dsConectorSetting='" + dsConectorSetting + '\'' +
                ", datasourceId='" + datasourceId + '\'' +
                '}';
    }
}
