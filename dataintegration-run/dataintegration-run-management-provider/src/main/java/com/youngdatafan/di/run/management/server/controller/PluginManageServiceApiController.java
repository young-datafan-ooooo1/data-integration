package com.youngdatafan.di.run.management.server.controller;

import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.exception.ValidationException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.dataintegration.core.util.sql.DataSourceWrap;
import com.youngdatafan.di.run.management.datasource.PluginRunDataSourceRepository;
import com.youngdatafan.di.run.management.server.api.PluginManageServiceApi;
import com.youngdatafan.di.run.management.server.dto.FieldMaxMinDTO;
import com.youngdatafan.di.run.management.server.dto.FieldSumDTO;
import com.youngdatafan.di.run.management.server.dto.FunctionCategoryDTO;
import com.youngdatafan.di.run.management.server.dto.FunctionDTO;
import com.youngdatafan.di.run.management.server.dto.PluginCategoryDTO;
import com.youngdatafan.di.run.management.server.dto.PluginInfoDTO;
import com.youngdatafan.di.run.management.server.dto.PreStepSqlInfoDTO;
import com.youngdatafan.di.run.management.server.dto.ProjectExecutorDTO;
import com.youngdatafan.di.run.management.server.entity.DpPortalPluginInfo;
import com.youngdatafan.di.run.management.server.feign.BusinessModelServiceApiClient;
import com.youngdatafan.di.run.management.server.function.FunctionDescription;
import com.youngdatafan.di.run.management.server.function.FunctionLib;
import com.youngdatafan.di.run.management.server.service.DpPortalPluginInfoService;
import com.youngdatafan.di.run.management.server.service.ProjectExecutorService;
import com.youngdatafan.di.run.management.server.util.TreeUtil;
import com.youngdatafan.di.run.management.server.vo.CreateTableVO;
import com.youngdatafan.di.run.management.server.vo.ExecuteDDLVO;
import com.youngdatafan.di.run.management.server.vo.ExecuteFieldSumVO;
import com.youngdatafan.di.run.management.server.vo.FieldsInfoVO;
import com.youngdatafan.di.run.management.server.vo.ProjectExecutorParam;
import com.youngdatafan.di.run.management.server.websocket.ProjectExecuteCallback;
import com.youngdatafan.portal.model.management.businessmodel.dto.BusinessModelAndGroupListDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BusinessModelDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.GroupDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.ModelTypeAndGroupListDTO;
import com.youngdatafan.portal.model.management.common.enums.GroupTypeEnum;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBigNumber;
import org.pentaho.di.core.row.value.ValueMetaBinary;
import org.pentaho.di.core.row.value.ValueMetaBoolean;
import org.pentaho.di.core.row.value.ValueMetaDate;
import org.pentaho.di.core.row.value.ValueMetaInteger;
import org.pentaho.di.core.row.value.ValueMetaInternetAddress;
import org.pentaho.di.core.row.value.ValueMetaNone;
import org.pentaho.di.core.row.value.ValueMetaNumber;
import org.pentaho.di.core.row.value.ValueMetaSerializable;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.row.value.ValueMetaTimestamp;
import org.pentaho.di.core.util.StringUtil;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/2/18 4:13 下午
 */
@RestController
@RequestMapping("/pluginManage")
@Slf4j
public class PluginManageServiceApiController implements PluginManageServiceApi {

    @Autowired
    private ProjectExecutorService projectExecutorService;

    @Autowired
    private DpPortalPluginInfoService portalPluginInfoService;

    @Autowired
    private PluginRunDataSourceRepository pluginRunDataSourceRepository;

    @Autowired
    private BusinessModelServiceApiClient businessModelServiceApiClient;

    public static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    @Override
    public Result init() {
        PluginRegistry pluginRegistry = PluginRegistry.getInstance();
        List<PluginInterface> pluginInterfaces = pluginRegistry.getPlugins(StepPluginType.class);
        for (PluginInterface pluginInterface : pluginInterfaces) {
            DpPortalPluginInfo dpPortalPluginInfo = new DpPortalPluginInfo();
            dpPortalPluginInfo.setPluginId(pluginInterface.getIds()[0]);
            dpPortalPluginInfo.setPluginName(pluginInterface.getName());
            dpPortalPluginInfo.setPluginDescribe(pluginInterface.getDescription());
            dpPortalPluginInfo.setPluginCategory(pluginInterface.getCategory());
            dpPortalPluginInfo.setPluginType("basic");
            portalPluginInfoService.deleteByPrimaryKey(dpPortalPluginInfo.getPluginId());
            portalPluginInfoService.insert(dpPortalPluginInfo);

        }
        return null;
    }

