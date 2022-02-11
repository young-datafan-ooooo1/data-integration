package com.youngdatafan.di.run.management.server.trans;

import com.youngdatafan.kettle.springboot.core.bean.SystemVariablesBean;
import com.youngdatafan.kettle.springboot.core.properties.SystemVariableProperties;
import com.youngdatafan.kettle.springboot.core.variable.EngineSystemVariables;
import com.youngdatafan.di.run.management.server.util.AviatorUtils;
import com.youngdatafan.di.run.management.server.vo.ProjectExecutorParam;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.DefaultLogLevel;
import org.pentaho.di.core.logging.HasLogChannelInterface;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.LogParentProvidedInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransAdapter;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;


/**
 * 转换执行器
 *
 * @author Gavin
 * @since 2020-02-09 12:08:48
 */
public class TransExecutor implements LogParentProvidedInterface {
    // for i18n purposes, needed by Translator2!!
    private static Class<?> PKG = TransExecutor.class;

    private final TransMeta transMeta;

    public Trans trans;

    /**
     * 数据源ID
     */
    private String datasourceId;

    /**
     * 是否初始化
     */
    private boolean initialized;

    /**
     * 运行中
     */
    private boolean running;

    /**
     * 停止中
     */
    private boolean halting;

    /**
     * 暂停中
     */
    private boolean pausing;

    /**
     * 转换运行完成
     */
    private AtomicBoolean transFinished = new AtomicBoolean(false);

    /**
     * 项目启动时间
     */
    private Date startDate = new Date();

    private TransPreview transPreview = new TransPreview();

    public TransExecutor(TransMeta transMeta) {
        this.transMeta = transMeta;
    }

    /**
     * 启动脚本
     *
     * @param executionConfiguration TransExecutionConfiguration
     */
    public synchronized void start(TransExecutionConfiguration executionConfiguration, ProjectExecutorParam projectExecutorParam) throws KettleException {
        transFinished.set(false);

        if (trans == null || !running) {
            startDate = new Date();
            // Set the requested logging level..
            //
            DefaultLogLevel.setLogLevel(executionConfiguration.getLogLevel());

            transMeta.injectVariables(executionConfiguration.getVariables());

            // 优先设置系统变量，这样可以保证用户执行的时候运行覆盖系统默认参数
            final SystemVariableProperties systemVariableProperties = EngineSystemVariables.getInstance().getSystemVariableProperties();
            if (systemVariableProperties != null) {
                final List<SystemVariablesBean> systemVariables = systemVariableProperties.getSystemVariables();
                if (systemVariables != null && !systemVariables.isEmpty()) {
                    for (SystemVariablesBean systemVariable : systemVariables) {
                        // 执行表达式取值
                        String value = Const.NVL(AviatorUtils.execute(systemVariable.getExpression()), "");
                        transMeta.setParameterValue(systemVariable.getName(), value);
                        transMeta.setVariable(systemVariable.getName(), value);
                    }
                }
            }

            // Set the named parameters
            Map<String, String> paramMap = executionConfiguration.getParams();
            Set<String> keys = paramMap.keySet();
            for (String key : keys) {
                transMeta.setParameterValue(key, Const.NVL(paramMap.get(key), ""));
            }

            transMeta.activateParameters();

            // Also make sure to clear the log entries in the central log store & registry
            //
            if (trans != null) {
                KettleLogStore.discardLines(trans.getLogChannelId(), true);
            }

            //  创建trans
            trans = new Trans(transMeta);

            String spoonLogObjectId = UUID.randomUUID().toString();
            SimpleLoggingObject spoonLoggingObject = new SimpleLoggingObject(Thread.currentThread().getName() + "-" + Thread.currentThread().getId()
                    , LoggingObjectType.SPOON, null);
            spoonLoggingObject.setContainerObjectId(spoonLogObjectId);
            spoonLoggingObject.setLogLevel(executionConfiguration.getLogLevel());
            trans.setParent(spoonLoggingObject);

            trans.setLogLevel(executionConfiguration.getLogLevel());
            trans.setReplayDate(executionConfiguration.getReplayDate());
            trans.setRepository(executionConfiguration.getRepository());
            trans.setMonitored(false);

            if (trans != null) {
                Map<String, String> arguments = executionConfiguration.getArguments();
                final String[] args;
                if (arguments != null) {
                    args = convertArguments(arguments);
                } else {
                    args = null;
                }

                trans.getLogChannel().logBasic("正在启动项目");

                trans.setSafeModeEnabled(executionConfiguration.isSafeModeEnabled());
                trans.setGatheringMetrics(executionConfiguration.isGatheringMetrics());

                // 预处理脚本
                prepareExecution(args);

                // 设置数据预览
                if (projectExecutorParam.isPreview()) {
                    trans.setPreview(true);

                    transPreview.setPreviewSize(projectExecutorParam.getPreviewSize());

                    if (StringUtils.isBlank(projectExecutorParam.getPreviewModel())) {
                        transPreview.setPreviewMode(TransPreview.PreviewMode.FIRST);
                    } else {
                        transPreview.setPreviewMode(TransPreview.PreviewMode.valueOf(projectExecutorParam.getPreviewModel()));
                    }

                    transPreview.capturePreviewData(trans, transMeta.getSteps());
                }

                // 启动转换
                startTrans();

                trans.getLogChannel().logBasic("已开始执行项目");
            }
        }
    }

