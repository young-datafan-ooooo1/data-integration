package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 项目执行对象@author gavin
 *
 * @since 2020/2/13 10:20 上午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目历史执行")
public class ProjectHistoryExecuteVO {

    @ApiModelProperty(value = "项目名称,模糊查询")
    private String projectName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}
