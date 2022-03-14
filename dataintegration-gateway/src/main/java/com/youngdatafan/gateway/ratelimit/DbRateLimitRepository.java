package com.youngdatafan.gateway.ratelimit;

import com.youngdatafan.gateway.ratelimit.bean.RateLimitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gavin
 * @since 2020/6/12 5:41 下午
 */
@Component
public class DbRateLimitRepository {
    private static final Logger log = LoggerFactory.getLogger(DbRateLimitRepository.class);

    /**
     * 超时设置
     */
    private volatile Map<String, RateLimitConfig> routes = new ConcurrentHashMap<>();

    /**
     * 添加规则
     */
    public void save(RateLimitConfig rateLimitConfig) {
        log.info("保存限流规则: {}", rateLimitConfig);
        final String key = rateLimitConfig.getKey();
        routes.put(key, rateLimitConfig);
    }

    /**
     * 判断key是否存在
     *
     * @param key 规则资源
     */
    public boolean containsKey(String key) {
        return routes.containsKey(key);
    }

    /**
     * 删除规则
     */
    public void delete(String resource) {
        if (routes.containsKey(resource)) {
            final RateLimitConfig remove = routes.remove(resource);
            log.info("删除规则: {}", remove);
        }
    }

    /**
     * 获取规则
     */
    public RateLimitConfig get(String resource) {
        return routes.get(resource);
    }

    /**
     * 获取所有规则
     */
    public Collection<RateLimitConfig> getRoutes() {
        return routes.values();
    }

    public int size() {
        return routes.size();
    }

}
