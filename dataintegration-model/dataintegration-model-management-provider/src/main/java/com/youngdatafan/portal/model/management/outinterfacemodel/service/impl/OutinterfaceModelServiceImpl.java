package com.youngdatafan.portal.model.management.outinterfacemodel.service.impl;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.basicmodel.dto.ModelNameAndModelFilterDTO;
import com.youngdatafan.portal.model.management.basicmodel.entity.BasiceModel;
import com.youngdatafan.portal.model.management.basicmodel.mapper.BasiceModelMapper;
import com.youngdatafan.portal.model.management.basicmodel.mapper.DpPortalModelFilterMapper;
import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import com.youngdatafan.portal.model.management.common.enums.GroupTypeEnum;
import com.youngdatafan.portal.model.management.datasource.mapper.DatasourceMapper;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.*;
import com.youngdatafan.portal.model.management.outinterfacemodel.entity.DpPortalOutinterfaceModelQuerysql;
import com.youngdatafan.portal.model.management.outinterfacemodel.entity.OutinterfaceModel;
import com.youngdatafan.portal.model.management.outinterfacemodel.entity.OutinterfaceModelMetadata;
import com.youngdatafan.portal.model.management.outinterfacemodel.mapper.DpPortalOutinterfaceModelQuerysqlDao;
import com.youngdatafan.portal.model.management.outinterfacemodel.mapper.OutinterfaceGroupMapper;
import com.youngdatafan.portal.model.management.outinterfacemodel.mapper.OutinterfaceModelMapper;
import com.youngdatafan.portal.model.management.outinterfacemodel.mapper.OutinterfaceModelMetadataMapper;
import com.youngdatafan.portal.model.management.outinterfacemodel.service.OutinterfaceModelService;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.AddOutinterfaceModelVO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.OutinterfaceBasicModelMetaDataCopyDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.OutinterfacePreviewDataVO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.UpdateOutinterfaceModelVO;
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
public class OutinterfaceModelServiceImpl implements OutinterfaceModelService {


    private static final Pattern URL_PATTERN = Pattern.compile("([\\w.]+):(\\d+)[:/]?([\\w.]+)?");

    private static final Pattern TD_URL_PATTERN = Pattern.compile("([\\w.]+)/DATABASE=(\\w+).*DBS_PORT=(\\d+)");

    private static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    private final static String YWMX = "YWMX";

    private final static String ZDYMX = "ZDYMX";

    private final static String SCLMX = "SCLMX";

    @Resource
    OutinterfaceModelMapper outinterfaceModelMapper;

    @Resource
    OutinterfaceModelMetadataMapper outinterfaceModelMetadataMapper;

    @Resource
    OutinterfaceGroupMapper outinterfaceGroupMapper;

    @Resource
    BasiceModelMapper basiceModelMapper;

    @Resource
    DatasourceMapper datasourceMapper;

    @Resource
    DpPortalModelFilterMapper dpPortalModelFilterMapper;

    @Resource
    com.youngdatafan.portal.model.management.util.factory.SqlFactory SqlFactory;

    @Resource
    DpPortalOutinterfaceModelQuerysqlDao dpPortalOutinterfaceModelQuerysqlDao;

    @Override
    public List<OutinterfaceModelTypeAndGroupListDTO> selectModelAndGroupList(String userId) {
        List<String> list = new ArrayList<>();
        list.add(GroupTypeEnum.YWMX.code());
        list.add(GroupTypeEnum.ZDYMX.code());
        list.add(GroupTypeEnum.SCLMX.code());

        List<OutinterfaceModelAndGroupListDTO> listDTOS = outinterfaceModelMapper.selectModelAndGroupList(userId);

        List<OutinterfaceModelAndGroupListDTO> outinterfaceModelAndGroupListDTOList = outinterfaceModelMapper.selectCustomModelAndGroupList(userId);

        List<OutinterfaceModelAndGroupListDTO> outModelAndGroupList = outinterfaceModelMapper.selectOutModelAndGroupList(userId);

        listDTOS.addAll(outModelAndGroupList);

        listDTOS.addAll(outinterfaceModelAndGroupListDTOList);

        List<OutinterfaceModelTypeAndGroupListDTO> modelTypeAndGroupListDTOS = new ArrayList<>();

        for (String type : list) {

            OutinterfaceModelTypeAndGroupListDTO modelTypeAndGroupListDTO = new OutinterfaceModelTypeAndGroupListDTO();

            modelTypeAndGroupListDTO.setGroupTypeName(type);

            List<OutinterfaceModelAndGroupListDTO> outinterfaceModelAndGroupListDTOS = new ArrayList<>();

            for (OutinterfaceModelAndGroupListDTO outinterfaceModelAndGroupListDTO : listDTOS) {

                if (!StringUtils.isEmpty(outinterfaceModelAndGroupListDTO)) {
                    OutinterfaceGroupDTO groupDTO = outinterfaceModelAndGroupListDTO.getGroupDTO();

                    if (type.equals(groupDTO.getGroupType())) {

                        outinterfaceModelAndGroupListDTOS.add(outinterfaceModelAndGroupListDTO);
                    }
                }
            }
            modelTypeAndGroupListDTO.setOutinterfaceModelAndGroupListDTOS(outinterfaceModelAndGroupListDTOS);
            modelTypeAndGroupListDTOS.add(modelTypeAndGroupListDTO);
        }

        return modelTypeAndGroupListDTOS;
    }

