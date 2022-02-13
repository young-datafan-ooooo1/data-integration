package com.youngdatafan.kettle.springboot.core.executor;

import com.youngdatafan.kettle.springboot.core.properties.EngineExecutorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * kettle执行器配置对象-工具类
 *
 * @author gavin
 * @since 2020/2/17 9:45 上午
 */
@Component
public class KettleEnginePropertiesHelper {

    private static EngineExecutorProperties INSTANCE;

    private final EngineExecutorProperties executorProperties;

    @Autowired
    public KettleEnginePropertiesHelper(EngineExecutorProperties executorProperties) {
        this.executorProperties = executorProperties;
    }

    @PostConstruct
    public void init() {
        KettleEnginePropertiesHelper.INSTANCE = executorProperties;
    }

    public static EngineExecutorProperties getInstance() {
        return KettleEnginePropertiesHelper.INSTANCE;
    }

    /**
     * 获取参数值
     *
     * @param key key
     * @return value
     */
    public static String get(String key) {
        return KettleEnginePropertiesHelper.INSTANCE.getMap().get(key);
    }

}
