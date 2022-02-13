package com.youngdatafan.di.run.management.server.mapper;

import com.youngdatafan.di.run.management.server.entity.DpPortalPluginInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/19 11:33 下午
 */
public interface DpPortalPluginInfoMapper {
    int deleteByPrimaryKey(String pluginId);

    int insert(DpPortalPluginInfo record);

    int insertSelective(DpPortalPluginInfo record);

    DpPortalPluginInfo selectByPrimaryKey(String pluginId);

    int updateByPrimaryKeySelective(DpPortalPluginInfo record);

    int updateByPrimaryKey(DpPortalPluginInfo record);

    List<DpPortalPluginInfo> getAllBasicPluginInfo(@Param("userId") String userId);

    List<DpPortalPluginInfo> getAllBusinessPluginInfoByIds(@Param("pluginIds") List<String> list);

    List<DpPortalPluginInfo> getAllBusinessModelPluginInfo();
}