package com.youngdatafan.portal.system.management.config.mapper;

import com.youngdatafan.portal.system.management.config.entity.DpPortalSysconf;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalSysconfMapper.
 */
public interface DpPortalSysconfMapper {
    /**
     * deleteByPrimaryKey.
     *
     * @param paramKey paramKey
     * @return int
     */
    int deleteByPrimaryKey(String paramKey);

    /**
     * DpPortalSysconf.
     *
     * @param record record
     * @return int
     */
    int insert(DpPortalSysconf record);

    /**
     * insertSelective.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpPortalSysconf record);

    /**
     * selectByPrimaryKey.
     *
     * @param paramKey paramKey
     * @return DpPortalSysconf
     */
    DpPortalSysconf selectByPrimaryKey(String paramKey);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record updateByPrimaryKeySelective
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalSysconf record);

    /**
     * DpPortalSysconf.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpPortalSysconf record);

    /**
     * selectByType.
     *
     * @param paramType paramType
     * @return List
     */
    List<DpPortalSysconf> selectByType(@Param("paramType") String paramType);
}
