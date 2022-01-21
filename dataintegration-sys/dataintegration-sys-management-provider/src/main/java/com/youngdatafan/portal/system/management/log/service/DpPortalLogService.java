package com.youngdatafan.portal.system.management.log.service;

import com.youngdatafan.portal.system.management.log.dto.LogDTO;
import com.youngdatafan.portal.system.management.log.entity.DpPortalLog;
import com.youngdatafan.portal.system.management.log.vo.LogVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/12 1:44 下午
 */
public interface DpPortalLogService {


    int deleteByPrimaryKey(Long logId);

    int insert(DpPortalLog record);

    int insertSelective(DpPortalLog record);

    DpPortalLog selectByPrimaryKey(Long logId);

    int batchInsert(List<DpPortalLog> dpPortalLogs);

    int batchInsertVO(List<LogVO> logs);

    int deleteByReqTime(Date date);

    PageInfo<LogDTO> selectByReqTime(Date startDate, Date endDate, String userName, Page page);
}
