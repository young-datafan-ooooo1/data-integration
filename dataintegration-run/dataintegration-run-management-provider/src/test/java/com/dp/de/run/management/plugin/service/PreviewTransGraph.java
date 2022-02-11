package com.dp.de.run.management.plugin.service;

import com.youngdatafan.di.run.management.server.trans.TransPreview;
import com.youngdatafan.di.run.management.server.trans.TransPreviewExecutor;
import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.gui.OverwritePrompter;
import org.pentaho.di.core.logging.DefaultLogLevel;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransAdapter;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.TransSupplier;
import org.pentaho.di.trans.step.StepMeta;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import org.eclipse.swt.events.MouseWheelListener;

/**
 * This class handles the display of the transformations in a graphical way using icons, arrows, etc. One transformation
 * is handled per TransGraph
 *
 * @author Matt
 * @since 17-mei-2003
 */
public class PreviewTransGraph {
    // for i18n purposes, needed by Translator2!!
    private static Class<?> PKG = PreviewTransGraph.class;

    private static LogChannelInterface log = new LogChannel("trans");

    private final TransMeta transMeta;

    public Trans trans;

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
     * 运行配置，Pentaho local or Sql
     */
    private String runConfiguration;

    /**
     * 当前选中的步骤，用户断点执行
     */
    private StepMeta debugCurrentStep;

    public PreviewTransGraph(TransMeta transMeta) {
        this.transMeta = transMeta;
    }

    public static void main(String[] args) throws Exception {
        KettleEnvironment.init();

        File file = new File(PreviewTransGraph.class.getClassLoader().getResource("1.xml").getPath());

        Document document = XMLHandler.loadXMLFile(file);
        Element root = document.getDocumentElement();

        TransMeta transMeta = new TransMeta();
        transMeta.loadXML(
                root, file.getPath(), null, null, true, new Variables(),
                new OverwritePrompter() {

                    @Override
                    public boolean overwritePrompt(String message, String rememberText, String rememberPropertyName) {
                        // Yes means: overwrite
                        return true;
                    }

                });

        if (transMeta.hasMissingPlugins()) {
            //TODO
        }

        String previewStepName = "Excel输入";
        int previewSize = 1;

        final TransPreviewExecutor transPreviewExecutor = new TransPreviewExecutor(transMeta);

        transPreviewExecutor.start(previewStepName, previewSize);

        final Trans trans = transPreviewExecutor.getTrans();

        // 循环发送运行状态
        while (!transPreviewExecutor.isFinishedOrStopped()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }

        final TransPreview transPreview = transPreviewExecutor.getTransPreview();
        System.out.println(transPreview.getData(previewStepName));
        System.out.println(transPreview.getFieldNames(previewStepName));

    }


    private Trans createLegacyTrans() {
        try {
            return new Trans(transMeta, null, transMeta.getName(),
                    transMeta.getRepositoryDirectory().getPath(),
                    transMeta.getFilename());
        } catch (KettleException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void start(TransExecutionConfiguration executionConfiguration) {
        if (trans == null || !running) {
            try {
                // Set the requested logging level..
                //
                DefaultLogLevel.setLogLevel(executionConfiguration.getLogLevel());

                transMeta.injectVariables(executionConfiguration.getVariables());

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

                // Important: even though transMeta is passed to the Trans constructor, it is not the same object as is in
                // memory
                // To be able to completely test this, we need to run it as we would normally do in pan
                //
                trans = new TransSupplier(transMeta, log, this::createLegacyTrans).get();


                String spoonLogObjectId = UUID.randomUUID().toString();
                SimpleLoggingObject spoonLoggingObject = new SimpleLoggingObject(Thread.currentThread().getName() + "-" + Thread.currentThread().getId()
                        , LoggingObjectType.SPOON, null);
                spoonLoggingObject.setContainerObjectId(spoonLogObjectId);
                spoonLoggingObject.setLogLevel(executionConfiguration.getLogLevel());
                trans.setParent(spoonLoggingObject);

                trans.setLogLevel(executionConfiguration.getLogLevel());
                trans.setReplayDate(executionConfiguration.getReplayDate());
                trans.setRepository(executionConfiguration.getRepository());
                trans.setMonitored(true);

//                log.logBasic(BaseMessages.getString(PKG, "TransLog.Log.TransformationOpened"));
            } catch (KettleException e) {
                e.printStackTrace();
            }

            if (trans != null) {
                Map<String, String> arguments = executionConfiguration.getArguments();
                final String[] args;
                if (arguments != null) {
                    args = convertArguments(arguments);
                } else {
                    args = null;
                }

//                log.logMinimal(BaseMessages.getString(PKG, "TransLog.Log.LaunchingTransformation")
//                        + trans.getTransMeta().getName() + "]...");

                trans.setSafeModeEnabled(executionConfiguration.isSafeModeEnabled());
                trans.setGatheringMetrics(executionConfiguration.isGatheringMetrics());

                prepareTrans(args);

//                log.logMinimal(BaseMessages.getString(PKG, "TransLog.Log.StartedExecutionOfTransformation"));

            }
        } else {
//                modalMessageDialog(getString("TransLog.Dialog.DoNoStartTransformationTwice.Title"),
//                        getString("TransLog.Dialog.DoNoStartTransformationTwice.Message"), SWT.OK | SWT.ICON_WARNING);
        }

    }

    private synchronized void prepareTrans(final String[] args) {
        try {
            trans.prepareExecution(args);

            initialized = true;
        } catch (KettleException e) {
            log.logError(trans.getName() + ": preparing transformation execution failed", e);
        }

        if (trans.isReadyToStart()) {
            checkStartThreads(); // After init, launch the threads.
        } else {
            initialized = false;
            running = false;
        }
    }

    private void checkStartThreads() {
        if (initialized && !running && trans != null) {
            startThreads();
        }
    }

    private String[] convertArguments(Map<String, String> arguments) {
        String[] argumentNames = arguments.keySet().toArray(new String[arguments.size()]);
        Arrays.sort(argumentNames);

        String[] args = new String[argumentNames.length];
        for (int i = 0; i < args.length; i++) {
            String argumentName = argumentNames[i];
            args[i] = arguments.get(argumentName);
        }
        return args;
    }

    public void stop() {
        if ((running && !halting)) {
            halting = true;
            trans.stopAll();
            log.logMinimal(BaseMessages.getString(PKG, "TransLog.Log.ProcessingOfTransformationStopped"));

            running = false;
            initialized = false;
            halting = false;

            transMeta.setInternalKettleVariables(); // set the original vars back as they may be changed by a mapping
        }
    }

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

    private synchronized void startThreads() {
        running = true;
        try {
            // Add a listener to the transformation.
            // If the transformation is done, we want to do the end processing, etc.
            //
            trans.addTransListener(new TransAdapter() {

                @Override
                public void transFinished(Trans trans) {
                    //TODO
                }
            });

            trans.startThreads();

        } catch (Exception e) {
            log.logError("Error starting step threads", e);
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

    public String getRunConfiguration() {
        return runConfiguration;
    }

    public void setRunConfiguration(String runConfiguration) {
        this.runConfiguration = runConfiguration;
    }

    public StepMeta getDebugCurrentStep() {
        return debugCurrentStep;
    }

    public void setDebugCurrentStep(StepMeta debugCurrentStep) {
        this.debugCurrentStep = debugCurrentStep;
    }
}
