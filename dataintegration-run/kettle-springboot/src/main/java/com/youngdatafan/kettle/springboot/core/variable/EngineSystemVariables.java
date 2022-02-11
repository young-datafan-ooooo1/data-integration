/*! ******************************************************************************
 * kettle 引擎配置文件
 *
 ******************************************************************************/

package com.youngdatafan.kettle.springboot.core.variable;

import com.youngdatafan.kettle.springboot.core.properties.SystemVariableProperties;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 系统变量
 *
 * @author gavin
 * @since 2020-02-14 17:23:35
 */
@Component
public class EngineSystemVariables {

    private final SystemVariableProperties systemVariableProperties;

    private static class KettleSystemVariablesHelper {
        private static EngineSystemVariables INSTANCE;
    }

    @Autowired
    public EngineSystemVariables(SystemVariableProperties systemVariableProperties) {
        this.systemVariableProperties = systemVariableProperties;
    }

    public static EngineSystemVariables getInstance() {
        return KettleSystemVariablesHelper.INSTANCE;
    }

    @PostConstruct
    public void init() throws KettleException {
        // 赋值到Helper类
        KettleSystemVariablesHelper.INSTANCE = this;
    }

    public SystemVariableProperties getSystemVariableProperties() {
        return systemVariableProperties;
    }
}
