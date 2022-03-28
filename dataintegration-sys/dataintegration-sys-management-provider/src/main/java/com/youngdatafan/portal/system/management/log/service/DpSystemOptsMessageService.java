package com.youngdatafan.portal.system.management.log.service;

import com.youngdatafan.portal.system.management.log.entity.DpSystemOptsMessage;
import com.youngdatafan.portal.system.management.summary.dto.SystemOptsMessageDTO;
import java.util.List;

/**
 * DpSystemOptsMessageService.
 */
public interface DpSystemOptsMessageService {

    /**
     * deleteByPrimaryKey.
     *
     * @param opsId opsId
     * @return int
     */
    int deleteByPrimaryKey(String opsId);

    /**
     * insert.
     *
     * @param record record
     * @return int
     */
    int insert(DpSystemOptsMessage record);

    /**
     * insertSelective.
     *
     * @param record insertSelective
     * @return int
     */
    int insertSelective(DpSystemOptsMessage record);

    /**
     * selectByPrimaryKey.
     *
     * @param opsId opsId
     * @return DpSystemOptsMessage
     */
    DpSystemOptsMessage selectByPrimaryKey(String opsId);

    /**
     * updateByPrimaryKeySelective.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(DpSystemOptsMessage record);

    /**
     * updateByPrimaryKey.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpSystemOptsMessage record);

    /**
     * selectLast7DayMessage.
     *
     * @param userId userId
     * @return list
     */
    List<SystemOptsMessageDTO> selectLast7DayMessage(String userId);

}

