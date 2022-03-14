package com.youngdatafan.gateway.core.config;

import io.github.resilience4j.core.lang.Nullable;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 超时限制配置
 *
 * @author gavin
 * @since 2020/7/15 2:43 下午
 */
@ConfigurationProperties(
        prefix = "resilience4j.timelimiter"
)
@Component
public class Resilience4jTimeLimiterProperties {
    private Map<String, InstanceProperties> instances = new HashMap<>();

    /**
     * 资源限制初始化配置
     */
    private Map<String, ResourceLimitConfig> resourceLimitConfigs = new HashMap<>();

    public Map<String, ResourceLimitConfig> getResourceLimitConfigs() {
        return resourceLimitConfigs;
    }

    public void setResourceLimitConfigs(Map<String, ResourceLimitConfig> resourceLimitConfigs) {
        this.resourceLimitConfigs = resourceLimitConfigs;
    }

    public Map<String, InstanceProperties> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, InstanceProperties> instances) {
        this.instances = instances;
    }

    /**
     * Class storing property values for configuring {@link io.github.resilience4j.circuitbreaker.CircuitBreaker} instances.
     */
    public static class InstanceProperties {

        @DurationMin(millis = 1)
        @Nullable
        private Duration timeoutDuration;

        @Nullable
        private Boolean cancelRunningFuture;

        @Nullable
        public Duration getTimeoutDuration() {
            return timeoutDuration;
        }

        public void setTimeoutDuration(@Nullable Duration timeoutDuration) {
            this.timeoutDuration = timeoutDuration;
        }

        @Nullable
        public Boolean getCancelRunningFuture() {
            return cancelRunningFuture;
        }

        public void setCancelRunningFuture(@Nullable Boolean cancelRunningFuture) {
            this.cancelRunningFuture = cancelRunningFuture;
        }
    }

    public static class ResourceLimitConfig {

        /**
         * 限流配置
         */
        private RedisRateLimiter.Config rateLimitConfig;

        /**
         * 超时配置
         */
        private InstanceProperties timeLimitConfig;

        public RedisRateLimiter.Config getRateLimitConfig() {
            return rateLimitConfig;
        }

        public void setRateLimitConfig(RedisRateLimiter.Config rateLimitConfig) {
            this.rateLimitConfig = rateLimitConfig;
        }

        public InstanceProperties getTimeLimitConfig() {
            return timeLimitConfig;
        }

        public void setTimeLimitConfig(InstanceProperties timeLimitConfig) {
            this.timeLimitConfig = timeLimitConfig;
        }
    }
}
