package com.youngdatafan.common.sso.config;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * AuthorizationServerConfiguration.
 * Created by Steven on 2019/10/26.
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManagerBean;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenEnhancer tokenEnhancer;

    private final TokenStore tokenStore;

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    private final JwtAuthorizationProperties jwtAuthorizationProperties;

    private final UserDetailsService userDetailsService;

    private final AuthorizationCodeServices authorizationCodeServices;

    @Resource
    private DataSource dataSource;

    @Autowired
    public AuthorizationServerConfiguration(AuthenticationManager authenticationManagerBean, PasswordEncoder passwordEncoder, JwtTokenEnhancer tokenEnhancer, TokenStore tokenStore,
                                            JwtAccessTokenConverter jwtAccessTokenConverter, JwtAuthorizationProperties jwtAuthorizationProperties, UserDetailsService userDetailsService,
                                            AuthorizationCodeServices authorizationCodeServices) {
        this.authenticationManagerBean = authenticationManagerBean;
        this.passwordEncoder = passwordEncoder;
        this.tokenEnhancer = tokenEnhancer;
        this.tokenStore = tokenStore;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.jwtAuthorizationProperties = jwtAuthorizationProperties;
        this.userDetailsService = userDetailsService;
        this.authorizationCodeServices = authorizationCodeServices;
    }

    /**
     * jdbcClientDetailsService.
     *
     * @return JdbcClientDetailsService
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(jwtAuthorizationProperties.getWithClient())
            .secret(passwordEncoder.encode(jwtAuthorizationProperties.getClientSecret()))
            .authorizedGrantTypes(jwtAuthorizationProperties.getGrantTypes())
            .scopes(jwtAuthorizationProperties.getScopes())
            .accessTokenValiditySeconds(jwtAuthorizationProperties.getAccessTokenValiditySeconds())
            .refreshTokenValiditySeconds(jwtAuthorizationProperties.getRefreshTokenValiditySeconds())
            .redirectUris(jwtAuthorizationProperties.getRedirectUris());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(tokenEnhancer);
        enhancers.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancers);

        endpoints.tokenStore(tokenStore)
            .accessTokenConverter(jwtAccessTokenConverter)
            .tokenEnhancer(enhancerChain)
            .authenticationManager(authenticationManagerBean)
            .accessTokenConverter(jwtAccessTokenConverter).tokenStore(tokenStore).authorizationCodeServices(authorizationCodeServices)
            .reuseRefreshTokens(false)
            .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()");

    }

}
