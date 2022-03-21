package com.youngdatafan.portal.common.project.conf;

import com.youngdatafan.dataintegration.core.util.MdcThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步线程池配置.
 *
 * @author gavin
 * @since 2020/2/12 12:37 下午
 */
@Configuration
public class AsyncExecutorConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncExecutorConfiguration.class);

    /**
     * 核心线程数：线程池创建时候初始化的线程数.
     */
    @Value("${dp.async.taskExecutor.corePoolSize}")
    private int corePoolSize;

    /**
     * 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程.
     */
    @Value("${dp.async.taskExecutor.maxPoolSize}")
    private int maxPoolSize;

    /**
     * 缓冲队列：用来缓冲执行任务的队列.
     */
    @Value("${dp.async.taskExecutor.queueCapacity}")
    private int queueCapacity;

    /**
     * 允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁.
     */
    @Value("${dp.async.taskExecutor.keepAliveSeconds}")
    private int keepAliveSeconds;

    /**
     * asyncTaskExecutor.
     *
     * @return Executor
     */
    @Bean("asyncTaskExecutor")
    public Executor myTaskAsyncPool() {
        ThreadPoolTaskExecutor executor = new MdcThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);

        //线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("dp-async-");
        // 当pool已经达到max size的时候，如何处理新任务
        // 这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，
        // 该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；
        // 如果线程池已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                LOGGER.error("异步线程池已经满了，本次操作同步执行。");
                if (!executor.isShutdown()) {
                    r.run();
                }
            }
        });

        executor.initialize();
        return executor;
    }

}
