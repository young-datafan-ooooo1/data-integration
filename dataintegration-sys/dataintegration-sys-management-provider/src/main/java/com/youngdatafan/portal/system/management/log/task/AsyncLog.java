package com.youngdatafan.portal.system.management.log.task;

import com.youngdatafan.portal.system.management.log.entity.DpPortalLog;
import com.youngdatafan.portal.system.management.log.service.DpPortalLogService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * AsyncLog.
 */
@Component
public class AsyncLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncLog.class);

    @Value("${ruleflow.async.log.queueSize:30000}")
    private int queueSize;

    @Value("${ruleflow.async.log.batchSize:1000}")
    private int batchSize;

    @Value("${ruleflow.async.log.threadSize:5}")
    private int threadPoolSize;

    @Value("${ruleflow.async.log.allowOverflow:false}")
    private boolean allowOverflow;

    private ArrayBlockingQueue<DpPortalLog> blockingQueue;

    private volatile boolean runBatch;

    private ExecutorService threadPool;

    @Autowired
    private DpPortalLogService dpPortalLogService;

    /**
     * init.
     */
    @PostConstruct
    public void init() {
        blockingQueue = new ArrayBlockingQueue<>(queueSize);
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * runBatch.
     */
    @Scheduled(cron = "${ruleflow.async.log.cron:0/5 * * * * ?}")
    public void runBatch() {
        if (runBatch) {
            LOGGER.info("batchSave Is already running.");
            return;
        }

        runBatch = true;
        try {
            // 批量保存日志
            batchSave();

        } catch (Throwable e) {
            runBatch = false;
            throw e;
        }

        runBatch = false;
    }

    /**
     * 添加到异步日志队列中.
     *
     * @param dpPortalLog 调用日志
     */
    public void put(DpPortalLog dpPortalLog) {
        // 如果队列满了，主动调用批量方法
        if (blockingQueue.size() >= queueSize) {
            // 允许溢出
            if (allowOverflow) {
                LOGGER.debug("skip the record , allowOverflow.");
                return;

            } else {
                // 执行批处理
                runBatch();
            }
        }

        try {
            blockingQueue.put(dpPortalLog);
        } catch (InterruptedException e) {
            LOGGER.warn("Interrupted!", e);
        }
    }

    /**
     * 批量保存日志.
     */
    private void batchSave() {
        List<DpPortalLog> buffer = new ArrayList<>(batchSize);

        do {
            // 获取日志对象
            DpPortalLog poll = blockingQueue.poll();
            if (poll == null) {
                break;
            }
            buffer.add(poll);

            if (buffer.size() >= batchSize) {
                // 保存日志
                save(buffer);
                // 构建新的日志缓冲
                buffer = new ArrayList<>(batchSize);
            }

        } while (true);

        // 保存日志
        save(buffer);
    }

    private void save(final List<DpPortalLog> buffer) {
        if (buffer.size() > 0) {
            threadPool.execute(() -> dpPortalLogService.batchInsert(buffer));
        }

    }
}
