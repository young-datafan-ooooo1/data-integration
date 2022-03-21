package com.dp.de.run.management.plugin.service;

import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.di.run.management.DiRunManagementApplication;
import com.youngdatafan.di.run.management.server.service.ProjectExecutorService;
import com.youngdatafan.di.run.management.server.vo.ProjectExecutorParam;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;

/**
 * @author gavin
 * @since 2020/1/16 7:21 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DiRunManagementApplication.class, ProjectExecutorServiceTest.class}
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProjectExecutorServiceTest {

    @Autowired
    private ProjectExecutorService projectExecutorService;

    @Test
    public void execute() throws Exception {
        final InputStream resourceAsStream = JsonToXmlTest.class.getClassLoader().getResourceAsStream("run.json");
        final byte[] data = new byte[resourceAsStream.available()];
        IOUtils.readFully(resourceAsStream, data);
        String xml = new String(data);
        ProjectExecutorParam executorParamVO = new ProjectExecutorParam();
        executorParamVO.setLogLevel("Basic");
        executorParamVO.setProjectId("b38b3b6f5a5c462dae6d517803f4b13f");
        executorParamVO.setProjectName("测试项目");
        executorParamVO.setUserId("00000000");
        executorParamVO.setUserName("admin");
        executorParamVO.setSafeModeEnabled(false);
        executorParamVO.setPreview(true);
        executorParamVO.setPreviewSize(100);

        String executorId = projectExecutorService.generateExecutorId(executorParamVO.getProjectId());
        executorParamVO.setExecutorId(executorId);

        projectExecutorService.executeByFile(xml, executorParamVO
                , (userId, webSocketMessage) -> {

                    // 打印
                    System.out.println(JsonUtils.toString(webSocketMessage));
                }
        );
    }

}
