package com.youngdatafan.portal.system.management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author; jeremychen
 * @Descripition;
 * @Date;2020/5/14 5;27 下午
 */
@Component
@ConfigurationProperties(prefix = "vue-config")
@RefreshScope
public class VueConfig {
    private String baseUrl;
    private String ssoUrl;
    private String systemUrl;
    private String groupUrl;
    private String projectUrl;
    private String runManagerUrl;
    private String modelMangerUrl;
    private String wsUrl;
    private String onLineUrl;
    private String fileUrl;
    private String recordUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSsoUrl() {
        return ssoUrl;
    }

    public void setSsoUrl(String ssoUrl) {
        this.ssoUrl = ssoUrl;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(String systemUrl) {
        this.systemUrl = systemUrl;
    }

    public String getGroupUrl() {
        return groupUrl;
    }

    public void setGroupUrl(String groupUrl) {
        this.groupUrl = groupUrl;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getRunManagerUrl() {
        return runManagerUrl;
    }

    public void setRunManagerUrl(String runManagerUrl) {
        this.runManagerUrl = runManagerUrl;
    }

    public String getModelMangerUrl() {
        return modelMangerUrl;
    }

    public void setModelMangerUrl(String modelMangerUrl) {
        this.modelMangerUrl = modelMangerUrl;
    }

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public String getOnLineUrl() {
        return onLineUrl;
    }

    public void setOnLineUrl(String onLineUrl) {
        this.onLineUrl = onLineUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }
}
