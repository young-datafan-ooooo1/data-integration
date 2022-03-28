package com.youngdatafan.portal.model.management.businessmodel.service.impl;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.basicmodel.dto.ModelNameAndModelFilterDTO;
import com.youngdatafan.portal.model.management.basicmodel.entity.BasiceModel;
import com.youngdatafan.portal.model.management.basicmodel.mapper.BasiceModelMapper;
import com.youngdatafan.portal.model.management.basicmodel.mapper.DpPortalModelFilterMapper;
import com.youngdatafan.portal.model.management.businessmodel.dto.*;
import com.youngdatafan.portal.model.management.businessmodel.entity.BusinessModel;
import com.youngdatafan.portal.model.management.businessmodel.entity.BusinessModelMetadata;
import com.youngdatafan.portal.model.management.businessmodel.entity.DpPortalBusinessModelQuerysql;
import com.youngdatafan.portal.model.management.businessmodel.mapper.BusinessModelMapper;
import com.youngdatafan.portal.model.management.businessmodel.mapper.BusinessModelMetadataMapper;
import com.youngdatafan.portal.model.management.businessmodel.mapper.DpPortalBusinessModelQuerysqlDao;
import com.youngdatafan.portal.model.management.businessmodel.mapper.GroupMapper;
import com.youngdatafan.portal.model.management.businessmodel.service.BusinessModelService;
import com.youngdatafan.portal.model.management.businessmodel.vo.AddBusinessModelVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.BasicModelMetaDataCopyDTO;
import com.youngdatafan.portal.model.management.businessmodel.vo.BusinessPreviewDataVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.UpdateBusinessModelVO;
import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import com.youngdatafan.portal.model.management.common.enums.GroupTypeEnum;
import com.youngdatafan.portal.model.management.datasource.mapper.DatasourceMapper;
import com.youngdatafan.portal.model.management.util.enums.TrueFalse;
import com.youngdatafan.portal.model.management.util.factory.sql.SqlJoin;
import com.youngdatafan.portal.model.management.util.jdbc.JdbcUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/19 11:01 AM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class BusinessModelServiceImpl implements BusinessModelService {


    private static final Pattern URL_PATTERN = Pattern.compile("([\\w.]+):(\\d+)[:/]?([\\w.]+)?");

    private static final Pattern TD_URL_PATTERN = Pattern.compile("([\\w.]+)/DATABASE=(\\w+).*DBS_PORT=(\\d+)");

    private static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    private final static String YWMX = "YWMX";

    private final static String ZDYMX = "ZDYMX";

    private final static String SCLMX = "SCLMX";

    private final static String SCJK = "SCJK";

    @Resource
    BusinessModelMapper businessModelMapper;

    @Resource
    BusinessModelMetadataMapper businessModelMetadataMapper;

    @Resource
    GroupMapper groupMapper;

    @Resource
    BasiceModelMapper basiceModelMapper;

    @Resource
    DatasourceMapper datasourceMapper;

    @Resource
    DpPortalModelFilterMapper dpPortalModelFilterMapper;

    @Resource
    com.youngdatafan.portal.model.management.util.factory.SqlFactory SqlFactory;

    @Resource
    DpPortalBusinessModelQuerysqlDao dpPortalBusinessModelQuerysqlDao;

    @Override
    public List<ModelTypeAndGroupListDTO> selectModelAndGroupList(String userId) {
        List<String> list = new ArrayList<>();
        list.add(GroupTypeEnum.YWMX.code());
        list.add(GroupTypeEnum.ZDYMX.code());
        list.add(GroupTypeEnum.SCLMX.code());
        list.add(GroupTypeEnum.SCJK.code());

        List<BusinessModelAndGroupListDTO> listDTOS = businessModelMapper.selectModelAndGroupList(userId);

        List<BusinessModelAndGroupListDTO> businessModelAndGroupListDTOList = businessModelMapper.selectCustomModelAndGroupList(userId);

        List<BusinessModelAndGroupListDTO> outModelAndGroupList = businessModelMapper.selectOutModelAndGroupList(userId);

        List<BusinessModelAndGroupListDTO> outinterfaceModelAndGroupList = businessModelMapper.selectOutinterfaceModelAndGroupList(userId);

        listDTOS.addAll(outModelAndGroupList);

        listDTOS.addAll(businessModelAndGroupListDTOList);

        listDTOS.addAll(outinterfaceModelAndGroupList);

        List<ModelTypeAndGroupListDTO> modelTypeAndGroupListDTOS = new ArrayList<>();

        for (String type : list) {

            ModelTypeAndGroupListDTO modelTypeAndGroupListDTO = new ModelTypeAndGroupListDTO();

            modelTypeAndGroupListDTO.setGroupTypeName(type);

            List<BusinessModelAndGroupListDTO> businessModelAndGroupListDTOS = new ArrayList<>();

            for (BusinessModelAndGroupListDTO businessModelAndGroupListDTO : listDTOS) {

                if (!StringUtils.isEmpty(businessModelAndGroupListDTO)) {
                    GroupDTO groupDTO = businessModelAndGroupListDTO.getGroupDTO();

                    if (type.equals(groupDTO.getGroupType())) {

                        businessModelAndGroupListDTOS.add(businessModelAndGroupListDTO);
                    }
                }
            }
            modelTypeAndGroupListDTO.setBusinessModelAndGroupListDTOS(businessModelAndGroupListDTOS);
            modelTypeAndGroupListDTOS.add(modelTypeAndGroupListDTO);
        }

        return modelTypeAndGroupListDTOS;
    }

    @Override
    public BusinessModelDTO selectModelByModelName(String userId, String modelName) {
        return businessModelMapper.selectModelByModelName(userId, modelName);
    }

    @Override
    public Result<ModelDatasourceDTO, Object> selectDatasource(String userId, String businessModelName) {
        return Result.success(businessModelMapper.selecDatasourceByModelName(businessModelName));
    }

    @Override
    public Result<List<BusinessModelMetaDataDTO>, Object> selectBusinessModelMetaData(String userId, String businessModelName) {
        return Result.success(businessModelMapper.selectBusinessMetaData(businessModelName));
    }

    @Override
    public Result<ModelDatasourceDTO, Object> selectDatasourceByModelName(String userId, String businessModelName, String modeType) {
        ModelDatasourceDTO modelDatasourceDTO = null;

        switch (modeType) {
            case YWMX:
                modelDatasourceDTO = businessModelMapper.selecDatasourceByModelName(businessModelName);
                break;
            case ZDYMX:
                modelDatasourceDTO = businessModelMapper.selecDatasourceByZDYModelName(businessModelName);
                break;
            case SCLMX:
                modelDatasourceDTO = businessModelMapper.selecDatasourceBySCLModelName(businessModelName);
                break;
            case SCJK:
                modelDatasourceDTO = businessModelMapper.selecDatasourceBySCJKModelName(businessModelName);
                break;
            default:
                break;
        }
        return Result.success(modelDatasourceDTO);
    }


    @Override
    public Result<Boolean, Object> insert(String userId, AddBusinessModelVO addBusinessModelVO) {

        BasiceModel basiceModel = basiceModelMapper.selectByPrimaryKey(addBusinessModelVO.getBasicModelTableNameValue(), userId);

        if (StringUtils.isEmpty(basiceModel)) {
            throw new RuntimeException("此基础模型不存在");
        }

        BusinessModel businessModel = new BusinessModel();
        businessModel.setModelName(UUID.randomUUID().toString());
        businessModel.setCreateTime(new Date());
        businessModel.setCreateUserId(userId);
        businessModel.setDescription(addBusinessModelVO.getDescription());
        businessModel.setBasicModelName(addBusinessModelVO.getBasicModelTableNameValue());
        businessModel.setEnabled(addBusinessModelVO.getEnabled());
        businessModel.setChineseName(addBusinessModelVO.getName());
        businessModel.setUpdateTime(new Date());
        businessModel.setEnabled(TrueFalse.T.getDesc().equals(addBusinessModelVO.getEnabled()) ? TrueFalse.T.code() : TrueFalse.F.code());
        businessModel.setCreateUserId(userId);

        businessModel.setModelSort(addBusinessModelVO.getSortNum());

        StringBuffer stringBuffer = new StringBuffer("select ");

        List<BasicModelMetaDataCopyDTO> list = addBusinessModelVO.getBasicModelMetaDataCopyDTOS();

        List<BusinessModelMetadata> businessModelMetadatas = new ArrayList<>();

        List<ModelFilterVO> modelFilterVOS = addBusinessModelVO.getModelFilterVOS();

        if (!StringUtils.isEmpty(list)) {

            for (BasicModelMetaDataCopyDTO s : list
            ) {
                stringBuffer = stringBuffer.append(s.getColumnName() + ", ");

                BusinessModelMetadata businessModelMetadata = new BusinessModelMetadata();

                businessModelMetadata.setBusinessModelName(businessModel.getModelName());

                businessModelMetadata.setColumnName(s.getColumnName());

                businessModelMetadata.setCustomColumnName(StringUtils.isEmpty(s.getCName()) ? null : s.getCName());

                businessModelMetadata.setColumnSerial(s.getModelDataSort());

                businessModelMetadatas.add(businessModelMetadata);
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 2);

//            stringBuffer.append(" from " + basiceModel.getTableName());

            stringBuffer.append(" from ( ? )");

            List<ModelFilterVO> modelFilterVOList = dpPortalModelFilterMapper.queryByModelName(addBusinessModelVO.getBasicModelTableNameValue());

            modelFilterVOS.addAll(modelFilterVOList);

            joinQuerySql(modelFilterVOList, modelFilterVOS, stringBuffer);

            String querySql = stringBuffer.toString();


            businessModel.setQuerySql(querySql);
        }


        if (businessModelMapper.insert(businessModel) > 0) {

            if (modelFilterVOS.size() > 0) {

                for (ModelFilterVO modelFilterVO : modelFilterVOS) {
                    modelFilterVO.setModelId(businessModel.getModelName());
                }

                if (dpPortalModelFilterMapper.insertModelFilter(modelFilterVOS) < 0) {
                    throw new RuntimeException();
                }
            }

            if (businessModelMapper.insertModelGroup(businessModel.getModelName(), addBusinessModelVO.getGroup()) < 0) {
                throw new RuntimeException();
            }

            if (!StringUtils.isEmpty(list)) {

                if (businessModelMetadatas.size() > 0) {
                    if (businessModelMetadataMapper.insertBusinessModelMetaData(businessModelMetadatas) < 0) {
                        throw new RuntimeException();
                    }
                }
            }

            DpPortalBusinessModelQuerysql dpPortalBusinessModelQuerysql = new DpPortalBusinessModelQuerysql(businessModel.getModelName(), businessModel.getQuerySql());

            if (dpPortalBusinessModelQuerysqlDao.insert(dpPortalBusinessModelQuerysql) < 0) {
                throw new RuntimeException();
            }

            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);
        }

    }

    @Override
    public Result<Boolean, Object> delete(String userId, String modelName) {

        if (businessModelMapper.deleteByPrimaryKey(userId, modelName) > 0) {

            if (businessModelMetadataMapper.deleteByModelName(modelName) < 0) {
                throw new RuntimeException();
            }

            if (businessModelMapper.deleteModelGroup(modelName) < 0) {
                throw new RuntimeException();
            }

            if (dpPortalModelFilterMapper.deleteById(modelName) < 0) {
                throw new RuntimeException();
            }

            if (dpPortalBusinessModelQuerysqlDao.deleteById(modelName) < 0) {
                throw new RuntimeException();
            }

        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean, Object> update(String userId, UpdateBusinessModelVO updateBusinessModelVO) {

        BasiceModel basiceModel = basiceModelMapper.selectByPrimaryKey(updateBusinessModelVO.getBasicModelTableNameValue(), userId);

        if (StringUtils.isEmpty(basiceModel)) {
            throw new RuntimeException("此基础模型不存在");
        }

        BusinessModel businessModel = new BusinessModel();
        businessModel.setModelName(updateBusinessModelVO.getModelName());
        businessModel.setCreateUserId(userId);
        businessModel.setDescription(updateBusinessModelVO.getDescription());
        businessModel.setBasicModelName(updateBusinessModelVO.getBasicModelTableNameValue());
        businessModel.setEnabled(updateBusinessModelVO.getEnabled());
        businessModel.setChineseName(updateBusinessModelVO.getName());
        businessModel.setUpdateTime(new Date());
        businessModel.setEnabled(TrueFalse.T.getDesc().equals(updateBusinessModelVO.getEnabled()) ? TrueFalse.T.code() : TrueFalse.F.code());

        businessModel.setCreateUserId(userId);
        businessModel.setModelSort(updateBusinessModelVO.getSortNum());

        List<BusinessModelMetadata> businessModelMetadatas = new ArrayList<>();

        List<BasicModelMetaDataCopyDTO> list = updateBusinessModelVO.getBasicModelMetaDataCopyDTOS();

        List<ModelFilterVO> modelFilter = updateBusinessModelVO.getModelFilterVOS();

        StringBuffer stringBuffer = new StringBuffer("select ");

        if (!StringUtils.isEmpty(list)) {

            for (BasicModelMetaDataCopyDTO s : list
            ) {
                BusinessModelMetadata businessModelMetadata = new BusinessModelMetadata();

                stringBuffer = stringBuffer.append(s.getColumnName() + ", ");

                businessModelMetadata.setBusinessModelName(businessModel.getModelName());

                businessModelMetadata.setColumnName(s.getColumnName());

                businessModelMetadata.setColumnSerial(s.getModelDataSort());

                businessModelMetadata.setCustomColumnName(StringUtils.isEmpty(s.getBusinessModelName()) ? null : s.getBusinessModelName());

                businessModelMetadatas.add(businessModelMetadata);
            }


            stringBuffer.deleteCharAt(stringBuffer.length() - 2);

            stringBuffer.append(" from ( ? )");

            List<ModelFilterVO> modelFilterVOList = dpPortalModelFilterMapper.queryByModelName(updateBusinessModelVO.getBasicModelTableNameValue());

            joinQuerySql(modelFilterVOList, modelFilter, stringBuffer);

            String querySql = stringBuffer.toString();

            businessModel.setQuerySql(querySql);
        }


        if (businessModelMapper.updateByPrimaryKeySelective(businessModel) >= 0) {

            if (businessModelMapper.deleteModelGroup(businessModel.getModelName()) < 0) {
                throw new RuntimeException();
            }

            if (businessModelMapper.insertModelGroup(businessModel.getModelName(), updateBusinessModelVO.getGroup()) < 0) {
                throw new RuntimeException();
            }

            if (businessModelMetadataMapper.deleteByModelName(businessModel.getModelName()) < 0) {
                throw new RuntimeException();
            }


            if (dpPortalModelFilterMapper.deleteById(businessModel.getModelName()) < 0) {
                throw new RuntimeException();
            }


            if (modelFilter != null && modelFilter.size() > 0) {

                for (ModelFilterVO modelFilterVO : modelFilter) {
                    modelFilterVO.setModelId(businessModel.getModelName());
                }

                if (dpPortalModelFilterMapper.insertModelFilter(modelFilter) < 0) {
                    throw new RuntimeException();
                }

            }


            if (!StringUtils.isEmpty(list)) {
                if (businessModelMetadataMapper.insertBusinessModelMetaData(businessModelMetadatas) < 0) {
                    throw new RuntimeException();
                }
            }

            DpPortalBusinessModelQuerysql dpPortalBusinessModelQuerysql = new DpPortalBusinessModelQuerysql(businessModel.getModelName(), businessModel.getQuerySql());

            if (dpPortalBusinessModelQuerysqlDao.replaceIntoItem(dpPortalBusinessModelQuerysql) < 0) {
                throw new RuntimeException();
            }

            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);

        }
    }

    @Override
    public PageInfo<BusinessModelDTO> selectAll(String userId, String modelName, String groupName, String basicModelName, Integer curPage, Integer pageSize) {
        PageHelper.startPage(curPage, pageSize);

        List<BusinessModelDTO> list = businessModelMapper.selectBusinessModel(userId, groupName, basicModelName, StringUtils.isEmpty(modelName) ? null : "%" + modelName + "%");

        List<String> list1 = new ArrayList<>();

        for (BusinessModelDTO o : list
        ) {
            list1.add(o.getModelName());
        }

        if (list1.size() > 0) {
            List<SelectBusinessModelNameAndColumnCountDTO> businessModelDTOS = businessModelMapper.selectModelMetaDatas(list1);

            for (BusinessModelDTO o : list
            ) {
                if (businessModelDTOS.size() > 0) {
                    for (SelectBusinessModelNameAndColumnCountDTO s : businessModelDTOS
                    ) {
                        if (o.getModelName().equals(s.getModelName())) {
                            Integer i = s.getBusinessModelMetaDataDTOS().size();
                            o.setColumnCount(i.toString());
                            o.setBusinessModelMetaDataDTOS(s.getBusinessModelMetaDataDTOS());
                            continue;
                        }
                    }
                }
                o.setEnabled(TrueFalse.T.code().equals(o.getEnabled()) ? TrueFalse.T.getDesc() : TrueFalse.F.getDesc());
            }

            List<ModelNameAndModelFilterDTO> modelNameAndModelFilterDTOS = dpPortalModelFilterMapper.selectMdelFilters(list1);

            for (BusinessModelDTO o : list
            ) {
                for (ModelNameAndModelFilterDTO s : modelNameAndModelFilterDTOS
                ) {
                    if (o.getModelName().equals(s.getModelName())) {
                        o.setModelFilterVOS(s.getModelFilterVOS());
                        continue;
                    }
                }
            }


        } else {
            for (BusinessModelDTO o : list
            ) {
                o.setColumnCount("0");
                o.setEnabled(TrueFalse.T.code().equals(o.getEnabled()) ? TrueFalse.T.getDesc() : TrueFalse.F.getDesc());
            }
        }


        PageInfo<BusinessModelDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<BasicModelGroupDTO> getBasicModelGroup(String userId) {

        return businessModelMapper.getBasicModelGroup(userId, GroupTypeEnum.JBMX.code());
    }

    @Override
    public List<BasicModelNameAndCnameDTO> getBasicModel(String userId, String groupName) {

        return businessModelMapper.getBasicModel(userId,
                org.apache.commons.lang.StringUtils.isEmpty(groupName) ? null : groupName,
                GroupTypeEnum.JBMX.code(), TrueFalse.T.code());
    }

    @Override
    public List<BasicModelAndMetaDataDTO> getBasicModelColumns(String userId, String basicModel) {
        return businessModelMetadataMapper.selectBasicModelColumns(userId, basicModel);
    }

    @Override
    public List<GroupDTO> getAllGroup(String userId) {
        return groupMapper.selectAllGroup(userId, GroupTypeEnum.YWMX.code());
    }

    @Override
    public List<BatchDownloadBusinessModelDTO> batchDownloadExcel(String userId, List<String> modelNames) {
        List<BatchDownloadBusinessModelDTO> list = businessModelMapper.batchDownloadExcel(userId, modelNames);
        return list;
    }


    @Override
    public Result<Boolean, Object> queryBusinessModelExists(String userId, String businessModelName, String businessModelGroup) {
        Integer flag = businessModelMapper.queryBusinessModelExists(userId, businessModelName, businessModelGroup);
        if (flag > 0) {
            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);
        }
    }

    @Override
    public ModelDTO selectModelAndMetaData(String userId, String modelName) {
        return businessModelMapper.selectModelAndMetaData(userId, modelName);
    }

    @Override
    public Result<List<Map<String, Object>>, Object> testPriview(String userId, BusinessPreviewDataVO businessPreviewDataVO) {

        String basicModelId = businessPreviewDataVO.getBasicModelId();
        DatasourceAndTableNameDTO datasource = businessModelMapper.selectByBasicModelId(basicModelId);

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


        List<ModelFilterVO> modelFilterVOList = dpPortalModelFilterMapper.queryByModelName(basicModelId);

        List<ModelFilterVO> modelFilterVOS = businessPreviewDataVO.getModelFilterVOS();

        modelFilterVOS.addAll(modelFilterVOList);

        List<BasicModelMetaDataVO> modelMetaDataVOS = businessPreviewDataVO.getModelMetaDataVOS();
        //        StringBuffer stringBuffer = new StringBuffer("select ");
//        for (BasicModelMetaDataVO basicModelMetaDataVO : modelMetaDataVOS) {
//            stringBuffer.append(basicModelMetaDataVO.getColumnName() + " as " + basicModelMetaDataVO.getColumnChineseName() + ",");
//        }
//
//        stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
//
//        stringBuffer.append(" from " + datasource.getTableName());

        SqlJoin datasourceEntity = SqlFactory.getDatasourceEntity(datasource.getDsType());
        String tableName = datasource.getTableSchema() + "." + datasource.getTableName();

        StringBuffer stringBuffer = datasourceEntity.getStringBuffer(modelMetaDataVOS, modelFilterVOS, tableName, true);

//        joinQuerySql(modelFilterVOList, modelFilterVOS, stringBuffer);


        try {
            List<Map<String, Object>> list = JdbcUtils.querydatasourceIdSql(stringBuffer.toString(), database);
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), Boolean.FALSE, "筛选条件异常");
        }
    }

    private void joinQuerySql(List<ModelFilterVO> modelFilterVOList, List<ModelFilterVO> modelFilterVOS, StringBuffer stringBuffer) {
        if (!StringUtils.isEmpty(modelFilterVOList) && modelFilterVOList.size() > 0) {
            stringBuffer.append(" where ");
            for (ModelFilterVO modelFilterVO : modelFilterVOList) {
                if (!StringUtils.isEmpty(modelFilterVO.getRelation())) {
                    stringBuffer.append(" " + modelFilterVO.getRelation() + " ");
                }
                JdbcUtils.appendSql(stringBuffer, modelFilterVO);
            }

            if (modelFilterVOS.size() > 0) {
                stringBuffer.append(" and ");
                for (ModelFilterVO modelFilterVO : modelFilterVOS) {
                    JdbcUtils.appendSql(stringBuffer, modelFilterVO);
                }
            }

        } else {
            if (modelFilterVOS.size() > 0) {
                stringBuffer.append(" where ");
                for (ModelFilterVO modelFilterVO : modelFilterVOS) {
                    JdbcUtils.appendSql(stringBuffer, modelFilterVO);
                }
            }
        }
    }
}
