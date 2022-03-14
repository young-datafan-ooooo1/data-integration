package com.youngdatafan.gateway.core.config;

import com.youngdatafan.gateway.core.filter.DynamicCircuitBreakerResilience4JFilterFactory;
import com.youngdatafan.gateway.ratelimit.DbRateLimitRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.cloud.gateway.filter.factory.FallbackHeadersGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author gavin
 * @create 2020/7/13 7:48 下午
 */
@Configuration(proxyBeanMethods = false)
public class Resilience4jConfiguration {

    @Bean
    public TimeLimiterRegistry timeLimiterRegistry(Resilience4jTimeLimiterProperties resilience4jTimeLimiterProperties) {
        final TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.ofDefaults();

        for (Map.Entry<String, Resilience4jTimeLimiterProperties.InstanceProperties> entry : resilience4jTimeLimiterProperties.getInstances().entrySet()) {
            final Resilience4jTimeLimiterProperties.InstanceProperties value = entry.getValue();
            final TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(value.getTimeoutDuration())
                    .cancelRunningFuture(value.getCancelRunningFuture()).build();

            // 注册
            timeLimiterRegistry.timeLimiter(entry.getKey(), timeLimiterConfig);
        }

        return timeLimiterRegistry;
    }

    @Bean
    public FallbackHeadersGatewayFilterFactory fallbackHeadersGatewayFilterFactory() {
        return new FallbackHeadersGatewayFilterFactory();
    }

    @Bean
    public DynamicCircuitBreakerResilience4JFilterFactory springCloudCircuitBreakerResilience4JFilterFactory(
            DbRateLimitRepository dbRateLimitRepository,
            TimeLimiterRegistry timeLimiterRegistry,
            CircuitBreakerRegistry circuitBreakerRegistry) {
        return new DynamicCircuitBreakerResilience4JFilterFactory(
                dbRateLimitRepository, timeLimiterRegistry, circuitBreakerRegistry);
    }

}
