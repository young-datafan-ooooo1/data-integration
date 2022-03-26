package com.youngdatafan.portal.system.management.log.mapper;

import com.youngdatafan.portal.system.management.log.dto.LogDTO;
import com.youngdatafan.portal.system.management.log.entity.DpPortalLog;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalLogMapper.
 */
public interface DpPortalLogMapper {
    /**
     * deleteByPrimaryKey.
     *
     * @param logId logId
     * @return int
     */
    int deleteByPrimaryKey(Long logId);

    /**
     * insert.
     *
     * @param record record
     * @return int
     */
    int insert(DpPortalLog record);

    /**
     * insertSelective.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpPortalLog record);

    /**
     * selectByPrimaryKey.
     *
     * @param logId selectByPrimaryKey
     * @return DpPortalLog
     */
    DpPortalLog selectByPrimaryKey(Long logId);

    /**
     * batchInsert.
     *
     * @param dpPortalLogs dpPortalLogs
     * @return int
     */
    int batchInsert(List<DpPortalLog> dpPortalLogs);

    /**
     * deleteByReqTime.
     *
     * @param date date
     * @return int
     */
    int deleteByReqTime(@Param("date") Date date);

    /**
     * selectByReqTime.
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @param userName  userName
     * @return List
     */
    List<LogDTO> selectByReqTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("userName") String userName);

}
