package com.youngdatafan.dataintegration.file.management.job;

import com.youngdatafan.dataintegration.file.management.dto.DpFileRegularCleanDTO;
import com.youngdatafan.dataintegration.file.management.service.DpFileRegularCleanService;
import java.util.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时清理文件任务.
 *
 * @author songxiaolang
 * @since 2022-01-05 14:01
 */
@Component
@Slf4j
public class TimingCleanFileJob {

    private final DpFileRegularCleanService dpFileRegularCleanService;

    @Autowired
    public TimingCleanFileJob(DpFileRegularCleanService dpFileRegularCleanService) {
        this.dpFileRegularCleanService = dpFileRegularCleanService;
    }

    /**
     * 每月最后一天凌晨2点执行.
     * "0 0 2 28-31 * ?
     */
    @Scheduled(cron = "${escat.file.timing.clean:0 0 2 28-31 * ?}")
    public void timingCleanFileJob() {
        log.info("开始执行定时清理文件信息");
        final Calendar calendar = Calendar.getInstance();
        //如果不是最后一天 则不执行定时任务
        int actualMaximum = calendar.getActualMaximum(Calendar.DATE);
        if (calendar.get(Calendar.DATE) == actualMaximum) {
            DpFileRegularCleanDTO dpFileRegularCleanDTO = dpFileRegularCleanService.get();
            dpFileRegularCleanService.timingCleanFile(dpFileRegularCleanDTO);
        } else {
            log.info("不是最后一天,当前月份的最后一天为:{}号", actualMaximum);
        }
    }

}
