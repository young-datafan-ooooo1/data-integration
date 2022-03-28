package com.youngdatafan.portal.model.management.outmodel.service.impl;

import com.youngdatafan.portal.model.management.businessmodel.mapper.GroupMapper;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.common.enums.GroupTypeEnum;
import com.youngdatafan.portal.model.management.outmodel.dto.*;
import com.youngdatafan.portal.model.management.outmodel.entity.DpPortalClearTableRecord;
import com.youngdatafan.portal.model.management.outmodel.entity.OutModel;
import com.youngdatafan.portal.model.management.outmodel.mapper.DpPortalClearTableRecordDao;
import com.youngdatafan.portal.model.management.outmodel.mapper.OutModelMapper;
import com.youngdatafan.portal.model.management.outmodel.mapper.OutModelMetaDataMapper;
import com.youngdatafan.portal.model.management.outmodel.service.OutModelService;
import com.youngdatafan.portal.model.management.outmodel.vo.AddOutModelVO;
import com.youngdatafan.portal.model.management.outmodel.vo.OutModelMetaDataVO;
import com.youngdatafan.portal.model.management.outmodel.vo.UpdateOutModelVO;
import com.youngdatafan.portal.model.management.util.enums.ModelTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : 模型授权组s接口实现类</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/12 11:08 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class OutModelServiceImpl implements OutModelService {

    public static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    OutModelMapper outModelMapper;

    OutModelMetaDataMapper outModelMetaDataMapper;

    GroupMapper groupMapper;

    DpPortalClearTableRecordDao dpPortalClearTableRecordDao;

    @Autowired
    public OutModelServiceImpl(GroupMapper groupMapper,
                               OutModelMetaDataMapper outModelMetaDataMapper,
                               OutModelMapper outModelMapper,
                               DpPortalClearTableRecordDao dpPortalClearTableRecordDao) {
        this.groupMapper = groupMapper;
        this.outModelMapper = outModelMapper;
        this.outModelMetaDataMapper = outModelMetaDataMapper;
        this.dpPortalClearTableRecordDao = dpPortalClearTableRecordDao;
    }

    @Override
    public Boolean delete(String userId, List<String> outModelId) {

        List<OutModel> outModels = outModelMapper.selectItems(outModelId);

        if (outModelMapper.batchDeletePrimaryKey(outModelId, userId) >= 0) {
            int i = outModelMapper.deleteModelGroupByModelNames(outModelId);
            if (i < 0) {
                throw new RuntimeException();
            }
            if (outModelMetaDataMapper.deleteOutModelMetaDataByModelName(outModelId) < 0) {
                throw new RuntimeException();
            } else {
                List<DpPortalClearTableRecord> list = new ArrayList<>();
                for (OutModel outModel : outModels) {
                    DpPortalClearTableRecord dpPortalClearTableRecord = new DpPortalClearTableRecord();
                    dpPortalClearTableRecord.setCreateTime(new Date());
                    dpPortalClearTableRecord.setCreateUserId(userId);
                    dpPortalClearTableRecord.setModelId(outModel.getModelId());
                    dpPortalClearTableRecord.setDatasourceId(outModel.getDatasourceId());
                    dpPortalClearTableRecord.setTableName(outModel.getTableName());
                    dpPortalClearTableRecord.setModelType(ModelTypeEnum.OUTMODEL.code());

                    list.add(dpPortalClearTableRecord);
                }

                if (dpPortalClearTableRecordDao.batchInsert(list) < 0) {
                    throw new RuntimeException();
                }
            }
        } else {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public String add(String userId, AddOutModelVO addOutModelVO) {
        OutModel outModel = new OutModel();
        BeanUtils.copyProperties(addOutModelVO, outModel);
        outModel.setModelId(UUID.randomUUID().toString());
        outModel.setCreateUserId(userId);
        Date date = new Date();
        outModel.setCreateTime(date);
        outModel.setUpdateTime(date);

        outModel.setProjectName(addOutModelVO.getProjectId());


        outModel.setQuerySql(addOutModelVO.getQuerySql());

        List<OutModelMetaDataVO> outModelMetaDataVOS = addOutModelVO.getOutModelMetaDataVOS();

        if (outModelMapper.insertSelective(outModel) > 0) {
            if (!org.apache.commons.lang.StringUtils.isEmpty(addOutModelVO.getGroupId())) {
                if (outModelMapper.insertModelGroup(outModel.getModelId(), addOutModelVO.getGroupId()) < 0) {
                    throw new RuntimeException("新增失败");
                }
            }

            if (!StringUtils.isEmpty(outModelMetaDataVOS)) {
                for (OutModelMetaDataVO outModelMetaDataVO : outModelMetaDataVOS
                ) {
                    outModelMetaDataVO.setModelName(outModel.getModelId());
                }
                if (outModelMetaDataMapper.batchInsertOutModelMeta(outModelMetaDataVOS) < 0) {
                    throw new RuntimeException("新增失败");
                }
            }
            return outModel.getModelId();
        } else {
            throw new RuntimeException("新增失败");
        }
    }

    @Override
    public List<OutModelGroupDTO> getGroups(String userId) {
        List<OutModelGroupDTO> list = outModelMapper.selectGroupIdByGroupTypeAndUserId(userId, GroupTypeEnum.SCLMX.code());
        return list;
    }

    @Override
    public OutModelAndMetaDataWithPageDTO selectAllOutModel(String userId, Integer curPage, Integer pageSize,
                                                            String groupId, String projectId, String modelName, String enabled) {

        PageHelper.startPage(curPage, pageSize);
        List<OutModelAndMetaDataDTO> list = outModelMapper.selectAllGroupByUserIdAndProjectIdAndGroupID(userId, groupId, projectId,
                StringUtils.isEmpty(modelName) ? null : "%" + modelName + "%", enabled);

        List<String> modelNames = new ArrayList<>();

        for (OutModelAndMetaDataDTO outModelAndMetaDataDTO : list) {
            if (outModelAndMetaDataDTO.getQuerySql() != "") {
                outModelAndMetaDataDTO.setQuerySql(PASSWORD_ENCODER.encode(outModelAndMetaDataDTO.getQuerySql(), false));
            }
            modelNames.add(outModelAndMetaDataDTO.getModelId());
        }

        if (modelNames.size() > 0) {

            List<ModelIdAndModelMetaDataDTO> modelIdAndModelMetaDataDTOS = outModelMetaDataMapper.selectModelMeataDataList(modelNames);
            System.out.println(modelIdAndModelMetaDataDTOS.size());
            for (OutModelAndMetaDataDTO outModelAndMetaDataDTO : list) {

                for (ModelIdAndModelMetaDataDTO m : modelIdAndModelMetaDataDTOS) {
                    if (outModelAndMetaDataDTO.getModelId().equals(m.getModelId())) {
                        List<OutModelMetaDataDTO> list1 = m.getList();
                        outModelAndMetaDataDTO.setColumnSize(list1.size());
                        outModelAndMetaDataDTO.setOutModelMetaDataDTOS(list1);
                    }
                }
            }

        }
        PageInfo<OutModelAndMetaDataDTO> pageInfo = new PageInfo<>(list);

        OutModelAndMetaDataWithPageDTO o = new OutModelAndMetaDataWithPageDTO();
        o.setTotal(pageInfo.getTotal());
        o.setOutModelAndMetaData(pageInfo.getList());
        return o;
    }

    @Override
    public List<OutModelProjectIdAndNamesDTO> getAllProject(String userId) {
        return outModelMetaDataMapper.selectProjects(userId);
    }

    @Override
    public OutModelAndMetaDataListDTO selectOutModelAndMetaData(String userId, String modelName) {
        return outModelMapper.selectOutModelAndMetaData(userId, modelName);
    }

    @Override
    public ModelDTO selectModel(String userId, String modelName) {
        return outModelMapper.selectModel(userId, modelName);
    }

    @Override
    public Boolean updateAll(String userId, UpdateOutModelVO updateOutModelVO) {
        updateOutModelVO.setCreateUserId(userId);
        updateOutModelVO.setUpdateTime(new Date());
        int i = outModelMapper.updateAllByPrimaryKey(updateOutModelVO);

        if (i >= 0) {
            deleteModelGroupFunction(updateOutModelVO);

            List<OutModelMetaDataVO> outModelMetaDataVOS = updateOutModelVO.getOutModelMetaDataVOS();

            batchInsertOutModelMetaFunction(updateOutModelVO, outModelMetaDataVOS);

        } else {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    @Override
    public Boolean updateSelective(String userId, UpdateOutModelVO updateOutModelVO) {
        updateOutModelVO.setCreateUserId(userId);
        updateOutModelVO.setUpdateTime(new Date());
        int i = outModelMapper.updateSelective(updateOutModelVO);

        if (i >= 0) {
            if (!StringUtils.isEmpty(updateOutModelVO.getModelName())) {
                deleteModelGroupFunction(updateOutModelVO);
            }
            List<OutModelMetaDataVO> outModelMetaDataVOS = updateOutModelVO.getOutModelMetaDataVOS();

            if (!StringUtils.isEmpty(outModelMetaDataVOS)) {
                batchInsertOutModelMetaFunction(updateOutModelVO, outModelMetaDataVOS);
            }
        } else {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void batchInsertOutModelMetaFunction(UpdateOutModelVO updateOutModelVO, List<OutModelMetaDataVO> outModelMetaDataVOS) {

        if (outModelMetaDataMapper.deleteOutModelMetaDataByOne(updateOutModelVO.getModelId()) >= 0) {
            if (!StringUtils.isEmpty(outModelMetaDataVOS)) {
                for (OutModelMetaDataVO outModelMetaDataVO : outModelMetaDataVOS
                ) {
                    outModelMetaDataVO.setModelName(updateOutModelVO.getModelId());
                }
                if (outModelMetaDataMapper.batchInsertOutModelMeta(outModelMetaDataVOS) < 0) {
                    throw new RuntimeException("新增失败");
                }
            }
        } else {
            throw new RuntimeException("修改失败");
        }
    }


    private void deleteModelGroupFunction(UpdateOutModelVO updateOutModelVO) {
        if (outModelMapper.deleteModelGroup(updateOutModelVO.getModelName()) >= 0) {
            if (outModelMapper.insertModelGroup(updateOutModelVO.getModelId(), updateOutModelVO.getGroupId()) < 0) {
                throw new RuntimeException("修改失败");
            }
        } else {
            throw new RuntimeException("修改失败");
        }
    }

    @Override
    public Boolean outModelIsExist(String userId, String modelId) {
        int i = outModelMapper.selectModelByModelId(userId, modelId);

        return i > 0;
    }
}
