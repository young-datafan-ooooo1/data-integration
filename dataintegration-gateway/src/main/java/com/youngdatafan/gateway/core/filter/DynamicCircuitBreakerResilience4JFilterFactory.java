package com.youngdatafan.gateway.core.filter;

import com.youngdatafan.gateway.ratelimit.DbRateLimitRepository;
import com.youngdatafan.gateway.ratelimit.RequestUriResolver;
import com.youngdatafan.gateway.ratelimit.bean.RateLimitConfig;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.support.ServiceUnavailableException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;
import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

/**
 * 动态的resilience4j熔断器.
 *
 * @author gavin
 * @since 2020/7/14 6:41 下午
 */
@Component
public class DynamicCircuitBreakerResilience4JFilterFactory extends
        AbstractGatewayFilterFactory<SpringCloudCircuitBreakerFilterFactory.Config> {
    /**
     * 默认配置名.
     */
    public static final String DEFAULT_CONFIG_NAME = "DynamicCircuitBreaker";
    private static final Logger log = LoggerFactory.getLogger(DynamicCircuitBreakerResilience4JFilterFactory.class);
    private final DbRateLimitRepository dbRateLimitRepository;
    private final TimeLimiterRegistry timeLimiterRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    // do not use this dispatcherHandler directly, use getDispatcherHandler() instead.
    private volatile DispatcherHandler dispatcherHandler;

    public DynamicCircuitBreakerResilience4JFilterFactory(DbRateLimitRepository dbRateLimitRepository
            , TimeLimiterRegistry timeLimiterRegistry, CircuitBreakerRegistry circuitBreakerRegistry) {
        super(SpringCloudCircuitBreakerFilterFactory.Config.class);
        this.dbRateLimitRepository = dbRateLimitRepository;
        this.timeLimiterRegistry = timeLimiterRegistry;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return singletonList(NAME_KEY);
    }

    @Override
    public GatewayFilter apply(SpringCloudCircuitBreakerFilterFactory.Config config) {

        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange,
                                     GatewayFilterChain chain) {
                // 获取请求资源key
                final String key = RequestUriResolver.getKey(exchange);

                // 超时时间获取
                Duration timeoutDuration;
                if (dbRateLimitRepository.containsKey(key)) {
                    final RateLimitConfig rateLimitConfig = dbRateLimitRepository.get(key);
                    // 默认1s
                    timeoutDuration = rateLimitConfig.getTimeLimiterConfig().getTimeoutDuration();
                } else {
                    // 否则取默认配置
                    timeoutDuration = timeLimiterRegistry.timeLimiter(DynamicCircuitBreakerResilience4JFilterFactory.DEFAULT_CONFIG_NAME).getTimeLimiterConfig().getTimeoutDuration();
                }

                // 获取熔断配置
                String circuitBreakerName = config.getName() == null ? DynamicCircuitBreakerResilience4JFilterFactory.DEFAULT_CONFIG_NAME : config.getName();
                final CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(key
                        , circuitBreakerRegistry.getConfiguration(circuitBreakerName).get());

                // 执行
                return chain.filter(exchange)
                        .transform(CircuitBreakerOperator.of(circuitBreaker))
                        .timeout(timeoutDuration)
                        // Since we are using the Mono timeout we need to tell the circuit breaker
                        // about the error
                        .doOnError(java.util.concurrent.TimeoutException.class,
                                t -> circuitBreaker.onError(timeoutDuration.toMillis(), TimeUnit.MILLISECONDS,
                                        t))
                        .onErrorResume(t -> handleErrorWithoutFallback(t));
            }

            @Override
            public String toString() {
                return filterToStringCreator(DynamicCircuitBreakerResilience4JFilterFactory.this)
                        .append("name", config.getName())
                        .append("fallback", config.getFallbackUri()).toString();
            }
        };
    }

    private Mono<Void> handleErrorWithoutFallback(Throwable t) {
        if (t instanceof java.util.concurrent.TimeoutException) {
            return Mono.error(new TimeoutException());
        }
        if (t instanceof CallNotPermittedException) {
            return Mono.error(new ServiceUnavailableException());
        }
        return Mono.error(t);
    }

    @Override
    public String name() {
        return DynamicCircuitBreakerResilience4JFilterFactory.DEFAULT_CONFIG_NAME;
    }

}
