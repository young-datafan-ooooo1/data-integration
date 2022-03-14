package com.dp.de.run.management.plugin.service;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author gavin
 * @since 2020/3/3 1:15 下午
 */
public class HikariDataSourceTest {

    @Test
    public void test() throws Exception {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql://tencent.vincenthsing.top:50810/dp_dev?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("123456");

        hikariDataSource.setMinimumIdle(1);
        hikariDataSource.setMaximumPoolSize(10);

        hikariDataSource.getConnection();

        hikariDataSource.close();
    }
}
