package com.youngdatafan.gateway.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * @author gavin
 * @since 2020/3/16 3:27 下午
 */
@EnableWebFluxSecurity
public class SSOResourceServerConfigurer {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, SSOResourceProperties SSOResourceProperties) {
        if (SSOResourceProperties.getWhiteListPatterns() != null) {
            http.authorizeExchange()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .pathMatchers(SSOResourceProperties.getWhiteListPatterns()).permitAll()
                    .anyExchange().authenticated();
        } else {
            http.authorizeExchange()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyExchange().authenticated();
        }

        // 运行从参数中获取token
        final DpServerBearerTokenAuthenticationConverter authenticationConverter = new DpServerBearerTokenAuthenticationConverter();
        authenticationConverter.setAllowUriQueryParameter(true);

        http.cors().disable()
                .csrf().disable()
                .oauth2ResourceServer().bearerTokenConverter(authenticationConverter)
                .jwt();

        return http.build();
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

}
