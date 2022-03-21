package com.youngdatafan.portal.system.management.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.StringJoiner;
import lombok.Data;

/**
 * 限流规则对象.
 */
@Data
@ApiModel(description = "限流规则对象")
public class DpGatewayRateLimitVO {

    /**
     * 资源.
     */
    @ApiModelProperty(value = "资源")
    @NotNull
    private String resource;

    /**
     * 资源类型（1：api请求uri）.
     */
    @ApiModelProperty(value = "资源类型（1：api请求uri）")
    @NotNull
    private String type;

    /**
     * 命名空间.
     */
    @ApiModelProperty(value = "命名空间", required = true)
    @NotNull
    private String namespace;

    /**
     * 流控模式(1:直接).
     */
    @ApiModelProperty(value = "流控模式(1:直接)", required = true)
    @NotNull
    private String mode;

    /**
     * 阈值类型(1:QPS).
     */
    @ApiModelProperty(value = "阈值类型(1:QPS)", required = true)
    @NotNull
    private String thresholdType;

    /**
     * 阈值取值.
     */
    @ApiModelProperty(value = "阈值取值", required = true)
    @NotNull
    private String thresholdValue;

    /**
     * 超时时间（毫秒）.
     */
    @ApiModelProperty(value = "超时时间（毫秒）", required = true)
    @NotNull
    private Integer timeOut;

    /**
     * 失败处置方式（1:快速失败）.
     */
    @ApiModelProperty(value = "失败处置方式（1:快速失败）", required = true)
    @NotNull
    private String fallBack;

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
