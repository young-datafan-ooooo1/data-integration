package com.youngdatafan.di.run.management.server.dto;

import java.util.List;
import lombok.Builder;

/**
 * 项目执行步骤状态对象
 *
 * @author gavin
 * @since 2020/2/13 1:45 下午
 */
@Builder
public class ProjectExecutorStepDTO {

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 复制的行数
     */
    private int copy;

    /**
     * Pri/in/out
     */
    private String priority;

    /**
     * 读行数
     */
    private long linesRead;
    /**
     * 写行数
     */
    private long linesWritten;

    /**
     * 输入
     */
    private long linesInput;
    /**
     * 输出
     */
    private long linesOutput;
    /**
     * 更新
     */
    private long linesUpdated;
    /**
     * 拒绝
     */
    private long linesRejected;
    /**
     * 错误(错误大于0，则说明步骤运行失败)
     */
    private long errors;

    /**
     * 步骤执行状态 {@see org.pentaho.di.trans.step.BaseStepData.StepExecutionStatus}
     */
    private String stepExecutionStatus;
    /**
     * 状态描述
     */
    private String statusDescription;

    /**
     * 累计运行时间
     */
    private double seconds;

    /**
     * 速度（条记录/秒）
     */
    private String speed;

    /**
     * 预览数据 字段名集合
     */
    private List<String> previewFieldNames;

    /**
     * 预览数据 字段名集合
     */
    private List<String[]> previewRows;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public int getCopy() {
        return copy;
    }

    public void setCopy(int copy) {
        this.copy = copy;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getLinesRead() {
        return linesRead;
    }

    public void setLinesRead(long linesRead) {
        this.linesRead = linesRead;
    }

    public long getLinesWritten() {
        return linesWritten;
    }

    public void setLinesWritten(long linesWritten) {
        this.linesWritten = linesWritten;
    }

    public long getLinesInput() {
        return linesInput;
    }

    public void setLinesInput(long linesInput) {
        this.linesInput = linesInput;
    }

    public long getLinesOutput() {
        return linesOutput;
    }

    public void setLinesOutput(long linesOutput) {
        this.linesOutput = linesOutput;
    }

    public long getLinesUpdated() {
        return linesUpdated;
    }

    public void setLinesUpdated(long linesUpdated) {
        this.linesUpdated = linesUpdated;
    }

    public long getLinesRejected() {
        return linesRejected;
    }

    public void setLinesRejected(long linesRejected) {
        this.linesRejected = linesRejected;
    }

    public long getErrors() {
        return errors;
    }

    public void setErrors(long errors) {
        this.errors = errors;
    }

    public String getStepExecutionStatus() {
        return stepExecutionStatus;
    }

    public void setStepExecutionStatus(String stepExecutionStatus) {
        this.stepExecutionStatus = stepExecutionStatus;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public double getSeconds() {
        return seconds;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public List<String> getPreviewFieldNames() {
        return previewFieldNames;
    }

    public void setPreviewFieldNames(List<String> previewFieldNames) {
        this.previewFieldNames = previewFieldNames;
    }

    public List<String[]> getPreviewRows() {
        return previewRows;
    }

    public void setPreviewRows(List<String[]> previewRows) {
        this.previewRows = previewRows;
    }
}
