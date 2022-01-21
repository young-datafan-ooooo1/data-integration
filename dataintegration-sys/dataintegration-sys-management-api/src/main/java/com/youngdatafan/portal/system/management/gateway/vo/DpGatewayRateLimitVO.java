package com.youngdatafan.portal.system.management.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.StringJoiner;

@ApiModel(description = "限流规则对象")
public class DpGatewayRateLimitVO {

    /**
     * 资源
     */
    @ApiModelProperty(value = "资源")
    @NotNull
    private String resource;

    /**
     * 资源类型（1：api请求uri）
     */
    @ApiModelProperty(value = "资源类型（1：api请求uri）")
    @NotNull
    private String type;
    /**
     * 命名空间
     */
    @ApiModelProperty(value = "命名空间", required = true)
    @NotNull
    private String namespace;

    /**
     * 流控模式(1:直接)
     */
    @ApiModelProperty(value = "流控模式(1:直接)", required = true)
    @NotNull
    private String mode;

    /**
     * 阈值类型(1:QPS)
     */
    @ApiModelProperty(value = "阈值类型(1:QPS)", required = true)
    @NotNull
    private String thresholdType;

    /**
     * 阈值取值
     */
    @ApiModelProperty(value = "阈值取值", required = true)
    @NotNull
    private String thresholdValue;

    /**
     * 超时时间（毫秒）
     */
    @ApiModelProperty(value = "超时时间（毫秒）", required = true)
    @NotNull
    private Integer timeOut;

    /**
     * 失败处置方式（1:快速失败）
     */
    @ApiModelProperty(value = "失败处置方式（1:快速失败）", required = true)
    @NotNull
    private String fallBack;

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

    @Override
    public String toString() {
        return new StringJoiner(", ", DpGatewayRateLimitVO.class.getSimpleName() + "[", "]")
                .add("resource='" + resource + "'")
                .add("type='" + type + "'")
                .add("namespace='" + namespace + "'")
                .add("mode='" + mode + "'")
                .add("thresholdType='" + thresholdType + "'")
                .add("thresholdValue='" + thresholdValue + "'")
                .add("timeOut=" + timeOut)
                .add("fallBack='" + fallBack + "'")
                .toString();
    }
}
