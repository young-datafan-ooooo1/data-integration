package com.youngdatafan.gateway.tenant.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.Pair;
import com.youngdatafan.gateway.tenant.UserTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gavin
 * @since 2020/7/29 3:32 下午
 */
@RestController
@RequestMapping("/actuator/dp")
public class TenantServerController {

    private final UserTenantRepository userTenantRepository;

    @Autowired
    public TenantServerController(UserTenantRepository userTenantRepository) {
        this.userTenantRepository = userTenantRepository;
    }

    @GetMapping("/tenants")
    public Result tenants() {
        return Result.success(new Pair<>(userTenantRepository.getUserTenantRel()
                , userTenantRepository.getTenantServers()));
    }

}
