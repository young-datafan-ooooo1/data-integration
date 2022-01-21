package com.youngdatafan.portal.system.management.log.task;

import com.youngdatafan.portal.system.management.log.service.DpPortalLogService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: jeremychen
 * @Descripition:日志清理
 * @Date:2020/2/12 3:06 下午
 */
@Component
public class CleanLogTask {
    private static final Logger logger = LoggerFactory.getLogger(AsyncLog.class);

    @Value("${protal.log.retentionDays}")
    private int retentionDays;

    @Autowired
    private DpPortalLogService dpPortalLogService;

    @Scheduled(cron = "${protal.log.run.cron}")
    public void cleanLog() {
        logger.info("开始清理日志");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -retentionDays);
        Date sevenDayAgo = calendar.getTime();
        logger.info("删除的日期为：{}", DateFormatUtils.format(sevenDayAgo, "yyyy-MM-dd HH:mm:ss"));
        dpPortalLogService.deleteByReqTime(sevenDayAgo);

    }

}
