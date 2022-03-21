package com.youngdatafan.portal.model.management.modelgrant.service.impl;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.businessmodel.mapper.BusinessModelMapper;
import com.youngdatafan.portal.model.management.modelgrant.dto.BusinessModelListDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.GroupIdBusinessModelListDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelGrantGroupDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelNameAndGroupNameAndTypes;
import com.youngdatafan.portal.model.management.modelgrant.entity.ModelGroup;
import com.youngdatafan.portal.model.management.modelgrant.entity.ModelGroupBusinessModel;
import com.youngdatafan.portal.model.management.modelgrant.mapper.ModelGroupBusinessModelMapper;
import com.youngdatafan.portal.model.management.modelgrant.mapper.ModelGroupMapper;
import com.youngdatafan.portal.model.management.modelgrant.service.ModelGroupService;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

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
public class ModelGroupServiceImpl implements ModelGroupService {


    @Autowired
    ModelGroupBusinessModelMapper modelGroupBusinessModelMapper;

    @Autowired
    ModelGroupMapper modelGroupMapper;

    @Autowired
    BusinessModelMapper businessModelMapper;

    @Override
    public boolean delete(String userId, String modelGrantGroupName) {
        if (modelGroupMapper.deleteByPrimaryKey(modelGrantGroupName, userId) > 0) {
            if (modelGroupMapper.deleteModelGroupBusinessModel(modelGrantGroupName) < 0) {
                throw new RuntimeException();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PageInfo<ModelGrantGroupDTO> selectAll(String userId, Integer curPage, Integer pageSize, String modelGrantGroupName) {
        PageHelper.startPage(curPage, pageSize);
        List<ModelGrantGroupDTO> list = modelGroupMapper.selectAllModelGrantGroup(userId, StringUtils.isEmpty(modelGrantGroupName) ? null : "%" + modelGrantGroupName + "%");

        PageInfo<ModelGrantGroupDTO> pageInfo = new PageInfo<>(list);

        List<String> list1 = new ArrayList<>();

        for (ModelGrantGroupDTO m : list
        ) {
            list1.add(m.getGroupId());
        }

        if (list1.size() > 0) {
            List<GroupIdBusinessModelListDTO> listDTOS = modelGroupMapper.selectAllBusinessModelByGroupIds(list1);

            for (ModelGrantGroupDTO modelGrantGroupDTO : list
            ) {

                for (GroupIdBusinessModelListDTO groupIdBusinessModelListDTO : listDTOS) {
                    if (modelGrantGroupDTO.getGroupId().equals(groupIdBusinessModelListDTO.getGroupid())) {

                        List<BusinessModelListDTO> businessModelListDTOS = groupIdBusinessModelListDTO.getBusinessModelListDTOS();
                        modelGrantGroupDTO.setBusinessModelListDTOS(businessModelListDTOS);
                        System.out.println(businessModelListDTOS.size());
                        modelGrantGroupDTO.setModelCount(businessModelListDTOS.size());
                    }
                }
            }
        }


        return pageInfo;
    }

    @Override
    public Result update(String userId, UpdateModelGroupVO updateModelGroupVO) {
        try {
            ModelGroup modelGroup = new ModelGroup();

            BeanUtils.copyProperties(updateModelGroupVO, modelGroup);
            modelGroup.setUpdateTime(new Date());

            if (modelGroupMapper.updateByPrimaryKeySelective(modelGroup) > 0) {

                modelGroupBusinessModelMapper.deleteByGroupName(updateModelGroupVO.getGroupId());

                List<ModelGroupBusinessModel> list = new ArrayList<>();
                List<String> modelIds = updateModelGroupVO.getModelId();

                if (modelIds.size() > 0) {
                    for (String modelName : modelIds) {
                        ModelGroupBusinessModel modelGroupBusinessModel = new ModelGroupBusinessModel();
                        modelGroupBusinessModel.setGroupName(updateModelGroupVO.getGroupId());
                        modelGroupBusinessModel.setModelName(modelName);
                        list.add(modelGroupBusinessModel);
                    }

                    if (modelGroupBusinessModelMapper.addModelGroupBusinessModels(list) > 0) {
                        return Result.success(true);
                    } else {
                        throw new RuntimeException();
                    }
                }
                return Result.success(true);
            } else {
                return Result.success(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public boolean insert(String userId, AddModelGroupVO addModelGroupVO) {

        try {
            ModelGroup modelGroup = new ModelGroup();

            BeanUtils.copyProperties(addModelGroupVO, modelGroup);

            modelGroup.setGroupId(UUID.randomUUID().toString());

            modelGroup.setCreateTime(new Date());

            modelGroup.setUpdateTime(new Date());

            modelGroup.setCreateUserId(userId);

            if (modelGroupMapper.insert(modelGroup) > 0) {
                List<ModelGroupBusinessModel> list = new ArrayList<>();
                List<String> modelIds = addModelGroupVO.getModelId();

                if (modelIds.size() > 0) {

                    for (String modelName : modelIds) {
                        ModelGroupBusinessModel modelGroupBusinessModel = new ModelGroupBusinessModel();
                        modelGroupBusinessModel.setGroupName(modelGroup.getGroupId());
                        modelGroupBusinessModel.setModelName(modelName);
                        list.add(modelGroupBusinessModel);
                    }

                    if (modelGroupBusinessModelMapper.addModelGroupBusinessModels(list) > 0) {
                        return true;
                    } else {
                        throw new RuntimeException();
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ModelNameAndGroupNameAndTypes selectAllBusinessodel(String userId, String modelName, String modelGroupNam, String modelGroupType
            , Integer curPage, Integer pageSize) {
        PageHelper.startPage(curPage, pageSize);
        ModelNameAndGroupNameAndTypes modelNameAndGroupNameAndTypes = new ModelNameAndGroupNameAndTypes();

        List<BusinessModelListDTO> listDTOS = businessModelMapper.selectBusinessModelByCreatUserId(userId, modelName, modelGroupNam, modelGroupType);

        List<String> modelNames = new ArrayList<>();

        Set<String> modelGroupNames = new HashSet<>();

        Set<String> modelGroupTypes = new HashSet<>();

        for (BusinessModelListDTO businessModelListDTO : listDTOS
        ) {
            if (!StringUtils.isEmpty(businessModelListDTO.getModelName())) {
                modelNames.add(businessModelListDTO.getModelChineseName());
            }
            if (!StringUtils.isEmpty(businessModelListDTO.getModelGroupName())) {
                modelGroupNames.add(businessModelListDTO.getModelGroupName());
            }
            if (!StringUtils.isEmpty(businessModelListDTO.getModelGroupType())) {
                modelGroupTypes.add(businessModelListDTO.getModelGroupType());
            }
        }
        PageInfo<BusinessModelListDTO> pageInfo = new PageInfo<>(listDTOS);

        modelNameAndGroupNameAndTypes.setBusinessModelListDTOS(pageInfo.getList());

        modelNameAndGroupNameAndTypes.setTotal(pageInfo.getTotal());
        modelNameAndGroupNameAndTypes.setModelNames(modelNames);
        modelNameAndGroupNameAndTypes.setGroupNames(modelGroupNames);
        modelNameAndGroupNameAndTypes.setGroupTypes(modelGroupTypes);

        return modelNameAndGroupNameAndTypes;

    }

    @Override
    public Boolean queryModelGroupNameIsExits(String userId, String modelGroupName) {
        if (modelGroupMapper.selectModelGroupByGroupName(userId, modelGroupName) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
