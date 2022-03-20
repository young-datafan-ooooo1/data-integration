package com.youngdatafan.gateway.core.filter;


import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.Constans;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.gateway.core.config.RepeatRequestProperties;
import java.net.URI;
import java.time.Duration;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 重复请求过滤器
 * 同一时间内针对用户，不能重复请求同一个接口
 *
 * @author gavin
 * @since 2020/8/28 6:14 下午
 */
@ConditionalOnProperty(prefix = "dp.repeat", name = "enable", havingValue = "true")
@Component
public class RepeatRequestFilter implements GlobalFilter, Ordered {

    public static final String KEY_PREFIX = "repeat_";
    private static final Logger log = LoggerFactory.getLogger(RepeatRequestProperties.class);
    private final ReactiveStringRedisTemplate redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final RepeatRequestProperties repeatRequestProperties;
    /**
     * 忽略的路径匹配器
     */
    private ServerWebExchangeMatcher ignoredPathMatcher;

    @Autowired
    public RepeatRequestFilter(ReactiveStringRedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate, RepeatRequestProperties repeatRequestProperties) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.repeatRequestProperties = repeatRequestProperties;
    }

    @PostConstruct
    public void init() {
        final String[] ignoredPaths = repeatRequestProperties.getIgnoredPaths();
        // 初始化路径匹配器
        if (ignoredPaths == null || ignoredPaths.length == 0) {
            ignoredPathMatcher = ServerWebExchangeMatchers.anyExchange();
        } else {
            ignoredPathMatcher = ServerWebExchangeMatchers.pathMatchers(ignoredPaths);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final URI uri = request.getURI();
        final HttpHeaders headers = request.getHeaders();
        // 获取token uid
        final String tokenUid = headers.getFirst(Constans.TOKEN_UID);

        final HttpMethod method = request.getMethod();
        if (uri.getScheme().startsWith("ws")
                || !isModificationMethod(method)
                || StringUtils.isBlank(tokenUid)) {
            return chain.filter(exchange);
        }

        return ignoredPathMatcher.matches(exchange).flatMap((Function<ServerWebExchangeMatcher.MatchResult, Mono<Void>>) matchResult -> {
            if (matchResult.isMatch()) {
                // 过滤忽略的地址
                return chain.filter(exchange);

            } else {
                final String requestUri = request.getURI().getPath();
                String key = KEY_PREFIX + tokenUid + request.getMethodValue() + requestUri;

                return redisTemplate.hasKey(key).flatMap((Function<Boolean, Mono<Void>>) exists -> {
                    if (exists != null && exists) {
                        log.warn("重复的请求.");

                        // 返回错误信息
                        final Result result = Result.fail(StatusCode.CODE_10009.getCode(), null, "重复的请求");
                        final ServerHttpResponse response = exchange.getResponse();
                        final DataBuffer buffer = AccessPreparedFilter.getFailResponse(response, HttpStatus.BAD_REQUEST, result);
                        return response.writeWith(Mono.just(buffer));
                    }

                    // 保存key到redis中，并且最后删除redis缓存
                    return redisTemplate.opsForValue().set(key, "", Duration.ofSeconds(repeatRequestProperties.getKeyCacheSeconds()))
                            .flatMap((Function<Boolean, Mono<Void>>) aBoolean -> chain.filter(exchange)
                                    // 删掉redis缓存
                                    .then(redisTemplate.delete(key).flatMap(aLong -> Mono.empty())))
                            // 系统错误的时候清理掉cache
                            .doOnError(consumer -> stringRedisTemplate.delete(key));
                });
            }
        });
    }

    /**
     * 是否修改请求方法
     *
     * @param method HttpMethod
     * @return true isModification
     */
    public boolean isModificationMethod(HttpMethod method) {
        return HttpMethod.POST == method || HttpMethod.DELETE == method
                || HttpMethod.PUT == method || HttpMethod.PATCH == method;
    }

    @Override
    public int getOrder() {
        return AccessPreparedFilter.ORDER + 1;
    }

}
