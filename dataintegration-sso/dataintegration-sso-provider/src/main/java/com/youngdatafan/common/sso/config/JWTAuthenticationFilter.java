package com.youngdatafan.common.sso.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

    private final TokenStore tokenStore;

    public JWTAuthenticationFilter(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        if (request1.getRequestURI().startsWith("/actuator/")) {
            return;
        }
        HttpServletResponse response = (HttpServletResponse) res;
        Authentication authentication = TokenAuthenticationService.getAuthentication(request1, response, tokenStore);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);


    }
}
