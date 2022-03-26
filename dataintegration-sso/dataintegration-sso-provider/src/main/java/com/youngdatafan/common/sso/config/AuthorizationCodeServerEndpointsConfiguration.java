package com.youngdatafan.common.sso.config;

import com.youngdatafan.common.sso.controller.OauthCodeController;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

/**
 * AuthorizationCodeServerEndpointsConfiguration.
 */
@Configuration
@Import(AuthorizationCodeServerEndpointsConfiguration.TokenKeyEndpointRegistrar.class)
public class AuthorizationCodeServerEndpointsConfiguration {

    private AuthorizationServerEndpointsConfigurer endpoints = new AuthorizationServerEndpointsConfigurer();

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

    /**
     * init.
     */
    @PostConstruct
    public void init() {
        for (AuthorizationServerConfigurer configurer : configurers) {
            try {
                configurer.configure(endpoints);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot configure enpdoints", e);
            }
        }
        endpoints.setClientDetailsService(clientDetailsService);
    }

    /**
     * oauthCodeController.
     *
     * @return OauthCodeController
     * @throws Exception Exception
     */
    @Bean
    public OauthCodeController oauthCodeController() throws Exception {
        OauthCodeController oauthCodeController = new OauthCodeController();
        FrameworkEndpointHandlerMapping mapping = getEndpointsConfigurer().getFrameworkEndpointHandlerMapping();
        oauthCodeController.setUserApprovalPage(extractPath(mapping, "/oauth/confirm_access"));
        oauthCodeController.setProviderExceptionHandler(exceptionTranslator());
        oauthCodeController.setErrorPage(extractPath(mapping, "/oauth/error"));
        oauthCodeController.setTokenGranter(tokenGranter());
        oauthCodeController.setClientDetailsService(clientDetailsService);
        oauthCodeController.setAuthorizationCodeServices(authorizationCodeServices());
        oauthCodeController.setOAuth2RequestFactory(oauth2RequestFactory());
        oauthCodeController.setOAuth2RequestValidator(oauth2RequestValidator());
        oauthCodeController.setUserApprovalHandler(userApprovalHandler());
        oauthCodeController.setRedirectResolver(redirectResolver());
        return oauthCodeController;
    }

    private String extractPath(FrameworkEndpointHandlerMapping mapping, String page) {
        String path = mapping.getPath(page);
        if (path.contains(":")) {
            return path;
        }
        return "forward:" + path;
    }

    /**
     * getEndpointsConfigurer.
     *
     * @return AuthorizationServerEndpointsConfigurer.
     */
    public AuthorizationServerEndpointsConfigurer getEndpointsConfigurer() {
        if (!endpoints.isTokenServicesOverride()) {
            try {
                endpoints.tokenServices(endpoints.getDefaultAuthorizationServerTokenServices());
            } catch (Exception e) {
                throw new BeanCreationException("Cannot create token services", e);
            }
        }
        return endpoints;
    }

    private Set<HttpMethod> allowedTokenEndpointRequestMethods() {
        return getEndpointsConfigurer().getAllowedTokenEndpointRequestMethods();
    }

    private OAuth2RequestFactory oauth2RequestFactory() throws Exception {
        return getEndpointsConfigurer().getOAuth2RequestFactory();
    }

    private UserApprovalHandler userApprovalHandler() throws Exception {
        return getEndpointsConfigurer().getUserApprovalHandler();
    }

    private OAuth2RequestValidator oauth2RequestValidator() throws Exception {
        return getEndpointsConfigurer().getOAuth2RequestValidator();
    }

    private AuthorizationCodeServices authorizationCodeServices() throws Exception {
        return getEndpointsConfigurer().getAuthorizationCodeServices();
    }

    private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator() {
        return getEndpointsConfigurer().getExceptionTranslator();
    }

    private RedirectResolver redirectResolver() {
        return getEndpointsConfigurer().getRedirectResolver();
    }

    private TokenGranter tokenGranter() throws Exception {
        return getEndpointsConfigurer().getTokenGranter();
    }

    @Component
    protected static class TokenKeyEndpointRegistrar implements BeanDefinitionRegistryPostProcessor {

        private BeanDefinitionRegistry registry;

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory,
                JwtAccessTokenConverter.class, false, false);
            if (names.length > 0) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(TokenKeyEndpoint.class);
                builder.addConstructorArgReference(names[0]);
                registry.registerBeanDefinition(TokenKeyEndpoint.class.getName(), builder.getBeanDefinition());
            }
        }

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            this.registry = registry;
        }

    }
}
