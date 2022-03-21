package com.youngdatafan.dataintegration.file.management.job;

import com.youngdatafan.dataintegration.file.management.service.DpFileRegularCleanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 检查定时清理任务表.
 *
 * @author songxiaolang
 * @since 2022-01-06 16:04
 */
@Component
public class InitializationTimingTaskTable implements CommandLineRunner {

    private final DpFileRegularCleanService dpFileRegularCleanService;

    @Autowired
    public InitializationTimingTaskTable(DpFileRegularCleanService dpFileRegularCleanService) {
        this.dpFileRegularCleanService = dpFileRegularCleanService;
    }

    @Override
    public void run(String... args) {
        dpFileRegularCleanService.inspectFileRegularCleanTable();
    }
}
