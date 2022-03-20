package com.youngdatafan.portal.system.management.log.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.portal.system.management.common.utils.BaseController;
import com.youngdatafan.portal.system.management.log.api.LogServiceApi;
import com.youngdatafan.portal.system.management.log.dto.LogDTO;
import com.youngdatafan.portal.system.management.log.entity.DpPortalLog;
import com.youngdatafan.portal.system.management.log.service.DpPortalLogService;
import com.youngdatafan.portal.system.management.log.task.AsyncLog;
import com.youngdatafan.portal.system.management.log.vo.LogVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/12 2:30 下午
 */
@RestController
@RequestMapping("/log")
public class LogServiceApiController extends BaseController<LogDTO> implements LogServiceApi {


    @Autowired
    private AsyncLog asyncLog;

    @Autowired
    private DpPortalLogService dpPortalLogService;

    @Override
    public Result<LogDTO, Object> add(LogVO logVO) {

        DpPortalLog dpPortalLog = new DpPortalLog();
        dpPortalLog.setLogId(UUIDUtils.nextId());
        dpPortalLog.setExcTime(logVO.getExcTime());
        dpPortalLog.setReqIp(logVO.getReqIp());
        dpPortalLog.setReqSize(logVO.getReqSize());
        dpPortalLog.setReqTime(logVO.getReqTime());
        dpPortalLog.setReqUri(logVO.getReqUri());
        dpPortalLog.setServerName(logVO.getServerName());
        LogDTO logDTO = new LogDTO();
        BeanUtils.copyProperties(dpPortalLog, logDTO);
        asyncLog.put(dpPortalLog);
        return Result.success(logDTO);
    }

    @Override
    public Result<Integer, Object> batchAdd(List<LogVO> logs) {
        return Result.success(dpPortalLogService.batchInsertVO(logs));
    }

    @Override
    public Result getLogsByReqTimePage(String pageSize, String curPage, Date startDate, Date endDate, String userName) {
        Page page = this.initPage(pageSize, curPage);
        PageInfo<LogDTO> logDTOPageInfo = dpPortalLogService.selectByReqTime(startDate, endDate,userName, page);
        return Result.success(logDTOPageInfo);
    }
}
