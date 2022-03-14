package com.youngdatafan.gateway.log.task;

import com.youngdatafan.gateway.log.feign.LogServiceApiClient;
import com.youngdatafan.portal.system.management.log.vo.LogVO;
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
 * 异步日志作业.
 *
 * @author gavin
 * @since 2020-02-24 15:12:41
 */
@Component
public class AsyncLogTask {
    private static final Logger logger = LoggerFactory.getLogger(AsyncLogTask.class);
    private final LogServiceApiClient logServiceApiClient;
    @Value("${gateway.async.log.queueSize:30000}")
    private int queueSize;
    @Value("${gateway.async.log.batchSize:1000}")
    private int batchSize;
    @Value("${gateway.async.log.threadSize:5}")
    private int threadPoolSize;
    @Value("${gateway.async.log.allowOverflow:false}")
    private boolean allowOverflow;
    private ArrayBlockingQueue<LogVO> blockingQueue;
    private volatile boolean runBatch = false;
    private ExecutorService threadPool;

    @Autowired
    public AsyncLogTask(LogServiceApiClient logServiceApiClient) {
        this.logServiceApiClient = logServiceApiClient;
    }

    @PostConstruct
    public void init() {
        blockingQueue = new ArrayBlockingQueue<>(queueSize);
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Scheduled(fixedDelayString = "${gateway.async.log:5000}")
    public void runBatch() {
        if (runBatch) {
            logger.info("batchSave Is already running.");
            return;
        }

        runBatch = true;
        try {
            // 批量保存日志
            batchSave();

        } catch (Throwable e) {
            runBatch = false;
            logger.error("日志写入数据库失败", e);
        }

        runBatch = false;
    }

    /**
     * 添加到异步日志队列中
     *
     * @param dpPortalLog 调用日志
     */
    public void put(LogVO dpPortalLog) {
        // 如果队列满了，主动调用批量方法
        if (blockingQueue.size() >= queueSize) {
            // 允许溢出
            if (allowOverflow) {
                logger.debug("skip the record , allowOverflow.");
                return;

            } else {
                // 执行批处理
                runBatch();
            }
        }

        try {
            blockingQueue.put(dpPortalLog);
        } catch (InterruptedException e) {
            logger.warn("Interrupted!", e);
        }
    }

    /**
     * 批量保存日志
     */
    private void batchSave() {
        List<LogVO> buffer = new ArrayList<>(batchSize);

        do {
            // 获取日志对象
            LogVO poll = blockingQueue.poll();
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

    private void save(final List<LogVO> buffer) {
        if (buffer.size() > 0) {
            threadPool.execute(() -> logServiceApiClient.batchAdd(buffer));
        }
    }
}
