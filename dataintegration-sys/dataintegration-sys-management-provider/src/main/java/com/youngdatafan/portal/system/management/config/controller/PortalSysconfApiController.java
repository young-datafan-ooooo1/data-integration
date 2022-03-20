package com.youngdatafan.portal.system.management.config.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.config.api.PortalSysconfApi;
import com.youngdatafan.portal.system.management.config.dto.PortalSysconfDTO;
import com.youngdatafan.portal.system.management.config.entity.DpPortalSysconf;
import com.youngdatafan.portal.system.management.config.service.DpPortalSysconfService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/7/24 5:40 下午
 */
@RestController
@RequestMapping("/portalSysconf")
public class PortalSysconfApiController implements PortalSysconfApi {

    @Resource
    private DpPortalSysconfService dpPortalSysconfService;

    @Override
    public Result<List<PortalSysconfDTO>, Object> getPortalSysconfByType(String paramType) {
        List<DpPortalSysconf> dpPortalSysconfList = dpPortalSysconfService.selectByType(paramType);
        List<PortalSysconfDTO> list = new ArrayList<>();
        dpPortalSysconfList.forEach(o -> {
            PortalSysconfDTO portalSysconfDTO = new PortalSysconfDTO();
            BeanUtils.copyProperties(o, portalSysconfDTO);
            list.add(portalSysconfDTO);
        });
        return Result.success(list);
    }
}
