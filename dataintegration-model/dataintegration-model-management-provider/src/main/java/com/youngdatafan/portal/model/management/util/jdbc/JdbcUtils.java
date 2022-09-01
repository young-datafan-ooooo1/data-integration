package com.youngdatafan.portal.model.management.util.jdbc;

import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.portal.model.management.basicmodel.dto.AllColumnDTO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.datasource.vo.ConnectionDetailVO;
import com.youngdatafan.portal.model.management.util.enums.DimensionMetricEnum;
import com.youngdatafan.portal.model.management.util.enums.TrueFalse;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/27 3:57 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class JdbcUtils {

    private static final Pattern URL_PATTERN = Pattern.compile("([\\w.]+):(\\d+)[:/]?([\\w.]+)?");

    private static final Pattern TD_URL_PATTERN = Pattern.compile("([\\w.]+)/DATABASE=(\\w+).*DBS_PORT=(\\d+)");


    private static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();


    /**
     * 获取数据源的schema
     *
     * @param datasourceDTO
     * @return
     * @throws Exception
     */
    public static final List<String> getSchema(DatasourceDTO datasourceDTO) {
        final Database database = getDatabaseConnect(datasourceDTO);

        // 模式schema
        try {
            String[] schemas = database.getSchemas();

            List<String> list = new ArrayList<>();

            for (String schema : schemas) {
                list.add(schema);
            }
            return list;
        } catch (KettleDatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            database.disconnect();
        }
    }

    /**
     * 获取schema的表
     *
     * @param datasourceDTO
     * @return
     * @throws Exception
     */
    public static List<String> getTable(DatasourceDTO datasourceDTO, String schema) {

        final Database database = getDatabaseConnect(datasourceDTO);
        try {
            final String[] tablenames = database.getTablenames(schema, true);

            List<String> tables = new ArrayList<>();

            for (String tablename : tablenames) {
                if (tablename.contains(".")) {
                    String[] table = tablename.split("\\.");
                    tables.add(table[1]);
                } else {
                    tables.add(tablename);
                }

            }
            return tables;
        } catch (KettleDatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            database.disconnect();
        }
    }

    /**
     * 获取schema的视图
     *
     * @param datasourceDTO
     * @return
     * @throws Exception
     */
    public static List<String> getViews(DatasourceDTO datasourceDTO, String schema) {

        final Database database = getDatabaseConnect(datasourceDTO);
        try {
            String[] views = database.getViews(schema, false);

            List<String> tables = Arrays.asList(views);

            return tables;
        } catch (KettleDatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            database.disconnect();
        }
    }

    /**
     * mysql获取tables
     *
     * @param datasourceDTO
     * @return
     * @throws Exception
     */
    public static List<String> getNoSchemaTable(DatasourceDTO datasourceDTO) {
        final Database database = getDatabaseConnect(datasourceDTO);

        try {
            final String[] tablenames = database.getTablenames();

            List<String> tables = Arrays.asList(tablenames);

            return tables;
        } catch (KettleDatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            database.disconnect();
        }
    }

    /**
     * 获取mysql的视图
     *
     * @param datasourceDTO
     * @return
     * @throws Exception
     */
    public static List<String> getNoSchemaView(DatasourceDTO datasourceDTO) {

        final Database database = getDatabaseConnect(datasourceDTO);

        try {

            final String[] views = database.getViews();

            List<String> tables = Arrays.asList(views);

            return tables;
        } catch (KettleDatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            database.disconnect();
        }
    }


    /**
     * 初始化数据库连接
     *
     * @param datasourceDTO
     * @return
     * @throws KettleDatabaseException
     */
    private static Database getDatabaseConnect(DatasourceDTO datasourceDTO) {

        DatasourceExplainDTO datasourceExplainDTO = explainURL(datasourceDTO.getDsUrl());

        if (StringUtils.isEmpty(datasourceExplainDTO)) {
            throw new RuntimeException();
        }
        String password = PASSWORD_ENCODER.decode(datasourceDTO.getDsPassword());

        ConnectionDetailVO connectionDetailVO = new ConnectionDetailVO();
        ConnectionDetailVO.ConnectionBean connectionBean = new ConnectionDetailVO.ConnectionBean();

        ConnectionDetailVO.ConnectionBean.AttributesBean attributesBean = new ConnectionDetailVO.ConnectionBean.AttributesBean();

        JSONArray jsonArray = new JSONObject(datasourceDTO.getDsConectorSetting()).getJSONArray("optionsParameterVo");
        List<ConnectionDetailVO.ConnectionBean.AttributesBean.AttributeBean> attributeBeanLis = new ArrayList<>();

        for (Object parameterVo: jsonArray) {
            ConnectionDetailVO.ConnectionBean.AttributesBean.AttributeBean attributeBean = new ConnectionDetailVO.ConnectionBean.AttributesBean.AttributeBean();

            JSONObject jsonObject = (JSONObject)parameterVo;
            attributeBean.setCode(jsonObject.getString("code"));
            attributeBean.setAttribute(jsonObject.getString("attribute"));

            attributeBeanLis.add(attributeBean);
        }

        attributesBean.setAttribute(attributeBeanLis);

        connectionBean.setAttributes(attributesBean);
        connectionBean.setUsername(datasourceDTO.getDsUsername());
        connectionBean.setAccess("JDBC");
        connectionBean.setType(datasourceDTO.getDsType());
        connectionBean.setServer(datasourceExplainDTO.getHost());
        connectionBean.setDatabase(datasourceExplainDTO.getDb());
        connectionBean.setPassword(password);
        connectionBean.setPort(datasourceExplainDTO.getPort());
        connectionDetailVO.setConnection(connectionBean);
        String projectFile = StringEscapeUtils.unescapeXml(JsonUtils.toString(connectionDetailVO));
        projectFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + XML.toString(new JSONLinkedObject(projectFile));
        DatabaseMeta databaseMeta = null;
        try {
            databaseMeta = new DatabaseMeta(projectFile);
        } catch (KettleXMLException e) {
            throw new RuntimeException(e);
        }

        final Database database = new Database(databaseMeta);
        try {
            database.connect();
        } catch (KettleDatabaseException e) {
            throw new RuntimeException(e);
        }
        return database;
    }

    /**
     * 获取表字段
     *
     * @param datasourceDTO
     * @param schemaTable
     * @param table
     * @return
     * @throws Exception
     */
    public static List<AllColumnDTO> getColumns(DatasourceDTO datasourceDTO, String schemaTable, String table) {

        final Database database = getDatabaseConnect(datasourceDTO);

        try {
            RowMetaInterface tableFields = database.getTableFieldsMeta(schemaTable, table);

            List<AllColumnDTO> tables = new ArrayList<>();

            final int size = tableFields.size();
            for (int i = 0; i < size; i++) {
                final ValueMetaInterface valueMeta = tableFields.getValueMeta(i);
                // 字段信息
                final String fieldName = valueMeta.getName();
                final String fieldType = ValueMetaInterface.getTypeDescription(valueMeta.getType());

                final int length = valueMeta.getLength();
                final int precision = valueMeta.getPrecision();

                final String comments = valueMeta.getComments();

                AllColumnDTO allColumnDTO = new AllColumnDTO();
                allColumnDTO.setColumnName(fieldName);
                allColumnDTO.setColumnChineseName(comments);
                allColumnDTO.setColumnLength(length);
                allColumnDTO.setColumnPrecision(precision);
                allColumnDTO.setColumnType(fieldType);
                allColumnDTO.setModelDataSort((i + 1) + "");
                allColumnDTO.setStatistics(TrueFalse.T.getDesc());
                if ("String".equals(fieldType) || "Date".equals(fieldType) || "Timestamp".equals(fieldType)) {
                    allColumnDTO.setDimensionMetric(DimensionMetricEnum.DIMENSION.code());
                } else if ("Number".equals(fieldType) || "BigNumber".equals(fieldType) || "Integer".equals(fieldType)) {
                    allColumnDTO.setDimensionMetric(DimensionMetricEnum.METRIC.code());
                } else {
                    allColumnDTO.setDimensionMetric(DimensionMetricEnum.NULL.code());
                }
                tables.add(allColumnDTO);
            }
            return tables;
        } catch (KettleDatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            database.disconnect();
        }
    }

    /**
     * 获取表的查询sql
     *
     * @param datasourceDTO
     * @param schema
     * @param table
     * @return
     * @throws Exception
     */
    public static String getTableQuerySql(DatasourceDTO datasourceDTO, String schema, String table) {

        final Database database = getDatabaseConnect(datasourceDTO);

        try {

            DatabaseMeta databaseMeta = database.getDatabaseMeta();

            RowMetaInterface tableFields = database.getTableFieldsMeta(schema, table);

            if (tableFields.isEmpty()) {
                return null;
            } else {
                StringBuilder stringBuilder = new StringBuilder("select ");

                for (int i = 0, size = tableFields.size(); i < size; i++) {
                    stringBuilder.append(databaseMeta.quoteField(tableFields.getValueMeta(i).getName()) + ",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);

                if (StringUtils.isEmpty(schema)) {
                    stringBuilder.append(" from  " + table);
                } else {
                    stringBuilder.append(" from  " + schema + "." + table);
                }

                return stringBuilder.toString();
            }
        } catch (KettleDatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            database.disconnect();
        }
    }


    /**
     * 通过sql获取获取字段
     *
     * @param datasourceDTO
     * @param
     * @return
     * @throws Exception
     */
    public static List<AllColumnDTO> getColumns(DatasourceDTO datasourceDTO, String sql) {

        DatasourceExplainDTO datasourceExplainDTO = explainURL(datasourceDTO.getDsUrl());

        if (StringUtils.isEmpty(datasourceExplainDTO)) {
            throw new RuntimeException();
        }
        List<AllColumnDTO> list = new ArrayList<>();

        String password = PASSWORD_ENCODER.decode(datasourceDTO.getDsPassword());

        DatabaseMeta databaseMeta = new DatabaseMeta(datasourceDTO.getDatasourceId()
                , datasourceDTO.getDsType(), "JDBC", datasourceExplainDTO.getHost()
                , datasourceExplainDTO.getDb(), datasourceExplainDTO.getPort(), datasourceDTO.getDsUsername(), password);

        final Database database = new Database(databaseMeta);


        try {
            database.connect();

//            PreparedStatement preparedStatement = database.prepareSQL(sql);

//            ResultSetMetaData metaData = preparedStatement.getMetaData();

            RowMetaInterface queryFields = database.getQueryFields(sql, false);

            List<ValueMetaInterface> valueMetaList = queryFields.getValueMetaList();

            for (int i = 0; i < valueMetaList.size(); i++) {

                ValueMetaInterface valueMeta = valueMetaList.get(i);

//                String columnName = metaData.getColumnName(i + 1);

                String columnTypeName = valueMeta.getTypeDesc();

                int columnDisplaySize = valueMeta.getLength();

                int precision = valueMeta.getPrecision();

                AllColumnDTO allColumnDTO = new AllColumnDTO();

                allColumnDTO.setColumnName(valueMeta.getName());
                allColumnDTO.setColumnType(columnTypeName);
                allColumnDTO.setColumnLength(columnDisplaySize);
                allColumnDTO.setColumnPrecision(precision);

                list.add(allColumnDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            database.disconnect();
        }
        return list;
    }


    public static List<AllColumnDTO> getInsertColumns(DatasourceDTO datasourceDTO, String schema, List<String> list) {


        final Database database = getDatabaseConnect(datasourceDTO);
        try {

            List<AllColumnDTO> tables = new ArrayList<>();

            for (String s : list) {

                final RowMetaInterface tableFields = database.getTableFieldsMeta(schema, s);

                final int size = tableFields.size();
                for (int i = 0; i < size; i++) {

                    final ValueMetaInterface valueMeta = tableFields.getValueMeta(i);
                    // 字段信息
                    final String fieldName = valueMeta.getName();
                    final String fieldType = ValueMetaInterface.getTypeDescription(valueMeta.getType());

                    final int length = valueMeta.getLength();
                    final int precision = valueMeta.getPrecision();

                    final String comments = valueMeta.getComments();

                    AllColumnDTO allColumnDTO = new AllColumnDTO();
                    allColumnDTO.setTableName(s);
                    allColumnDTO.setColumnName(fieldName);
                    allColumnDTO.setColumnChineseName(comments);
                    allColumnDTO.setColumnLength(length);
                    allColumnDTO.setColumnPrecision(precision);
                    allColumnDTO.setColumnType(fieldType);
                    allColumnDTO.setModelDataSort((i + 1) + "");
                    allColumnDTO.setStatistics(TrueFalse.T.getDesc());

                    if ("String".equals(fieldType) || "Date".equals(fieldType) || "Timestamp".equals(fieldType)) {
                        allColumnDTO.setDimensionMetric(DimensionMetricEnum.DIMENSION.code());
                    } else if ("Number".equals(fieldType) || "BigNumber".equals(fieldType) || "Integer".equals(fieldType)) {
                        allColumnDTO.setDimensionMetric(DimensionMetricEnum.METRIC.code());
                    } else {
                        allColumnDTO.setDimensionMetric(DimensionMetricEnum.NULL.code());
                    }

                    tables.add(allColumnDTO);
                }
            }


            return tables;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            database.disconnect();
        }
    }


    public static DatasourceExplainDTO explainURL(String dbUrl) {
        final Matcher matcher = URL_PATTERN.matcher(dbUrl);
        DatasourceExplainDTO datasourceExplainDTO = new DatasourceExplainDTO();

        if (matcher.find()) {
            datasourceExplainDTO.setHost(matcher.group(1));
            datasourceExplainDTO.setPort(matcher.group(2));
            datasourceExplainDTO.setDb(matcher.group(3));
        } else {
            final Matcher tdMatcher = TD_URL_PATTERN.matcher(dbUrl);
            if (tdMatcher.find()) {
                datasourceExplainDTO.setHost(tdMatcher.group(1));
                datasourceExplainDTO.setPort(tdMatcher.group(3));
                datasourceExplainDTO.setDb(tdMatcher.group(2));
            }
        }

        return datasourceExplainDTO;
    }


    public static String getParamByUrl(String url, String name) {
        url += "&";
        String pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            System.out.println(m.group(0));
            return m.group(0).split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }

    public static List<Map<String, Object>> querydatasourceIdSql(String querySql, Database database) throws Exception {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            database.connect();

//            connection = database.getDataSource().getConnection();

            preparedStatement = database.prepareSQL(querySql);
//            preparedStatement = connection.prepareStatement(querySql);

            resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> list = new ArrayList<>();

            ResultSetMetaData rsmd = resultSet.getMetaData();

            List<String> columnList = new ArrayList<>();

            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                String columnName = rsmd.getColumnName(i);
                columnList.add(columnName);
            }

            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0, size = columnList.size(); i < size; i++) {
                    map.put(columnList.get(i), resultSet.getString(i + 1));
                }
                list.add(map);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            database.disconnect();
        }
    }


    /**
     * 删除输出模型时并删除临时表
     *
     * @param datasourceDTO
     * @param table
     * @return
     */
    public static Boolean dropTable(DatasourceDTO datasourceDTO, String table) {

        PreparedStatement preparedStatement = null;
        final Database database = getDatabaseConnect(datasourceDTO);

        try {

            String sql = "drop table " + table;

            preparedStatement = database.prepareSQL(sql);

            boolean execute = preparedStatement.execute();


            if (execute) {
                return true;
            } else {
                return false;
            }
        } catch (KettleDatabaseException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            database.disconnect();
        }
    }

    public static void appendSql(StringBuffer stringBuffer, ModelFilterVO modelFilterVO) {
        if (!StringUtils.isEmpty(modelFilterVO.getRelation())) {
            stringBuffer.append(" " + modelFilterVO.getRelation() + " ");
        }

        String fieldType = modelFilterVO.getColumnType().toLowerCase();

        if ("string".equals(fieldType)) {

            if (!StringUtils.isEmpty(modelFilterVO.getFixedValue())) {
                stringBuffer.append(modelFilterVO.getModelColumn() + modelFilterVO.getOpertationSign() + "'" + modelFilterVO.getFixedValue() + "'");
                return;
            }

            if (!StringUtils.isEmpty(modelFilterVO.getModelColumnVal())) {
                stringBuffer.append(modelFilterVO.getModelColumn() + modelFilterVO.getOpertationSign() + modelFilterVO.getModelColumnVal());
                return;
            }
        } else if ("number".equals(fieldType) || "bigNumber".equals(fieldType) || "integer".equals(fieldType)) {
            if (!StringUtils.isEmpty(modelFilterVO.getFixedValue())) {
                stringBuffer.append(modelFilterVO.getModelColumn() + modelFilterVO.getOpertationSign() + modelFilterVO.getFixedValue());
                return;
            }

            if (!StringUtils.isEmpty(modelFilterVO.getModelColumnVal())) {
                stringBuffer.append(modelFilterVO.getModelColumn() + modelFilterVO.getOpertationSign() + modelFilterVO.getModelColumnVal());
                return;
            }
        }
    }

    /**
     * 获取数据源的视图
     *
     * @param datasourceDTO
     * @return
     * @throws Exception
     */
    public static final List<String> getViews(DatasourceDTO datasourceDTO) {
        final Database database = getDatabaseConnect(datasourceDTO);

        try {
            // 模式views
            final String[] views = database.getViews();

            List<String> list = new ArrayList<>();

            for (String view : views) {
                list.add(view);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            database.disconnect();
        }
    }

    /**
     * 获取数据源的synonyms
     *
     * @param datasourceDTO
     * @return
     * @throws Exception
     */
    public static final List<String> getSynonyms(DatasourceDTO datasourceDTO) throws Exception {
        final Database database = getDatabaseConnect(datasourceDTO);
        try {
            // 模式views
            final String[] synonyms = database.getSynonyms();

            List<String> list = new ArrayList<>();

            for (String string : synonyms) {
                list.add(string);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            database.disconnect();
        }
    }


    /**
     * 获取数据源的synonyms
     *
     * @param datasourceDTO
     * @return
     * @throws Exception
     */
    public static final String[] getProcedure(DatasourceDTO datasourceDTO) {
        DatasourceExplainDTO datasourceExplainDTO = explainURL(datasourceDTO.getDsUrl());

        if (StringUtils.isEmpty(datasourceExplainDTO)) {
            throw new RuntimeException();
        }
        String password = PASSWORD_ENCODER.decode(datasourceDTO.getDsPassword());

        DatabaseMeta databaseMeta = new DatabaseMeta(datasourceDTO.getDatasourceId()
                , datasourceDTO.getDsType(), "JDBC", datasourceExplainDTO.getHost()
                , datasourceExplainDTO.getDb(), datasourceExplainDTO.getPort(), datasourceDTO.getDsUsername(), password);
        final Database database = new Database(databaseMeta);

        try {
            database.connect();

            // 模式views
            final String[] synonyms = database.getProcedures();

            return synonyms;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            database.disconnect();
        }
    }
}
