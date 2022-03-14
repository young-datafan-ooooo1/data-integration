package com.youngdatafan.portal.common.group.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

/**
 * 动态配置测试.
 *
 * @author gavin
 * @since 2020/4/30 2:52 下午
 */
@Component
@ConfigurationProperties(prefix = "dynamic")
public class DynamicTestConfig {

    private String name;

    private int age;

    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DynamicTestConfig.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("age=" + age)
                .add("sex='" + sex + "'")
                .toString();
    }
}