    @Override
    public Result<List<PluginInfoDTO<PluginInfoDTO>>, Object> getAllBasicPluginInfoByUserId(String userId) {


        List<DpPortalPluginInfo> dpPortalPluginInfos = portalPluginInfoService.getAllBasicPluginInfo(userId);

        List<PluginInfoDTO<PluginInfoDTO>> pluginInfoDTOS = new ArrayList<>();
        dpPortalPluginInfos.forEach(o -> {
            PluginInfoDTO pluginInfoDTO = new PluginInfoDTO();
            BeanUtils.copyProperties(o, pluginInfoDTO);
            pluginInfoDTO.setId(o.getPluginId());
            pluginInfoDTO.setLabel(o.getPluginName());
            pluginInfoDTO.setNodeId(o.getPluginId());
            pluginInfoDTO.setPluginImage(o.getPluginImage());
            pluginInfoDTOS.add(pluginInfoDTO);
        });

        PluginInfoDTO<PluginInfoDTO> pluginInfoDTOPluginInfoDTO = TreeUtil.build(pluginInfoDTOS);


        return Result.success(pluginInfoDTOPluginInfoDTO.getChildren());
    }


    @Override
    public Result<List<PluginCategoryDTO>, Object> getAllBusinessPluginInfoByUserId(String userId, String pluginIds) {
        if (pluginIds == null || pluginIds.equals("")) {
            return Result.success(new ArrayList<>());
        }
        //过滤出非业务模型插件
        List<DpPortalPluginInfo> dpPortalPluginInfos = portalPluginInfoService.getAllBusinessPluginInfo(Arrays.asList(pluginIds.split(","))).stream()
            .filter(o -> !(o.getPluginId().equals("BasicModelInput")) && !(o.getPluginId().equals("CustomizeModelInput"))).sorted(Comparator.comparingInt(o -> o.getCategoryOrder()))
            .collect(Collectors.toList());

        List<PluginInfoDTO> pluginInfoDTOS = new ArrayList<>();
        dpPortalPluginInfos.forEach(o -> {
            PluginInfoDTO pluginInfoDTO = new PluginInfoDTO();
            BeanUtils.copyProperties(o, pluginInfoDTO);
            pluginInfoDTOS.add(pluginInfoDTO);
        });
        List<PluginCategoryDTO> pluginCategoryDTOS = new ArrayList<>();

        Map<Integer, List<PluginInfoDTO>> listMap = pluginInfoDTOS.stream().collect(Collectors.groupingBy(o -> o.getCategoryOrder()));
        for (Map.Entry<Integer, List<PluginInfoDTO>> entry : listMap.entrySet()) {
            PluginCategoryDTO pluginCategoryDTO = new PluginCategoryDTO();
            pluginCategoryDTO.setCategory(entry.getValue().get(0).getPluginCategory());
            List<PluginInfoDTO> pluginInfoDTOS1 = entry.getValue().stream().sorted(Comparator.comparingInt(o -> o.getPluginOrder())).collect(Collectors.toList());
            pluginCategoryDTO.setPluginInfoDTOS(pluginInfoDTOS1);
            pluginCategoryDTOS.add(pluginCategoryDTO);
        }
        return Result.success(pluginCategoryDTOS);
    }

