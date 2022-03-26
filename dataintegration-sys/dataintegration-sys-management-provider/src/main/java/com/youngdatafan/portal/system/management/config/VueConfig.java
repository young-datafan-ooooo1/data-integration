package com.youngdatafan.portal.system.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * VueConfig.
 */
@Component
@ConfigurationProperties(prefix = "vue-config")
@RefreshScope
@Data
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


}
