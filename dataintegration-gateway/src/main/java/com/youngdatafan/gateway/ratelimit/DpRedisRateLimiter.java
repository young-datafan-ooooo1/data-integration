package com.youngdatafan.gateway.ratelimit;

import com.youngdatafan.gateway.ratelimit.bean.EnvironmentEnum;
import com.youngdatafan.gateway.ratelimit.bean.RateLimitConfig;
import com.youngdatafan.gateway.ratelimit.util.TokenConstants;
import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gavin
 */
@Primary
@Component(DpRedisRateLimiter.BEAN_NAME)
public class DpRedisRateLimiter extends AbstractRateLimiter<DpRedisRateLimiter.Config> {
    public static final String BEAN_NAME = "dpRedisRateLimiter";
    private final DbRateLimitRepository dbRateLimitRepository;
    private Log log = LogFactory.getLog(this.getClass());
    private ReactiveRedisTemplate<String, String> redisTemplate;
    private RedisScript<List<Long>> script;
    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Autowired
    public DpRedisRateLimiter(ReactiveRedisTemplate<String, String> redisTemplate,
                              RedisScript<List<Long>> script, ConfigurationService configurationService, DbRateLimitRepository dbRateLimitRepository) {
        super(Config.class, "redis-rate-limiter", configurationService);
        this.redisTemplate = redisTemplate;
        this.script = script;
        this.dbRateLimitRepository = dbRateLimitRepository;
        this.initialized.compareAndSet(false, true);
    }

    public static long getTime(int type) {
        long time = Instant.now().getEpochSecond();
        switch (type) {
            case 1:
                break;
            case 2:
                time = time / (60);
                break;
            case 3:
                time = time / (60 * 60);
                break;
            case 4:
                time = time / (60 * 60 * 24);
                break;
        }
        return time;
    }

    static List<String> getKeys(String id) {
        String prefix = "request_rate_limiter.{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        if (!this.initialized.get()) {
            throw new IllegalStateException("RateRedisRateLimiter is not initialized");

        } else {
            RateLimitConfig limitConfig = getLimitConfig(id);
            Config config = null;

            // 使用自定义限流配置
            if (limitConfig != null && limitConfig.getTokenConfig().size() > 0) {
                // IP限流
                Map<String, Config> conf = limitConfig.getTokenConfig();
                config = conf.get(TokenConstants.API_REQUEST_URI);

                if (config == null) {
                    // 如果没有配置就用默认的
                    config = getConfig().get(RouteDefinitionRouteLocator.DEFAULT_FILTERS);
                }
            }

            // 校验IP访问过滤
            return isSingleAllow(id, config);
        }
    }

    /**
     * 单级检测限流
     *
     * @param key
     * @param config
     * @return
     */
    private Mono<Response> isSingleAllow(String key, Config config) {
        if (config == null) {
            // 找不到限流配置就直接通过
            return Mono.just(new Response(true, Maps.newHashMapWithExpectedSize(1)));
        }

        // 允许用户每秒处理多少个请求
        int replenishRate = config.getReplenishRate();

        // 令牌桶的容量，允许在一秒钟内完成的最大请求数
        int burstCapacity = config.getBurstCapacity();

        try {
            List<String> keys = getKeys(key);

            // The arguments to the LUA script. time() returns unixtime in seconds.
            List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "",
                    getTime(config.timeType) + "", "1");
            // allowed, tokens_left = redis.eval(SCRIPT, keys, args)
            Flux<List<Long>> flux = this.redisTemplate.execute(this.script, keys, scriptArgs);
            // .log("redisratelimiter", Level.FINER);
            return flux.onErrorResume(throwable -> Flux.just(Arrays.asList(1L, -1L)))
                    .reduce(new ArrayList<Long>(), (longs, l) -> {
                        longs.addAll(l);
                        return longs;
                    }).map(results -> {
                        boolean allowed = results.get(0) == 1L;

                        Response response = new Response(allowed, Maps.newHashMapWithExpectedSize(1));

                        if (log.isDebugEnabled()) {
                            log.debug("response: " + response);
                        }
                        return response;
                    });
        } catch (Exception e) {
            // 忽略redis故障导致限流问题
            log.error("Error determining if user allowed from redis", e);
        }
        return Mono.just(new Response(true, Maps.newHashMapWithExpectedSize(1)));
    }

    /**
     * 获取配置信息
     */
    private RateLimitConfig getLimitConfig(String key) {
        return dbRateLimitRepository.get(key);
    }

    @Validated
    public static class Config {
        /**
         * 令牌桶总容量。
         */
        @Min(1L)
        private int replenishRate = 1;

        /**
         * 令牌桶每秒填充平均速率
         */
        @Min(1L)
        private int burstCapacity = 1;

        /**
         * 时间类型（2：分，3：小时。4：天）
         */
        private int timeType;

        private EnvironmentEnum environmentEnum;

        public Config() {
        }

        public int getReplenishRate() {
            return this.replenishRate;
        }

        public Config setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
            return this;
        }

        public int getBurstCapacity() {
            return this.burstCapacity;
        }

        public Config setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
            return this;
        }

        public int getTimeType() {
            return timeType;
        }

        public void setTimeType(int timeType) {
            this.timeType = timeType;
        }

        public EnvironmentEnum getEnvironmentEnum() {
            return environmentEnum;
        }

        public void setEnvironmentEnum(EnvironmentEnum environmentEnum) {
            this.environmentEnum = environmentEnum;
        }

        @Override
        public String toString() {
            return "Config{replenishRate=" + this.replenishRate + ", burstCapacity=" + this.burstCapacity + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Config config = (Config) o;
            return replenishRate == config.replenishRate &&
                    burstCapacity == config.burstCapacity &&
                    timeType == config.timeType &&
                    environmentEnum == config.environmentEnum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(replenishRate, burstCapacity, timeType, environmentEnum);
        }
    }
}
