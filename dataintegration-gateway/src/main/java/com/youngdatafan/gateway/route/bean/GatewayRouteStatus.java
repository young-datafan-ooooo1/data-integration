package com.youngdatafan.gateway.route.bean;

/**
 * 网关规则状态
 *
 * @author gavin
 * @since 2020/6/13 3:39 下午
 */
public class GatewayRouteStatus {

    /**
     * 最后访问时间
     */
    private long lastAccessTime;

    /**
     * 数据库中没有，内存中有，发生的次数
     */
    private int nonExistentCount;

    public GatewayRouteStatus() {
    }

    public GatewayRouteStatus(int nonExistentCount) {
        this.nonExistentCount = nonExistentCount;
    }

    /**
     * 次数+1
     */
    public void addNonExistentCount() {
        nonExistentCount++;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public int getNonExistentCount() {
        return nonExistentCount;
    }

    public void setNonExistentCount(int nonExistentCount) {
        this.nonExistentCount = nonExistentCount;
    }
}
