package com.youngdatafan.dataintegration.core.util.sql;

import java.util.Map;
import javax.sql.DataSource;

/**
 * 数据源包装对象.
 *
 * @author gavin
 * @since 2020/2/28 1:51 下午
 */
public class DataSourceWrap {

    /**
     * 数据库类型.
     */
    private DatabaseType databaseType;

    /**
     * 数据源id.
     */
    private String dataSourceId;

    /**
     * 数据库连接池.
     */
    private volatile DataSource dataSource;

    /**
     * 最后使用时间.
     */
    private volatile long lastUsedTime;

    /**
     * 高级参数.
     */
    private Map<String, String> params;

    private String host;

    private String db;

    private String port;

    private String user;

    private String pass;

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public long getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(long lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
