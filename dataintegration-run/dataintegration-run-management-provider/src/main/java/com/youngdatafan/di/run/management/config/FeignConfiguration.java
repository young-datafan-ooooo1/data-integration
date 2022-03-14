package com.youngdatafan.di.run.management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gavin
 * @since 2020/6/13 5:01 下午
 */
@Configuration
public class FeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Decoder feignDecoder(HttpMessageConverters httpMessageConverters) {
        return new ResponseEntityDecoder(new SpringDecoder(() -> httpMessageConverters));
    }

    @Bean
    public MappingJackson2HttpMessageConverter feignHttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置日期格式
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(smt);
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //设置中文编码格式
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);

        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public HttpMessageConverters feignHttpMessageConverters(MappingJackson2HttpMessageConverter messageConverters) {
        return new HttpMessageConverters(messageConverters);
    }


    @Bean
    Request.Options requestOptions(ConfigurableEnvironment env) {
        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 30000);
        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 2000);
        // feign请求超时设置
        return new Request.Options(ribbonConnectionTimeout, ribbonReadTimeout);
    }

    @Bean
    public Retryer feignRetryer(ConfigurableEnvironment env) {
        // 重试配置
        int retryerCount = env.getProperty("ribbon.RetryerCount", int.class, 2);
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), retryerCount);
    }

}
