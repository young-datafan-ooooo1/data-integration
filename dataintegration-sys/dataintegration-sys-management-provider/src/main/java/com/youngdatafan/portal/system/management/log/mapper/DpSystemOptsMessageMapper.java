package com.youngdatafan.portal.system.management.log.mapper;

import com.youngdatafan.portal.system.management.log.entity.DpSystemOptsMessage;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpSystemOptsMessageMapper.
 */
public interface DpSystemOptsMessageMapper {
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
     * @param record DpSystemOptsMessage
     * @return int
     */
    int insert(DpSystemOptsMessage record);

    /**
     * insertSelective.
     *
     * @param record record
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
     * @return List
     */
    List<DpSystemOptsMessage> selectLast7DayMessage(@Param("userId") String userId);
}