    /**
     * 预处理转换
     */
    public void prepareExecution(String[] args) throws KettleException {
        try {
            trans.prepareExecution(args);

            initialized = true;
        } catch (KettleException e) {
            initialized = false;
            running = false;
            throw e;
        }
    }

    /**
     * 执行转换
     */
    private synchronized void startTrans() throws KettleException {
        if (trans.isReadyToStart()) {
            checkStartThreads(); // After init, launch the threads.
        } else {
            initialized = false;
            running = false;
        }
    }

    /**
     * 检查启动线程状态
     */
    private void checkStartThreads() {
        if (initialized && !running && trans != null) {
            startThreads();
        }
    }

    /**
     * 参数转换
     *
     * @param arguments map
     * @return 字符串数组
     */
    private String[] convertArguments(Map<String, String> arguments) {
        String[] argumentNames = arguments.keySet().toArray(new String[0]);
        Arrays.sort(argumentNames);

        String[] args = new String[argumentNames.length];
        for (int i = 0; i < args.length; i++) {
            String argumentName = argumentNames[i];
            args[i] = arguments.get(argumentName);
        }
        return args;
    }

    /**
     * 停止运行脚本
     */
    public void stop() {
        if ((running && !halting)) {
            halting = true;
            trans.stopAll();
            trans.getLogChannel().logMinimal(BaseMessages.getString(PKG, "脚本处理已停止。"));

            running = false;
            initialized = false;
            halting = false;

            transMeta.setInternalKettleVariables(); // set the original vars back as they may be changed by a mapping
        }
    }

    /**
     * 暂停或者恢复脚本
     */
    public synchronized void pauseResume() {
        if (running) {
            // Get the pause toolbar item
            //
            if (!pausing) {
                pausing = true;
                trans.pauseRunning();
            } else {
                pausing = false;
                trans.resumeRunning();
            }
        }
    }

    /**
     * 启动线程
     */
    private synchronized void startThreads() {
        running = true;
        try {
            // Add a listener to the transformation.
            // If the transformation is done, we want to do the end processing, etc.
            //
            trans.addTransListener(new TransAdapter() {

                @Override
                public void transFinished(Trans trans) {
                    transFinished.set(true);
                    trans.getLogChannel().logBasic("项目执行完成，耗时{0}秒", (System.currentTimeMillis() - startDate.getTime()) / 1000);
                }
            });

            trans.startThreads();

        } catch (Exception e) {
            // 线程启动失败，停止所有作业
            trans.stopAll();
            trans.getLogChannel().logError("启动步骤线程时出错。", e);
        }
    }

    public TransMeta getTransMeta() {
        return transMeta;
    }

    public Trans getTrans() {
        return trans;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isHalting() {
        return halting;
    }

    public boolean isPausing() {
        return pausing;
    }

    public boolean isFinishedOrStopped() {
        //为什么不用trans.isFinishedOrStopped()？因为改方法返回true时
        // ，步骤状态在finally里面的代码不一定执行完成了，所以会导致转换运行完成，步骤状态还是运行中的问题

        // 先判断转换是否完成
        if (transFinished.get()) {
            return true;
        } else {
            // 如果转换已经停止，则返回
            return trans.isStopped();
        }
    }

    @Override
    public HasLogChannelInterface getLogChannelProvider() {
        return new HasLogChannelInterface() {
            @Override
            public LogChannelInterface getLogChannel() {
                return getTrans() != null ? getTrans().getLogChannel() : getTransMeta().getLogChannel();
            }
        };
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public TransPreview getTransPreview() {
        return transPreview;
    }
}
