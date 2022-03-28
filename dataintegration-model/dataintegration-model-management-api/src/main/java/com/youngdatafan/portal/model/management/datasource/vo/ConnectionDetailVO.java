package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 连接信息.
 */

@ApiModel(description = "连接信息")
public class ConnectionDetailVO {

    /**
     * 连接对象.
     */
    @ApiModelProperty("连接信息")
    private ConnectionBean connection;

    /**
     * 获取连接对象.
     *
     * @return 连接对象
     */
    public ConnectionBean getConnection() {
        return connection;
    }

    /**
     * 设置连接对象.
     *
     * @param connection 连接信息.
     */
    public void setConnection(ConnectionBean connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "ConnectionDetailVO{" + "connection=" + connection + '}';
    }

    /**
     * 内部类连接对象.
     */
    @ApiModel("内部连接对象")
    @Data
    @ToString
    public static class ConnectionBean {

        /**
         * 连接名称.
         */
        @ApiModelProperty(value = "连接名称", required = true)
        private String name;

        /**
         * Host地址.
         */
        @ApiModelProperty(value = "Host地址", required = true)
        private String server;

        /**
         * 数据库类型.
         */
        @ApiModelProperty(value = "数据库类型", required = true)
        private String type;

        /**
         * 连接方式.
         */
        @ApiModelProperty(value = "连接方式", required = true, example = "Native")
        private String access;

        /**
         * 数据库.
         */
        @ApiModelProperty(value = "数据库")
        private String database;

        /**
         * 端口.
         */
        @ApiModelProperty(value = "端口")
        private String port;

        /**
         * 用户名.
         */
        @ApiModelProperty(value = "账号")
        private String username;

        /**
         * 密码.
         */
        @ApiModelProperty(value = "密码")
        private String password;

        /**
         * 服务器类型.
         */
        @SuppressWarnings("checkstyle:MemberName")
        @ApiModelProperty(value = "server_type")
        private String server_type;

        /**
         * 高级参数.
         */
        @ApiModelProperty(value = "高级参数")
        private AttributesBean attributes;

        /**
         * 内部属性信息.
         */
        @Data
        @ApiModel("内部属性信息")
        public static class AttributesBean {

            /**
             * 属性信息集合.
             */
            @ApiModelProperty("属性信息集合")
            private List<AttributeBean> attribute;


            /**
             * 单个属性信息内部类.
             */
            @ApiModel("单个属性信息内部类")
            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class AttributeBean {

                /**
                 * 属性key.
                 */
                @ApiModelProperty(value = "key")
                private String code;

                /**
                 * 属性value.
                 */
                @ApiModelProperty(value = "value")
                private String attribute;

                /**
                 * 中文.
                 */
                @ApiModelProperty(value = "中文")
                private String label;
            }
        }
    }

}
