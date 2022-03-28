package com.youngdatafan.portal.model.management.basicmodel.service.impl;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.portal.model.management.basicmodel.dto.*;
import com.youngdatafan.portal.model.management.basicmodel.entity.BasiceModel;
import com.youngdatafan.portal.model.management.basicmodel.entity.DpPortalBasicModelQuerysql;
import com.youngdatafan.portal.model.management.basicmodel.mapper.BasiceModelMapper;
import com.youngdatafan.portal.model.management.basicmodel.mapper.BasiceModelMetadataMapper;
import com.youngdatafan.portal.model.management.basicmodel.mapper.DpPortalBasicModelQuerysqlDao;
import com.youngdatafan.portal.model.management.basicmodel.mapper.DpPortalModelFilterMapper;
import com.youngdatafan.portal.model.management.basicmodel.service.BasiceModelService;
import com.youngdatafan.portal.model.management.basicmodel.vo.AddBasicModelVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.BatchInserModelVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.TableNameAndModelNameVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.UpdateBasicModelVO;
import com.youngdatafan.portal.model.management.businessmodel.service.BusinessModelService;
import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import com.youngdatafan.portal.model.management.common.entity.ModelMetaDataDTO;
import com.youngdatafan.portal.model.management.common.enums.GroupTypeEnum;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.datasource.entity.Datasource;
import com.youngdatafan.portal.model.management.datasource.mapper.DatasourceMapper;
import com.youngdatafan.portal.model.management.outinterfacemodel.service.OutinterfaceModelService;
import com.youngdatafan.portal.model.management.outmodel.service.OutModelService;
import com.youngdatafan.portal.model.management.util.enums.TrueFalse;
import com.youngdatafan.portal.model.management.util.factory.sql.SqlJoin;
import com.youngdatafan.portal.model.management.util.jdbc.JdbcUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/18 5:04 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class BasiceModelserviceImpl implements BasiceModelService {

    Logger logger = LoggerFactory.getLogger(BasiceModelserviceImpl.class);

    private static final Pattern URL_PATTERN = Pattern.compile("([\\w.]+):(\\d+)[:/]?([\\w.]+)?");

    private static final Pattern TD_URL_PATTERN = Pattern.compile("([\\w.]+)/DATABASE=(\\w+).*DBS_PORT=(\\d+)");

    private static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    @Resource
    BasiceModelMapper basiceModelMapper;

    @Resource
    com.youngdatafan.portal.model.management.util.factory.SqlFactory SqlFactory;

    @Resource
    BasiceModelMetadataMapper basiceModelMetadataMapper;

    @Resource
    DatasourceMapper datasourceMapper;

    @Resource
    BusinessModelService businessModelService;

    @Resource
    OutinterfaceModelService outinterfaceModelService;

    @Resource
    OutModelService outModelService;

    @Resource
    DpPortalModelFilterMapper dpPortalModelFilterMapper;

    @Resource
    DpPortalBasicModelQuerysqlDao dpPortalBasicModelQuerysqlDao;

    /**
     * 默认数据源类型
     */
    private final String EXECUTI_ENGINE = "EXECUTIENGINE";

    @Value("#{${datasource.types}}")
    Map<String, String> datasourceTypes;


    @Value("#{${mysql.data.type}}")
    List<String> mysqlDataType;

    @Override
    public Result delete(String userId, String modelName) {
        if (basiceModelMapper.deleteByPrimaryKey(modelName, userId) > 0) {

            if (basiceModelMapper.deleteModelGroup(modelName) < 0) {
                throw new RuntimeException();
            }

            if (dpPortalModelFilterMapper.deleteById(modelName) < 0) {
                throw new RuntimeException();
            }

            if (basiceModelMetadataMapper.deleteByModelName(modelName) < 0) {
                throw new RuntimeException();
            }

            if (dpPortalBasicModelQuerysqlDao.deleteById(modelName) < 0) {
                throw new RuntimeException();
            }
            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);
        }
    }

    @Override
    public Result insert(String userId, AddBasicModelVO addBasicModelVO) {
        BasiceModel basiceModel = new BasiceModel();
        basiceModel.setCreateTime(new Date());
        basiceModel.setCreateUserId(userId);
        basiceModel.setDescription(addBasicModelVO.getDescription());
        basiceModel.setDsName(addBasicModelVO.getDatasourceName());

        basiceModel.setModelSort(addBasicModelVO.getSortNum());
        basiceModel.setModelName(UUID.randomUUID().toString());
        basiceModel.setcName(addBasicModelVO.getCName());
        basiceModel.setTableName(addBasicModelVO.getTableName());
        basiceModel.setCreateUserId(userId);
        basiceModel.setUpdateTime(new Date());

        basiceModel.setTableSchema(addBasicModelVO.getSchemaName());

        basiceModel.setEnabled(TrueFalse.T.getDesc().equals(addBasicModelVO.getEnabled()) ? TrueFalse.T.code() : TrueFalse.F.code());

        String modelType = addBasicModelVO.getModelType();

        modelType = getGroupType(modelType);

        basiceModel.setModelType(modelType);

        for (BasicModelMetaDataVO n : addBasicModelVO.getBasicModelMetaDataVOS()
        ) {
            n.setModelName(basiceModel.getModelName());
        }

        if (basiceModelMapper.insert(basiceModel) > 0) {

            List<ModelFilterVO> modelFilter = addBasicModelVO.getModelFilter();

            if (!StringUtils.isEmpty(modelFilter)) {

                for (ModelFilterVO modelFilterVO : modelFilter) {
                    modelFilterVO.setModelId(basiceModel.getModelName());
                }

                if (modelFilter.size() > 0) {
                    int insertFlag = dpPortalModelFilterMapper.insertModelFilter(modelFilter);

                    if (insertFlag < 0) {
                        throw new RuntimeException();
                    }
                }
            }


            if (basiceModelMapper.insertModelGroup(basiceModel.getModelName(), addBasicModelVO.getGroup()) < 0) {
                throw new RuntimeException();
            }
            List<BasicModelMetaDataVO> list = addBasicModelVO.getBasicModelMetaDataVOS();
            for (BasicModelMetaDataVO ba : list) {
                ba.setStatistics(TrueFalse.T.getDesc().equals(ba.getStatistics()) ? TrueFalse.T.code() : TrueFalse.F.code());
            }

            List<BasicModelMetaDataVO> basicModelMetaDataVOS = addBasicModelVO.getBasicModelMetaDataVOS();
            if (basicModelMetaDataVOS.size() == 0) {
                return Result.success(true);
            }
            if (basiceModelMetadataMapper.insertBasicModelMetaData(basicModelMetaDataVOS) < 0) {
                throw new RuntimeException();
            }

            //获取数据库类型，根据不同数据库拼接不同sql
            StringBuffer stringBufferSql = appendSqlFIlter(basiceModel, modelFilter, basicModelMetaDataVOS);

            DpPortalBasicModelQuerysql dpPortalBasicModelQuerysql = new DpPortalBasicModelQuerysql(basiceModel.getModelName(), stringBufferSql.toString());

            if (dpPortalBasicModelQuerysqlDao.insert(dpPortalBasicModelQuerysql) < 0) {
                throw new RuntimeException();
            }
            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);
        }
    }

    private StringBuffer appendSqlFIlter(BasiceModel basiceModel, List<ModelFilterVO> modelFilter, List<BasicModelMetaDataVO> basicModelMetaDataVOS) {
        Datasource datasource = datasourceMapper.selectByPrimaryKey(basiceModel.getDsName());

        SqlJoin datasourceEntity = SqlFactory.getDatasourceEntity(datasource.getDsType());
        String tableName = basiceModel.getTableSchema() + "." + basiceModel.getTableName();
        StringBuffer stringBuffer = datasourceEntity.getStringBuffer(basicModelMetaDataVOS, modelFilter, tableName, false);

        return stringBuffer;

    }

    @Override
    public Result update(String userId, UpdateBasicModelVO updateBasicModelVO) {
        BasiceModel basiceModel = new BasiceModel();
        basiceModel.setDescription(updateBasicModelVO.getDescription());
        basiceModel.setDsName(updateBasicModelVO.getDatasourceName());
        basiceModel.setModelSort(updateBasicModelVO.getSortNum());
        basiceModel.setcName(updateBasicModelVO.getCName());
        basiceModel.setTableName(updateBasicModelVO.getTableName());
        basiceModel.setUpdateTime(new Date());
        basiceModel.setModelName(updateBasicModelVO.getName());

        basiceModel.setTableSchema(updateBasicModelVO.getSchemaName());

        basiceModel.setEnabled(TrueFalse.T.getDesc().equals(updateBasicModelVO.getEnabled()) ? TrueFalse.T.code() : TrueFalse.F.code());

        if (basiceModelMapper.updateByPrimaryKeySelective(basiceModel) >= 0) {

            if (basiceModelMapper.deleteModelGroup(updateBasicModelVO.getName()) < 0) {
                throw new RuntimeException();
            }

            //group是选择的分组，groupId是模型自带的id
            if (basiceModelMapper.insertModelGroup(updateBasicModelVO.getName(), updateBasicModelVO.getGroup()) < 0) {
                throw new RuntimeException();
            }

            if (basiceModelMetadataMapper.deleteByModelName(updateBasicModelVO.getName()) < 0) {
                throw new RuntimeException();
            }


            if (dpPortalModelFilterMapper.deleteById(updateBasicModelVO.getName()) < 0) {
                throw new RuntimeException();
            }


            List<ModelFilterVO> modelFilter = updateBasicModelVO.getModelFilter();

            if (modelFilter != null && modelFilter.size() > 0) {

                for (ModelFilterVO modelFilterVO : modelFilter) {
                    modelFilterVO.setModelId(updateBasicModelVO.getName());
                }

                if (dpPortalModelFilterMapper.insertModelFilter(modelFilter) < 0) {
                    throw new RuntimeException();
                }
            }


            List<BasicModelMetaDataVO> list = updateBasicModelVO.getBasicModelMetaDataVOS();

            if (list.isEmpty()) {
                return Result.success(true);
            }

            for (BasicModelMetaDataVO n : list
            ) {
                n.setModelName(updateBasicModelVO.getName());
                n.setStatistics(TrueFalse.T.getDesc().equals(n.getStatistics()) ? TrueFalse.T.code() : TrueFalse.F.code());
            }

            if (basiceModelMetadataMapper.insertBasicModelMetaData(list) < 0) {
                throw new RuntimeException();
            }

            StringBuffer stringBufferSql = appendSqlFIlter(basiceModel, modelFilter, list);
            DpPortalBasicModelQuerysql dpPortalBasicModelQuerysql = new DpPortalBasicModelQuerysql(basiceModel.getModelName(), stringBufferSql.toString());

            if (dpPortalBasicModelQuerysqlDao.replaceInto(dpPortalBasicModelQuerysql) < 0) {
                throw new RuntimeException();
            }

            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);
        }
    }

    @Override
    public PageInfo<BasicModelDTO> selectAll(String userId, String modelName, String groupName, String datasource, Integer curPage, Integer pageSize, String modelType) {
        PageHelper.startPage(curPage, pageSize);

        modelType = getGroupType(modelType);
        List<BasicModelDTO> list = basiceModelMapper.selectBasicModelByUserId(userId, StringUtils.isEmpty(modelName) ? null : "%" + modelName + "%", groupName, datasource, modelType);

        List<String> list1 = new ArrayList<>();

        for (BasicModelDTO o : list
        ) {
            list1.add(o.getName());
        }

        if (!list1.isEmpty()) {

            List<SelectModelNameAndColumnCountDTO> list2 = basiceModelMetadataMapper.selectBusinessModelNameAndColumnCount(list1);
            for (BasicModelDTO o : list
            ) {
                if (!StringUtils.isEmpty(list2)) {
                    for (SelectModelNameAndColumnCountDTO s : list2
                    ) {
                        if (o.getName().equals(s.getModelName())) {
                            Integer i = s.getBasicModelMetaDataDTOS().size();
                            o.setColumnCount(i.toString());
                            o.setBasicModelMetaDataDTOS(s.getBasicModelMetaDataDTOS());
                            continue;
                        }
                    }
                } else {
                    o.setColumnCount("0");
                }
                o.setEnabled(TrueFalse.T.code().equals(o.getEnabled()) ? TrueFalse.T.getDesc() : TrueFalse.F.getDesc());
            }

            List<ModelNameAndModelFilterDTO> modelNameAndModelFilterDTOS = dpPortalModelFilterMapper.selectMdelFilters(list1);

            for (BasicModelDTO o : list
            ) {
                for (ModelNameAndModelFilterDTO s : modelNameAndModelFilterDTOS
                ) {
                    if (o.getName().equals(s.getModelName())) {
                        o.setModelFilterVOS(s.getModelFilterVOS());
                        continue;
                    }
                }
            }

        } else {
            for (BasicModelDTO o : list
            ) {
                o.setColumnCount("0");
                o.setEnabled(TrueFalse.T.code().equals(o.getEnabled()) ? TrueFalse.T.getDesc() : TrueFalse.F.getDesc());
            }
        }

        PageInfo<BasicModelDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<BasicModelDatasourceDTO> getAllDatasources(String userId) {
        return basiceModelMapper.getAllDatasources(userId, EXECUTI_ENGINE);
    }

    @Override
    public Result getAllSchema(String datasourceName, String userId) {

        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceName);
        try {
            List<String> list = JdbcUtils.getSchema(datasource);
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "获取失败");
        }
    }

    @Override
    public Result getAllTables(String datasourceName, String userId, String schema) {
        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceName);
        try {
            List<String> list = null;
            if ("MYSQL".equals(datasource.getDsType())) {
                list = JdbcUtils.getNoSchemaTable(datasource);
            } else {
                list = JdbcUtils.getTable(datasource, schema);
            }
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "获取失败");
        }
    }

    @Override
    public Result<TablesAndViewDTO, Object> getAllTablesAndViews(String userId, String datasourceName, String schema) {
        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceName);
        try {
            List<String> tables = null;
            List<String> views = null;

            if ("MYSQL".equals(datasource.getDsType())) {
                tables = JdbcUtils.getNoSchemaTable(datasource);
                views = JdbcUtils.getNoSchemaView(datasource);
            } else {
                tables = JdbcUtils.getTable(datasource, schema);
                views = JdbcUtils.getViews(datasource, schema);
            }
            TablesAndViewDTO tablesAndViewDTO = new TablesAndViewDTO(tables, views);

            return Result.success(tablesAndViewDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, e.getMessage());
        }
    }

    @Override
    public Result<List<AllColumnDTO>, Object> getAllColumns(String userId, String tables, String datasourceName, String schema) {
        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceName);
        try {
            List<AllColumnDTO> list = JdbcUtils.getColumns(datasource, schema, tables);
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "获取失败");
        }
    }

    @Override
    public Result<String, Object> getTableQuerySql(String userId, String datasourceName, String table, String schema) {
        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceName);

        try {
            String querySql = JdbcUtils.getTableQuerySql(datasource, schema, table);

            return Result.success(querySql);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "获取失败");
        }
    }

    public Connection connectorDatasource(DatasourceDTO datasource) {
        try {
            String driverName = null;

            Class.forName(datasource.getDriverClassName());
            Connection conn = DriverManager.getConnection(datasource.getDsUrl(), datasource.getDsUsername(), datasource.getDsPassword());
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet jdbcExcute(Connection connection, String sql_query) {

        try (PreparedStatement pst = connection.prepareStatement(sql_query)) {

            ResultSet resultSet = pst.executeQuery();

            return resultSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<GroupDTO> getAllGroup(String userId, String groupType) {

        groupType = getGroupType(groupType);

        List<GroupDTO> list = basiceModelMapper.getAllGroup(userId, groupType);
        return list;
    }

    private String getGroupType(String groupType) {
        if (groupType.equals(GroupTypeEnum.JBMX.code())) {
            groupType = GroupTypeEnum.JBMX.code();
        } else if (groupType.equals(GroupTypeEnum.ZDYMX.code())) {
            groupType = GroupTypeEnum.ZDYMX.code();
        } else {
            groupType = GroupTypeEnum.SCLMX.code();
        }
        return groupType;
    }

    @Override
    public List<BasicModelAndMetaDataDTO> getBasicModelAndMetaData(String userId, List<String> list) {
        return basiceModelMapper.selectBasicModelAndMetaData(list, userId);
    }

    @Override
    public Result batchInsertModel(String userId, BatchInserModelVO batchInserModelVO) {
        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, batchInserModelVO.getDataSourceName());

        List<TableNameAndModelNameVO> list = batchInserModelVO.getTableNameAndModelNameVOS();

        List<String> list1 = new ArrayList<>();

        for (TableNameAndModelNameVO s : list
        ) {
            list1.add(s.getTableName());
        }

        if (list.size() == 0) {
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "所选表为空");
        }

        List<TableNameAndModelNameVO> tableNameAndModelNameVOS = batchInserModelVO.getTableNameAndModelNameVOS();

        List<String> modelNames = new ArrayList<>();

        for (TableNameAndModelNameVO tableNameAndModelNameVO : tableNameAndModelNameVOS) {
            modelNames.add(tableNameAndModelNameVO.getModelName());
        }

        int counts = basiceModelMapper.queryModelByModelNameAndType(batchInserModelVO.getGroup(), batchInserModelVO.getModelType(), modelNames);
        if (counts > 0) {
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "有重复模型名称存在");
        }

        try {
            List<AllColumnDTO> allColumnDTOS = JdbcUtils.getInsertColumns(datasource,batchInserModelVO.getSchemaName(), list1);

            List<BasicModelAndColumnsDTO> basicModelAndColumnsDTOS = new ArrayList<>();
            for (TableNameAndModelNameVO tableNameAndModelNameVO : list
            ) {
                BasicModelAndColumnsDTO basicModelAndColumnsDTO = new BasicModelAndColumnsDTO();

                BasicModelCopyDTO basiceModel = new BasicModelCopyDTO();

                basiceModel.setModelName(UUID.randomUUID().toString());

                basiceModel.setCName(tableNameAndModelNameVO.getModelName());

                basiceModel.setDsName(batchInserModelVO.getDataSourceName());

                basiceModel.setTableSchema(batchInserModelVO.getSchemaName());

                basiceModel.setCreateUserId(userId);

                basiceModel.setCreateTime(new Date());

                basiceModel.setModelSort("99");

                basiceModel.setUpdateTime(new Date());

                String type = "";
                if (GroupTypeEnum.JBMX.code().equals(batchInserModelVO.getModelType())) {
                    type = GroupTypeEnum.JBMX.code();
                } else if (GroupTypeEnum.ZDYMX.code().equals(batchInserModelVO.getModelType())) {
                    type = GroupTypeEnum.ZDYMX.code();

                }

                basiceModel.setModelType(type);

                basiceModel.setTableName(tableNameAndModelNameVO.getTableName());

                basiceModel.setEnabled(TrueFalse.T.code());

                basiceModel.setModelName(UUID.randomUUID().toString());

                basicModelAndColumnsDTO.setBasicModelCopyDTO(basiceModel);


                List<AllColumnDTO> columnDTOS = new ArrayList<>();

                for (AllColumnDTO allColumnDTO : allColumnDTOS) {

                    if (tableNameAndModelNameVO.getTableName().equals(allColumnDTO.getTableName())) {

                        allColumnDTO.setModelName(basiceModel.getModelName());

                        columnDTOS.add(allColumnDTO);
                    }

                }

                basicModelAndColumnsDTO.setList(columnDTOS);

                basicModelAndColumnsDTOS.add(basicModelAndColumnsDTO);
            }

            for (BasicModelAndColumnsDTO basicModelAndColumnsDTO : basicModelAndColumnsDTOS) {

                BasiceModel basiceModel = new BasiceModel();
                BasicModelCopyDTO basicModelCopyDTO = basicModelAndColumnsDTO.getBasicModelCopyDTO();

                BeanUtils.copyProperties(basicModelCopyDTO, basiceModel);

                System.out.println(basiceModel.getModelType());
                if (basiceModelMapper.insert(basiceModel) > 0) {

                    if (basiceModelMapper.insertModelGroup(basiceModel.getModelName(), batchInserModelVO.getGroup()) < 0) {
                        throw new RuntimeException();
                    }

                    List<AllColumnDTO> allColumnDTOs = basicModelAndColumnsDTO.getList();

                    if (basiceModelMetadataMapper.insertAllColunms(allColumnDTOs) < 0) {
                        throw new RuntimeException();
                    }

                    List<BasicModelMetaDataVO> basicModelMetaDataVOS = new ArrayList<>();
                    basicModelMetaDataVOS.addAll(allColumnDTOS);

                    StringBuffer stringBufferSql = appendSqlFIlter(basiceModel, new ArrayList<>(), basicModelMetaDataVOS);

                    DpPortalBasicModelQuerysql dpPortalBasicModelQuerysql = new DpPortalBasicModelQuerysql(basiceModel.getModelName(), stringBufferSql.toString());

                    if (dpPortalBasicModelQuerysqlDao.insert(dpPortalBasicModelQuerysql) < 0) {
                        throw new RuntimeException();
                    }
                }
            }

            return Result.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "获取表字段失败");
        }
    }


    @Override
    public Result<Boolean, Object> queryBasiceModelExists(String userId, String basicModelName, String basicModelGroup) {
        Integer flag = basiceModelMapper.queryBasiceModelExists(userId, basicModelName, basicModelGroup);
        if (flag > 0) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }

    @Override
    public Result<CustomModelDTO, Object> selectCustomModel(String userId, String modelName) {
        System.out.println(modelName);

        CustomModelDTO customModelDTO = basiceModelMapper.selectCustomModel(userId, modelName, GroupTypeEnum.ZDYMX.code());

        System.out.println(customModelDTO.getBasicModelMetaDataDTOS());
        appendQuerySql(customModelDTO);

        return Result.success(customModelDTO);
    }

    private void appendQuerySql(CustomModelDTO customModelDTO) {
        List<BasicModelMetaDataDTO> basicModelMetaDataDTOS = customModelDTO.getBasicModelMetaDataDTOS();


        if (basicModelMetaDataDTOS.size() > 0) {
            StringBuffer queryColumn = new StringBuffer();
            for (BasicModelMetaDataDTO basicModelDatasourceDTO : basicModelMetaDataDTOS) {
                queryColumn.append(basicModelDatasourceDTO.getColumnName() + ",");
            }

            String columns = queryColumn.substring(0, queryColumn.length() - 1);

            String querySql = "select " + columns + " from " + customModelDTO.getTableName();

            customModelDTO.setQuerySql(querySql);
        }
    }


    @Override
    public ModelDTO selectModelsByModelName(String userId, String modelName, String modelType) {
        ModelDTO modelDTO = null;

        if (GroupTypeEnum.YWMX.code().equals(modelType)) {

            modelDTO = businessModelService.selectModelAndMetaData(userId, modelName);

            if (!StringUtils.isEmpty(modelDTO.getBusinessModelSql())) {
                String querySql = modelDTO.getBusinessModelSql().replace("?", modelDTO.getBasicModelSql());

                modelDTO.setQuerySql(querySql);
            }


        } else if (GroupTypeEnum.SCJK.code().equals(modelType)) {

            modelDTO = outinterfaceModelService.selectModelAndMetaData(userId, modelName);

            if (!StringUtils.isEmpty(modelDTO.getBusinessModelSql())) {
                String querySql = modelDTO.getBusinessModelSql().replace("?", modelDTO.getBasicModelSql());

                modelDTO.setQuerySql(querySql);
            }


        } else if (GroupTypeEnum.SCLMX.code().equals(modelType)) {

            modelDTO = outModelService.selectModel(userId, modelName);

        } else if (GroupTypeEnum.ZDYMX.code().equals(modelType)) {

            modelDTO = basiceModelMapper.selectModel(userId, modelName, modelType.toUpperCase());

            List<ModelMetaDataDTO> modelMetaDataDTOS = modelDTO.getModelMetaDataDTOS();

            if (!modelMetaDataDTOS.isEmpty()) {

                StringBuffer queryColumn = new StringBuffer();
                for (ModelMetaDataDTO modelMetaDataDTO : modelMetaDataDTOS) {
                    queryColumn.append(modelMetaDataDTO.getColumnName() + ",");
                }
                String columns = queryColumn.substring(0, queryColumn.length() - 1);

                String querySql = "select " + columns + " from " + modelDTO.getTableName();

                modelDTO.setQuerySql(querySql);
            }

        }
        return modelDTO;
    }

    @Override
    public Result<List<Map<String, Object>>, Object> testPreview(String userId, String datasourceId,
                                                                 List<BasicModelMetaDataVO> basicModelMetaDataVOS,
                                                                 List<ModelFilterVO> modelFilterVOS,
                                                                 String tableName) {

        Datasource datasource = datasourceMapper.selectByPrimaryKey(datasourceId);

        final Matcher matcher = URL_PATTERN.matcher(datasource.getDsUrl());

        final Matcher tdMatcher = TD_URL_PATTERN.matcher(datasource.getDsUrl());


        Database database;
        if (matcher.find()) {
            // 初始化kettle环境，只需要初始化一次
            DatabaseMeta databaseMeta = new DatabaseMeta(UUID.randomUUID().toString()
                , datasource.getDsType().toUpperCase(), "JDBC", matcher.group(1)
                , matcher.group(3), matcher.group(2), datasource.getDsUsername(), PASSWORD_ENCODER.decode(datasource.getDsPassword()));

            database = new Database(databaseMeta);
        } else if (tdMatcher.find()) {
            DatabaseMeta databaseMeta = new DatabaseMeta(UUID.randomUUID().toString()
                , datasource.getDsType().toUpperCase(), "JDBC", matcher.group(1)
                , matcher.group(2), matcher.group(3), datasource.getDsUsername(), PASSWORD_ENCODER.decode(datasource.getDsPassword()));
            database = new Database(databaseMeta);
        } else {
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "数据源异常");
        }

        SqlJoin datasourceEntity = SqlFactory.getDatasourceEntity(datasource.getDsType());

        StringBuffer stringBuffer = datasourceEntity.getStringBuffer(basicModelMetaDataVOS, modelFilterVOS, tableName, true);

//        StringBuffer stringBuffer = getStringBuffer(basicModelMetaDataVOS, modelFilterVOS, tableName);

        try {
            List<Map<String, Object>> list = JdbcUtils.querydatasourceIdSql(stringBuffer.toString(), database);
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "筛选条件异常");
        }
    }

    @Override
    public List<AllColumnDTO> getAllColumnsBySql(String userId, String datasourceName, String sql) {
        DatasourceDTO datasource = datasourceMapper.getDatasourceByIdAndUserId(userId, datasourceName);
        List<AllColumnDTO> columns = JdbcUtils.getColumns(datasource, sql);
        return columns;
    }



}
