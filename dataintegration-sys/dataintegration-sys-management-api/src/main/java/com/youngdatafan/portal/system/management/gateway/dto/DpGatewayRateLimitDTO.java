package com.youngdatafan.portal.system.management.gateway.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.Setter;

/**
 * 限流规则对象.
 */
@Setter
@Getter
@ApiModel(description = "限流规则对象")
public class DpGatewayRateLimitDTO {

    /**
     * id.
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 资源.
     */
    @ApiModelProperty(value = "资源")
    private String resource;

    /**
     * 资源类型（1：api请求uri）.
     */
    @ApiModelProperty(value = "资源类型（1：api请求uri）")
    private String type;

    /**
     * 命名空间.
     */
    @ApiModelProperty(value = "命名空间")
    private String namespace;

    /**
     * 流控模式(1:直接).
     */
    @ApiModelProperty(value = "流控模式(1:直接)")
    private String mode;

    /**
     * 阈值类型(1:QPS).
     */
    @ApiModelProperty(value = "阈值类型(1:QPS)")
    private String thresholdType;

    /**
     * 阈值取值.
     */
    @ApiModelProperty(value = "阈值取值")
    private String thresholdValue;

    /**
     * 超时时间（毫秒）.
     */
    @ApiModelProperty(value = "超时时间（毫秒）")
    private Integer timeOut;

    /**
     * 失败处置方式（1:快速失败）.
     */
    @ApiModelProperty(value = "失败处置方式（1:快速失败）")
    private String fallBack;

    /**
     * 创建时间.
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间.
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 状态（1启用、2失效）.
     */
    @ApiModelProperty(value = "状态（1启用、2失效）")
    private Integer status;

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
