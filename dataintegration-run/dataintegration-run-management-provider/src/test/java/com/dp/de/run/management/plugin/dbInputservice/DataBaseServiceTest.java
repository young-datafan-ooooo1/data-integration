/*
package com.dp.de.run.management.plugin.dbInputservice;

import com.datafan.dataintegration.core.model.Result;
import com.dp.de.run.management.plugin.service.JsonToXmlTest;
import com.dp.de.run.management.plugin.service.ProjectExecutorServiceTest;
import com.youngdatafan.di.run.management.DiRunManagementApplication;

import com.youngdatafan.di.run.management.kettle.trans.steps.connect.vo.ConnectionDetailVO;
import com.youngdatafan.di.run.management.kettle.trans.steps.connection.service.DataBaseInputService;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.DatabaseTestResults;
import com.datafan.dataintegration.core.util.sql.DatabaseType;
import org.pentaho.di.core.xml.XMLHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DiRunManagementApplication.class, ProjectExecutorServiceTest.class}
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DataBaseServiceTest {

@Autowired
private DataBaseInputService dataBaseInputService;
    @Test
    public void testConnect() throws Exception {
        // 初始化kettle环境，只需要初始化一次
        KettleEnvironment.init(false);

        DatabaseMeta databaseMeta = new DatabaseMeta(UUID.randomUUID().toString()
                , DatabaseType.CLICKHOUSE.name(), "JDBC", "tencent.vincenthsing.top"
                , "default", "50812", "default", "jRtXD8F81");

        databaseMeta.setMaximumPoolSize(10);
        databaseMeta.setForcingIdentifiersToLowerCase(true);
        databaseMeta.setInitialPoolSize(1);
        databaseMeta.setDescription(null);
        DatabaseTestResults databaseTestResults = databaseMeta.testConnectionSuccess();
        databaseTestResults.isSuccess();
        System.out.println("-----------------");
        System.out.println(databaseTestResults.isSuccess());
        System.out.println(databaseTestResults.getMessage());



    }


    @Test
    public void testConnectByFile() throws Exception {
        KettleEnvironment.init();

        final InputStream resourceAsStream = JsonToXmlTest.class.getClassLoader().getResourceAsStream("connect.xml");
        final byte[] data = new byte[resourceAsStream.available()];
        IOUtils.readFully(resourceAsStream, data);
        String projectFile = new String(data);
        System.out.println(projectFile);
        //json转xml
       */
/* projectFile = StringEscapeUtils.unescapeXml(projectFile);
        projectFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + XML.toString(new JSONLinkedObject(projectFile));*//*


        DatabaseMeta databaseMetaByfile =    new DatabaseMeta(projectFile);
        System.out.println(databaseMetaByfile.testConnectionSuccess().isSuccess());
    }


    */
/**
     *     "name": "deby",
     *     "server": "127.0.0.1",
     *     "type": "MYSQL",
     *     "access": "Native",
     *     "database": "test",
     *     "port": "3306",
     *     "username": "root",
     *     "password": "Encrypted 2be98afc86aa7f2e4cb79ff228dc6fa8c",
     *      final Connection connection = DriverManager.getConnection("jdbc:clickhouse://tencent.vincenthsing.top:50812/default"
     *                 , "default", "jRtXD8F8");
     * @throws Exception
     *//*

    @Test
    public void testConnectService() throws Exception {
        ConnectionDetailVO connectionDetailVO =new ConnectionDetailVO();
       ConnectionDetailVO.ConnectionBean connectionBean =new ConnectionDetailVO.ConnectionBean();
        connectionBean.setUsername("deby");
        connectionBean.setAccess("Native");
        connectionBean.setType("MYSQL");
        connectionBean.setServer("10.242.10.166");
        connectionBean.setDatabase("dp");
        connectionBean.setUsername("root");
        connectionBean.setPassword("QazxsW77!11");
        connectionBean.setPort("3306");
        connectionDetailVO.setConnection(connectionBean);
        Result result = dataBaseInputService.getProcedure(connectionDetailVO);
        System.out.println(result);
    }




}
*/
