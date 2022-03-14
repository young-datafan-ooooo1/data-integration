package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author gavin
 * @since 2020/3/4 11:56 上午
 */
@Data
@ApiModel("项目历史执行")
public class ProjectHistoryExecuteDTO {

    @ApiModelProperty("流水号")
    private Long id;

    @ApiModelProperty("执行器id")
    private String executorId;

    @ApiModelProperty("项目编号")
    private String projectId;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("执行用户编号")
    private String userId;

    @ApiModelProperty("执行用户名")
    private String userName;

    @ApiModelProperty("执行状态")
    private String status;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("执行时间秒")
    private Integer execSecond;
}