    @Override
    public OutinterfaceModelDTO selectModelByModelName(String userId, String modelName) {
        return outinterfaceModelMapper.selectModelByModelName(userId, modelName);
    }

    @Override
    public Result<OutinterfaceModelDatasourceDTO, Object> selectDatasource(String userId, String outinterfaceModelName) {
        return Result.success(outinterfaceModelMapper.selecDatasourceByModelName(outinterfaceModelName));
    }

    @Override
    public Result<List<OutinterfaceModelMetaDataDTO>, Object> selectOutinterfaceModelMetaData(String userId, String outinterfaceModelName) {
        return Result.success(outinterfaceModelMapper.selectOutinterfaceMetaData(outinterfaceModelName));
    }

    @Override
    public Result<OutinterfaceModelDatasourceDTO, Object> selectDatasourceByModelName(String userId, String outinterfaceModelName, String modeType) {
        OutinterfaceModelDatasourceDTO modelDatasourceDTO = null;

        switch (modeType) {
            case YWMX:
                modelDatasourceDTO = outinterfaceModelMapper.selecDatasourceByModelName(outinterfaceModelName);
                break;
            case ZDYMX:
                modelDatasourceDTO = outinterfaceModelMapper.selecDatasourceByZDYModelName(outinterfaceModelName);
                break;
            case SCLMX:
                modelDatasourceDTO = outinterfaceModelMapper.selecDatasourceBySCLModelName(outinterfaceModelName);
                break;
            default:
                break;
        }
        return Result.success(modelDatasourceDTO);
    }


