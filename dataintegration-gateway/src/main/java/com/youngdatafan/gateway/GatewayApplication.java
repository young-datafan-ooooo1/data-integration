package com.youngdatafan.gateway;

import com.datafan.dataintegration.core.lock.CacheLockAspect;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 网关启动main.
 *
 * @author gavin
 * @since 2020/2/6 4:38 下午
 */
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.youngdatafan.gateway")
@EnableScheduling
@EnableFeignClients
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Import(value = CacheLockAspect.class)
public class GatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class).run(args);
    }

}
