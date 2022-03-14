package com.youngdatafan.gateway.core.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.impl.NullClaim;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.datafan.dataintegration.core.exception.ValidationException;
import com.datafan.dataintegration.core.model.Result;
import com.datafan.dataintegration.core.util.Constans;
import com.datafan.dataintegration.core.util.JsonUtils;
import com.datafan.dataintegration.core.util.StatusCode;
import com.datafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.gateway.core.util.IpUtil;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 请求预处理
 * 能进入到这个过滤器，说明token认证通过，或者是白名单的请求地址
 *
 * @author gavin
 * @since 2020/2/24 11:25 上午
 */
@Component
public class AccessPreparedFilter implements GlobalFilter, Ordered {
    public static final String JWT_TOKEN_TYPE = "Bearer";
    public static final String TOKEN_LAST_ACCESS_TIME = "LastAccess_";

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE;

    private final ReactiveStringRedisTemplate redisTemplate;
    private final NullClaim CLAIM_DEFAULT_VALUE = new NullClaim();
    /**
     * token访问超时时间
     */
    @Value("${dp.gateway.tokenAccessTimeoutSeconds:1800}")
    private long tokenAccessTimeoutSeconds;

    @Autowired
    public AccessPreparedFilter(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取失败的响应报文
     *
     * @param response ServerHttpResponse
     * @param result   返回对象
     * @return 响应报文
     */
    public static DataBuffer getFailResponse(ServerHttpResponse response, HttpStatus httpStatus, Result result) {
        byte[] bits = JsonUtils.toString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return buffer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();

        // 获取token
        final String authorization = getToken(request);

        if (StringUtils.isBlank(authorization)) {
            // 找不到token则不校验
            return filter(exchange, chain, request, authorization, null, null, null, null);
        }

        // 解析token
        final DecodedJWT decode = JWT.decode(authorization);
        final Map<String, Claim> claims = decode.getClaims();

        // 获取token中的参数
        final String userId = claims.getOrDefault("user_id", CLAIM_DEFAULT_VALUE).asString();
        final String userName = claims.getOrDefault("user_name", CLAIM_DEFAULT_VALUE).asString();
        final String token_uid = claims.getOrDefault("token_uid", CLAIM_DEFAULT_VALUE).asString();
        final String roles = getRoles(claims);

        // 根据token uid获取token的最后访问时间
        final long now = System.currentTimeMillis();
        final String tokenKey = TOKEN_LAST_ACCESS_TIME + token_uid;

        return redisTemplate.opsForValue().get(tokenKey)
                // 空处理
                .switchIfEmpty(Mono.just("NULL"))
                .flatMap((lastAccessTime) -> {
                    if (("NULL".equals(lastAccessTime) ||
                            (now - Long.parseLong(lastAccessTime)) / 1000 > tokenAccessTimeoutSeconds)) {

                        final ServerHttpResponse response = exchange.getResponse();
                        // 清理cookies
                        clearCookies(exchange, response);
                        // 获取失败的响应报文
                        final Result result = Result.fail(StatusCode.CODE_10007, null, "登录已失效，请重新登录");
                        final DataBuffer buffer = getFailResponse(response, HttpStatus.UNAUTHORIZED, result);
                        return response.writeWith(Mono.just(buffer));

                    } else {
                        // 设置token并设置过期时间，并且继续下一个过滤器
                        return redisTemplate.opsForValue().set(tokenKey, String.valueOf(now), Duration.ofSeconds(tokenAccessTimeoutSeconds + 10))
                                .flatMap((Function<Boolean, Mono<Void>>) success -> {
                                    if (!success) {
                                        return Mono.error(new ValidationException(StatusCode.CODE_10010.getCode()
                                                , "set [" + tokenKey + "] expire error."));
                                    }
                                    return filter(exchange, chain, request, authorization, userId, userName, roles, token_uid);
                                });
                    }
                });
    }

    /**
     * 获取token
     */
    private String getToken(ServerHttpRequest request) {
        // Authorization头中必须携带jwt token
        String authorization = request.getHeaders().getFirst("Authorization");

        if (StringUtils.isBlank(authorization)) {
            // websocket通过参数传递token
            authorization = request.getQueryParams().getFirst("access_token");
        }

        //去除Bearer头
        if (authorization != null && authorization.startsWith(JWT_TOKEN_TYPE)) {
            authorization = authorization.substring(JWT_TOKEN_TYPE.length() + 1);
        }
        return authorization;
    }

    private String getRoles(Map<String, Claim> claims) {
        String roles = null;
        final Claim authorities = claims.get("authorities");
        if (authorities != null) {
            roles = StringUtils.join(authorities.asArray(String.class), ",");
        }
        return roles;
    }

    /**
     * 继续下一个过滤器
     */
    private Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain, ServerHttpRequest request
            , String authorization, String userId, String userName, String roles, String tokenUid) {
        // 请求id
        final String reqIpAddress = IpUtil.getIpAddress(request);
        final String logTraceCode = UUIDUtils.generateUUID32();

        // 设置日志mdc
        MDC.put(Constans.MDC_IP, reqIpAddress);
        MDC.put(Constans.MDC_REQUESTURI, request.getURI().getPath());
        MDC.put(Constans.MDC_TRACECODE, logTraceCode);

        // 添加自定义head
        ServerHttpRequest mutableReq = request.mutate()
                .header(Constans.AUTHORIZATION_USERID, userId)
                .header(Constans.AUTHORIZATION_USERNAME, userName)
                .header(Constans.AUTHORIZATION_ROLES, roles)
                .header(Constans.AUTHORIZATION_REQ_IP, reqIpAddress)
                .header(Constans.TOKEN_UID, tokenUid)
                .header(Constans.AUTHORIZATION, authorization)
                .header(Constans.LOG_TRACECODE_HEAD, logTraceCode)
                .build();

        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange).doFinally(subscriber -> {
            // 清理mdc
            MDC.remove(Constans.MDC_IP);
            MDC.remove(Constans.MDC_REQUESTURI);
            MDC.remove(Constans.MDC_TRACECODE);
        });
    }

    /**
     * 清理cookies
     */
    private void clearCookies(ServerWebExchange exchange, ServerHttpResponse response) {
        // 删除所有cookies信息
        for (String key : exchange.getRequest().getCookies().keySet()) {
            final ResponseCookie cookie = ResponseCookie.from(key, "")
                    .maxAge(0).path("/").build();
            response.addCookie(cookie);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
