package com.youngdatafan.portal.system.management.config.mapper;

import com.youngdatafan.portal.system.management.config.entity.DpPortalSysconf;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/7/24 5:33 下午
 */
public interface DpPortalSysconfMapper {
    int deleteByPrimaryKey(String paramKey);

    int insert(DpPortalSysconf record);

    int insertSelective(DpPortalSysconf record);

    DpPortalSysconf selectByPrimaryKey(String paramKey);

    int updateByPrimaryKeySelective(DpPortalSysconf record);

    int updateByPrimaryKey(DpPortalSysconf record);

    List<DpPortalSysconf> selectByType(@Param("paramType") String paramType);
}