package com.youngdatafan.kettle.springboot.core.conf;

import com.youngdatafan.kettle.springboot.core.datasource.KettleDataSourceConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author gavin
 * @since 2020/2/7 5:47 下午
 */
@Configuration
@ComponentScan(value = "com.youngdatafan.kettle.springboot.core")
@MapperScan(value = "com.youngdatafan.kettle.springboot.core.mapper")
@Import(KettleDataSourceConfiguration.class)
public class KettleSpringBootConfiguration {
}
