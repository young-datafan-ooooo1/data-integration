package com.youngdatafan.common.sso.config;

import com.youngdatafan.portal.system.management.user.dto.UserDTO;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * DpUser.
 */
public class DpUser extends User {

    /**
     * 用户id.
     */
    private final String userId;

    private final String loginIp;

    private final String userNameCn;

    /**
     * 租户id.
     */
    private final String tenantId;

    private final UserDTO userDTO;

    public DpUser(String userId, String username, String password, String loginIp, String userNameCn, String tenantId, UserDTO userDTO, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.loginIp = loginIp;
        this.userNameCn = userNameCn;
        this.tenantId = tenantId;
        this.userDTO = userDTO;
    }

    public DpUser(String userId, String username, String userNameCn, String password, String loginIp, String tenantId, UserDTO userDTO, boolean enabled, boolean accountNonExpired,
                  boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.loginIp = loginIp;
        this.userNameCn = userNameCn;
        this.tenantId = tenantId;
        this.userDTO = userDTO;
    }

    /**
     * getUserId.
     *
     * @return String
     */
    public String getUserId() {
        return userId;

    }

    /**
     * getLoginIp.
     *
     * @return ip
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * getUserNameCn.
     *
     * @return userNameCn
     */
    public String getUserNameCn() {
        return userNameCn;
    }

    /**
     * getTenantId.
     *
     * @return tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * getUserDTO.
     *
     * @return UserDTO
     */
    public UserDTO getUserDTO() {
        return userDTO;
    }
}
