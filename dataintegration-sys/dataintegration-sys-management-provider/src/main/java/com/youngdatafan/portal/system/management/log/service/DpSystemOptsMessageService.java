package com.youngdatafan.portal.system.management.log.service;

import com.youngdatafan.portal.system.management.log.entity.DpSystemOptsMessage;
import com.youngdatafan.portal.system.management.summary.dto.SystemOptsMessageDTO;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/5/28 4:08 下午
 */
public interface DpSystemOptsMessageService {


    int deleteByPrimaryKey(String opsId);

    int insert(DpSystemOptsMessage record);

    int insertSelective(DpSystemOptsMessage record);

    DpSystemOptsMessage selectByPrimaryKey(String opsId);

    int updateByPrimaryKeySelective(DpSystemOptsMessage record);

    int updateByPrimaryKey(DpSystemOptsMessage record);

    List<SystemOptsMessageDTO> selectLast7DayMessage(String userId);


}

