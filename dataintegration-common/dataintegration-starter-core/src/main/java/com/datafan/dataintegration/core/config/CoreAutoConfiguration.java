package com.datafan.dataintegration.core.config;

import com.datafan.dataintegration.core.handler.GlobalExceptionHandler;
import com.datafan.dataintegration.core.handler.GlobalWebsocketExceptionAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author gavin
 * @create 2020/2/7 5:47 下午
 */
@Configuration
@Import({GlobalExceptionHandler.class, GlobalWebsocketExceptionAspect.class})
public class CoreAutoConfiguration {
}
