package com.youngdatafan.di.run.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 插件容器管理启动Main
 *
 * @author gavin
 * @since 2020-01-09 15:59
 */
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
@EnableScheduling
@EnableFeignClients
@MapperScan(value = "com.youngdatafan.di.run.management.server.mapper")
@ComponentScan(value = "com.youngdatafan.di.run.management")
public class DiRunManagementApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DiRunManagementApplication.class).run(args);
    }

}
