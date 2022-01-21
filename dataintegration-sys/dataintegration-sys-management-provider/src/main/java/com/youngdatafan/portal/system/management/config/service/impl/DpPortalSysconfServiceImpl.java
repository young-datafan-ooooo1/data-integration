package com.youngdatafan.portal.system.management.config.service.impl;

import com.youngdatafan.portal.system.management.config.entity.DpPortalSysconf;
import com.youngdatafan.portal.system.management.config.mapper.DpPortalSysconfMapper;
import com.youngdatafan.portal.system.management.config.service.DpPortalSysconfService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/7/24 5:33 下午
 */
@Service
public class DpPortalSysconfServiceImpl implements DpPortalSysconfService {

    @Resource
    private DpPortalSysconfMapper dpPortalSysconfMapper;

    @Override
    public int deleteByPrimaryKey(String paramKey) {
        return dpPortalSysconfMapper.deleteByPrimaryKey(paramKey);
    }

    @Override
    public int insert(DpPortalSysconf record) {
        return dpPortalSysconfMapper.insert(record);
    }

    @Override
    public int insertSelective(DpPortalSysconf record) {
        return dpPortalSysconfMapper.insertSelective(record);
    }

    @Override
    public DpPortalSysconf selectByPrimaryKey(String paramKey) {
        return dpPortalSysconfMapper.selectByPrimaryKey(paramKey);
    }

    @Override
    public int updateByPrimaryKeySelective(DpPortalSysconf record) {
        return dpPortalSysconfMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(DpPortalSysconf record) {
        return dpPortalSysconfMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<DpPortalSysconf> selectByType(String paramType) {
        return dpPortalSysconfMapper.selectByType(paramType);
    }

}
