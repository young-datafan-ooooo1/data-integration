package com.youngdatafan.di.run.management.server.service;


import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.youngdatafan.di.run.management.server.dto.ProjectPreviewExecutorDTO;
import com.youngdatafan.di.run.management.server.trans.LogBrowser;
import com.youngdatafan.di.run.management.server.trans.TransPreview;
import com.youngdatafan.di.run.management.server.trans.TransPreviewExecutor;
import com.youngdatafan.di.run.management.server.vo.ProjectFilePreviewExecutorVO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

/**
 * 项目预览执行实现
 *
 * @author gavin
 * @since 2020/2/13 10:53 上午
 */
@Service
public class ProjectPreviewExecutorService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectPreviewExecutorService.class);

    /**
     * 临时文件夹
     */
    @Value("${dp.project.execute.tmpFolder:./tmp/}")
    private String tmpFolder;
    /**
     * 临时文件后缀
     */
    @Value("${dp.project.execute.tmpFileSuffix:.xml}")
    private String tmpFileSuffix;
    @Value("${dp.project.execute.deleteTmpFile:true}")
    private boolean deleteTmpFile;
    /**
     * 刷新频率，单位：毫秒
     */
    @Value("${dp.project.execute.refreshRateMs:800}")
    private int refreshRateMs;

    /**
     * 执行预览超时时间
     */
    @Value("${dp.project.execute.previewTimeoutSeconds:300}")
    private int previewTimeoutSeconds;

    /**
     * 根据项目文件执行
     *
     * @param previewExecutorVO 执行参数
     */
    public ProjectPreviewExecutorDTO executeByFile(ProjectFilePreviewExecutorVO previewExecutorVO) throws Exception {
        String executorId = generateExecutorId(previewExecutorVO.getProjectId());

        try {
            // 执行
            return execute(executorId, previewExecutorVO);

        } finally {
            if (deleteTmpFile) {
                File tmpFile = new File(tmpFolder, executorId + tmpFileSuffix);
                if (tmpFile.exists()) {
                    logger.info("删除临时文件： filePath: {}, 删除状态:{}", tmpFile.getPath(), tmpFile.delete());
                }
            }
        }
    }

    /**
     * 执行
     *
     * @param previewExecutorVO ProjectFilePreviewExecutorVO
     * @return 执行器
     */
    private ProjectPreviewExecutorDTO execute(String executorId, ProjectFilePreviewExecutorVO previewExecutorVO) throws Exception {
        final long timeMillis = System.currentTimeMillis();

        // 启动转换
        TransPreviewExecutor transExecutor = start(executorId, previewExecutorVO);

        // 日志浏览器
        LogBrowser logBrowser = new LogBrowser(transExecutor);

        // 循环发送运行状态
        while (!transExecutor.isFinishedOrStopped()) {

            if ((System.currentTimeMillis() - timeMillis) / 1000 >= previewTimeoutSeconds) {
                //超时时间处理
                logger.warn("执行预览超时，跳出循环。");
                break;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(refreshRateMs);
            } catch (InterruptedException e) {
                logger.error("execute InterruptedException.");
                break;
            }
        }

        logger.info("项目执行完成，项目id:{}", previewExecutorVO.getProjectId());

        // 获取预览数据
        final TransPreview transPreview = transExecutor.getTransPreview();
        String previewStepName = previewExecutorVO.getPreviewStepName();
        // 获取预览数据
        final List<String[]> previewData = transPreview.getData(previewStepName);
        // 获取预览字段
        final List<String> previewFieldNames = transPreview.getFieldNames(previewStepName);
        // 预览字段类型
        List<String> previewFieldTypes = null;
        if (previewFieldNames != null) {
            final RowMetaInterface rowMeta = transPreview.getRowMeta(previewStepName);
            previewFieldTypes = new ArrayList<>(previewFieldNames.size());

            for (int i = 0; i < previewFieldNames.size(); i++) {
                previewFieldTypes.add(rowMeta.getValueMeta(i).getTypeDesc());
            }
        }

        // 构建返回对象
        return ProjectPreviewExecutorDTO.builder()
                .errors(transExecutor.getTrans().getErrors())
                .previewRows(previewData)
                .previewFieldNames(previewFieldNames)
                .previewFieldTypes(previewFieldTypes)
                .log(logBrowser.getRealTimeLog()).build();
    }

    /**
     * 生成唯一的执行id
     *
     * @return 唯一的执行id
     */
    public String generateExecutorId(String projectId) {
        // requestId + 12位随机字母
        return projectId + "_" + RandomStringUtils.randomAlphabetic(12);
    }

    /**
     * 启动转换
     *
     * @param executorId        执行器id
     * @param previewExecutorVO ProjectFilePreviewExecutorVO
     * @return TransPreviewExecutor
     */
    private TransPreviewExecutor start(String executorId, ProjectFilePreviewExecutorVO previewExecutorVO) throws Exception {
        // 构建TransMeta 对象
        TransMeta transMeta = buildTransMeta(executorId, previewExecutorVO.getProjectFile());

        final TransPreviewExecutor transPreviewExecutor = new TransPreviewExecutor(transMeta);

        try {
            // 启动预览作业
            transPreviewExecutor.start(previewExecutorVO.getVariables(), previewExecutorVO.getPreviewStepName(), previewExecutorVO.getPreviewSize());

        } catch (Exception e) {
            logger.error("启动预览作业失败", e);
            // 获取步骤日志
            throw new DpException(StatusCode.CODE_10010.getCode(), new LogBrowser(transPreviewExecutor).getRealTimeLog());
        }
        return transPreviewExecutor;
    }

    /**
     * 构建TransMeta 对象
     *
     * @param projectFile 项目文件
     * @return TransMeta
     */
    public TransMeta buildTransMeta(String executorId, String projectFile) throws IOException, KettleXMLException, KettleMissingPluginsException {
        Document document;
        //json转xml
        if (!projectFile.startsWith("<?xml")) {
            // json转xml
            projectFile = StringEscapeUtils.unescapeXml(projectFile);
            projectFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + XML.toString(new JSONLinkedObject(projectFile));

            logger.debug("json转换成xml，转换后的xml:{}", projectFile);
        }

        // 写到临时目录
        File outFile = new File(tmpFolder, executorId + tmpFileSuffix);
        FileUtils.writeStringToFile(outFile, projectFile);

        // 加载xml
        document = XMLHandler.loadXMLString(projectFile);

        TransMeta transMeta = new TransMeta();
        transMeta.loadXML(
                document.getDocumentElement(), outFile.getPath(), null, null, true, new Variables(),
                (message, rememberText, rememberPropertyName) -> {
                    // Yes means: overwrite
                    return true;
                });

        if (transMeta.hasMissingPlugins()) {
            logger.info("【{}】缺少执行插件。", projectFile);
        }

        return transMeta;
    }

}
