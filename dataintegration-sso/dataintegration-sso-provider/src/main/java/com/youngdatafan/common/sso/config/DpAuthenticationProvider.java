package com.youngdatafan.common.sso.config;

import com.youngdatafan.common.sso.feign.UserServiceApiClient;
import com.youngdatafan.common.sso.service.UserRedisService;
import com.youngdatafan.common.sso.utils.SM3Utils;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.portal.system.management.user.dto.UserDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

/**
 * 自定义认证器.
 *
 * @author gavin
 */

@Slf4j
@Component
public class DpAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Resource
    private UserServiceApiClient userServiceApiClient;

    @Value("${login.lock.expireTime}")
    private long lockExpireTime;
    
    @Resource
    private HttpServletRequest request;

    @Resource
    private UserRedisService userRedisService;

    @Resource
    private TokenStore tokenStore;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        final String username = userDetails.getUsername();
        final String password = userDetails.getPassword();

        UserDTO userDTO;
        if (userDetails instanceof DpUser) {
            userDTO = ((DpUser) userDetails).getUserDTO();
        } else {
            throw new UsernameNotFoundException("服务器内部错误，用户类型不匹配");
        }
        if (userDTO == null || username == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        if (password != null && SM3Utils.encode(password).equals(userDTO.getUserPasswd())) {
            if (userDTO.getStatus().equals("2") && userDTO.getLockTime() != null && (System.currentTimeMillis() - userDTO.getLockTime().getTime()) > lockExpireTime) {
                userRedisService.resetPasswordErrLongTime(username);
                userServiceApiClient.unLockUser(userDTO.getUserId());
            }

            if (userDTO.getStatus().equals("2") && userDTO.getLockTime() != null && (System.currentTimeMillis() - userDTO.getLockTime().getTime()) <= lockExpireTime) {
                throw new UsernameNotFoundException("用户已锁定");
            }

            if (userDTO.getStatus().equals("1")) {
                throw new UsernameNotFoundException("用户已失效");
            }

            Map<Object, Object> object = (Map<Object, Object>) usernamePasswordAuthenticationToken.getDetails();

            //登录前，先注销
            Collection<OAuth2AccessToken> list = tokenStore.findTokensByClientIdAndUserName(object.get("client_id").toString(), username);
            for (OAuth2AccessToken oAuth2AccessToken : list) {
                // 删除 tokenuid 缓存
                userRedisService.delTokenUidCache(oAuth2AccessToken.getValue());
                if (oAuth2AccessToken.getRefreshToken() != null) {
                    tokenStore.removeRefreshToken(oAuth2AccessToken.getRefreshToken());
                }
                tokenStore.removeAccessToken(oAuth2AccessToken);
            }

            userServiceApiClient.updateLastLoginTime(userDTO.getUserId(), DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

            userRedisService.resetPasswordErrLongTime(username);

        } else {

            userRedisService.addPasswordErrLongTime(username);

            int ret = userRedisService.getPasswordErrLongTime(username);
            //如果大于五次锁定用户
            if (ret >= 5) {
                userServiceApiClient.lockUser(userDTO.getUserId());
            }

            throw new UsernameNotFoundException("用户名或密码错误");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        String password = (String) usernamePasswordAuthenticationToken.getCredentials();
        Result<UserDTO, Object> result = userServiceApiClient.selectLoginInfoByUserName(username);
        if (!result.getCode().equals(StatusCode.CODE_10000.getCode())) {
            return new DpUser(null, username, password, null, null, null, null, Collections.emptyList());
        }

        UserDTO userDTO = result.getContent();
        if (userDTO == null) {
            return new DpUser(null, username, password, null, null, null, null, Collections.emptyList());
        }

        List<String> roleIds = userDTO.getRoleIds();
        List<String> tenantIds = userDTO.getTenantIds();
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String id : roleIds) {
            authorities.add(new SimpleGrantedAuthority(id));
        }
        String reqIp = request.getHeader("authorization-reqIp");
        return new DpUser(userDTO.getUserId(), username, password, reqIp, userDTO.getDescribe(), StringUtils.join(tenantIds, ","), userDTO, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
