package com.youngdatafan.common.sso.config;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.common.sso.feign.UserServiceApiClient;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * MyUserDetailsService.
 */
@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserServiceApiClient userServiceApiClient;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String reqIp = request.getHeader("authorization-reqIp");
        Result<UserDTO, Object> result = userServiceApiClient.selectLoginInfoByUserName(s);
        if (!result.getCode().equals(StatusCode.CODE_10000)) {
            throw new UsernameNotFoundException("查询用户异常");
        }
        UserDTO userDTO = result.getContent();
        if (userDTO == null) {
            throw new UsernameNotFoundException("用户名不存在");
        } else {
            List<String> roleIds = userDTO.getRoleIds();
            List<String> tenantIds = userDTO.getTenantIds();
            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String id : roleIds) {
                authorities.add(new SimpleGrantedAuthority(id));
            }

            return new DpUser(userDTO.getUserId(), userDTO.getUserName(), userDTO.getUserPasswd(), reqIp, userDTO.getDescribe(), StringUtils.join(tenantIds, ","), userDTO, authorities);
        }

    }
}
