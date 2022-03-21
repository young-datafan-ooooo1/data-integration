package com.youngdatafan.portal.common.group;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 分组管理启动Main.
 *
 * @author gavin
 * @since 2020-01-09 15:59
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableSwagger2
@MapperScan(value = "com.youngdatafan.portal.common.group.**.mapper")
@ComponentScan(value = "com.youngdatafan.portal.common.group")
public class CommonGroupApplication {

    /**
     * 启动.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(CommonGroupApplication.class).run(args);
    }

}
