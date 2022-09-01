package com.youngdatafan.portal.model.management.datasource.service.impl;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.model.management.datasource.dto.DataSourceListDTO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceParamsDTO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceTypeDTO;
import com.youngdatafan.portal.model.management.datasource.dto.StatementDto;
import com.youngdatafan.portal.model.management.datasource.entity.Datasource;
import com.youngdatafan.portal.model.management.datasource.mapper.DatasourceMapper;
import com.youngdatafan.portal.model.management.datasource.service.DatasourceService;
import com.youngdatafan.portal.model.management.datasource.vo.ConnectionDetailVO;
import com.youngdatafan.portal.model.management.datasource.vo.DatasourceConnectorVO;
import com.youngdatafan.portal.model.management.datasource.vo.JCDataSourceVO;
import com.youngdatafan.portal.model.management.datasource.vo.ParameterVo;
import com.youngdatafan.portal.model.management.util.jdbc.DatasourceExplainDTO;
import com.youngdatafan.portal.model.management.util.jdbc.JdbcUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.DatabaseTestResults;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 4:04 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Service
@Transactional(rollbackFor = {Throwable.class})
public class DatasourceServiceImpl implements DatasourceService {

    private Logger logger = LoggerFactory.getLogger(DatasourceServiceImpl.class);

    private final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    private final DatasourceMapper datasourceMapper;

    private static final String ENCRYPTED = "Encrypted";

    @Value("#{${datasource.types}}")
    Map<String, String> datasourceTypes;

    public DatasourceServiceImpl(DatasourceMapper datasourceMapper) {
        this.datasourceMapper = datasourceMapper;
    }

    @Override

    public boolean insert(String userId, Datasource datasource) {
        String encodePassword = PASSWORD_ENCODER.encode(datasource.getDsPassword(), true);

        datasource.setDsPassword(encodePassword);
        for (Map.Entry<String, String> entry : datasourceTypes.entrySet()
        ) {
            if (entry.getKey().equals(datasource.getDsType())) {
                datasource.setDriverClassName(entry.getValue());
                break;
            }
        }
        datasource.setCreateTime(new Date());
        datasource.setUpdateTime(new Date());
        //将数据源名称修改为数据源id
        datasource.setDatasourceId(UUID.randomUUID().toString());
        datasource.setCreateUserId(userId);

        return datasourceMapper.insert(datasource) > 0;
    }

    @Override
    public boolean delete(String userId, String datasourceName) {
        Datasource datasource = datasourceMapper.selectOneByUserIdAndDsName(userId, datasourceName);

        if (datasource != null) {
            if ("00000000".equals(userId) && "ENGINE_CLICKHOUSE".equals(datasourceName)) {
                throw new RuntimeException("此数据不能删除");
            }
            return datasourceMapper.deleteByPrimaryKey(datasourceName) > 0;
        } else {
            throw new RuntimeException("此数据无权删除");
        }

    }

