package com.youngdatafan.portal.system.management.log.mapper;

import com.youngdatafan.portal.system.management.log.dto.LogDTO;
import com.youngdatafan.portal.system.management.log.entity.DpPortalLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/12 1:44 下午
 */
public interface DpPortalLogMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(DpPortalLog record);

    int insertSelective(DpPortalLog record);

    DpPortalLog selectByPrimaryKey(Long logId);

    int batchInsert(List<DpPortalLog> dpPortalLogs);

    int deleteByReqTime(@Param("date") Date date);

    List<LogDTO> selectByReqTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("userName") String userName);

}
