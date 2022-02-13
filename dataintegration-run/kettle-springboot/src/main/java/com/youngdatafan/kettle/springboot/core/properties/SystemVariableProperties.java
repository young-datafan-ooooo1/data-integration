package com.youngdatafan.kettle.springboot.core.properties;

import com.youngdatafan.kettle.springboot.core.bean.SystemVariablesBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统变量配置
 *
 * @author gavin
 * @since 2020/2/14 4:47 下午
 */
@Component
@ConfigurationProperties(prefix = "kettle.engine.variable")
public class SystemVariableProperties {

    /**
     * 核心池大小
     */
    private List<SystemVariablesBean> systemVariables;

    public List<SystemVariablesBean> getSystemVariables() {
        return systemVariables;
    }

    public void setSystemVariables(List<SystemVariablesBean> systemVariables) {
        this.systemVariables = systemVariables;
    }
}
