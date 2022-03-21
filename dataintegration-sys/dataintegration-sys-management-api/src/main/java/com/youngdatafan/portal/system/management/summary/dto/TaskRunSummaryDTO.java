package com.youngdatafan.portal.system.management.summary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务运行状态汇总.
 */
@Data
@ApiModel("任务运行状态汇总")
public class TaskRunSummaryDTO {

    @ApiModelProperty("日期")
    private String day;

    @ApiModelProperty("总数")
    private int total;

    @ApiModelProperty("成功")
    private int success;

    @ApiModelProperty("重启中(处于中断中的状态)")
    private int restarting;

    @ApiModelProperty("成功(忽略)")
    private int successWithIgnore;

    @ApiModelProperty("已中断")
    private int canceled;

    @ApiModelProperty("报错")
    private int error;

    @ApiModelProperty("警告")
    private int warn;

    @ApiModelProperty("等待翻牌")
    private int waitingShift;

    @ApiModelProperty("空闲")
    private int idle;

    @ApiModelProperty("待执行")
    private int pending;

    @ApiModelProperty("依赖未满足")
    private int dependent;

    @ApiModelProperty("执行中")
    private int running;

    @ApiModelProperty("失败重试中")
    private int retrying;

    @ApiModelProperty("重跑中")
    private int rerunning;

    @ApiModelProperty("执行中(报错)")
    private int runningWithError;

    @ApiModelProperty("执行中(中断)")
    private int runningWithCanceled;

    @ApiModelProperty("中断中")
    private int canceling;

    @ApiModelProperty("执行中(警告)")
    private int runningWithWarn;
}
