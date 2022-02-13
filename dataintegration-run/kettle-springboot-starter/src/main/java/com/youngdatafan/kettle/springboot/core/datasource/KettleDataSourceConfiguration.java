package com.youngdatafan.kettle.springboot.core.datasource;

import com.youngdatafan.kettle.springboot.core.jdbc.EngineMetaDataJdbcTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 引擎数据源配置
 *
 * @author gavin
 * @since 2020/2/14 5:42 下午
 */
@Configuration
@MapperScan(basePackages = "com.youngdatafan.kettle.springboot.core.mapper", sqlSessionTemplateRef = "EngineMetaDataSqlSessionTemplate")
public class KettleDataSourceConfiguration {

    /**
     * 引擎元数据-jdbc配置
     */
    @Primary
    @Bean(name = "engineMetaDataSourceProperties")
    @ConfigurationProperties(prefix = "kettle.datasource.engine.metadata")
    public DataSourceProperties ds2DataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 引擎元数据-数据源
     */
    @Primary
    @Bean(name = EngineMetaDataJdbcTemplate.DS_NAME)
    public DataSource ds2DataSource(@Qualifier("engineMetaDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return createDataSource(dataSourceProperties);
    }

    @Bean("EngineMetaDataSqlSession")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(EngineMetaDataJdbcTemplate.DS_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        // 加载xml
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/com/youngdatafan/kettle/springboot/core/mapping/*.xml"));
        return sessionFactory.getObject();
    }

    @Bean("EngineMetaDataSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("EngineMetaDataSqlSession") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 创建数据源
     *
     * @param dataSourceProperty 数据源信息
     * @return 数据源
     */
    private DataSource createDataSource(DataSourceProperties dataSourceProperty) {
        return createHikariDataSource(dataSourceProperty);
    }

    /**
     * 创建Hikari连接池
     */
    private DataSource createHikariDataSource(DataSourceProperties dataSourceProperty) {
        HikariConfig hikariConfig = dataSourceProperty.getHikari();
        hikariConfig.setJdbcUrl(dataSourceProperty.getUrl());
        hikariConfig.setUsername(dataSourceProperty.getUsername());
        hikariConfig.setPassword(dataSourceProperty.getPassword());
        hikariConfig.setDriverClassName(dataSourceProperty.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }

}
