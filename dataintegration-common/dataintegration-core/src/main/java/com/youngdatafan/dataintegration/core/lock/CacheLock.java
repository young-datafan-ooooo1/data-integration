package com.youngdatafan.dataintegration.core.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式缓存锁.
 *
 * @author gavin
 * @since 2020/8/28 3:40 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * 锁key.
     *
     * @return key
     */
    String key();

    /**
     * 获取锁的超时时间.
     *
     * @return expire
     */
    int expire() default 5;

    /**
     * 获取锁的超时时间的单位.
     *
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 获取锁的超时时间的表达式.
     * @return 表达式
     */
    String expireExpression() default "";

    /**
     * unless 符合条件才上锁.
     * @return unless 语意
     */
    String unless() default "";

}
