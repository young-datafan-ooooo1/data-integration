package com.youngdatafan.portal.common.group.controller;

import com.youngdatafan.portal.common.group.conf.DynamicTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DynamicConfigTest.
 *
 * @author gavin
 * @since 2020/4/30 2:29 下午
 */
@RefreshScope
@RestController
public class DynamicConfigTestController {

    private final DynamicTestConfig dynamicTestConfig;

    @Value("${description:test}")
    private String description;

    @Autowired
    public DynamicConfigTestController(DynamicTestConfig dynamicTestConfig) {
        this.dynamicTestConfig = dynamicTestConfig;
    }

    /**
     * 获取描述.
     *
     * @return String
     */
    @RequestMapping("/description")
    public String testDescription() {
        return description;
    }

    /**
     * 获取配置 .
     *
     * @return String
     */
    @RequestMapping("/config")
    public String testConfig() {
        return dynamicTestConfig.toString();
    }

}
