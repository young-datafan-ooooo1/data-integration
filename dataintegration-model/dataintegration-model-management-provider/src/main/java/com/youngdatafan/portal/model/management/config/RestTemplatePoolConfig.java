package com.youngdatafan.portal.model.management.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;


/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/28 6:17 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Configuration
public class RestTemplatePoolConfig {
    @Value("${restTemplate.poolSize}")
    private int poolSize;
    @Value("${restTemplate.perRouteSize}")
    private int perRouteSize;
    @Value("${restTemplate.connectTimeout}")
    private int connectTimeout;
    @Value("${restTemplate.readTimeut}")
    private int readTimeut;
    @Value("${restTemplate.waitTimeout}")
    private int waitTimeout;

    @Bean
    @Primary
    public RestTemplate getRestTemplateWithPool() {
        return RestTemplateFactory.getRestTemplateWithPool(poolSize, perRouteSize, connectTimeout, readTimeut, waitTimeout);
    }

}
