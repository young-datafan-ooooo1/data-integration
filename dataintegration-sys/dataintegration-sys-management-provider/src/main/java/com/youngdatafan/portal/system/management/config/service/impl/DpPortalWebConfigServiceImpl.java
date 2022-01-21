package com.youngdatafan.portal.system.management.config.service.impl;

import com.youngdatafan.portal.system.management.config.dto.PortalWebConfigDTO;
import com.youngdatafan.portal.system.management.config.entity.DpPortalWebConfig;
import com.youngdatafan.portal.system.management.config.mapper.DpPortalWebConfigMapper;
import com.youngdatafan.portal.system.management.config.service.DpPortalWebConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/7/13 6:38 下午
 */
@Slf4j
@Service
public class DpPortalWebConfigServiceImpl implements DpPortalWebConfigService {

    private String variableRegex = "(\\$\\{)([a-zA-z0-9-]+)(\\})";
    @Resource
    private DpPortalWebConfigMapper dpPortalWebConfigMapper;


    @Override
    public List<PortalWebConfigDTO> selectAll(String userId, String userName) {
        List<DpPortalWebConfig> dpPortalWebConfigs = dpPortalWebConfigMapper.selectAll();
        List<PortalWebConfigDTO> list = new ArrayList<>(dpPortalWebConfigs.size());
        for (DpPortalWebConfig dpPortalWebConfig : dpPortalWebConfigs) {
            PortalWebConfigDTO portalWebConfigDTO = new PortalWebConfigDTO();
            BeanUtils.copyProperties(dpPortalWebConfig, portalWebConfigDTO);
            list.add(portalWebConfigDTO);
        }

        return list;
    }

    @Override
    public String selectByKey(String key) {
        DpPortalWebConfig dpPortalWebConfig = dpPortalWebConfigMapper.selectByPrimaryKey(key);
        return dpPortalWebConfig.getValue();
    }

}
