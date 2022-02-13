package com.youngdatafan.di.run.management.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author gavin
 * @since 2020/2/13 10:20 上午
 */
@Data
@NoArgsConstructor
@ApiModel(description = "项目执行对象")
public class ProjectFileExecutorVO {

    @ApiModelProperty(value = "请求编号")
    private String requestId;

    @ApiModelProperty(value = "项目编号", required = true)
    @NotNull
    @Length(min = 1, max = 100)
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    @NotNull
    @Length(min = 1, max = 100)
    private String projectName;

    @ApiModelProperty(value = "项目类型，（使用拼音简写）：探索脚本(TSJB)、集成脚本(JCJB)、报表(BB)", required = true)
    @NotNull
    @Length(min = 1, max = 10)
    private String projectType;

    @ApiModelProperty(value = "项目文件内容")
    @NotNull
    private String projectFile;

    @ApiModelProperty(value = "日志级别，Nothing,Error,Minimal,Basic,Detailed,Debug,Rowlevel", required = true)
    @NotNull
    private String logLevel;

    /**
     * 从这个步骤开始执行
     */
    @ApiModelProperty(value = "从这个步骤开始执行", required = true)
    private String startWithThisStepName;

    /**
     * 运行到此步骤
     */
    @ApiModelProperty(value = "运行到此步骤", required = true)
    private String runToThisStepName;

    /**
     * 运行此步骤
     */
    @ApiModelProperty(value = "运行此步骤", required = true)
    private String runThisStepName;

    @ApiModelProperty(value = "是否启用安全模式", required = true)
    @NotNull
    private boolean safeModeEnabled;

    /**
     * 开启数据预览
     */
    private boolean preview;

    /**
     * 数据预览行数
     */
    private int previewSize;

    /**
     * 数据预览行数
     */
    private String previewModel;

    /**
     * 变量
     */
    private Map<String, String> variables;
}