    @Override
    public Result<List<PluginCategoryDTO>, Object> getAllBusinessModelPluginInfoByUserId(String userId) {
        List<PluginCategoryDTO> pluginCategoryDTOS = new ArrayList<>();
        Result<List<ModelTypeAndGroupListDTO>, Object> result = businessModelServiceApiClient.selectModelAndGroups(userId);

        List<ModelTypeAndGroupListDTO> modelTypeAndGroupListDTOS;
        if (result.getCode().equals("10000")) {
            modelTypeAndGroupListDTOS = result.getContent();
        } else {
            log.error("获取模型信息失败，code={},message={}", result.getCode(), result.getMsg());
            throw new ValidationException(StatusCode.CODE_10010.getCode(), "获取模型信息失败");

        }
        //处理业务模型插件
        Optional<DpPortalPluginInfo> option =
            portalPluginInfoService.getAllBusinessModelPluginInfo().stream().filter(o -> o.getPluginId().equals("BasicModelInput") || o.getPluginId().equals("CustomizeModelInput")).findFirst();
        if (option.isPresent()) {
            DpPortalPluginInfo dpPortalPluginInfo = option.get();
            PluginCategoryDTO pluginCategoryBusiness = new PluginCategoryDTO();
            pluginCategoryBusiness.setCategory(dpPortalPluginInfo.getPluginCategory());
            if (modelTypeAndGroupListDTOS != null && modelTypeAndGroupListDTOS.size() > 0) {

                List<PluginCategoryDTO> pluginCategoryDTOS1 = new ArrayList<>();

                for (ModelTypeAndGroupListDTO modelTypeAndGroupListDTO : modelTypeAndGroupListDTOS) {
                    PluginCategoryDTO pluginCategoryDTO = new PluginCategoryDTO();
                    String groupTypeName = modelTypeAndGroupListDTO.getGroupTypeName();
                    GroupTypeEnum groupTypeEnum = GroupTypeEnum.valueOf(groupTypeName);
                    if (groupTypeEnum != null && !groupTypeEnum.getDesc().equals("")) {
                        pluginCategoryDTO.setCategory(groupTypeEnum.getDesc());
                    } else {
                        pluginCategoryDTO.setCategory(groupTypeName);

                    }

                    List<BusinessModelAndGroupListDTO> businessModelAndGroupLists = modelTypeAndGroupListDTO.getBusinessModelAndGroupListDTOS();
                    List<PluginCategoryDTO> pluginCategoryDTOS2 = new ArrayList<>();

                    if (businessModelAndGroupLists != null && businessModelAndGroupLists.size() > 0) {
                        for (BusinessModelAndGroupListDTO businessModelAndGroupListDTO : businessModelAndGroupLists) {

                            GroupDTO groupDTO = businessModelAndGroupListDTO.getGroupDTO();
                            List<BusinessModelDTO> businessModels = businessModelAndGroupListDTO.getBusinessModels();
                            PluginCategoryDTO pluginCategoryChild = new PluginCategoryDTO();
                            if (groupDTO == null) {
                                pluginCategoryChild.setCategory("其他");
                            } else {
                                pluginCategoryChild.setCategory(groupDTO.getGroupName());

                            }
                            List<PluginInfoDTO> pluginInfos = new ArrayList<>();
                            for (BusinessModelDTO businessModelDTO : businessModels) {
                                PluginInfoDTO pluginInfoDTO = new PluginInfoDTO();
                                if (groupTypeName.equals("ZDYMX")) {
                                    pluginInfoDTO.setPluginId("CustomizeModelInput");
                                    pluginInfoDTO.setPluginName("自定义模型");

                                } else {
                                    pluginInfoDTO.setPluginId("BasicModelInput");
                                    pluginInfoDTO.setPluginName(dpPortalPluginInfo.getPluginName());

                                }
                                pluginInfoDTO.setModelType(groupTypeName);
                                pluginInfoDTO.setModelName(businessModelDTO.getModelName());
                                pluginInfoDTO.setModelNameCn(businessModelDTO.getChineseName());
                                pluginInfoDTO.setPluginCategory(pluginCategoryChild.getCategory());
                                pluginInfoDTO.setPluginDescribe(dpPortalPluginInfo.getPluginDescribe());
                                pluginInfoDTO.setPluginImage(dpPortalPluginInfo.getPluginImage());
                                pluginInfoDTO.setPluginType(dpPortalPluginInfo.getPluginType());
                                pluginInfoDTO.setSecondPluginType(dpPortalPluginInfo.getSecondPluginType());
                                pluginInfos.add(pluginInfoDTO);
                            }
                            pluginCategoryChild.setPluginInfoDTOS(pluginInfos);
                            pluginCategoryDTOS2.add(pluginCategoryChild);
                        }
                    }
                    pluginCategoryDTO.setPluginCategoryDTOs(pluginCategoryDTOS2);
                    pluginCategoryDTOS1.add(pluginCategoryDTO);
                }
                pluginCategoryBusiness.setPluginCategoryDTOs(pluginCategoryDTOS1);
            }
            pluginCategoryDTOS.add(pluginCategoryBusiness);

        }

        return Result.success(pluginCategoryDTOS);
    }


