package com.youngdatafan.portal.system.management.log.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.StringJoiner;
import lombok.Data;

/**
 * 日志对象.
 */
@Data
@ApiModel(description = "日志对象")
public class LogVO {

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
     * 执行时间.
     */
    @ApiModelProperty("执行时间")
    private Integer excTime;

    /**
     * 请求用户id.
     */
    @ApiModelProperty("请求用户id")
    private String reqUserId;

    /**
     * 请求用户名.
     */
    @ApiModelProperty("请求用户名")
    private String reqUserName;

    @Override
    public String toString() {
        return new StringJoiner(", ", LogVO.class.getSimpleName() + "[", "]")
                .add("serverName='" + serverName + "'")
                .add("reqUri='" + reqUri + "'")
                .add("reqIp='" + reqIp + "'")
                .add("reqTime=" + reqTime)
                .add("reqSize=" + reqSize)
                .add("excTime=" + excTime)
                .add("reqUserId='" + reqUserId + "'")
                .add("reqUserName='" + reqUserName + "'")
                .toString();
    }
}