    @Override
    public boolean update(Datasource datasource) {
        datasource.setUpdateTime(new Date());
        String encodePassword = null;

        if (datasource.getDsPassword().startsWith(ENCRYPTED)) {
            encodePassword = datasource.getDsPassword();
        } else {
            encodePassword = PASSWORD_ENCODER.encode(datasource.getDsPassword(), true);
        }
        datasource.setDsPassword(encodePassword);
        if (datasourceMapper.updateByPrimaryKeySelective(datasource) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PageInfo<DataSourceListDTO> selectAllByUserId(String userId, Integer curPage, Integer pageSize, String datasourceName, String datasourceType) {
        PageHelper.startPage(curPage, pageSize);

        List<DataSourceListDTO> list = datasourceMapper.selectAllByUserId(userId, datasourceName, StringUtils.isEmpty(datasourceType) ? null : datasourceType);

        PageInfo<DataSourceListDTO> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public List<DatasourceTypeDTO> getAllDatasourceTypes() {

        List<DatasourceTypeDTO> list = new ArrayList<>();

        for (Map.Entry<String, String> entry : datasourceTypes.entrySet()
        ) {
            DatasourceTypeDTO datasourceTypeDTO = new DatasourceTypeDTO();
            datasourceTypeDTO.setDatasourceTypeName(entry.getKey());
            list.add(datasourceTypeDTO);
        }

        return list;
    }

    @Override
    public DatasourceDTO getDatasourceByDatasourceName(String datasourceName) {
        DatasourceDTO datasourceDTO = datasourceMapper.getDatasourceByNameAndUserId(datasourceName);
        return datasourceDTO;
    }

    @Override
    public DatasourceDTO getDatasourceByDatasourceIdAndUserId(String userId, String datasourceId) {
        DatasourceDTO datasourceDTO = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceId);

        DatasourceExplainDTO datasourceExplainDTO = JdbcUtils.explainURL(datasourceDTO.getDsUrl());
        datasourceDTO.setPort(datasourceExplainDTO.getPort());
        datasourceDTO.setDatabse(datasourceExplainDTO.getDb());
        datasourceDTO.setDbhost(datasourceExplainDTO.getHost());
        return datasourceDTO;
    }

    @Override
    public DatasourceDTO getDatasourceByIdNotUserID(String datasourceId) {
        DatasourceDTO datasourceDTO = datasourceMapper.getDatasourceById(datasourceId);
        return datasourceDTO;
    }

    @Override
    public Connection connectorDatasource(DatasourceConnectorVO datasourceConnectorVO) {
        try {
            String driverName = null;

            for (Map.Entry<String, String> entry : datasourceTypes.entrySet()
            ) {
                if (entry.getKey().equals(datasourceConnectorVO.getDsType())) {
                    driverName = entry.getValue();
                    break;
                }
            }
//            Class.forName(datasourceConnectorVO.getDriverClassName());

            Class.forName(driverName);

            Connection conn = null;
            DriverManager.setLoginTimeout(10);
            if (datasourceConnectorVO.getDsPassword().startsWith(ENCRYPTED)) {
                conn = DriverManager.getConnection(datasourceConnectorVO.getDsUrl(), datasourceConnectorVO.getDsUsername(), PASSWORD_ENCODER.decode(datasourceConnectorVO.getDsPassword()));
            } else {
                conn = DriverManager.getConnection(datasourceConnectorVO.getDsUrl(), datasourceConnectorVO.getDsUsername(), datasourceConnectorVO.getDsPassword());
            }
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Result<List<DatasourceDTO>, Object> getDatasourceNoPageBySourcePlat(String userId, String sourcePlat) {

        List<DatasourceDTO> list = datasourceMapper.selectAll(userId, StringUtils.isEmpty(sourcePlat) ? null : sourcePlat);

        list.forEach(item -> {
            String[] split = item.getDsUrl().split("\\?");
            List<DatasourceParamsDTO> datasourceParamsDTOS = new ArrayList<>();
            annalysUrl(datasourceParamsDTOS, split);
            item.setParamsDTOS(datasourceParamsDTOS);
        });

        return Result.success(list);
    }

    @Override
    public void annalysUrl(List<DatasourceParamsDTO> datasourceParamsDTOS, String[] split) {
        if (split.length >= 2) {
            String[] split1 = split[1].split("&");

            String[] split2 = split[1].split(",");

            if (split1.length > 0) {
                for (int i = 0, j = split1.length; i < j; i++) {
                    DatasourceParamsDTO d = new DatasourceParamsDTO();

                    String[] split3 = split1[i].split("=");
                    d.setKey(split3[0]);
                    d.setValue(split3[1]);

                    datasourceParamsDTOS.add(d);
                }
            } else {
                for (int i = 0, j = split2.length; i < j; i++) {
                    DatasourceParamsDTO d = new DatasourceParamsDTO();

                    String[] split3 = split1[i].split("=");
                    d.setKey(split3[0]);
                    d.setValue(split3[1]);

                    datasourceParamsDTOS.add(d);
                }
            }
        }
    }

    @Override
    public Result<StatementDto, Object> getStatementDto(String datasourceName, String schema, String userId) {
        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceName);
        StatementDto statementDto = new StatementDto();
        try {
            List<String> tableList = null;
            if ("MYSQL".equals(datasource.getDsType())) {
                tableList = JdbcUtils.getNoSchemaTable(datasource);
            } else {
                if (StringUtils.isNotBlank(schema)) {
                    tableList = JdbcUtils.getTable(datasource, schema);
                }
            }
            statementDto.setTables(tableList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "获取table失败");
        }
        try {
            List<String> schemas = JdbcUtils.getSchema(datasource);
            statementDto.setSchemas(schemas);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "获取schemas失败");
        }
        try {
            List<String> views = JdbcUtils.getViews(datasource);
            statementDto.setViews(views);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "获取views失败");
        }
        try {
            List<String> synonyms = JdbcUtils.getSynonyms(datasource);
            statementDto.setSynonyms(synonyms);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "获取synonyms失败");
        }
        return Result.success(statementDto);
    }

