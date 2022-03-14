package com.youngdatafan.gateway.core.util;

/**
 * 常量
 *
 * @author gavin
 * @since 2020/8/28 4:10 下午
 */
public interface Constans {

    /**
     * 网关锁名称
     */
    String GATEWAY_LOCK_NAME = "dp_gateway_locks";

    /**
     * 网关路由配置锁
     */
    String LOCK_GATEWAY_ROUTE = "gateway_route";

    /**
     * 多租户配置锁
     */
    String LOCK_GATEWAY_TENANT = "gateway_tenant";

    /**
     * 网关限流配置锁
     */
    String LOCK_GATEWAY_RATELIMIT = "gateway_ratelimit";

}
