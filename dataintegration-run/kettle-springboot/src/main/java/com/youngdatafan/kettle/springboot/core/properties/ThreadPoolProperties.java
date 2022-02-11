package com.youngdatafan.kettle.springboot.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 执行器线程池配置
 *
 * @author gavin
 * @since 2020/2/14 4:47 下午
 */
@Component
@ConfigurationProperties(prefix = "kettle.engine.thread")
public class ThreadPoolProperties {

    /**
     * 核心池大小
     */
    private int corePoolSize;

    /**
     * 池最大
     */
    private int maximumPoolSize;

    /**
     * 空闲收回，秒
     */
    private int keepAliveTime;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
