package com.youngdatafan.portal.system.management.config.mapper;

import com.youngdatafan.portal.system.management.config.entity.DpPortalWebConfig;

import java.util.List;

/**
* @Author: jeremychen
* @Descripition: 
* @Date:2020/7/13 6:38 下午
*/
public interface DpPortalWebConfigMapper {
    int deleteByPrimaryKey(String key);

    int insert(DpPortalWebConfig record);

    int insertSelective(DpPortalWebConfig record);

    DpPortalWebConfig selectByPrimaryKey(String key);

    int updateByPrimaryKeySelective(DpPortalWebConfig record);

    int updateByPrimaryKey(DpPortalWebConfig record);

    List<DpPortalWebConfig> selectAll();

}