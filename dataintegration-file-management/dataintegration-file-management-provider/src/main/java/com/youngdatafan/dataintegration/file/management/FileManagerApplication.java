package com.youngdatafan.dataintegration.file.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 系统管理启动主类.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/7 11:21 上午
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableSwagger2
@EnableCaching
@MapperScan(value = "com.youngdatafan.dataintegration.file.management.mapper")
@ComponentScan(value = "com.youngdatafan.dataintegration.file.management")
public class FileManagerApplication {

    /**
     * 主类启动方法.
     * @param args 其他参数
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(FileManagerApplication.class).run(args);
    }
}
