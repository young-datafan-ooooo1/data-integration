package com.youngdatafan.di.run.management.server.service;

import com.youngdatafan.di.run.management.server.entity.DpPortalPluginInfo;
import com.youngdatafan.di.run.management.server.mapper.DpPortalPluginInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/19 4:47 下午
 */
@Service
public class DpPortalPluginInfoService {

    @Resource
    private DpPortalPluginInfoMapper dpPortalPluginInfoMapper;


    public int deleteByPrimaryKey(String pluginId) {
        return dpPortalPluginInfoMapper.deleteByPrimaryKey(pluginId);
    }


    public int insert(DpPortalPluginInfo record) {
        return dpPortalPluginInfoMapper.insert(record);
    }


    public int insertSelective(DpPortalPluginInfo record) {
        return dpPortalPluginInfoMapper.insertSelective(record);
    }


    public DpPortalPluginInfo selectByPrimaryKey(String pluginId) {
        return dpPortalPluginInfoMapper.selectByPrimaryKey(pluginId);
    }


    public int updateByPrimaryKeySelective(DpPortalPluginInfo record) {
        return dpPortalPluginInfoMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(DpPortalPluginInfo record) {
        return dpPortalPluginInfoMapper.updateByPrimaryKey(record);
    }

    public List<DpPortalPluginInfo> getAllBasicPluginInfo(String  userId) {
        return dpPortalPluginInfoMapper.getAllBasicPluginInfo(userId);
    }

    public List<DpPortalPluginInfo> getAllBusinessPluginInfo(List<String> list) {
        return dpPortalPluginInfoMapper.getAllBusinessPluginInfoByIds(list);

    }

    public List<DpPortalPluginInfo> getAllBusinessModelPluginInfo() {
        return dpPortalPluginInfoMapper.getAllBusinessModelPluginInfo();

    }


}

