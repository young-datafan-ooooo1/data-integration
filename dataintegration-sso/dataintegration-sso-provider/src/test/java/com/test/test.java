package com.test;

import com.youngdatafan.common.sso.SSOApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/7/7 7:50 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SSOApplication.class, test.class}
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class test {
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testToken() {
        String authorization = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpbl9uYW1lIjoiemhhbmdyZW5odWEiLCJ1c2VyX2lkIjoiMTRiZjM1YjgtZDM0Mi00NjYzLTk1NTAtNTQ2NmYwYjQ1NGQwIiwidXNlcl9uYW1lIjoiemhhbmdyZW5odWEiLCJzY29wZSI6WyJkZWZhdWx0LXNjb3BlIl0sInVzZXJfbmFtZV9jbiI6InpoYW5ncmVuaHVhIiwiZXhwIjoxNTk0OTg0Nzg1LCJsb2dpbl9pcCI6IjU4LjMzLjEzNy4xOTQiLCJhdXRob3JpdGllcyI6WyIwIl0sImp0aSI6IjNlZWE0ZjNiLWY2NGMtNDIyNy05OGU5LTUwMjlkMGMzNjA5NyIsImNsaWVudF9pZCI6ImRwLWNsaWVudCJ9.O4M3ON78kav4j_h36XvYaj27KEYya9hn96wqYQAuZPfstcX_3S69UEimJU7SlI-tsW2xbSRWf8DmUZKTogdLPQJtJfd1eHR1u6yHBvYpskijIs5bAjSkbxRAvVtsjDx5f7sp-Tfmy2R7w4nmgdNfQtgTXYgPr8gw0pe_MVp0sPeAPD39mJw24Fr51wmfVEuYk0lRmqbfBUzwRG_uRnYgBbe6-aWK7DXBlhxqJQZWNjj6eIdRwM86uWd4Hk032csW-dIlxDJq-jnUILkFuuI1qzzSmBWCoOFbHDSXyWqT6OAqMnQGYxAK2OVTqhHD79fhLOfeALZKhl5buMOKnSI-Gg";
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(authorization);
        System.out.println(oAuth2AccessToken);

        Collection<OAuth2AccessToken> xx = tokenStore.findTokensByClientIdAndUserName("dp-client", "zhangrenhua");

        System.out.println(xx);
    }

    @Test
    public void testPasswordEncode() {
        System.out.println(passwordEncoder.encode("dp-secret"));
    }
}
