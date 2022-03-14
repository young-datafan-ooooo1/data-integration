package com.youngdatafan.gateway.ratelimit.bean;

import com.youngdatafan.gateway.ratelimit.DpRedisRateLimiter;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import java.util.Map;
import java.util.StringJoiner;

/**
 * @author gavin
 */
public class RateLimitConfig {

    /**
     * 限流key值
     */
    private String key;

    /**
     * 限流多维度配置
     */
    private Map<String, DpRedisRateLimiter.Config> tokenConfig;

    /**
     * 超时配置
     */
    private TimeLimiterConfig timeLimiterConfig;

    /**
     * 熔断配置
     */
    private CircuitBreakerConfig circuitBreakerConfig;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, DpRedisRateLimiter.Config> getTokenConfig() {
        return tokenConfig;
    }

    public void setTokenConfig(Map<String, DpRedisRateLimiter.Config> tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    public TimeLimiterConfig getTimeLimiterConfig() {
        return timeLimiterConfig;
    }

    public void setTimeLimiterConfig(TimeLimiterConfig timeLimiterConfig) {
        this.timeLimiterConfig = timeLimiterConfig;
    }

    public CircuitBreakerConfig getCircuitBreakerConfig() {
        return circuitBreakerConfig;
    }

    public void setCircuitBreakerConfig(CircuitBreakerConfig circuitBreakerConfig) {
        this.circuitBreakerConfig = circuitBreakerConfig;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RateLimitConfig.class.getSimpleName() + "[", "]")
                .add("key='" + key + "'")
                .add("tokenConfig=" + tokenConfig)
                .add("timeLimiterConfig=" + timeLimiterConfig)
                .add("circuitBreakerConfig=" + circuitBreakerConfig)
                .toString();
    }

}
