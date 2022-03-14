package com.youngdatafan.gateway.core.filter;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.AbstractServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import static org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter.filterRequest;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * 重写NettyRoutingFilter，支持路由自定义超时配置
 *
 * @author gavin
 * @since 2020/11/12 7:05 下午
 */
@Component
public class DpNettyRoutingFilter implements GlobalFilter, Ordered {

    public static final String RESPONSE_TIMEOUT = "response-timeout";
    private static final Log log = LogFactory.getLog(DpNettyRoutingFilter.class);
    private final HttpClient httpClient;

    private final ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider;

    private final HttpClientProperties properties;

    // do not use this headersFilters directly, use getHeadersFilters() instead.
    private volatile List<HttpHeadersFilter> headersFilters;

    public DpNettyRoutingFilter(HttpClient httpClient, ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider, HttpClientProperties properties) {
        this.httpClient = httpClient;
        this.headersFiltersProvider = headersFiltersProvider;
        this.properties = properties;
    }

    /**
     * 解析Duration
     *
     * @param text 时间表达式，比如：10ms,10s,10m,10h,10d
     * @return Duration
     */
    public Duration parseDuration(String text) {
        text = text.toLowerCase();
        if (text.endsWith("ms")) {
            return Duration.ofMillis(Long.parseLong(text.substring(0, text.length() - 2)));
        } else if (text.endsWith("s")) {
            return Duration.ofSeconds(Long.parseLong(text.substring(0, text.length() - 1)));
        } else if (text.endsWith("m")) {
            return Duration.ofMinutes(Long.parseLong(text.substring(0, text.length() - 1)));
        } else if (text.endsWith("h")) {
            return Duration.ofHours(Long.parseLong(text.substring(0, text.length() - 1)));
        } else if (text.endsWith("d")) {
            return Duration.ofDays(Long.parseLong(text.substring(0, text.length() - 1)));
        } else {
            throw new RuntimeException("时间解析错误，Duration text：" + text);
        }
    }

    public List<HttpHeadersFilter> getHeadersFilters() {
        if (headersFilters == null) {
            headersFilters = headersFiltersProvider.getIfAvailable();
        }
        return headersFilters;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);

        String scheme = requestUrl.getScheme();
        if (isAlreadyRouted(exchange)
                || (!"http".equals(scheme) && !"https".equals(scheme))) {
            return chain.filter(exchange);
        }
        setAlreadyRouted(exchange);

        ServerHttpRequest request = exchange.getRequest();

        final HttpMethod method = HttpMethod.valueOf(request.getMethodValue());
        final String url = requestUrl.toASCIIString();

        HttpHeaders filtered = filterRequest(getHeadersFilters(), exchange);

        final DefaultHttpHeaders httpHeaders = new DefaultHttpHeaders();
        filtered.forEach(httpHeaders::set);

        boolean preserveHost = exchange
                .getAttributeOrDefault(PRESERVE_HOST_HEADER_ATTRIBUTE, false);

        Flux<HttpClientResponse> responseFlux = this.httpClient.headers(headers -> {
            headers.add(httpHeaders);
            if (preserveHost) {
                String host = request.getHeaders().getFirst(HttpHeaders.HOST);
                headers.add(HttpHeaders.HOST, host);
            } else {
                // let Netty set it based on hostname
                headers.remove(HttpHeaders.HOST);
            }
        }).request(method).uri(url).send((req, nettyOutbound) -> {
            if (log.isTraceEnabled()) {
                nettyOutbound.withConnection(connection -> log.trace(
                        "outbound route: " + connection.channel().id().asShortText()
                                + ", inbound: " + exchange.getLogPrefix()));
            }
            return nettyOutbound.send(request.getBody()
                    .map(dataBuffer -> ((NettyDataBuffer) dataBuffer).getNativeBuffer()));
        }).responseConnection((res, connection) -> {

            // Defer committing the response until all route filters have run
            // Put client response as ServerWebExchange attribute and write
            // response later NettyWriteResponseFilter
            exchange.getAttributes().put(CLIENT_RESPONSE_ATTR, res);
            exchange.getAttributes().put(CLIENT_RESPONSE_CONN_ATTR, connection);

            ServerHttpResponse response = exchange.getResponse();
            // put headers and status so filters can modify the response
            HttpHeaders headers = new HttpHeaders();

            res.responseHeaders()
                    .forEach(entry -> headers.add(entry.getKey(), entry.getValue()));

            String contentTypeValue = headers.getFirst(HttpHeaders.CONTENT_TYPE);
            if (StringUtils.hasLength(contentTypeValue)) {
                exchange.getAttributes().put(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR,
                        contentTypeValue);
            }

            HttpStatus status = HttpStatus.resolve(res.status().code());
            if (status != null) {
                response.setStatusCode(status);
            } else if (response instanceof AbstractServerHttpResponse) {
                // https://jira.spring.io/browse/SPR-16748
                ((AbstractServerHttpResponse) response)
                        .setStatusCodeValue(res.status().code());
            } else {
                // TODO: log warning here, not throw error?
                throw new IllegalStateException("Unable to set status code on response: "
                        + res.status().code() + ", " + response.getClass());
            }

            // make sure headers filters run after setting status so it is
            // available in response
            HttpHeaders filteredResponseHeaders = HttpHeadersFilter
                    .filter(getHeadersFilters(), headers, exchange, HttpHeadersFilter.Type.RESPONSE);

            if (!filteredResponseHeaders.containsKey(HttpHeaders.TRANSFER_ENCODING)
                    && filteredResponseHeaders.containsKey(HttpHeaders.CONTENT_LENGTH)) {
                // It is not valid to have both the transfer-encoding header and
                // the content-length header.
                // Remove the transfer-encoding header in the response if the
                // content-length header is present.
                response.getHeaders().remove(HttpHeaders.TRANSFER_ENCODING);
            }

            exchange.getAttributes().put(CLIENT_RESPONSE_HEADER_NAMES,
                    filteredResponseHeaders.keySet());

            response.getHeaders().putAll(filteredResponseHeaders);

            return Mono.just(res);
        });

        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        // 如果路由有单独的响应超时配置，则使用路由的，否则使用默认超时
        Duration responseTimeout = properties.getResponseTimeout();
        if (route.getMetadata() != null) {
            final String metaResponseTimeout = (String) route.getMetadata().get(DpNettyRoutingFilter.RESPONSE_TIMEOUT);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(metaResponseTimeout)) {
                responseTimeout = parseDuration(metaResponseTimeout);
            }
        }

        if (responseTimeout != null) {
            responseFlux = responseFlux.timeout(responseTimeout,
                    Mono.error(new TimeoutException("Response took longer than timeout: "
                            + responseTimeout)))
                    .onErrorMap(TimeoutException.class,
                            th -> new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT,
                                    th.getMessage(), th));
        }

        return responseFlux.then(chain.filter(exchange));
    }
}
