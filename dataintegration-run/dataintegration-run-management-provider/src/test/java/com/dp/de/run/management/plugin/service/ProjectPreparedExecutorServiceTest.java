package com.dp.de.run.management.plugin.service;

import com.youngdatafan.di.run.management.DiRunManagementApplication;
import com.youngdatafan.di.run.management.server.service.ProjectScheduleExecutorService;
import com.youngdatafan.di.run.management.server.vo.ProjectExecutorParam;
import com.youngdatafan.di.run.management.server.vo.ProjectFilePreviewExecutorVO;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gavin
 * @since 2020/1/16 7:21 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DiRunManagementApplication.class, ProjectPreparedExecutorServiceTest.class}
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProjectPreparedExecutorServiceTest {

    @Autowired
    private ProjectScheduleExecutorService projectExecutorService;

    @Test
    public void execute() throws Exception {
        final InputStream resourceAsStream = JsonToXmlTest.class.getClassLoader().getResourceAsStream("run.json");
        final byte[] data = new byte[resourceAsStream.available()];
        IOUtils.readFully(resourceAsStream, data);
        String xml = new String(data);
        ProjectFilePreviewExecutorVO executorParamVO = new ProjectFilePreviewExecutorVO();
        executorParamVO.setProjectId("f39786247c5d4d8e96641020fa704900");
        executorParamVO.setProjectName("测试项目");

        executorParamVO.setPreviewSize(100);
        executorParamVO.setProjectFile(xml);
        Map<String, String> variables=new HashMap<>();
        variables.put("Internal.Job.Filename.Directory","Parent Job File Directory");
        variables.put("Internal.Job.Filename.Name","Parent Job Filename");
        variables.put("Internal.Job.Name","Parent Job Name");
        variables.put("Internal.Job.Repository.Directory","Parent Job Repository Directory");

        // 创建执行参数对象
        ProjectExecutorParam projectExecutorParam = new ProjectExecutorParam();
        BeanUtils.copyProperties(executorParamVO, projectExecutorParam);
        projectExecutorParam.setExecutorId("f39786247c5d4d8e96641020fa704900");
        projectExecutorParam.setUserId("userId");
        projectExecutorParam.setUserName("userName");
        projectExecutorParam.setRequestId("/project/f39786247c5d4d8e96641020fa704900");
        projectExecutorParam.setVariables(variables);
        projectExecutorParam.setPreview(true);
        projectExecutorParam.setLogLevel("Basic");
        projectExecutorParam.setSafeModeEnabled(false);
//        projectExecutorService.executeByFile(xml, projectExecutorParam
//                , (destination, webSocketMessage) -> {
//                    // 打印日志
//                    System.out.println(" // 打印日志");
//                    System.out.println(JsonUtils.toString(webSocketMessage));
//                });

    }

}
