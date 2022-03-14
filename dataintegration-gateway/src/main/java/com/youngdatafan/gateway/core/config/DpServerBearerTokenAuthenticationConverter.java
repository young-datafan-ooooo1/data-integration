package com.youngdatafan.gateway.core.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gavin
 * @since 2020/6/30 3:15 下午
 */
public class DpServerBearerTokenAuthenticationConverter implements ServerAuthenticationConverter {
    private static final Pattern authorizationPattern = Pattern.compile(
            "^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$",
            Pattern.CASE_INSENSITIVE);

    private boolean allowUriQueryParameter = false;

    private static String resolveFromAuthorizationHeader(HttpHeaders headers) {
        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            Matcher matcher = authorizationPattern.matcher(authorization);

            if (!matcher.matches()) {
                BearerTokenError error = invalidTokenError();
                throw new OAuth2AuthenticationException(error);
            }

            return matcher.group("token");
        }
        return null;
    }

    private static BearerTokenError invalidTokenError() {
        return new BearerTokenError(BearerTokenErrorCodes.INVALID_TOKEN,
                HttpStatus.UNAUTHORIZED,
                "Bearer token is malformed",
                "https://tools.ietf.org/html/rfc6750#section-3.1");
    }

    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(token(exchange.getRequest()))
                .map(token -> {
                    if (token.isEmpty()) {
                        BearerTokenError error = invalidTokenError();
                        throw new OAuth2AuthenticationException(error);
                    }
                    return new BearerTokenAuthenticationToken(token);
                });
    }

    private String token(ServerHttpRequest request) {
        String authorizationHeaderToken = resolveFromAuthorizationHeader(request.getHeaders());
        String parameterToken = request.getQueryParams().getFirst("access_token");
        if (authorizationHeaderToken != null) {
            if (parameterToken != null) {
                BearerTokenError error = new BearerTokenError(BearerTokenErrorCodes.INVALID_REQUEST,
                        HttpStatus.BAD_REQUEST,
                        "Found multiple bearer tokens in the request",
                        "https://tools.ietf.org/html/rfc6750#section-3.1");
                throw new OAuth2AuthenticationException(error);
            }
            return authorizationHeaderToken;
        } else if (parameterToken != null && allowUriQueryParameter) {
            return parameterToken;
        }
        return null;
    }

    /**
     * Set if transport of access token using URI query parameter is supported. Defaults to {@code false}.
     * <p>
     * The spec recommends against using this mechanism for sending bearer tokens, and even goes as far as
     * stating that it was only included for completeness.
     *
     * @param allowUriQueryParameter if the URI query parameter is supported
     */
    public void setAllowUriQueryParameter(boolean allowUriQueryParameter) {
        this.allowUriQueryParameter = allowUriQueryParameter;
    }

}
