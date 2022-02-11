/*! ******************************************************************************
 * kettle 引擎配置文件
 *
 ******************************************************************************/

package com.youngdatafan.kettle.springboot.core.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * 引擎元数据-jdbc操作工具类
 *
 * @author gavin
 */
@Component
public class EngineMetaDataJdbcTemplate {
    /**
     * 数据源名称
     */
    public static final String DS_NAME = "EngineMetaData";

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EngineMetaDataJdbcTemplate(@Qualifier(EngineMetaDataJdbcTemplate.DS_NAME) DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static class EngineJdbcTemplateHelper {
        private static EngineMetaDataJdbcTemplate INSTANCE;
    }

    public static EngineMetaDataJdbcTemplate getInstance() {
        return EngineJdbcTemplateHelper.INSTANCE;
    }

    @PostConstruct
    public void init() {
        // 赋值到Helper类
        EngineJdbcTemplateHelper.INSTANCE = this;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
