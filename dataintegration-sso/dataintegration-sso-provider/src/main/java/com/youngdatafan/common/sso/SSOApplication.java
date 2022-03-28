package com.youngdatafan.common.sso;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * sso服务启动类.
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableFeignClients("com.youngdatafan.common.sso.feign")
@EnableSwagger2
@ComponentScan(value = "com.youngdatafan.common.sso")
@EnableCaching
public class SSOApplication {
    /**
     * main.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(SSOApplication.class).run(args);
    }
}
