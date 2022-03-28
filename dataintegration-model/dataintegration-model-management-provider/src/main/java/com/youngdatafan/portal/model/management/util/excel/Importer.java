package com.youngdatafan.portal.model.management.util.excel;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelAndMetaDataListDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelCopyDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelMetaDataDTO;
import com.youngdatafan.portal.model.management.basicmodel.entity.BasiceModel;
import com.youngdatafan.portal.model.management.basicmodel.entity.BasiceModelMetadata;
import com.youngdatafan.portal.model.management.basicmodel.entity.DpPortalBasicModelQuerysql;
import com.youngdatafan.portal.model.management.basicmodel.mapper.BasiceModelMapper;
import com.youngdatafan.portal.model.management.basicmodel.mapper.BasiceModelMetadataMapper;
import com.youngdatafan.portal.model.management.basicmodel.mapper.DpPortalBasicModelQuerysqlDao;
import com.youngdatafan.portal.model.management.businessmodel.dto.*;
import com.youngdatafan.portal.model.management.businessmodel.entity.BusinessModelMetadata;
import com.youngdatafan.portal.model.management.businessmodel.entity.DpPortalBusinessModelQuerysql;
import com.youngdatafan.portal.model.management.businessmodel.mapper.BusinessModelMapper;
import com.youngdatafan.portal.model.management.businessmodel.mapper.BusinessModelMetadataMapper;
import com.youngdatafan.portal.model.management.businessmodel.mapper.DpPortalBusinessModelQuerysqlDao;
import com.youngdatafan.portal.model.management.businessmodel.mapper.GroupMapper;
import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.enums.GroupTypeEnum;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.datasource.entity.Datasource;
import com.youngdatafan.portal.model.management.datasource.mapper.DatasourceMapper;
import com.youngdatafan.portal.model.management.util.enums.DimensionMetricEnum;
import com.youngdatafan.portal.model.management.util.enums.TrueFalse;
import com.youngdatafan.portal.model.management.util.factory.sql.SqlJoin;
import com.youngdatafan.portal.model.management.util.mapper.ExcelColumnsMapper;
import com.youngdatafan.portal.model.management.util.mapper.ExcelTablesMapper;
import com.youngdatafan.portal.model.management.util.model.ExcelColumns;
import com.youngdatafan.portal.model.management.util.model.ExcelTables;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class Importer implements IImporter {
    private static final Logger logger = LoggerFactory.getLogger(Importer.class);

    public static final String BASIC_MODEL = "basic_model";
    public static final String BASIC_MODEL_METADATA = "basic_model_metadata";
    public static final String BUSINESS_MODEL = "business_model";
    public static final String BUSINESS_MODEL_METADATA = "business_model_metadata";

    private final String EXECUTI_ENGINE = "EXECUTIENGINE";

    @Resource
    com.youngdatafan.portal.model.management.util.factory.SqlFactory SqlFactory;
    @Resource
    private ExcelTablesMapper excelTablesMapper;
    @Resource
    private ExcelColumnsMapper excelColumnsMapper;
    @Resource
    private BasiceModelMapper basiceModelMapper;
    @Resource
    private BasiceModelMetadataMapper basiceModelMetadataMapper;
    @Resource
    private BusinessModelMapper businessModelMapper;
    @Resource
    private BusinessModelMetadataMapper businessModelMetadataMapper;

    @Resource
    DpPortalBasicModelQuerysqlDao dpPortalBasicModelQuerysqlDao;


    @Resource
    DpPortalBusinessModelQuerysqlDao dpPortalBusinessModelQuerysqlDao;
    @Resource
    DatasourceMapper datasourceMapper;

    @Resource
    GroupMapper groupMapper;

    @Resource
    private IParser parser;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Result importBasicModel(String userId, XSSFWorkbook workbook, String modelType) {
        ObjectMapper mapper = new ObjectMapper();
        ExcelUploadResult excelUploadResult = parseExcel(BASIC_MODEL, workbook, excelColumnsMapper.selectByTable(BASIC_MODEL));

        String type = "";
        if (modelType.equals(GroupTypeEnum.JBMX.code())) {
            type = GroupTypeEnum.JBMX.code();
        } else if (modelType.equals(GroupTypeEnum.ZDYMX.code())) {
            type = GroupTypeEnum.ZDYMX.code();
        }

        List<BasicModelAndMetaDataDTO> basicModelExcels = new ArrayList<>();
        for (ObjectNode objectNode : excelUploadResult.getResultObject()) {
            try {
                basicModelExcels.add(mapper.treeToValue(objectNode, BasicModelAndMetaDataDTO.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException("excel上传对象转换错误");
            }
        }

        List<String> list = new ArrayList<>();

        List<BasicModelCopyDTO> list1 = new ArrayList<>();
        for (BasicModelAndMetaDataDTO basicModelAndMetaDataDTO : basicModelExcels
        ) {

            if (!list.contains(basicModelAndMetaDataDTO.getCName())) {

                list.add(basicModelAndMetaDataDTO.getCName());

                BasicModelCopyDTO basiceModel = new BasicModelCopyDTO();

                basiceModel.setModelName(UUID.randomUUID().toString());

                basiceModel.setGroupType(basicModelAndMetaDataDTO.getModelType());

                basiceModel.setCName(basicModelAndMetaDataDTO.getCName());

                basiceModel.setDsName(basicModelAndMetaDataDTO.getDsName());

                basiceModel.setTableSchema(basicModelAndMetaDataDTO.getSchemaName());

                basiceModel.setCreateUserId(userId);

                basiceModel.setCreateTime(new Date());

                basiceModel.setModelSort(basicModelAndMetaDataDTO.getModelSort());

                basiceModel.setUpdateTime(new Date());


                basiceModel.setModelType(type);

                basiceModel.setDescription(basicModelAndMetaDataDTO.getDescription());

                basiceModel.setTableName(basicModelAndMetaDataDTO.getTableName());

                basiceModel.setCName(basicModelAndMetaDataDTO.getCName());

                basiceModel.setEnabled(TrueFalse.T.code().equals(basicModelAndMetaDataDTO.getEnabled()) ? TrueFalse.T.code() : TrueFalse.F.code());

                list1.add(basiceModel);
            }
        }


        List<BasicModelAndMetaDataListDTO> listDTOS = new ArrayList<>();

        for (BasicModelCopyDTO b : list1) {

            BasicModelAndMetaDataListDTO basicModelAndMetaDataListDTO = new BasicModelAndMetaDataListDTO();

            basicModelAndMetaDataListDTO.setBasicModelDTO(b);

            List<BasicModelMetaDataDTO> basicModelMetaDataDTOS = new ArrayList<>();

            for (BasicModelAndMetaDataDTO basicModelAndMetaDataDTO : basicModelExcels
            ) {
                BasicModelMetaDataDTO basicModelMetaDataDTO = new BasicModelMetaDataDTO();
                if (b.getCName().equals(basicModelAndMetaDataDTO.getCName())) {
                    basicModelMetaDataDTO.setColumnChineseName(basicModelAndMetaDataDTO.getColumnChineseName());

                    basicModelMetaDataDTO.setColumnDescription(basicModelAndMetaDataDTO.getColumnDescription());

                    basicModelMetaDataDTO.setColumnLength(org.apache.commons.lang.StringUtils.isBlank(basicModelAndMetaDataDTO.getColumnLength()) ? null : Integer.valueOf(basicModelAndMetaDataDTO.getColumnLength()));

                    basicModelMetaDataDTO.setModelName(b.getModelName());

                    basicModelMetaDataDTO.setColumnName(basicModelAndMetaDataDTO.getColumnName());

                    basicModelMetaDataDTO.setColumnPrecision(org.apache.commons.lang.StringUtils.isEmpty(basicModelAndMetaDataDTO.getColumnPrecision()) ? null : Integer.valueOf(basicModelAndMetaDataDTO.getColumnPrecision()));

                    basicModelMetaDataDTO.setModelDataSort(basicModelAndMetaDataDTO.getModelDataSort());

                    basicModelMetaDataDTO.setColumnChineseName(basicModelAndMetaDataDTO.getColumnChineseName());

                    basicModelMetaDataDTO.setColumnType(basicModelAndMetaDataDTO.getColumnType());

//                    businessModelAndBasicModelName.setBusinessModelEnable(TrueFalse.T.code().equals(basicModelAndMetaDataDTO.getStatistics()) ? TrueFalse.T.code() : TrueFalse.F.code());
                    String d = basicModelAndMetaDataDTO.getDimensionMetric();
                    if (org.apache.commons.lang.StringUtils.isEmpty(d)) {
                        basicModelMetaDataDTO.setDimensionMetric(DimensionMetricEnum.NULL.code());
                    } else {
                        basicModelMetaDataDTO.setDimensionMetric(DimensionMetricEnum.METRIC.code().equals(d) ? DimensionMetricEnum.METRIC.code() : DimensionMetricEnum.DIMENSION.code());
                    }

                    basicModelMetaDataDTO.setStatistics(TrueFalse.T.code().equals(basicModelAndMetaDataDTO.getStatistics()) ? TrueFalse.T.code() : TrueFalse.F.code());

                    basicModelMetaDataDTOS.add(basicModelMetaDataDTO);

                }
            }

            basicModelAndMetaDataListDTO.setBasicModelMetaDataDTO(basicModelMetaDataDTOS);

            listDTOS.add(basicModelAndMetaDataListDTO);
        }


        for (BasicModelAndMetaDataListDTO b : listDTOS) {

            BasiceModel basiceModel = new BasiceModel();

            BasicModelCopyDTO basicModelCopyDTO = b.getBasicModelDTO();

            BeanUtils.copyProperties(basicModelCopyDTO, basiceModel);

            DatasourceDTO datasourceDTO = datasourceMapper.getDatasourceByNameAndSourcePlat(EXECUTI_ENGINE, basiceModel.getDsName());

            if (StringUtils.isEmpty(datasourceDTO)) {

                List<String> engineDatasourceIds = datasourceMapper.getEngineDatasourceIds(EXECUTI_ENGINE);

                String[] strings = engineDatasourceIds.toArray(new String[]{});

                String  str= org.apache.commons.lang.StringUtils.join(strings,",");

                throw new RuntimeException(basiceModel.getDsName() + "非默认执行引擎数据源,请使用"+str);
            }

            String groupId = groupMapper.selectGroupIdByGroupNameAndUserId(userId, basicModelCopyDTO.getGroupType(), type);

            if (org.apache.commons.lang.StringUtils.isEmpty(groupId)) {
                throw new RuntimeException(basicModelCopyDTO.getGroupType() + "分组类型不存在");
            }
            String modelName = basiceModelMapper.selectModelNameByUserIdAndCname(userId, basicModelCopyDTO.getCName(), type, groupId);
            if (!StringUtils.isEmpty(modelName)) {
                if (basiceModelMapper.deleteByPrimaryKey(modelName, userId) > 0) {

                    if (basiceModelMapper.deleteModelGroup(modelName) < 0) {
                        throw new RuntimeException("上传失败");
                    }

                    if (basiceModelMetadataMapper.deleteByModelName(modelName) < 0) {
                        throw new RuntimeException("上传失败");
                    }
                } else {
                    throw new RuntimeException("上传失败");
                }

            }

            basiceModel.setDsName(datasourceDTO.getDatasourceId());
            if (basiceModelMapper.insert(basiceModel) > 0) {

                if (basiceModelMapper.insertModelGroup(basiceModel.getModelName(), groupId) < 0) {
                    throw new RuntimeException("上传失败");
                }
                List<BasicModelMetaDataDTO> basicModelMetaDataDTO = b.getBasicModelMetaDataDTO();

                if (basiceModelMetadataMapper.insertIntoBatch(basicModelMetaDataDTO) < 0) {
                    throw new RuntimeException("上传失败");
                }

                Datasource datasource = datasourceMapper.selectByPrimaryKey(datasourceDTO.getDatasourceId());

                SqlJoin datasourceEntity = SqlFactory.getDatasourceEntity(datasource.getDsType());

                List<BasicModelMetaDataVO> basicModelMetaDataVOS = new ArrayList<>();

                basicModelMetaDataVOS.addAll(basicModelMetaDataDTO);
                StringBuffer stringBuffer = datasourceEntity.getStringBuffer(basicModelMetaDataVOS, new ArrayList<>(), basiceModel.getTableName(), false);

                DpPortalBasicModelQuerysql dpPortalBasicModelQuerysql = new DpPortalBasicModelQuerysql(basiceModel.getModelName(), stringBuffer.toString());
                if (dpPortalBasicModelQuerysqlDao.insert(dpPortalBasicModelQuerysql) < 0) {
                    throw new RuntimeException();
                }
            }

        }


        return Result.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExcelRejectCell> importBasicModelMeta(XSSFWorkbook workbook) {
        ObjectMapper mapper = new ObjectMapper();
        ExcelUploadResult excelUploadResult = parseExcel(BASIC_MODEL_METADATA, workbook, excelColumnsMapper.selectByTable(BASIC_MODEL_METADATA));
        List<BasiceModelMetadata> basicModelMetadataExcels = new ArrayList<>();
        for (ObjectNode objectNode : excelUploadResult.getResultObject()) {
            try {
                basicModelMetadataExcels.add(mapper.treeToValue(objectNode, BasiceModelMetadata.class));
            } catch (JsonProcessingException e) {

                throw new RuntimeException("excel上传对象转换错误");
            }
        }


//        adminBasicModelMetadataMapper.insertIntoBatch(basicModelMetadataExcels);
        return excelUploadResult.getRejectCells();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importBusinessModel(String userId, XSSFWorkbook workbook) {
        ObjectMapper mapper = new ObjectMapper();
        ExcelUploadResult excelUploadResult = parseExcel(BUSINESS_MODEL, workbook, excelColumnsMapper.selectByTable(BUSINESS_MODEL));

        List<BatchDownloadBusinessModelDTO> businessModelExcels = new ArrayList<>();
        for (ObjectNode objectNode : excelUploadResult.getResultObject()) {
            try {
                businessModelExcels.add(mapper.treeToValue(objectNode, BatchDownloadBusinessModelDTO.class));

            } catch (JsonProcessingException e) {
                throw new RuntimeException("excel上传对象转换错误");
            }
        }


        List<String> list = new ArrayList<>();

        List<BusinessModelAndBasicModelName> businessModelAndBasicModelNames
                = new ArrayList<>();
        for (BatchDownloadBusinessModelDTO b : businessModelExcels) {

            if (!list.contains(b.getBusinessModelName())) {
                list.add(b.getBusinessModelName());

                BusinessModelAndBasicModelName businessModelAndBasicModelName = new BusinessModelAndBasicModelName();

                businessModelAndBasicModelName.setBasicModelName(b.getBasicModelName());

                businessModelAndBasicModelName.setBasicModelGroup(b.getBasicModelGroup());

                businessModelAndBasicModelName.setBusinessModelEnable(TrueFalse.T.code().equals(b.getBusinessModelEnable()) ? TrueFalse.T.code() : TrueFalse.F.code());

                businessModelAndBasicModelName.setModelSort(b.getModelSort());

                businessModelAndBasicModelName.setDescription(b.getDescription());

                businessModelAndBasicModelName.setBusinessModelGroupName(b.getBusinessModelGroupName());

                businessModelAndBasicModelName.setBusinessModelName(b.getBusinessModelName());

                businessModelAndBasicModelName.setBusinessModelId(UUID.randomUUID().toString());

                businessModelAndBasicModelName.setCreateTime(new Date());

                businessModelAndBasicModelName.setUpdateTime(new Date());

                businessModelAndBasicModelName.setCreateUserId(userId);

                businessModelAndBasicModelNames.add(businessModelAndBasicModelName);

            }

        }

        List<BusinessModelAndMetaListDTO> businessModelAndMetaListDTOS = new ArrayList<>();


        for (BusinessModelAndBasicModelName businessModelAndBasicModelName : businessModelAndBasicModelNames) {

            String tableName = businessModelMapper.selectTableNameByModelNameAndGroupName(userId, businessModelAndBasicModelName.getBasicModelName(), businessModelAndBasicModelName.getBasicModelGroup());

            if (org.apache.commons.lang.StringUtils.isEmpty(tableName)) {
                throw new RuntimeException(businessModelAndBasicModelName.getBasicModelName() + "基础模型查找不到");
            }

            BusinessModelAndMetaListDTO businessModelAndMetaListDTO = new BusinessModelAndMetaListDTO();

            businessModelAndMetaListDTO.setBusinessModelAndBasicModelName(businessModelAndBasicModelName);

            List<BusinessModelMetaDataCopyDTO>
                    businessModelMetaDataCopyDTOS = new ArrayList<>();

            StringBuffer stringBuffer = new StringBuffer("select ");

            for (BatchDownloadBusinessModelDTO b : businessModelExcels) {

                if (b.getBusinessModelName().equals(businessModelAndBasicModelName.getBusinessModelName())) {

                    BusinessModelMetaDataCopyDTO businessModelMetaDataCopyDTO = new BusinessModelMetaDataCopyDTO();

                    businessModelMetaDataCopyDTO.setBusinessModelName(businessModelAndBasicModelName.getBusinessModelId());

                    businessModelMetaDataCopyDTO.setColumnSerial(b.getModelColumnSort());

                    businessModelMetaDataCopyDTO.setColumnName(b.getBasicModelColumnName());

                    businessModelMetaDataCopyDTO.setCustomColumnName(b.getBusinessModelColumnName());

                    businessModelMetaDataCopyDTOS.add(businessModelMetaDataCopyDTO);

                    stringBuffer = stringBuffer.append(b.getBasicModelColumnName() + ", ");

                }
            }
            String prefixColumn = stringBuffer.substring(0, stringBuffer.length() - 2);


            String querySql = prefixColumn + " from " + tableName;

            businessModelAndBasicModelName.setQuerySql(querySql);


            businessModelAndMetaListDTO.setList(businessModelMetaDataCopyDTOS);

            businessModelAndMetaListDTOS.add(businessModelAndMetaListDTO);
        }

        for (BusinessModelAndMetaListDTO businessModelAndMetaListDTO : businessModelAndMetaListDTOS) {
            BusinessModelAndBasicModelName businessModelAndBasicModelName = businessModelAndMetaListDTO.getBusinessModelAndBasicModelName();

            String modelName = businessModelMapper.selectBasicModelIdByModelNameAndGroupName(userId, businessModelAndBasicModelName.getBasicModelName(), businessModelAndBasicModelName.getBasicModelGroup());

            if (org.apache.commons.lang.StringUtils.isEmpty(modelName)) {
                throw new RuntimeException(businessModelAndBasicModelName.getBasicModelName() + "基础模型查找不到");
            }

            String groupId = groupMapper.selectGroupIdByGroupNameAndUserId(userId, businessModelAndBasicModelName.getBusinessModelGroupName(), GroupTypeEnum.YWMX.code());

            if (org.apache.commons.lang.StringUtils.isEmpty(groupId)) {
                throw new RuntimeException(businessModelAndBasicModelName.getBusinessModelGroupName() + "分组查找不到");
            }

            BusinessModelDTO businessModelDTO = businessModelMapper.selectByPrimaryKey(userId, businessModelAndBasicModelName.getBusinessModelName(), groupId);

            if (!StringUtils.isEmpty(businessModelDTO)) {

                if (businessModelMapper.deleteByPrimaryKey(userId, businessModelDTO.getModelName()) > 0) {

                    if (businessModelMetadataMapper.deleteByModelName(businessModelDTO.getModelName()) < 0) {
                        throw new RuntimeException("上传失败");
                    }

                    if (businessModelMapper.deleteModelGroup(businessModelDTO.getModelName()) < 0) {
                        throw new RuntimeException("上传失败");
                    }

                } else {
                    throw new RuntimeException("上传失败");

                }
            }

            businessModelAndBasicModelName.setBasicModelName(modelName);

            if (businessModelMapper.insertBusinessModel(businessModelAndBasicModelName) > 0) {


                int flag = businessModelMapper.insertModelGroup(businessModelAndBasicModelName.getBusinessModelId(), groupId);

                if (flag < 0) {
                    throw new RuntimeException("上传失败");
                }

                if (businessModelMetadataMapper.insertIntoBatch(businessModelAndMetaListDTO.getList()) < 0) {
                    throw new RuntimeException("上传失败");
                }

                DpPortalBusinessModelQuerysql dpPortalBusinessModelQuerysql = new DpPortalBusinessModelQuerysql(businessModelAndBasicModelName.getBusinessModelId(), businessModelAndBasicModelName.getQuerySql());

                if (dpPortalBusinessModelQuerysqlDao.insert(dpPortalBusinessModelQuerysql) < 0) {
                    throw new RuntimeException();
                }

            }

        }
        return Result.success(true);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExcelRejectCell> importBusinessModelMeta(XSSFWorkbook workbook) {
        ObjectMapper mapper = new ObjectMapper();
        ExcelUploadResult excelUploadResult = parseExcel(BUSINESS_MODEL_METADATA, workbook, excelColumnsMapper.selectByTable(BUSINESS_MODEL_METADATA));
        List<BusinessModelMetadata> businessModelMetadataExcels = new ArrayList<>();
        for (ObjectNode objectNode : excelUploadResult.getResultObject()) {
            try {
                businessModelMetadataExcels.add(mapper.treeToValue(objectNode, BusinessModelMetadata.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("excel上传对象转换错误");
            }
        }

//        adminBusinessModelMetadataMapper.insertIntoBatch(businessModelMetadataExcels);
        return excelUploadResult.getRejectCells();
    }

    @Override
    public ExcelUploadResult parseExcel(String tableId, XSSFWorkbook workbook, List<ExcelColumns> excelColumns) {

        List<ObjectNode> resultObjects = new ArrayList<>();
        List<ExcelRejectCell> rejectCells = new LinkedList<>();

        ExcelTables excelTables = excelTablesMapper.selectByPrimaryKey(tableId);
        List<Future<ObjectNode>> list = new LinkedList<>();
        if (null != workbook.getSheet(excelTables.getSheet())) {
            for (Row row : workbook.getSheet(excelTables.getSheet())) {
                if (0 == row.getRowNum()) {
                    continue;
                }
                list.add(parser.parseRow(tableId, row, rejectCells, excelColumns));

            }
            for (Future<ObjectNode> future : list) {
                try {
                    ObjectNode objectNode = future.get(10, TimeUnit.MINUTES);
                    if (null != objectNode) {
                        resultObjects.add(objectNode);
                    }
                } catch (TimeoutException te) {
                    logger.info("Excel异步线程池行解析超时");
                    te.printStackTrace();
//                    future.cancel(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Excel异步线程池行解析报错:", e);
                }
            }
        }
        ExcelUploadResult excelUploadResult = new ExcelUploadResult();
        excelUploadResult.setRejectCells(rejectCells);
        excelUploadResult.setResultObject(resultObjects);
        return excelUploadResult;
    }

}
