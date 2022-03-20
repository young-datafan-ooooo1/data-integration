package com.youngdatafan.di.run.management.server.service;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.di.run.management.server.vo.PreviewDataVO;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.RowMeta;
import com.youngdatafan.dataintegration.core.util.sql.DataSourceWrap;
import com.youngdatafan.dataintegration.core.util.sql.DatabaseType;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gavin
 * @since 2020/2/29 2:02 下午
 */
@Slf4j
@Service
public class DataPreviewService {

    /**
     * 数据预览
     *
     * @param dataSourceWrap DataSourceWrap
     * @param previewDataVO  PreviewStatsVO
     * @return data
     */
    public List<Map<String, Object>> previewData(DataSourceWrap dataSourceWrap, PreviewDataVO previewDataVO) {
        try {
            return executeQuery(dataSourceWrap.getDatabaseType(), dataSourceWrap.getDataSource().getConnection()
                    , previewDataVO.getDataQuerySql(), 0, previewDataVO.getSize());
        } catch (Exception e) {
            throw new DpException(StatusCode.CODE_10010.getCode(), "数据预览查询失败");
        }
    }

    /**
     * 执行查询
     *
     * @param databaseType 数据库类型
     * @param connection   connection
     * @param sql          查询sql
     * @param rowMeta      参数值类型
     * @param params       查询参数
     * @param pageNo       页号
     * @param pageSize     每页大小
     * @return list map
     */
    public List<Map<String, Object>> executeQuery(DatabaseType databaseType, Connection connection
            , String sql, RowMeta rowMeta, List<Object> params, int pageNo, int pageSize) {
        List<Map<String, Object>> values = new ArrayList<>(pageSize / 2);

        final DatabaseMeta databaseMeta = new DatabaseMeta(""
                , databaseType.name(), "JDBC", "", "", "", "", "");
        final Database database = new Database(databaseMeta);
        ResultSet resultSet = null;

        try {
            database.setConnection(connection);
            // 获取分页sql
            /*String pagingQuerySQL = databaseMeta.getDatabaseInterface().get(sql
                , pageNo, pageSize);*/

            String[] fieldNames = null;
            if (rowMeta == null) {
                resultSet = database.openQuery(sql);
            } else {
                resultSet = database.openQuery(sql, rowMeta, params.toArray());
            }

            Object[] row;
            while ((row = database.getRow(resultSet)) != null) {
                // 获取字段名
                if (fieldNames == null) {
                    fieldNames = database.getReturnRowMeta().getFieldNames();
                }

                // 数组转map
                final int columnCount = fieldNames.length;
                Map<String, Object> value = new HashMap<>(columnCount);
                for (int i = 0; i < columnCount; i++) {
                    value.put(fieldNames[i], row[i]);
                }
                
                values.add(value);
            }

        } catch (Exception e) {
            log.error("数据查询sql", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            database.disconnect();
        }

        return values;
    }

    /**
     * 执行查询
     *
     * @param databaseType 数据库类型
     * @param connection   connection
     * @param sql          查询sql
     * @param pageNo       页号
     * @param pageSize     每页大小
     * @return list map
     */
    public List<Map<String, Object>> executeQuery(DatabaseType databaseType, Connection connection
            , String sql, int pageNo, int pageSize) {
        return executeQuery(databaseType, connection, sql, null, null, pageNo, pageSize);
    }
}