    @Override
    public Result<List<FunctionCategoryDTO>, Object> getFuncDescribe(String userId) {
        try {
            FunctionLib functionLib = new FunctionLib("function/functions_ck_zh.xml");
            List<FunctionDescription> functionDescriptions = functionLib.getFunctions();
            Map<String, List<FunctionDescription>> groupMap = functionDescriptions.stream().collect(Collectors.groupingBy(o -> o.getCategory()));

            List<FunctionCategoryDTO> functionCategoryDTOS = new ArrayList<>();
            for (Map.Entry<String, List<FunctionDescription>> entry : groupMap.entrySet()) {
                FunctionCategoryDTO functionCategoryDTO = new FunctionCategoryDTO();
                functionCategoryDTO.setCategory(entry.getKey());
                List<FunctionDTO> functionDTOS = new ArrayList<>();
                List<FunctionDescription> functionDescriptions1 = entry.getValue();
                for (FunctionDescription functionDescription : functionDescriptions1) {
                    FunctionDTO functionDTO = new FunctionDTO();
                    functionDTO.setName(functionDescription.getName());
                    functionDTO.setCkName(functionDescription.getCkName());
                    functionDTO.setDescribtion(functionDescription.getDescription());
                    functionDTO.setHtmlString(functionDescription.getHtmlReport());
                    functionDTOS.add(functionDTO);
                }
                functionCategoryDTO.setFunctionDTOs(functionDTOS);
                functionCategoryDTOS.add(functionCategoryDTO);
            }
            return Result.success(functionCategoryDTOS);
        } catch (KettleXMLException e) {
            log.error("获取公式信息失败", e);
            throw new ValidationException(StatusCode.CODE_10010.getCode(), "获取公式信息失败");

        }
    }

    private static String removeSelfQuoteStr(String str) {
        String finalStr = str.trim();
        if (str.startsWith("\"") && str.endsWith("\"")) {
            finalStr = finalStr.substring(1, finalStr.length() - 1);
        } else if (finalStr.startsWith("[") && finalStr.endsWith("]")) {
            finalStr = finalStr.substring(1, finalStr.length() - 1);
        } else if (finalStr.startsWith("`") && finalStr.endsWith("`")) {
            finalStr = finalStr.substring(1, finalStr.length() - 1);
        }
        return finalStr;
    }

