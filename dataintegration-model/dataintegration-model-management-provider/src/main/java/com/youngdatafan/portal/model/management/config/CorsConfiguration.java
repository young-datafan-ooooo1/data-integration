package com.youngdatafan.portal.model.management.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "local")
//@Configuration
public class CorsConfiguration extends WebMvcConfigurationSupport {
    //dddd
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

}
