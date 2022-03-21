package com.youngdatafan.portal.common.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 项目管理启动Main.
 *
 * @author gavin
 * @since 2020-01-09 15:59
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableSwagger2
@EnableAsync
@MapperScan(value = "com.youngdatafan.portal.common.project.**.mapper")
@ComponentScan(value = "com.youngdatafan.portal.common.project")
public class CommonProjectApplication {

    /**
     * main.
     * @param args args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(CommonProjectApplication.class).run(args);
    }

}
