package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author gavin
 * @since 2020/2/21 12:10 下午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目停止")
public class ProjectStopVO {

    @ApiModelProperty(value = "项目ID", required = true)
    @NotBlank
    String projectId;

    @ApiModelProperty(value = "执行器id", required = true)
    @NotBlank
    String executorId;

}
