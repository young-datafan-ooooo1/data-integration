package com.youngdatafan.portal.model.management.util.jdbc;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/29 5:23 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */

public class DatasourceExplainDTO {

    private String host;

    private String port;

    private String db;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    @Override
    public String toString() {
        return "DatasourceExplainDTO{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", db='" + db + '\'' +
                '}';
    }
}
