package com.youngdatafan.dataintegration.core.lock;

import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.parse.GenericTokenParser;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

/**
 * 分布式锁注解切面实现.
 *
 * @author gavin
 */
@Aspect
@Component
@Slf4j
public class CacheLockAspect {

    //解析spel表达式
    private final ExpressionParser parser = new SpelExpressionParser();

    //将方法参数纳入Spring管理
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    /**
     * 分布式锁point.
     *
     * @param cacheLock CacheLock注解实例
     */
    @Pointcut("@annotation(cacheLock)")
    public void cacheLockPointCut(CacheLock cacheLock) {
    }

    /**
     * 分布式锁around.
     *
     * @param joinPoint ProceedingJoinPoint
     * @param cacheLock CacheLock注解实例
     * @return object
     * @throws Throwable t
     */
    @Around(value = "cacheLockPointCut(cacheLock)", argNames = "joinPoint,cacheLock")
    public Object around(ProceedingJoinPoint joinPoint, CacheLock cacheLock) throws Throwable {

        Object object = null;
        boolean lockFlag = false;
        Lock lock = null;

        // 判断是否上锁，默认是通过的
        boolean isPass = this.isPass(joinPoint, cacheLock);
        try {
            if (isPass) {
                // 获取锁的key
                String key = this.getCacheKey(joinPoint, cacheLock);
                // 获取锁
                //spring-integration对redis分布锁的支持，底层也是lua脚本的实现，可完美解决线程挂掉造成的死锁，以及执行时间过长锁释放掉，误删别人的锁
                lock = redisLockRegistry.obtain(key);
                // 获取锁时间
                int expire = this.getExpireTime(joinPoint, cacheLock);
                // 获取锁，并且设置等待超时时间，尝试在指定时间内加锁，
                // 如果已经有其他锁锁住，获取当前线程不能加锁，则返回false，加锁失败；加锁成功则返回true
                lockFlag = lock.tryLock(expire, cacheLock.timeUnit());
                if (!lockFlag) {
                    return new NullPointerException("获取锁失败。");
                }
            }
            object = joinPoint.proceed();
        } catch (InterruptedException e) {
            log.error("obtain lock error {}", e.getMessage());
        } finally {
            if (lockFlag) {
                // 释放锁
                lock.unlock();
            }
        }
        return object;
    }

    /**
     * 获取缓存的时间.
     *
     * @param cacheLock 缓存锁注解
     * @return 返回过期时间
     */
    private int getExpireTime(ProceedingJoinPoint joinPoint, CacheLock cacheLock) throws DpException {
        // 获取expire上注解的时间
        Integer expire = cacheLock.expire();
        String spel = cacheLock.expireExpression();
        log.trace("expression {}", spel);
        if (StringUtils.isNotBlank(spel)) {
            //将参数纳入Spring管理
            EvaluationContext context = new StandardEvaluationContext();
            Map<String, Object> indexMap = this.getIndexMap(joinPoint, context);
            try {
                if (spel.contains("#{")) {
                    spel = this.getSpelContent(spel, indexMap);
                }
                log.trace("getExpireTime:spel: {}", spel);
                Expression expression = this.parser.parseExpression(spel);
                expire = expression.getValue(context, Integer.class);
            } catch (EvaluationException e) {
                throw new DpException(StatusCode.CODE_10010.getCode(), e.getMessage());
            }
        }
        return expire == null ? 5 : expire;
    }

    /**
     * 是否需要上锁.
     *
     * @param joinPoint 连接点
     * @param cacheLock 日志注解
     * @return 是否通过
     */
    private boolean isPass(ProceedingJoinPoint joinPoint, CacheLock cacheLock) throws DpException {

        boolean isPass = true;
        String spel = cacheLock.unless();
        log.trace("unless: {}", spel);
        if (StringUtils.isNotBlank(spel)) {

            //将参数纳入Spring管理
            EvaluationContext context = new StandardEvaluationContext();
            Map<String, Object> indexMap = this.getIndexMap(joinPoint, context);
            try {
                if (spel.contains("#{")) {
                    spel = this.getSpelContent(spel, indexMap);
                }
                log.trace("isPass:spel: {}", spel);
                Expression expression = this.parser.parseExpression(spel);
                isPass = Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
            } catch (EvaluationException e) {
                throw new DpException(StatusCode.CODE_10010.getCode(), e.getMessage());
            }
        }
        return isPass;
    }

    /**
     * 从表达式中获取.
     *
     * @param joinPoint 连接点
     * @param cacheLock 日志注解
     * @return 过期时间
     */
    private String getCacheKey(ProceedingJoinPoint joinPoint, CacheLock cacheLock) {

        String cacheKey = cacheLock.key();
        log.trace("cacheKey: {}", cacheKey);
        if (StringUtils.isNotBlank(cacheKey) && cacheKey.contains("#")) {
            String spel = cacheLock.key();
            log.trace("key: {}", spel);
            //将参数纳入Spring管理
            EvaluationContext context = new StandardEvaluationContext();
            Map<String, Object> indexMap = this.getIndexMap(joinPoint, context);
            try {
                if (spel.contains("#{")) {
                    spel = this.getSpelContent(spel, indexMap);
                }
                log.trace("getCacheKey:spel: {}", spel);
                Expression expression = parser.parseExpression(spel);
                cacheKey = expression.getValue(context, String.class);
            } catch (EvaluationException e) {
                log.error(e.getMessage());
                throw new DpException(StatusCode.CODE_10010.getCode(), e.getMessage());
            }
        }
        return cacheKey;
    }

    private String getSpelContent(String spel, Map<String, Object> indexMap) {

        String var = spel;
        for (String key : indexMap.keySet()) {
            String openToken = "#{";
            String closeToken = key + "}";
            String replaceTxt = "#" + indexMap.get(key);
            var = this.getText(var, replaceTxt, openToken, closeToken);
        }
        return var;
    }

    private String getText(String origin, String replaceTxt, String openToken, String closeToken) {
        GenericTokenParser genericTokenParser = new GenericTokenParser(openToken, closeToken, item -> replaceTxt);
        return genericTokenParser.parse(origin);
    }

    private Map<String, Object> getIndexMap(ProceedingJoinPoint joinPoint, EvaluationContext context) {
        //获取方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取参数对象数组
        Object[] args = joinPoint.getArgs();
        //获取方法参数名
        String[] params = this.discoverer.getParameterNames(method);
        Map<String, Object> indexMap = new HashMap<>();
        if (ObjectUtils.isNotEmpty(params)) {
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], args[len]);
                indexMap.put(len + "", params[len]);
            }
        }
        return indexMap;
    }
}
