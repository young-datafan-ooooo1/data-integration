package com.youngdatafan.portal.model.management.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.pentaho.di.core.KettleClientEnvironment;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * kettle启动监听器.
 *
 * @author gavin
 * @since 2020/2/18 4:38 下午
 */
@Component
public class KettleStarterListener implements ServletContextListener {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KettleStarterListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("KETTLE_CONTEXT_PATH", sce.getServletContext().getContextPath());
        /*
         *  The following lines are from Spoon.main
         *  because they are application-wide context.
         */
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<KettleException> pluginRegistryFuture = executor.submit(new Callable<KettleException>() {

            @Override
            public KettleException call() throws Exception {
                KettleClientEnvironment.getInstance().setClient(KettleClientEnvironment.ClientType.SPOON);
                try {
                    logger.info("开始加载kettle环境信息,KettleEnvironment.init(false)");
                    KettleEnvironment.init(false);
                    logger.info("加载kettle环境信息完成。");
                } catch (KettleException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
        KettleException registryException;
        try {
            registryException = pluginRegistryFuture.get();
            // 关闭线程池
            executor.shutdown();

            if (registryException != null) {
                throw registryException;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //shutdown
        KettleEnvironment.shutdown();
    }
}