    @Override
    public Result getCreateTableDDL(String userId, CreateTableVO createTableVO) {
        DataSourceWrap dataSourceWrap = pluginRunDataSourceRepository.getDataSource(userId, createTableVO.getDataSourceId());
        Database database = null;
        try {
            database = generateDataBase(dataSourceWrap, createTableVO.getDataSourceId());
            List<FieldsInfoVO> fieldsInfoVOS = createTableVO.getFieldsInfos();
            RowMetaInterface rowMetaInterface = new RowMeta();
            for (FieldsInfoVO fieldsInfoVO : fieldsInfoVOS) {
                String name = removeSelfQuoteStr(fieldsInfoVO.getName());
                ValueMetaInterface valueMetaInterface;
                switch (ValueMetaInterface.getTypeCode(fieldsInfoVO.getType())) {
                    case ValueMetaInterface.TYPE_NONE:
                        valueMetaInterface = new ValueMetaNone();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_NUMBER:
                        valueMetaInterface = new ValueMetaNumber();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision() < 0 ? 0 : fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_STRING:
                        valueMetaInterface = new ValueMetaString();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_DATE:
                        valueMetaInterface = new ValueMetaDate();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_BOOLEAN:
                        valueMetaInterface = new ValueMetaBoolean();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_INTEGER:
                        valueMetaInterface = new ValueMetaInteger();
                        valueMetaInterface.setName(name);

                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_BIGNUMBER:
                        valueMetaInterface = new ValueMetaBigNumber();
                        valueMetaInterface.setName(name);

                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision() < 0 ? 0 : fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_SERIALIZABLE:
                        valueMetaInterface = new ValueMetaSerializable();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_BINARY:
                        valueMetaInterface = new ValueMetaBinary();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_TIMESTAMP:
                        valueMetaInterface = new ValueMetaTimestamp();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    case ValueMetaInterface.TYPE_INET:
                        valueMetaInterface = new ValueMetaInternetAddress();
                        valueMetaInterface.setName(name);
                        valueMetaInterface.setLength(fieldsInfoVO.getLength());
                        valueMetaInterface.setPrecision(fieldsInfoVO.getPrecision());
                        break;
                    default:
                        return Result.fail(StatusCode.CODE_10010.getCode(), "", "不支持的数据类型：" + fieldsInfoVO.getType());
                }
                rowMetaInterface.addValueMeta(valueMetaInterface);
            }
            String schema = createTableVO.getSchema();
            String tableName = schema != null && !schema.equals("") ? schema + "." + createTableVO.getTableName() : createTableVO.getTableName();
            String tk = createTableVO.getTk();
            Boolean use_autoinc = createTableVO.isUse_autoinc();
            String pk = createTableVO.getPk();
            boolean semicolon = createTableVO.isSemicolon();
            String createSql;
            DatabaseMeta databaseMeta = database.getDatabaseMeta();
            databaseMeta.quoteReservedWords(rowMetaInterface);
            String quotedTk = tk != null ? databaseMeta.quoteField(tk) : null;
            if (database.checkTableExists(createTableVO.getSchema(), createTableVO.getTableName())) {
                createSql = database.getAlterTableStatement(tableName, rowMetaInterface, quotedTk, use_autoinc, pk, semicolon);

                //没有变化
                if (StringUtil.isEmpty(createSql)) {
                    return Result.fail(StatusCode.CODE_10010.getCode(), "无执行SQL");

                }
                return Result.success(createSql);

            } else {
                createSql = database.getCreateTableStatement(tableName, rowMetaInterface, quotedTk, use_autoinc, pk, semicolon);
                log.info("create sql is :{}", createSql);
                if (createSql != null && !createSql.equals("")) {
                    return Result.success(createSql);
                } else {
                    return Result.fail(StatusCode.CODE_10010.getCode(), "", "建表语句生成失败！");

                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail(StatusCode.CODE_10010.getCode(), "", e.getMessage());
        } finally {

            if (database != null) {
                database.disconnect();

            }


        }
    }

    @Override
    public Result executeCreateDDL(String userId, ExecuteDDLVO executeDDLVO) {
        Database database = null;
        PreparedStatement preparedStatement = null;
        try {
            DataSourceWrap dataSourceWrap = pluginRunDataSourceRepository.getDataSource(userId, executeDDLVO.getDataSourceId());
            database = generateDataBase(dataSourceWrap, executeDDLVO.getDataSourceId());
            //有多行sql同时执行的话，一条一条执行
            List<String> sqlList = Arrays.asList(executeDDLVO.getDdl().trim().replaceAll("\n", " ").split(";"));
            if (!sqlList.isEmpty()) {
                for (int i = 0; i < sqlList.size(); i++) {
                    if (StringUtils.isNotBlank(sqlList.get(i))) {
                        preparedStatement = database.prepareSQL(sqlList.get(i));
                        preparedStatement.execute();
                    }
                }
            }
            return Result.success("");
        } catch (SQLException | KettleDatabaseException e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), "", e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (database != null) {
                database.disconnect();

            }
        }

    }

    @Override
    public Result getFieldSumInfo(String userId, String userName, ExecuteFieldSumVO executeFieldSumVO) {
        String[] masterDatasourceId = new String[1];
        List<String> stepSqls;
        try {
            stepSqls = getPreStepSqls(userId, userName, executeFieldSumVO.getTransJson(), executeFieldSumVO.getCurrentStepName(), executeFieldSumVO.getField(), masterDatasourceId);
        } catch (DpException e) {
            return Result.fail(e.getStatusCode(), "", e.getMessage());
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select a.").append(executeFieldSumVO.getField()).append(", count(1) as cnt from (");
        for (int i = 0; i < stepSqls.size(); i++) {
            String sql = stepSqls.get(i);
            stringBuffer.append(" ").append(PASSWORD_ENCODER.decode(sql)).append(" \n");
            if (i < stepSqls.size() - 1) {
                stringBuffer.append("UNION ALL");
            }
        }

        stringBuffer.append(") a group by a.").append(executeFieldSumVO.getField());
        DataSourceWrap dataSourceWrap = pluginRunDataSourceRepository.getDataSource(userId, masterDatasourceId[0]);
        Database database = null;
        try {
            database = generateDataBase(dataSourceWrap, masterDatasourceId[0]);
            StringBuffer cntSql = new StringBuffer();
            String sql = stringBuffer.toString();
            cntSql.append("select count(1) from (").append(sql).append(")");
            log.info("执行sql为：{}", sql);
            ResultSet cntResult = database.openQuery(sql);
            cntResult.next();
            double valueCnt = cntResult.getDouble(1);

            if (valueCnt > 1000) {
                return Result.fail(StatusCode.CODE_10010.getCode(), "", "所选字段值类别过多，推荐范围分箱方式");
            }

            ResultSet resultSet = database.openQuery(sql);
            List<FieldSumDTO> fieldSumDTOS = new ArrayList<>();
            while (resultSet.next()) {
                Object fieldValue = resultSet.getObject(1);
                int cnt = resultSet.getInt(2);
                FieldSumDTO fieldSumDTO = new FieldSumDTO();
                fieldSumDTO.setCnt(cnt);
                fieldSumDTO.setFieldValue(fieldValue);
                fieldSumDTOS.add(fieldSumDTO);
            }
            return Result.success(fieldSumDTOS);
        } catch (SQLException e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), "", e.getMessage());
        } catch (KettleDatabaseException e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), "", e.getMessage());
        } finally {

            if (database != null) {
                database.disconnect();

            }
        }

    }

    @Override
    public Result getFieldMaxMinInfo(String userId, String userName, ExecuteFieldSumVO executeFieldSumVO) {
        String[] masterDatasourceId = new String[1];
        List<String> stepSqls;
        try {
            stepSqls = getPreStepSqls(userId, userName, executeFieldSumVO.getTransJson(), executeFieldSumVO.getCurrentStepName(), executeFieldSumVO.getField(), masterDatasourceId);
        } catch (DpException e) {
            return Result.fail(e.getStatusCode(), "", e.getMessage());
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select max(a.").append(executeFieldSumVO.getField()).append(") as max, min(a.").append(executeFieldSumVO.getField()).append(")as min from (");
        for (int i = 0; i < stepSqls.size(); i++) {
            String sql = stepSqls.get(i);
            stringBuffer.append(" ").append(PASSWORD_ENCODER.decode(sql)).append(" \n");
            if (i < stepSqls.size() - 1) {
                stringBuffer.append("UNION ALL");
            }
        }

        stringBuffer.append(") a ");
        DataSourceWrap dataSourceWrap = pluginRunDataSourceRepository.getDataSource(userId, masterDatasourceId[0]);
        Database database = null;
        try {
            database = generateDataBase(dataSourceWrap, masterDatasourceId[0]);
            String sql = stringBuffer.toString();
            ResultSet resultSet = database.openQuery(sql);
            FieldMaxMinDTO fieldMaxMinDTO = new FieldMaxMinDTO();

            if (resultSet.next()) {
                Object max = resultSet.getObject(1);
                Object min = resultSet.getObject(2);
                fieldMaxMinDTO.setMax(max);
                fieldMaxMinDTO.setMin(min);
            }
            return Result.success(fieldMaxMinDTO);
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), "", e.getMessage());
        } catch (KettleDatabaseException e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), "", e.getMessage());

        } finally {
            if (database != null) {
                database.disconnect();

            }
        }
    }

    @Override
    public Result getPreStepSql(String userId, String userName, ExecuteFieldSumVO executeFieldSumVO) {
        String[] masterDatasourceId = new String[1];
        List<String> stepSqls;
        try {
            stepSqls = getPreStepSqls(userId, userName, executeFieldSumVO.getTransJson(), executeFieldSumVO.getCurrentStepName(), executeFieldSumVO.getField(), masterDatasourceId);
        } catch (DpException e) {
            return Result.fail(e.getStatusCode(), "", e.getMessage());
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select a.* from (");
        for (int i = 0; i < stepSqls.size(); i++) {
            String sql = stepSqls.get(i);
            stringBuffer.append(" ").append(PASSWORD_ENCODER.decode(sql)).append(" \n");
            if (i < stepSqls.size() - 1) {
                stringBuffer.append("UNION ALL");
            }
        }
        stringBuffer.append(") a ");
        PreStepSqlInfoDTO preStepSqlInfoDTO = new PreStepSqlInfoDTO();
        preStepSqlInfoDTO.setDataSourceId(masterDatasourceId[0]);
        preStepSqlInfoDTO.setPreStepSql(PASSWORD_ENCODER.encode(stringBuffer.toString()));
        return Result.success(preStepSqlInfoDTO);
    }


    private List<String> getPreStepSqls(String userId, String userName, String transJson, String curStepName, String FieldName, String[] masterDatasourceId) throws DpException {

        ProjectExecutorParam executorParamVO = new ProjectExecutorParam();
        executorParamVO.setLogLevel("Basic");
        executorParamVO.setProjectId(UUIDUtils.generateUUID32());
        executorParamVO.setProjectName("临时运行项目");
        executorParamVO.setUserId(userId);
        executorParamVO.setUserName(userName);
        executorParamVO.setSafeModeEnabled(false);
        List<String> parentStepNames;
        TransMeta transMeta;
        String executorId = projectExecutorService.generateExecutorId(executorParamVO.getProjectId());
        String transXMl;
        //获取父步骤对象
        try {
            transMeta = projectExecutorService.buildTransMeta(executorId, transJson);
            String[] names = transMeta.getPrevStepNames(curStepName);
            List<TransHopMeta> transHopMetasFrom = transMeta.findAllTransHopFrom(transMeta.findStep(curStepName));
            TransHopMeta transHopMetaTo = transMeta.findTransHopTo(transMeta.findStep(curStepName));
            for (TransHopMeta transHopMeta : transHopMetasFrom) {
                transMeta.removeTransHop(transHopMeta);
            }
            transMeta.removeTransHop(transHopMetaTo);
            List<StepMeta> stepMetas = transMeta.getSteps();

            Iterator<StepMeta> it = stepMetas.iterator();
            while (it.hasNext()) {
                StepMeta step = it.next();
                if (step.getName().equals(curStepName)) {
                    it.remove();
                }
            }
            transXMl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + transMeta.getXML();
            parentStepNames = Arrays.asList(names);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DpException(StatusCode.CODE_10010.getCode(), e.getMessage(), e);
        } catch (KettleXMLException e) {
            e.printStackTrace();
            throw new DpException(StatusCode.CODE_10010.getCode(), e.getMessage(), e);
        } catch (KettleMissingPluginsException e) {
            e.printStackTrace();
            throw new DpException(StatusCode.CODE_10010.getCode(), e.getMessage(), e);

        } catch (KettleException e) {
            throw new DpException(StatusCode.CODE_10010.getCode(), e.getMessage(), e);
        }

        if (parentStepNames.isEmpty()) {
            throw new DpException(StatusCode.CODE_10010.getCode(), "父步骤不存在");
        }
        executorParamVO.setExecutorId(executorId);
        LinkedList<Result> results = new LinkedList<>();
        projectExecutorService.executeByFile(transXMl, executorParamVO
            , new ProjectExecuteCallback() {
                @Override
                public void onMessage(String userId, Result webSocketMessage) {

                    log.info("转换执行过程：{}", JsonUtils.toString(webSocketMessage));
                    //获取执行结果
                    results.add(webSocketMessage);
                }
            }
        );
        List<String> stepSqls = new ArrayList<>();
        //获取最后一步执行结果
        Result finalResult = results.getLast();
        if (finalResult != null && finalResult.getCode().equals(StatusCode.CODE_10000) && finalResult.getContent() != null) {
            ProjectExecutorDTO projectExecutorDTO = (ProjectExecutorDTO) finalResult.getContent();
            if (projectExecutorDTO.getErrors() > 0) {
                throw new DpException(StatusCode.CODE_10010.getCode(), "前置步骤执行失败");
            }
        }

        return stepSqls;
    }

    Database generateDataBase(DataSourceWrap dataSourceWrap, String dataSourceId) throws SQLException {
        String name = dataSourceId;
        String type = dataSourceWrap.getDatabaseType().name();
        String access = null;
        String host = dataSourceWrap.getHost();
        String db = dataSourceWrap.getDb();
        String port = dataSourceWrap.getPort();
        String user = dataSourceWrap.getUser();
        String pass = dataSourceWrap.getPass();

        DatabaseMeta databaseMeta = new DatabaseMeta(name, type, access, host, db, port, user, pass);
        Database database = new Database(databaseMeta);
        database.setConnection(dataSourceWrap.getDataSource().getConnection());
        return database;
    }

}
