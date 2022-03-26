package com.youngdatafan.portal.system.management.config.service;

import com.youngdatafan.portal.system.management.config.dto.PortalWebConfigDTO;
import java.util.List;

/**
 * DpPortalWebConfigService.
 */
public interface DpPortalWebConfigService {

    /**
     * selectAll.
     *
     * @param userId   userId
     * @param userName userId
     * @return list
     */
    List<PortalWebConfigDTO> selectAll(String userId, String userName);

    /**
     * selectByKey.
     *
     * @param key key
     * @return string
     */
    String selectByKey(String key);

}
