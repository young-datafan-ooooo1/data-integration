package com.youngdatafan.portal.system.management.config.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.config.api.ConfigureApi;
import com.youngdatafan.portal.system.management.config.dto.PortalWebConfigDTO;
import com.youngdatafan.portal.system.management.config.service.DpPortalWebConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/5/14 5:48 下午
 */
@RestController
@RequestMapping("/configure")
public class ConfigureApiController implements ConfigureApi {

    @Autowired
    private DpPortalWebConfigService dpPortalWebConfigService;


    @Override
    public Result<Map<String, String>, Object> getVueConfig(String userId, String userName) {
        List<PortalWebConfigDTO> list = dpPortalWebConfigService.selectAll(userId, userName);
        Map<String, String> map = new HashMap<>();
        for (PortalWebConfigDTO portalWebConfigDTO : list) {
            map.put(portalWebConfigDTO.getKey(), portalWebConfigDTO.getValue());
        }
        return Result.success(map);
    }

    @Override
    public Result<String, Object> getssoUrl() {
        return Result.success(dpPortalWebConfigService.selectByKey("ssoUrl"));
    }
}
