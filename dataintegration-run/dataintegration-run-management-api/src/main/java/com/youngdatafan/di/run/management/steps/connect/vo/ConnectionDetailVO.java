package com.youngdatafan.di.run.management.steps.connect.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "连接信息")
public class ConnectionDetailVO {


    /**
     * name : deby
     * server : 127.0.0.1
     * type : MYSQL
     * access : Native
     * database : test
     * port : 3306
     * username : root
     * password : Encrypted 2be98afc86aa7f2e4cb79ff228dc6fa8c
     * attributes : {"attribute":[{"code":"CLUSTER_DBNAME_0","attribute":"dp"},{"code":"CLUSTER_DBNAME_1","attribute":"ao"},{"code":"CLUSTER_HOSTNAME_0","attribute":"127.00.0.1"},{"code":"CLUSTER_HOSTNAME_1","attribute":"1213131"},{"code":"CLUSTER_PARTITION_0","attribute":"1"},{"code":"CLUSTER_PARTITION_1","attribute":"2"},{"code":"CLUSTER_PASSWORD_0","attribute":"Encrypted 2be98afc86aa7f2e4cb79ce108fc3fe8b"},{"code":"CLUSTER_PASSWORD_1","attribute":"Encrypted 2be98afc86aa7f2e4cb79ce218fc3fe8b"},{"code":"CLUSTER_PORT_0","attribute":"3306"},{"code":"CLUSTER_PORT_1","attribute":"3307"},{"code":"CLUSTER_USERNAME_0","attribute":"root"},{"code":"CLUSTER_USERNAME_1","attribute":"roo"},{"code":"EXTRA_OPTION_MYSQL.xuanxian","attribute":"1"},{"code":"FORCE_IDENTIFIERS_TO_LOWERCASE","attribute":"Y"},{"code":"FORCE_IDENTIFIERS_TO_UPPERCASE","attribute":"Y"},{"code":"INITIAL_POOL_SIZE","attribute":"10"},{"code":"IS_CLUSTERED","attribute":"Y"},{"code":"MAXIMUM_POOL_SIZE","attribute":"1000"},{"code":"POOLING_accessToUnderlyingConnectionAllowed","attribute":"false"},{"code":"POOLING_defaultAutoCommit","attribute":"true"},{"code":"POOLING_defaultCatalog","attribute":"1"},{"code":"POOLING_defaultReadOnly","attribute":"1"},{"code":"POOLING_defaultTransactionIsolation","attribute":"1"},{"code":"POOLING_initialSize","attribute":"0"},{"code":"POOLING_logAbandoned","attribute":"false"},{"code":"POOLING_maxActive","attribute":"8"},{"code":"POOLING_maxIdle","attribute":"8"},{"code":"POOLING_maxOpenPreparedStatements","attribute":"-1"},{"code":"POOLING_maxWait","attribute":"-1"},{"code":"POOLING_minIdle","attribute":"0"},{"code":"POOLING_poolPreparedStatements","attribute":"false"},{"code":"POOLING_removeAbandoned","attribute":"false"},{"code":"POOLING_removeAbandonedTimeout","attribute":"300"},{"code":"POOLING_testOnBorrow","attribute":"true"},{"code":"POOLING_testOnReturn","attribute":"false"},{"code":"POOLING_testWhileIdle","attribute":"false"},{"code":"POOLING_timeBetweenEvictionRunsMillis","attribute":"1"},{"code":"POOLING_validationQuery","attribute":"1"},{"code":"PORT_NUMBER","attribute":"3306"},{"code":"PREFERRED_SCHEMA_NAME","attribute":"111"},{"code":"PRESERVE_RESERVED_WORD_CASE","attribute":"Y"},{"code":"QUOTE_ALL_FIELDS","attribute":"Y"},{"code":"SQL_CONNECT","attribute":"selecet  1 "},{"code":"STREAM_RESULTS","attribute":"Y"},{"code":"SUPPORTS_BOOLEAN_DATA_TYPE","attribute":"Y"},{"code":"SUPPORTS_TIMESTAMP_DATA_TYPE","attribute":"Y"},{"code":"USE_POOLING","attribute":"Y"}]}
     */

    private ConnectionBean connection;

    public ConnectionBean getConnection() {
        return connection;
    }

    public void setConnection(ConnectionBean connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "ConnectionDetailVO{" +
                "connection=" + connection +
                '}';
    }

    public static class ConnectionBean {

        @ApiModelProperty(value = "连接名称", required = true)
        private String name;
        @ApiModelProperty(value = "Host地址", required = true)
        private String server;
        @ApiModelProperty(value = "数据库类型", required = true)
        private String type;
        @ApiModelProperty(value = "连接方式", required = true, example = "Native")
        private String access;
        @ApiModelProperty(value = "数据库")
        private String database;
        @ApiModelProperty(value = "端口")
        private String port;
        @ApiModelProperty(value = "账号")
        private String username;
        @ApiModelProperty(value = "密码")
        private String password;

        @ApiModelProperty(value = "密码")
        private Integer limit;

        @ApiModelProperty(value = "高级参数")
        private AttributesBean attributes;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            return "ConnectionBean{" +
                    "name='" + name + '\'' +
                    ", server='" + server + '\'' +
                    ", type='" + type + '\'' +
                    ", access='" + access + '\'' +
                    ", database='" + database + '\'' +
                    ", port='" + port + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", limit='" + limit + '\'' +
                    ", attributes=" + attributes +
                    '}';
        }

        public static class AttributesBean {
            /**
             * code : CLUSTER_DBNAME_0
             * attribute : dp
             */

            private List<AttributeBean> attribute;

            public List<AttributeBean> getAttribute() {
                return attribute;
            }

            public void setAttribute(List<AttributeBean> attribute) {
                this.attribute = attribute;
            }

            public static class AttributeBean {
                @ApiModelProperty(value = "key")
                private String code;
                @ApiModelProperty(value = "value")
                private String attribute;
                @ApiModelProperty(value = "中文")
                private String label;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getAttribute() {
                    return attribute;
                }

                public void setAttribute(String attribute) {
                    this.attribute = attribute;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }
            }
        }
    }
}
