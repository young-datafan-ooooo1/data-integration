/*! ******************************************************************************
 * kettle 引擎配置文件
 *
 ******************************************************************************/

package com.youngdatafan.kettle.springboot.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * dataflow执行器配置对象
 *
 * @author gavin
 * @since 2020-02-17 09:39:19
 */
@Component
@ConfigurationProperties(prefix = "kettle.global")
public class EngineExecutorProperties {

    /**
     * 公共map参数对象
     */
    private Map<String, String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
