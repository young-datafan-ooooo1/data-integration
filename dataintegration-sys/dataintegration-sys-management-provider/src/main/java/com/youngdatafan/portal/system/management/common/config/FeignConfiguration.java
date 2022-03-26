package com.youngdatafan.portal.system.management.common.config;

import feign.Request;
import feign.Retryer;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * FeignConfiguration.
 *
 * @author gavin
 * @since 2020/6/13 5:01 下午
 */
@Configuration
public class FeignConfiguration {

    @Bean
    Request.Options requestOptions(ConfigurableEnvironment env) {
        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 6000);
        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 2000);
        // feign请求超时设置
        return new Request.Options(ribbonConnectionTimeout, ribbonReadTimeout);
    }

    /**
     * feignRetryer.
     *
     * @param env env
     * @return Retryer
     */
    @Bean
    public Retryer feignRetryer(ConfigurableEnvironment env) {
        // 重试配置
        int retryerCount = env.getProperty("ribbon.RetryerCount", int.class, 2);
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), retryerCount);
    }

}
