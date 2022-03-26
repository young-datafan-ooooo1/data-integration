package com.youngdatafan.portal.system.management.log.service.impl;

import com.youngdatafan.portal.system.management.log.entity.DpSystemOptsMessage;
import com.youngdatafan.portal.system.management.log.mapper.DpSystemOptsMessageMapper;
import com.youngdatafan.portal.system.management.log.service.DpSystemOptsMessageService;
import com.youngdatafan.portal.system.management.summary.dto.SystemOptsMessageDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * DpSystemOptsMessageService.
 */
@Service
public class DpSystemOptsMessageServiceImpl implements DpSystemOptsMessageService {

    @Resource
    private DpSystemOptsMessageMapper dpSystemOptsMessageMapper;

    @Override
    public int deleteByPrimaryKey(String opsId) {
        return dpSystemOptsMessageMapper.deleteByPrimaryKey(opsId);
    }

    @Override
    public int insert(DpSystemOptsMessage record) {
        return dpSystemOptsMessageMapper.insert(record);
    }

    @Override
    public int insertSelective(DpSystemOptsMessage record) {
        return dpSystemOptsMessageMapper.insertSelective(record);
    }

    @Override
    public DpSystemOptsMessage selectByPrimaryKey(String opsId) {
        return dpSystemOptsMessageMapper.selectByPrimaryKey(opsId);
    }

    @Override
    public int updateByPrimaryKeySelective(DpSystemOptsMessage record) {
        return dpSystemOptsMessageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(DpSystemOptsMessage record) {
        return dpSystemOptsMessageMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SystemOptsMessageDTO> selectLast7DayMessage(String userId) {
        List<DpSystemOptsMessage> dpSystemOptsMessages = dpSystemOptsMessageMapper.selectLast7DayMessage(userId);
        List<SystemOptsMessageDTO> systemOptsMessageDTOS = new ArrayList<>();
        dpSystemOptsMessages.forEach(o -> {
            SystemOptsMessageDTO systemOptsMessageDTO = new SystemOptsMessageDTO();
            BeanUtils.copyProperties(o, systemOptsMessageDTO);
            systemOptsMessageDTOS.add(systemOptsMessageDTO);
        });
        return systemOptsMessageDTOS;
    }

}

