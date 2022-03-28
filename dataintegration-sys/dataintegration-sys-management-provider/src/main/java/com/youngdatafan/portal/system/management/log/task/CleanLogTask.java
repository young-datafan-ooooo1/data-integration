package com.youngdatafan.portal.system.management.log.task;

import com.youngdatafan.portal.system.management.log.service.DpPortalLogService;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * CleanLogTask.
 */
@Component
public class CleanLogTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncLog.class);

    @Value("${protal.log.retentionDays}")
    private int retentionDays;

    @Autowired
    private DpPortalLogService dpPortalLogService;

    /**
     * cleanLog.
     */
    @Scheduled(cron = "${protal.log.run.cron}")
    public void cleanLog() {
        LOGGER.info("开始清理日志");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -retentionDays);
        Date sevenDayAgo = calendar.getTime();
        LOGGER.info("删除的日期为：{}", DateFormatUtils.format(sevenDayAgo, "yyyy-MM-dd HH:mm:ss"));
        dpPortalLogService.deleteByReqTime(sevenDayAgo);

    }

}
