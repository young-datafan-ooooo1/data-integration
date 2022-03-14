package com.youngdatafan.gateway.tenant;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户租户仓库信息
 *
 * @author gavin
 * @since 2020/6/12 5:41 下午
 */
@Component
public class UserTenantRepository {
    private static final Logger log = LoggerFactory.getLogger(UserTenantRepository.class);

    /**
     * 租户与服务信息
     */
    private volatile Map<String, List<String>> tenantServers = new ConcurrentHashMap<>();

    /**
     * 用户与租户id映射关系
     */
    private volatile Map<String, String> userTenantRel = new ConcurrentHashMap<>();


    public Map<String, List<String>> getTenantServers() {
        return tenantServers;
    }


    public Map<String, String> getUserTenantRel() {
        return userTenantRel;
    }

    /**
     * 新增修改用户关系
     */
    public void upsetTenantUserRel(String userId, String tenantId) {
        log.info("upset用户关系 ,userId: {} ,tenantId:{}", userId, tenantId);
        userTenantRel.put(userId, tenantId);
    }

    /**
     * 删除租户用户关系
     */
    public void deleteTenantUserRel(String userId) {
        log.info("删除租户用户关系 ,userId: {}", userId);
        userTenantRel.remove(userId);
    }

    /**
     * 删除租户服务
     */
    public void deleteTenantServer(String tenantId, String serverName) {
        log.info("删除租户服务 ,serverName: {}", serverName);

        final List<String> servers = tenantServers.get(tenantId);
        if (servers == null) {
            return;
        }

        int index = 0;
        for (String s : servers) {
            if (s.equals(serverName)) {
                servers.set(index, "");
                break;
            }
            index++;
        }
    }

    /**
     * 新增租户服务
     */
    public void addTenantServer(String tenantId, String serverName) {
        log.info("新增租户服务 ,tenantId: {} ,serverName: {}", tenantId, serverName);

        final List<String> servers = tenantServers.get(tenantId);
        if (servers == null) {
            return;
        }

        // 判断是否存在
        if (!CollectionUtils.exists(servers, EqualPredicate.getInstance(serverName))) {
            servers.add(serverName);
        }
    }

    /**
     * 删除租户信息
     */
    public void deleteTenant(String tenantId) {
        log.info("删除租户信息 ,tenantId: {}", tenantId);
        tenantServers.remove(tenantId);
    }

    /**
     * upset租户信息
     */
    public void upsetTenant(String tenantId, List<String> servers) {
        log.info("upset租户信息 ,tenantId: {} ,servers: {}", tenantId, servers);
        tenantServers.put(tenantId, servers);
    }

    /**
     * 查看用户租户关系是否存在
     *
     * @param userId 用户id
     */
    public boolean containsUserTenantRel(String userId) {
        return userTenantRel.containsKey(userId);
    }

    /**
     * 查看租户信息是否存在
     *
     * @param tenantId 租户id
     */
    public boolean containsUserTenantInfo(String tenantId) {
        return tenantServers.containsKey(tenantId);
    }

    /**
     * 获取租户服务
     *
     * @param tenantId 租户id
     */
    public List<String> getTenantServers(String tenantId) {
        return tenantServers.get(tenantId);
    }

    /**
     * 获取租户id
     *
     * @param userId 用户id
     */
    public String getTenantId(String userId) {
        return userTenantRel.get(userId);
    }

    public int getTenantServersSize() {
        return tenantServers.size();
    }

    public int getUserTenantRelSize() {
        return userTenantRel.size();
    }

}
