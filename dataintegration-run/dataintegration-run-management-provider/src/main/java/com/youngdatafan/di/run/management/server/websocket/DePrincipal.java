package com.youngdatafan.di.run.management.server.websocket;

import java.security.Principal;

/**
 * @author gavin
 * @since 2020/2/20 12:42 下午
 */
public class DePrincipal implements Principal {
    private String userId;

    private String userName;

    public DePrincipal(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getName() {
        return userId;
    }
}
