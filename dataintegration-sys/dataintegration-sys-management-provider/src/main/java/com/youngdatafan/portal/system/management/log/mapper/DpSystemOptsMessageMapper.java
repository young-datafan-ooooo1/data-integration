package com.youngdatafan.portal.system.management.log.mapper;

import com.youngdatafan.portal.system.management.log.entity.DpSystemOptsMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/5/28 4:26 下午
 */
public interface DpSystemOptsMessageMapper {
    int deleteByPrimaryKey(String opsId);

    int insert(DpSystemOptsMessage record);

    int insertSelective(DpSystemOptsMessage record);

    DpSystemOptsMessage selectByPrimaryKey(String opsId);

    int updateByPrimaryKeySelective(DpSystemOptsMessage record);

    int updateByPrimaryKey(DpSystemOptsMessage record);

    List<DpSystemOptsMessage> selectLast7DayMessage(@Param("userId") String userId);
}