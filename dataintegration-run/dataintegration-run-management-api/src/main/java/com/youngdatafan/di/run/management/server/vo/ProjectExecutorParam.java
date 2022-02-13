package com.youngdatafan.di.run.management.server.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * 项目执行对象@author gavin
 *
 * @since 2020/2/13 10:20 上午
 */
@Data
@NoArgsConstructor
public class ProjectExecutorParam {

    /**
     * 请求编号
     */
    private String requestId;

    /**
     * 执行器id
     */
    private String executorId;

    /**
     * 日志级别，Nothing,Error,Minimal,Basic,Detailed,Debug,Rowlevel
     */
    private String logLevel;

    /**
     * 是否启用安全模式
     */
    private boolean safeModeEnabled;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 从这个步骤开始执行
     */
    private String startWithThisStepName;

    /**
     * 运行到此步骤
     */
    private String runToThisStepName;

    /**
     * 运行此步骤
     */
    private String runThisStepName;

    /**
     * 项目执行开始时间
     */
    private Date startTime;

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