    @Override
    public Result<String, Object> getStatementSql(String datasourceName, String schema, String table, String userId) {
        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceName);
        try {
            String tableQuerySql = JdbcUtils.getTableQuerySql(datasource, schema, table);
            return Result.success(tableQuerySql);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "获取columns失败");

        }

    }

    @Override
    public List<DatasourceDTO> getDatasourceNoPage(String userId, String sourcePlatform) {
        List<DatasourceDTO> list = datasourceMapper.selectAllByPlat(userId, sourcePlatform);
        return list;

    }

    @Override
    public String getUrl(JCDataSourceVO jcDataSourceVO) throws Exception {
        ConnectionDetailVO connectionDetailVO = new ConnectionDetailVO();
        ConnectionDetailVO.ConnectionBean connectionBean = new ConnectionDetailVO.ConnectionBean();

        ConnectionDetailVO.ConnectionBean.AttributesBean attributesBean = new ConnectionDetailVO.ConnectionBean.AttributesBean();

        List<ParameterVo> optionsParameterVo = jcDataSourceVO.getDsConnectorSetting().getOptionsParameterVo();
        List<ConnectionDetailVO.ConnectionBean.AttributesBean.AttributeBean> attributeBeanLis = new ArrayList<>();

        for (ParameterVo parameterVo: optionsParameterVo) {
            ConnectionDetailVO.ConnectionBean.AttributesBean.AttributeBean attributeBean = new ConnectionDetailVO.ConnectionBean.AttributesBean.AttributeBean();
            attributeBean.setCode(parameterVo.getCode());
            attributeBean.setAttribute(parameterVo.getAttribute());

            attributeBeanLis.add(attributeBean);
        }

        attributesBean.setAttribute(attributeBeanLis);

        connectionBean.setAttributes(attributesBean);
        connectionBean.setUsername(jcDataSourceVO.getDsUsername());
        connectionBean.setAccess(jcDataSourceVO.getAccess());
        connectionBean.setType(jcDataSourceVO.getDsType());
        connectionBean.setServer(jcDataSourceVO.getDsHost());
        connectionBean.setDatabase(jcDataSourceVO.getDatabase());
        connectionBean.setPassword(jcDataSourceVO.getDsPassword());
        connectionBean.setPort(jcDataSourceVO.getDsPort());
        connectionDetailVO.setConnection(connectionBean);
        String projectFile = StringEscapeUtils.unescapeXml(JsonUtils.toString(connectionDetailVO));
        projectFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + XML.toString(new JSONLinkedObject(projectFile));
        DatabaseMeta databaseMeta = new DatabaseMeta(projectFile);
        DatabaseTestResults databaseTestResults = databaseMeta.testConnectionSuccess();
        if (!databaseTestResults.isSuccess()) {
            logger.error(databaseTestResults.getMessage());
            throw new RuntimeException("测试连接不通过");

        }
        return databaseMeta.getURL();
    }


}
