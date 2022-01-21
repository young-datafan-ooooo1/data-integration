package com.youngdatafan.portal.system.management.config.service;

import com.youngdatafan.portal.system.management.config.dto.PortalWebConfigDTO;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/7/13 6:38 下午
 */
public interface DpPortalWebConfigService {

    List<PortalWebConfigDTO> selectAll(String userId, String userName);

    String selectByKey(String key);

}