    @Override
    public Result<Boolean, Object> insert(String userId, AddOutinterfaceModelVO addOutinterfaceModelVO) {

        BasiceModel basiceModel = basiceModelMapper.selectByPrimaryKey(addOutinterfaceModelVO.getBasicModelTableNameValue(), userId);

        if (StringUtils.isEmpty(basiceModel)) {
            throw new RuntimeException("此基础模型不存在");
        }

        OutinterfaceModel outinterfaceModel = new OutinterfaceModel();
        outinterfaceModel.setModelName(UUID.randomUUID().toString());
        outinterfaceModel.setCreateTime(new Date());
        outinterfaceModel.setCreateUserId(userId);
        outinterfaceModel.setDescription(addOutinterfaceModelVO.getDescription());
        outinterfaceModel.setBasicModelName(addOutinterfaceModelVO.getBasicModelTableNameValue());
        outinterfaceModel.setEnabled(addOutinterfaceModelVO.getEnabled());
        outinterfaceModel.setChineseName(addOutinterfaceModelVO.getName());
        outinterfaceModel.setUpdateTime(new Date());
        outinterfaceModel.setEnabled(TrueFalse.T.getDesc().equals(addOutinterfaceModelVO.getEnabled()) ? TrueFalse.T.code() : TrueFalse.F.code());
        outinterfaceModel.setCreateUserId(userId);

        outinterfaceModel.setModelSort(addOutinterfaceModelVO.getSortNum());

        StringBuffer stringBuffer = new StringBuffer("select ");

        List<OutinterfaceBasicModelMetaDataCopyDTO> list = addOutinterfaceModelVO.getBasicModelMetaDataCopyDTOS();

        List<OutinterfaceModelMetadata> outinterfaceModelMetadatas = new ArrayList<>();

        List<ModelFilterVO> modelFilterVOS = addOutinterfaceModelVO.getModelFilterVOS();

        if (!StringUtils.isEmpty(list)) {

            for (OutinterfaceBasicModelMetaDataCopyDTO s : list
            ) {
                stringBuffer = stringBuffer.append(s.getColumnName() + ", ");

                OutinterfaceModelMetadata outinterfaceModelMetadata = new OutinterfaceModelMetadata();

                outinterfaceModelMetadata.setOutinterfaceModelName(outinterfaceModel.getModelName());

                outinterfaceModelMetadata.setColumnName(s.getColumnName());

                outinterfaceModelMetadata.setCustomColumnName(StringUtils.isEmpty(s.getCName()) ? null : s.getCName());

                outinterfaceModelMetadata.setColumnSerial(s.getModelDataSort());

                outinterfaceModelMetadatas.add(outinterfaceModelMetadata);
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 2);

//            stringBuffer.append(" from " + basiceModel.getTableName());

            stringBuffer.append(" from ( ? )");

            List<ModelFilterVO> modelFilterVOList = dpPortalModelFilterMapper.queryByModelName(addOutinterfaceModelVO.getBasicModelTableNameValue());

            modelFilterVOS.addAll(modelFilterVOList);

            joinQuerySql(modelFilterVOList, modelFilterVOS, stringBuffer);

            String querySql = stringBuffer.toString();


            outinterfaceModel.setQuerySql(querySql);
        }


        if (outinterfaceModelMapper.insert(outinterfaceModel) > 0) {

            if (modelFilterVOS.size() > 0) {

                for (ModelFilterVO modelFilterVO : modelFilterVOS) {
                    modelFilterVO.setModelId(outinterfaceModel.getModelName());
                }

                if (dpPortalModelFilterMapper.insertModelFilter(modelFilterVOS) < 0) {
                    throw new RuntimeException();
                }
            }

            if (outinterfaceModelMapper.insertModelGroup(outinterfaceModel.getModelName(), addOutinterfaceModelVO.getGroup()) < 0) {
                throw new RuntimeException();
            }

            if (!StringUtils.isEmpty(list)) {

                if (outinterfaceModelMetadatas.size() > 0) {
                    if (outinterfaceModelMetadataMapper.insertOutinterfaceModelMetaData(outinterfaceModelMetadatas) < 0) {
                        throw new RuntimeException();
                    }
                }
            }

            DpPortalOutinterfaceModelQuerysql dpPortalOutinterfaceModelQuerysql = new DpPortalOutinterfaceModelQuerysql(outinterfaceModel.getModelName(), outinterfaceModel.getQuerySql());

            if (dpPortalOutinterfaceModelQuerysqlDao.insert(dpPortalOutinterfaceModelQuerysql) < 0) {
                throw new RuntimeException();
            }

            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);
        }

    }

    @Override
    public Result<Boolean, Object> delete(String userId, String modelName) {

        if (outinterfaceModelMapper.deleteByPrimaryKey(userId, modelName) > 0) {

            if (outinterfaceModelMetadataMapper.deleteByModelName(modelName) < 0) {
                throw new RuntimeException();
            }

            if (outinterfaceModelMapper.deleteModelGroup(modelName) < 0) {
                throw new RuntimeException();
            }

            if (dpPortalModelFilterMapper.deleteById(modelName) < 0) {
                throw new RuntimeException();
            }

            if (dpPortalOutinterfaceModelQuerysqlDao.deleteById(modelName) < 0) {
                throw new RuntimeException();
            }

        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean, Object> update(String userId, UpdateOutinterfaceModelVO updateOutinterfaceModelVO) {

        BasiceModel basiceModel = basiceModelMapper.selectByPrimaryKey(updateOutinterfaceModelVO.getBasicModelTableNameValue(), userId);

        if (StringUtils.isEmpty(basiceModel)) {
            throw new RuntimeException("此基础模型不存在");
        }

        OutinterfaceModel outinterfaceModel = new OutinterfaceModel();
        outinterfaceModel.setModelName(updateOutinterfaceModelVO.getModelName());
        outinterfaceModel.setCreateUserId(userId);
        outinterfaceModel.setDescription(updateOutinterfaceModelVO.getDescription());
        outinterfaceModel.setBasicModelName(updateOutinterfaceModelVO.getBasicModelTableNameValue());
        outinterfaceModel.setEnabled(updateOutinterfaceModelVO.getEnabled());
        outinterfaceModel.setChineseName(updateOutinterfaceModelVO.getName());
        outinterfaceModel.setUpdateTime(new Date());
        outinterfaceModel.setEnabled(TrueFalse.T.getDesc().equals(updateOutinterfaceModelVO.getEnabled()) ? TrueFalse.T.code() : TrueFalse.F.code());

        outinterfaceModel.setCreateUserId(userId);
        outinterfaceModel.setModelSort(updateOutinterfaceModelVO.getSortNum());

        List<OutinterfaceModelMetadata> outinterfaceModelMetadatas = new ArrayList<>();

        List<OutinterfaceBasicModelMetaDataCopyDTO> list = updateOutinterfaceModelVO.getBasicModelMetaDataCopyDTOS();

        List<ModelFilterVO> modelFilter = updateOutinterfaceModelVO.getModelFilterVOS();

        StringBuffer stringBuffer = new StringBuffer("select ");

        if (!StringUtils.isEmpty(list)) {

            for (OutinterfaceBasicModelMetaDataCopyDTO s : list
            ) {
                OutinterfaceModelMetadata outinterfaceModelMetadata = new OutinterfaceModelMetadata();

                stringBuffer = stringBuffer.append(s.getColumnName() + ", ");

                outinterfaceModelMetadata.setOutinterfaceModelName(outinterfaceModel.getModelName());

                outinterfaceModelMetadata.setColumnName(s.getColumnName());

                outinterfaceModelMetadata.setColumnSerial(s.getModelDataSort());

                outinterfaceModelMetadata.setCustomColumnName(StringUtils.isEmpty(s.getOutinterfaceModelName()) ? null : s.getOutinterfaceModelName());

                outinterfaceModelMetadatas.add(outinterfaceModelMetadata);
            }


            stringBuffer.deleteCharAt(stringBuffer.length() - 2);

            stringBuffer.append(" from ( ? )");

            List<ModelFilterVO> modelFilterVOList = dpPortalModelFilterMapper.queryByModelName(updateOutinterfaceModelVO.getBasicModelTableNameValue());

            joinQuerySql(modelFilterVOList, modelFilter, stringBuffer);

            String querySql = stringBuffer.toString();

            outinterfaceModel.setQuerySql(querySql);
        }


        if (outinterfaceModelMapper.updateByPrimaryKeySelective(outinterfaceModel) >= 0) {

            if (outinterfaceModelMapper.deleteModelGroup(outinterfaceModel.getModelName()) < 0) {
                throw new RuntimeException();
            }

            if (outinterfaceModelMapper.insertModelGroup(outinterfaceModel.getModelName(), updateOutinterfaceModelVO.getGroup()) < 0) {
                throw new RuntimeException();
            }

            if (outinterfaceModelMetadataMapper.deleteByModelName(outinterfaceModel.getModelName()) < 0) {
                throw new RuntimeException();
            }


            if (dpPortalModelFilterMapper.deleteById(outinterfaceModel.getModelName()) < 0) {
                throw new RuntimeException();
            }


            if (modelFilter != null && modelFilter.size() > 0) {

                for (ModelFilterVO modelFilterVO : modelFilter) {
                    modelFilterVO.setModelId(outinterfaceModel.getModelName());
                }

                if (dpPortalModelFilterMapper.insertModelFilter(modelFilter) < 0) {
                    throw new RuntimeException();
                }

            }


            if (!StringUtils.isEmpty(list)) {
                if (outinterfaceModelMetadataMapper.insertOutinterfaceModelMetaData(outinterfaceModelMetadatas) < 0) {
                    throw new RuntimeException();
                }
            }

            DpPortalOutinterfaceModelQuerysql dpPortalOutinterfaceModelQuerysql = new DpPortalOutinterfaceModelQuerysql(outinterfaceModel.getModelName(), outinterfaceModel.getQuerySql());

            if (dpPortalOutinterfaceModelQuerysqlDao.replaceIntoItem(dpPortalOutinterfaceModelQuerysql) < 0) {
                throw new RuntimeException();
            }

            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);

        }
    }

    @Override
    public PageInfo<OutinterfaceModelDTO> selectAll(String userId, String modelName, String groupName, String basicModelName, Integer curPage, Integer pageSize) {
        PageHelper.startPage(curPage, pageSize);

        List<OutinterfaceModelDTO> list = outinterfaceModelMapper.selectOutinterfaceModel(userId, groupName, basicModelName, StringUtils.isEmpty(modelName) ? null : "%" + modelName + "%");

        List<String> list1 = new ArrayList<>();

        for (OutinterfaceModelDTO o : list
        ) {
            list1.add(o.getModelName());
        }

        if (list1.size() > 0) {
            List<SelectOutinterfaceModelNameAndColumnCountDTO> outinterfaceModelDTOS = outinterfaceModelMapper.selectModelMetaDatas(list1);

            for (OutinterfaceModelDTO o : list
            ) {
                if (outinterfaceModelDTOS.size() > 0) {
                    for (SelectOutinterfaceModelNameAndColumnCountDTO s : outinterfaceModelDTOS
                    ) {
                        if (o.getModelName().equals(s.getModelName())) {
                            Integer i = s.getOutinterfaceModelMetaDataDTOS().size();
                            o.setColumnCount(i.toString());
                            o.setOutinterfaceModelMetaDataDTOS(s.getOutinterfaceModelMetaDataDTOS());
                            continue;
                        }
                    }
                }
                o.setEnabled(TrueFalse.T.code().equals(o.getEnabled()) ? TrueFalse.T.getDesc() : TrueFalse.F.getDesc());
            }

            List<ModelNameAndModelFilterDTO> modelNameAndModelFilterDTOS = dpPortalModelFilterMapper.selectMdelFilters(list1);

            for (OutinterfaceModelDTO o : list
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
            for (OutinterfaceModelDTO o : list
            ) {
                o.setColumnCount("0");
                o.setEnabled(TrueFalse.T.code().equals(o.getEnabled()) ? TrueFalse.T.getDesc() : TrueFalse.F.getDesc());
            }
        }


        PageInfo<OutinterfaceModelDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<OutinterfaceModelGroupDTO> getBasicModelGroup(String userId) {

        return outinterfaceModelMapper.getBasicModelGroup(userId, GroupTypeEnum.JBMX.code());
    }

    @Override
    public List<OutinterfaceModelNameAndCnameDTO> getBasicModel(String userId, String groupName) {

        return outinterfaceModelMapper.getBasicModel(userId,
                org.apache.commons.lang.StringUtils.isEmpty(groupName) ? null : groupName,
                GroupTypeEnum.JBMX.code(), TrueFalse.T.code());
    }

    @Override
    public List<OutinterfaceBasicModelAndMetaDataDTO> getBasicModelColumns(String userId, String basicModel) {
        return outinterfaceModelMetadataMapper.selectBasicModelColumns(userId, basicModel);
    }

    @Override
    public List<OutinterfaceGroupDTO> getAllGroup(String userId) {
        return outinterfaceGroupMapper.selectAllGroup(userId, GroupTypeEnum.SCJK.code());
    }

    @Override
    public List<BatchDownloadOutinterfaceModelDTO> batchDownloadExcel(String userId, List<String> modelNames) {
        List<BatchDownloadOutinterfaceModelDTO> list = outinterfaceModelMapper.batchDownloadExcel(userId, modelNames);
        return list;
    }


    @Override
    public Result<Boolean, Object> queryOutinterfaceModelExists(String userId, String outinterfaceModelName, String outinterfaceModelGroup) {
        Integer flag = outinterfaceModelMapper.queryOutinterfaceModelExists(userId, outinterfaceModelName, outinterfaceModelGroup);
        if (flag > 0) {
            return Result.success(Boolean.TRUE);
        } else {
            return Result.success(Boolean.FALSE);
        }
    }

    @Override
    public ModelDTO selectModelAndMetaData(String userId, String modelName) {
        return outinterfaceModelMapper.selectModelAndMetaData(userId, modelName);
    }

    @Override
    public Result<List<Map<String, Object>>, Object> testPriview(String userId, OutinterfacePreviewDataVO outinterfacePreviewDataVO) {

        String basicModelId = outinterfacePreviewDataVO.getBasicModelId();
        OutinterfaceDatasourceAndTableNameDTO datasource = outinterfaceModelMapper.selectByBasicModelId(basicModelId);

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

        List<ModelFilterVO> modelFilterVOS = outinterfacePreviewDataVO.getModelFilterVOS();

        modelFilterVOS.addAll(modelFilterVOList);

        List<BasicModelMetaDataVO> modelMetaDataVOS = outinterfacePreviewDataVO.getModelMetaDataVOS();
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
