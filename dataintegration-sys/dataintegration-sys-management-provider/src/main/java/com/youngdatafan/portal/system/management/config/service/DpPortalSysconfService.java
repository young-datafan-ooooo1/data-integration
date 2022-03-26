package com.youngdatafan.portal.system.management.config.service;

import com.youngdatafan.portal.system.management.config.entity.DpPortalSysconf;
import java.util.List;

/**
 * DpPortalSysconfService.
 */
public interface DpPortalSysconfService {

    /**
     * deleteByPrimaryKey.
     *
     * @param paramKey paramKey
     * @return int
     */
    int deleteByPrimaryKey(String paramKey);

    /**
     * insert.
     *
     * @param record DpPortalSysconf
     * @return int
     */
    int insert(DpPortalSysconf record);

    /**
     * insertSelective.
     *
     * @param record DpPortalSysconf
     * @return int
     */
    int insertSelective(DpPortalSysconf record);

    /**
     * selectByPrimaryKey.
     *
     * @param paramKey paramKey
     * @return DpPortalSyscon
     */
    DpPortalSysconf selectByPrimaryKey(String paramKey);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record DpPortalSysconf
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalSysconf record);

    /**
     * updateByPrimaryKey.
     *
     * @param record DpPortalSysconf
     * @return int
     */
    int updateByPrimaryKey(DpPortalSysconf record);

    /**
     * selectByType.
     *
     * @param paramType paramType
     * @return list
     */
    List<DpPortalSysconf> selectByType(String paramType);

}
