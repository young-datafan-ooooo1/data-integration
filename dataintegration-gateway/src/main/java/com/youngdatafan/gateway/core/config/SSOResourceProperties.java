package com.youngdatafan.gateway.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gavin
 * @since 2020/3/16 4:58 下午
 */
@Component
@ConfigurationProperties(prefix = "sso.resource")
public class SSOResourceProperties {

    /**
     * 白名单url匹配器
     */
    private String[] whiteListPatterns;

    public String[] getWhiteListPatterns() {
        return whiteListPatterns;
    }

    public void setWhiteListPatterns(String[] whiteListPatterns) {
        this.whiteListPatterns = whiteListPatterns;
    }
}
