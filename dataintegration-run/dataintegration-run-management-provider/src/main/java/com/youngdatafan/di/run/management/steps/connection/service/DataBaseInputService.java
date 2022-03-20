package com.youngdatafan.di.run.management.steps.connection.service;

import com.alibaba.fastjson.JSONObject;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.youngdatafan.di.run.management.steps.connect.dto.FieldDTO;
import com.youngdatafan.di.run.management.steps.connect.dto.PreviewDataInfoDTO;
import com.youngdatafan.di.run.management.steps.connect.vo.ConnectionDetailVO;
import com.youngdatafan.di.run.management.steps.connect.vo.QueryVO;
import com.youngdatafan.di.run.management.server.util.SqlUtills;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.DatabaseTestResults;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Echo_Sxl on 2020/8/6 18:18
 * @version 1.0
 */
@Slf4j
@Service
public class DataBaseInputService {

    @Autowired
    ResourceLoader resourceLoader;


    public Result getDbJson() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:kettle/db.json");
        InputStream resourceAsStream = resource.getInputStream();
        final byte[] data = new byte[resourceAsStream.available()];
        IOUtils.readFully(resourceAsStream, data);
        String string = new String(data);
        JSONObject json = JSONObject.parseObject(string);
        return Result.success(json);
    }

    public Result testConnect(ConnectionDetailVO connectionetail) {

        String projectFile = getJsonInfo(connectionetail);
        try {
            DatabaseMeta databaseMeta = new DatabaseMeta(projectFile);

            DatabaseTestResults databaseTestResults = databaseMeta.testConnectionSuccess();


            if (databaseTestResults.isSuccess()) {
                return Result.success(true);
            } else {
                return Result.fail(StatusCode.CODE_10010.getCode(), "", databaseTestResults.getMessage());
            }

        } catch (KettleXMLException e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "初始化DatabaseMeta异常");

        }

    }


    public Result getProcedure(ConnectionDetailVO connectionetail) {

        String projectFile = getJsonInfo(connectionetail);
        try {
            DatabaseMeta databaseMeta = new DatabaseMeta(projectFile);

            final Database database = new Database(databaseMeta);
            database.connect();
            try {
                String[] procedures = database.getProcedures();
                return Result.success(procedures);

            } catch (KettleDatabaseException e) {
                e.printStackTrace();
                return Result.fail(StatusCode.CODE_10010.getCode(), "", "获取存储过程失败");

            } finally {
                database.disconnect();
            }

        } catch (KettleXMLException | KettleDatabaseException e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "初始化DatabaseMeta异常");

        }

    }

    public Result<PreviewDataInfoDTO, Object> previewData(QueryVO queryVO) {
        PreviewDataInfoDTO previewDataInfoDTO = new PreviewDataInfoDTO();
        final ConnectionDetailVO connectionDetailVO = queryVO.getConnectionDetailVO();
        String projectFile = getJsonInfo(connectionDetailVO);

        // 控制最大limit
        final Integer limit = connectionDetailVO.getConnection().getLimit();
        int maxLimit = Math.min(limit == null ? 10 : limit, 10000);
        connectionDetailVO.getConnection().setLimit(maxLimit);

        Database database = null;
        ResultSet resultSet = null;

        try {
            DatabaseMeta databaseMeta = new DatabaseMeta(projectFile);
            database = new Database(databaseMeta);
            database.connect();
            database.setAutoCommit(false);

            String[] fieldNames = null;
            List<Object[]> datas = new ArrayList<>();

            // 执行查询
            resultSet = database.openQuery(queryVO.getQuerySql());

            Object[] row;
            while ((row = database.getRow(resultSet)) != null) {
                // 获取字段名
                if (fieldNames == null) {
                    fieldNames = database.getReturnRowMeta().getFieldNames();
                }

                datas.add(row);
            }

            if (fieldNames != null) {
                previewDataInfoDTO.setCloumns(Arrays.asList(fieldNames));
            }
            previewDataInfoDTO.setDataDTO(datas);

            return Result.success(previewDataInfoDTO);
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010.getCode(), "获取预览数据异常", e);

        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (database != null) {
                database.disconnect();
            }
        }

    }

    public Result getFieldInfo(QueryVO queryVO) {
        String projectFile = getJsonInfo(queryVO.getConnectionDetailVO());
        List<FieldDTO> cloumnInfos = new ArrayList<>();
        Database database = null;

        try {
            // 连接数据库
            DatabaseMeta databaseMeta = new DatabaseMeta(projectFile);
            database = new Database(databaseMeta);
            database.connect();

            String sql = queryVO.getQuerySql();
            sql = SqlUtills.parseSql(sql);

            // 查询字段信息
            RowMetaInterface queryFields = database.getQueryFields(sql, false);
            final int size = queryFields.size();

            for (int i = 0; i < size; i++) {
                FieldDTO fieldDTO = new FieldDTO();
                final ValueMetaInterface valueMeta = queryFields.getValueMeta(i);
                fieldDTO.setFieldName(valueMeta.getName());
                fieldDTO.setFieldType(ValueMetaInterface.getTypeDescription(valueMeta.getType()));
                fieldDTO.setFieldLength(valueMeta.getLength());
                fieldDTO.setFieldPrecision(valueMeta.getPrecision());
                cloumnInfos.add(fieldDTO);
            }

        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010.getCode(), "获取字段信息失败", e);

        } finally {
            if (database != null) {
                database.disconnect();
            }
        }

        return Result.success(cloumnInfos);
    }

    /**
     * @param connectionDetailVO 连接信息
     * @return xml
     */
    public String getJsonInfo(ConnectionDetailVO connectionDetailVO) {

        String projectFile = StringEscapeUtils.unescapeXml(JsonUtils.toString(connectionDetailVO));
        projectFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + XML.toString(new JSONLinkedObject(projectFile));
        return projectFile;
    }
}
