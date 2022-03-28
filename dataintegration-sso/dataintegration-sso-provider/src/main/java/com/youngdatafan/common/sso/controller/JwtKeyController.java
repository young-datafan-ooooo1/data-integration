package com.youngdatafan.common.sso.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JwtKeyController.
 *
 * @author gavin
 */
@RestController
public class JwtKeyController {

    private final KeyPair keyPair;

    @Autowired
    public JwtKeyController(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    /**
     * getKey.
     *
     * @return Map
     */
    @GetMapping("/oauth/.well-known/jwks.json")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
