package com.youngdatafan.portal.system.management.log.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.portal.system.management.log.dto.LogDTO;
import com.youngdatafan.portal.system.management.log.entity.DpPortalLog;
import com.youngdatafan.portal.system.management.log.mapper.DpPortalLogMapper;
import com.youngdatafan.portal.system.management.log.service.DpPortalLogService;
import com.youngdatafan.portal.system.management.log.vo.LogVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * DpPortalLogService.
 */
@Service
public class DpPortalLogServiceImpl implements DpPortalLogService {

    @Resource
    private DpPortalLogMapper dpPortalLogMapper;

    @Override
    public int deleteByPrimaryKey(Long logId) {
        return dpPortalLogMapper.deleteByPrimaryKey(logId);
    }

    @Override
    public int insert(DpPortalLog record) {
        return dpPortalLogMapper.insert(record);
    }

    @Override
    public int insertSelective(DpPortalLog record) {
        return dpPortalLogMapper.insertSelective(record);
    }

    @Override
    public DpPortalLog selectByPrimaryKey(Long logId) {
        return dpPortalLogMapper.selectByPrimaryKey(logId);
    }

    @Override
    public int batchInsert(List<DpPortalLog> dpPortalLogs) {
        return dpPortalLogMapper.batchInsert(dpPortalLogs);
    }

    @Override
    public int batchInsertVO(List<LogVO> logs) {
        List<DpPortalLog> dpPortalLogs = new ArrayList<>(logs.size());
        for (LogVO log : logs) {
            final DpPortalLog dpPortalLog = new DpPortalLog();
            BeanUtils.copyProperties(log, dpPortalLog);
            dpPortalLog.setLogId(UUIDUtils.nextId());
            dpPortalLogs.add(dpPortalLog);
        }

        return batchInsert(dpPortalLogs);
    }

    @Override
    public int deleteByReqTime(Date date) {
        return dpPortalLogMapper.deleteByReqTime(date);
    }

    @Override
    public PageInfo<LogDTO> selectByReqTime(Date startDate, Date endDate, String name, Page page) {
        String userName = name;
        if (userName != null) {
            userName = userName.trim();
        }
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<LogDTO> dpPortalLogs = dpPortalLogMapper.selectByReqTime(startDate, endDate, userName);

        return new PageInfo<>(dpPortalLogs);
    }

}
