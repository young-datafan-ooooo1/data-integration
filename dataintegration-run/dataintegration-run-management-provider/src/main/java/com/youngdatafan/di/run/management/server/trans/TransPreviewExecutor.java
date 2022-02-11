package com.youngdatafan.di.run.management.server.trans;

import com.youngdatafan.kettle.springboot.core.bean.SystemVariablesBean;
import com.youngdatafan.kettle.springboot.core.properties.SystemVariableProperties;
import com.youngdatafan.kettle.springboot.core.variable.EngineSystemVariables;
import com.youngdatafan.di.run.management.server.util.AviatorUtils;
import java.util.List;
import java.util.Map;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.HasLogChannelInterface;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.LogParentProvidedInterface;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.debug.BreakPointListener;
import org.pentaho.di.trans.debug.StepDebugMeta;
import org.pentaho.di.trans.debug.TransDebugMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.dummytrans.DummyTransMeta;


/**
 * 转换预览执行器
 *
 * @author Gavin
 * @since 2020-02-09 12:08:48
 */
public class TransPreviewExecutor implements LogParentProvidedInterface {
    // for i18n purposes, needed by Translator2!!
    private static Class<?> PKG = TransPreviewExecutor.class;

    private final TransMeta transMeta;

    public Trans trans;

    private TransPreview transPreview = new TransPreview();

    public TransPreviewExecutor(TransMeta transMeta) {
        this.transMeta = transMeta;
    }

    /**
     * 启动脚本
     *
     * @param previewStepName 预览数据步骤名
     * @param previewSize     数据预览记录数
     */
    public void start(String previewStepName, int previewSize) throws KettleException {
        start(null, previewStepName, previewSize);
    }

    /**
     * 启动脚本
     *
     * @param variables       变量
     * @param previewStepName 预览数据步骤名
     * @param previewSize     数据预览记录数
     */
    public void start(Map<String, String> variables, String previewStepName, int previewSize) throws KettleException {
        // 创建预览转换
        final TransMeta transMeta = generatePreviewTransMeta(previewStepName);

        // 设置参数变量
        if (variables != null) {
            transMeta.injectVariables(variables);
        }

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

        // This transformation is ready to run in preview!
        trans = new Trans(transMeta);

        // 转换与执行
        trans.prepareExecution(null);

        // 预览捕获
        trans.setPreview(true);
        transPreview.setPreviewSize(previewSize);
        transPreview.capturePreviewData(trans, transMeta.getSteps());

        // 添加预览/调试信息。。。
        //
        TransDebugMeta transDebugMeta = new TransDebugMeta(transMeta);
        StepMeta stepMeta = transMeta.findStep(previewStepName);
        StepDebugMeta stepDebugMeta = new StepDebugMeta(stepMeta);
        stepDebugMeta.setReadingFirstRows(true);
        stepDebugMeta.setRowCount(previewSize);
        transDebugMeta.getStepDebugMetaMap().put(stepMeta, stepDebugMeta);

        transDebugMeta.addBreakPointListers(new BreakPointListener() {
            @Override
            public void breakPointHit(TransDebugMeta transDebugMeta, StepDebugMeta stepDebugMeta,
                                      RowMetaInterface rowBufferMeta, List<Object[]> rowBuffer) {
                // 停止转换
                trans.stopAll();
            }
        });
        // 在转换上设置适当的侦听器。。。
        //
        transDebugMeta.addRowListenersToTransformation(trans);

        // 开始运行
        //
        trans.startThreads();
    }

    /**
     * 停止转换
     */
    public void stop() {
        if (trans != null) {
            trans.stopAll();
        }
    }

    /**
     * 判断转换是否完成或者停止
     */
    public boolean isFinishedOrStopped() {
        return this.trans.isFinishedOrStopped();
    }

    /**
     * 创建预览转换
     *
     * @param previewStepName 预览步骤名
     * @return 预览转换对象
     */
    public TransMeta generatePreviewTransMeta(String previewStepName) {
        final StepMeta one = transMeta.findStep(previewStepName);

        if (one == null) {
            throw new NullPointerException("找不到预览步骤[" + previewStepName + "]");
        }

        PluginRegistry registry = PluginRegistry.getInstance();
        TransMeta previewMeta = new TransMeta(transMeta);
        previewMeta.setName(transMeta.toString());

        one.setLocation(50, 50);
        one.setDraw(true);
        previewMeta.addStep(one);
        DummyTransMeta twoMeta = new DummyTransMeta();
        StepMeta two = new StepMeta(registry.getPluginId(StepPluginType.class, twoMeta), "dummy", twoMeta);
        two.setLocation(250, 50);
        two.setDraw(true);
        previewMeta.addStep(two);
        TransHopMeta hop = new TransHopMeta(one, two);
        previewMeta.addTransHop(hop);

        return previewMeta;
    }


    public TransMeta getTransMeta() {
        return transMeta;
    }

    public Trans getTrans() {
        return trans;
    }

    public TransPreview getTransPreview() {
        return transPreview;
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
}
