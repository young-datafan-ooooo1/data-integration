package com.youngdatafan.gateway.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gavin
 * @since 2020/9/2 3:52 下午
 */
@ConfigurationProperties(
        prefix = "dp.repeat"
)
@Component
public class RepeatRequestProperties {

    /**
     * key缓存时间
     */
    private long keyCacheSeconds = 300;

    private String[] ignoredPaths;

    public long getKeyCacheSeconds() {
        return keyCacheSeconds;
    }

    public void setKeyCacheSeconds(long keyCacheSeconds) {
        this.keyCacheSeconds = keyCacheSeconds;
    }

    public String[] getIgnoredPaths() {
        return ignoredPaths;
    }

    public void setIgnoredPaths(String[] ignoredPaths) {
        this.ignoredPaths = ignoredPaths;
    }
}
