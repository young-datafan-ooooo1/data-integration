package com.youngdatafan.portal.system.management.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.StringJoiner;
import lombok.Data;

/**
 * 日志返回对象.
 */
@Data
@ApiModel(description = "日志返回对象")
public class LogDTO {
    /**
     * (PK).
     */
    @ApiModelProperty("id")
    private Long logId;

    /**
     * 服务名称.
     */
    @ApiModelProperty("服务名称")
    private String serverName;

    /**
     * 请求uri.
     */
    @ApiModelProperty("请求uri")
    private String reqUri;

    /**
     * 远程ip地址.
     */
    @ApiModelProperty("远程ip地址")
    private String reqIp;

    /**
     * 请求时间.
     */
    @ApiModelProperty("请求时间")
    private Date reqTime;

    /**
     * 接收字节数.
     */
    @ApiModelProperty("接收字节数")
    private Integer reqSize;

    /**
     * 请求用户id.
     */
    private String reqUserId;

    /**
     * 请求用户名.
     */
    private String reqUserName;

    /**
     * 执行时间.
     */
    @ApiModelProperty("执行时间")
    private Integer excTime;

    @Override
    public String toString() {
        return new StringJoiner(", ", LogDTO.class.getSimpleName() + "[", "]")
                .add("logId=" + logId)
                .add("serverName='" + serverName + "'")
                .add("reqUri='" + reqUri + "'")
                .add("reqIp='" + reqIp + "'")
                .add("reqTime=" + reqTime)
                .add("reqSize=" + reqSize)
                .add("reqUserId='" + reqUserId + "'")
                .add("reqUserName='" + reqUserName + "'")
                .add("excTime=" + excTime)
                .toString();
    }
}
