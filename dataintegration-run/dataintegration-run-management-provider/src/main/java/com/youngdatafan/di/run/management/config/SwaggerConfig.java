package com.youngdatafan.di.run.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author gavin
 * @since 2020/5/22 2:24 下午
 */
@Profile({
        "local", "dev", "uat"
})
@Configuration
@EnableSwagger2
public class SwaggerConfig {
}
