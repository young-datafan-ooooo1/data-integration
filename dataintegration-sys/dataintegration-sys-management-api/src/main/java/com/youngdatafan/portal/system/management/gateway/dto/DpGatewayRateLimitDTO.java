package com.youngdatafan.portal.system.management.gateway.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.StringJoiner;

@ApiModel(description = "限流规则对象")
public class DpGatewayRateLimitDTO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 资源
     */
    @ApiModelProperty(value = "资源")
    private String resource;

    /**
     * 资源类型（1：api请求uri）
     */
    @ApiModelProperty(value = "资源类型（1：api请求uri）")
    private String type;

    /**
     * 命名空间
     */
    @ApiModelProperty(value = "命名空间")
    private String namespace;

    /**
     * 流控模式(1:直接)
     */
    @ApiModelProperty(value = "流控模式(1:直接)")
    private String mode;

    /**
     * 阈值类型(1:QPS)
     */
    @ApiModelProperty(value = "阈值类型(1:QPS)")
    private String thresholdType;

    /**
     * 阈值取值
     */
    @ApiModelProperty(value = "阈值取值")
    private String thresholdValue;

    /**
     * 超时时间（毫秒）
     */
    @ApiModelProperty(value = "超时时间（毫秒）")
    private Integer timeOut;

    /**
     * 失败处置方式（1:快速失败）
     */
    @ApiModelProperty(value = "失败处置方式（1:快速失败）")
    private String fallBack;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 状态（1启用、2失效）
     */
    @ApiModelProperty(value = "状态（1启用、2失效）")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public void setThresholdType(String thresholdType) {
        this.thresholdType = thresholdType;
    }

    public String getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(String thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public String getFallBack() {
        return fallBack;
    }

    public void setFallBack(String fallBack) {
        this.fallBack = fallBack;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DpGatewayRateLimitDTO.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("resource='" + resource + "'")
                .add("type='" + type + "'")
                .add("namespace='" + namespace + "'")
                .add("mode='" + mode + "'")
                .add("thresholdType='" + thresholdType + "'")
                .add("thresholdValue='" + thresholdValue + "'")
                .add("timeOut=" + timeOut)
                .add("fallBack='" + fallBack + "'")
                .add("createTime=" + createTime)
                .add("updateTime=" + updateTime)
                .add("status=" + status)
                .toString();
    }
}
