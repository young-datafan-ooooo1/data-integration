package com.youngdatafan.portal.system.management.log.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.system.management.log.dto.LogDTO;
import com.youngdatafan.portal.system.management.log.entity.DpPortalLog;
import com.youngdatafan.portal.system.management.log.vo.LogVO;
import java.util.Date;
import java.util.List;

/**
 * DpPortalLogService.
 */
public interface DpPortalLogService {

    /**
     * deleteByPrimaryKey.
     *
     * @param logId logId
     * @return int
     */
    int deleteByPrimaryKey(Long logId);

    /**
     * DpPortalLog.
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
     * @param logId logId
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
     * batchInsertVO.
     *
     * @param logs logs
     * @return int
     */
    int batchInsertVO(List<LogVO> logs);

    /**
     * deleteByReqTime.
     *
     * @param date date
     * @return int
     */
    int deleteByReqTime(Date date);

    /**
     * selectByReqTime.
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @param userName  userName
     * @param page      page
     * @return PageInfo
     */
    PageInfo<LogDTO> selectByReqTime(Date startDate, Date endDate, String userName, Page page);
}
