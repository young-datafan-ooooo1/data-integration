package com.youngdatafan.di.run.management.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 项目执行状态
 *
 * @author gavin
 * @since 2020/2/13 1:45 下午
 */
@Builder
@Data
@ApiModel("项目执行响应对象")
public class ProjectExecutorDTO {

    @ApiModelProperty(value = "请求编号")
    private String requestId;

    @ApiModelProperty("执行器id")
    private String executorId;


    @ApiModelProperty("是否执行完成")
    private boolean transFinished;

    @ApiModelProperty("运行时日志")
    private String log;

    @ApiModelProperty("错误记录(错误大于0，则说明项目运行失败)")
    private long errors;

    /**
     * 执行的步骤集合
     */
    private List<ProjectExecutorStepDTO> executorSteps;

}
