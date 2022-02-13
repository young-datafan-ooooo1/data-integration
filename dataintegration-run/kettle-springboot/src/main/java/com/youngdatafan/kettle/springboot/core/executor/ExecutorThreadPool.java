/*! ******************************************************************************
 * kettle 引擎配置文件
 *
 ******************************************************************************/

package com.youngdatafan.kettle.springboot.core.executor;

import com.youngdatafan.kettle.springboot.core.properties.ThreadPoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 引擎执行线程池操作工具类
 *
 * @author gavin
 */
@Component
public class ExecutorThreadPool implements Closeable {

    private final ThreadPoolProperties threadPoolProperties;
    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    public ExecutorThreadPool(ThreadPoolProperties threadPoolProperties) {
        this.threadPoolProperties = threadPoolProperties;
    }

    public static synchronized ExecutorThreadPool getInstance() {
        if (EngineExecuteThreadPoolHelper.INSTANCE == null) {
            // 如果没有初始化就使用默认配置
            final ThreadPoolProperties threadPoolProperties = new ThreadPoolProperties();
            threadPoolProperties.setCorePoolSize(100);
            threadPoolProperties.setMaximumPoolSize(120);
            threadPoolProperties.setKeepAliveTime(60);
            new ExecutorThreadPool(threadPoolProperties).init();
        }

        return EngineExecuteThreadPoolHelper.INSTANCE;
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
            threadPoolExecutor.shutdownNow();
        }
    }

    @PostConstruct
    public synchronized void init() {
        if (EngineExecuteThreadPoolHelper.INSTANCE != null) {
            return;
        }

        int corePoolSize = threadPoolProperties.getCorePoolSize();
        int maximumPoolSize = threadPoolProperties.getMaximumPoolSize();
        long keepAliveTime = threadPoolProperties.getKeepAliveTime();

        // 创建线程池
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>()
                , new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(Thread.currentThread().getThreadGroup(), r,
                        "engine-" + threadNumber.getAndIncrement(), 0);
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        });

        // 赋值到Helper类
        EngineExecuteThreadPoolHelper.INSTANCE = this;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    private static class EngineExecuteThreadPoolHelper {
        private static ExecutorThreadPool INSTANCE;
    }
}
