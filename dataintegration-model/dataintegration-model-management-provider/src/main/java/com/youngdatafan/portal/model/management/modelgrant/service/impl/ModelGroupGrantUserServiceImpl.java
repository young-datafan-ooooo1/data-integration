package com.youngdatafan.portal.model.management.modelgrant.service.impl;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.modelgrant.dto.*;
import com.youngdatafan.portal.model.management.modelgrant.entity.ModelGroupUserGrant;
import com.youngdatafan.portal.model.management.modelgrant.entity.ModelUserGrant;
import com.youngdatafan.portal.model.management.modelgrant.entity.UserGrant;
import com.youngdatafan.portal.model.management.modelgrant.mapper.ModelGroupMapper;
import com.youngdatafan.portal.model.management.modelgrant.mapper.ModelUserGrantMapper;
import com.youngdatafan.portal.model.management.modelgrant.mapper.UserGrantMapper;
import com.youngdatafan.portal.model.management.modelgrant.service.ModelGroupGrantUserService;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupGrantUserVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupGrantUserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.portal.model.management.modelgrant.mapper.ModelGroupBusinessModelMapper;
import com.youngdatafan.portal.model.management.modelgrant.mapper.ModelGroupUserGrantMapper;
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
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/12 11:35 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class ModelGroupGrantUserServiceImpl implements ModelGroupGrantUserService {

    @Autowired
    ModelGroupBusinessModelMapper modelGroupBusinessModelMapper;

    @Autowired
    UserGrantMapper userGrantMapper;

    @Autowired
    ModelGroupUserGrantMapper modelGroupUserGrantMapper;

    @Autowired
    ModelUserGrantMapper modelUserGrantMapper;

    @Autowired
    ModelGroupMapper modelGroupMapper;

    @Override
    public Result insert(String userId, AddModelGroupGrantUserVO addModelGroupGrantUserVO) {
        if (StringUtils.isEmpty(userGrantMapper.selectByPrimaryKey(addModelGroupGrantUserVO.getUserName()))) {

            UserGrant userGrant = new UserGrant();
            userGrant.setCreateTime(new Date());
            userGrant.setCreateUserId(userId);
            userGrant.setUpdateTime(new Date());

            userGrant.setUserGrantId(UUID.randomUUID().toString());

            userGrant.setEnabled(addModelGroupGrantUserVO.getEnabled());
            userGrant.setUserName(addModelGroupGrantUserVO.getUserId());
            userGrant.setDescription(addModelGroupGrantUserVO.getDescription());

            userGrant.setUserGrantId(UUID.randomUUID().toString());

            if (userGrantMapper.insert(userGrant) > 0) {
                addModelGroupGrantUserVO.setUserGrantId(userGrant.getUserGrantId());
                return insertModelGranpGrantUser(addModelGroupGrantUserVO);
            } else {
                return Result.fail(StatusCode.CODE_10010.getCode(), addModelGroupGrantUserVO, "新增失败");
            }
        } else {
            return Result.fail(StatusCode.CODE_10010.getCode(), addModelGroupGrantUserVO, "此用户已被授权");
        }
    }


    @Override
    public Result update(String userId, UpdateModelGroupGrantUserVO updateModelGroupGrantUserVO) {

        if (StringUtils.isEmpty(userGrantMapper.selectUserGrantByUserIdAndUserName(userId, updateModelGroupGrantUserVO.getUserGrantId()))) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "此用户组不存在");
        }

        String id = userGrantMapper.selectUserIdByUserName(updateModelGroupGrantUserVO.getUserId());

        if (StringUtils.isEmpty(id)) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "此用户不存在");
        }
//        updateModelGroupGrantUserVO.setUserId(id);

        int modelGroupFlagCrud = modelGroupUserGrantMapper.deleteModelGroupByUserId(updateModelGroupGrantUserVO.getUserId());
        if (modelGroupFlagCrud < 0) {
            throw new RuntimeException();
        }

        int modelFlagCrud = modelUserGrantMapper.deleteModelByUserId(updateModelGroupGrantUserVO.getUserId());
        if (modelFlagCrud < 0) {
            throw new RuntimeException();
        }

        AddModelGroupGrantUserVO addModelGroupGrantUserVO = new AddModelGroupGrantUserVO();

        BeanUtils.copyProperties(updateModelGroupGrantUserVO, addModelGroupGrantUserVO);

        UserGrant userGrant = new UserGrant();
        userGrant.setUpdateTime(new Date());

        userGrant.setEnabled(addModelGroupGrantUserVO.getEnabled());
        userGrant.setUserName(addModelGroupGrantUserVO.getUserId());
        userGrant.setDescription(addModelGroupGrantUserVO.getDescription());
        userGrant.setUserGrantId(updateModelGroupGrantUserVO.getUserGrantId());

        if (userGrantMapper.updateByPrimaryKeySelective(userGrant) > 0) {
            return insertModelGranpGrantUser(addModelGroupGrantUserVO);
        } else {
            throw new RuntimeException();
        }


    }

    @Override
    public Result delete(String userId, String grantId) {
        UserGrant userGrant = userGrantMapper.selectUserGrantByUserIdAndUserName(userId, grantId);
        if (StringUtils.isEmpty(userGrant)) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "此用户组不存在");
        } else {
            if (userGrantMapper.deleteByPrimaryKey(grantId) > 0) {
                int modeGroupCrudFlag = modelGroupUserGrantMapper.deleteModelGroupByUserName(userGrant.getUserName());
                if (modeGroupCrudFlag < 0) {
                    throw new RuntimeException();
                }

                int modeCrudFlag = modelUserGrantMapper.deleteModelByUserName(userGrant.getUserName());
                if (modeCrudFlag < 0) {
                    throw new RuntimeException();
                }
                return Result.success(true);

            } else {
                return Result.fail(StatusCode.CODE_10010.getCode(), false, "删除失败");
            }
        }

    }

    @Override
    public PageInfo<ModelGroupGrantUserDTO> selectAll(String userId, String userName, Integer curPage, Integer pageSize, String modelGrantGroupName) {
        PageHelper.startPage(curPage, pageSize);

        List<ModelGroupGrantUserDTO> list =
                userGrantMapper.selectUserGrant(userId, StringUtils.isEmpty(userName) ?
                        null : "%" + userName + "%", StringUtils.isEmpty(modelGrantGroupName) ?
                        null : "%" + modelGrantGroupName + "%");


        List<String> userIds = new ArrayList<>();
        for (ModelGroupGrantUserDTO m : list) {
            userIds.add(m.getUserId());
        }


        if (userIds.size() > 0) {

            List<UserIdAndBusinessModelListDTO> selectBusinessModelList = userGrantMapper.selectBusinessModelList(userIds);

            List<UserIdAndGroupIdAndGroupNamesDTO> userIdAndGroupIdAndGroupNamesDTOS = userGrantMapper.selecGroupIdAndGroupNames(userIds);


            for (ModelGroupGrantUserDTO m : list) {

                m.setGrantGroups(new ArrayList<>());

                m.setModels(new ArrayList<>());

                for (UserIdAndBusinessModelListDTO userIdAndBusinessModelListDTO : selectBusinessModelList
                ) {

                    if (userIdAndBusinessModelListDTO.getUserId().equals(m.getUserId())) {
                        List<BusinessModelListDTO> groupIdAndGroupNamesDTOS = userIdAndBusinessModelListDTO.getListDTOS();
                        m.setModels(groupIdAndGroupNamesDTOS);
                        m.setModelCount(groupIdAndGroupNamesDTOS.size());
                    }
                }

                for (UserIdAndGroupIdAndGroupNamesDTO userIdAndGroupIdAndGroupNamesDTO : userIdAndGroupIdAndGroupNamesDTOS
                ) {
                    if (m.getUserId().equals(userIdAndGroupIdAndGroupNamesDTO.getUserId())) {
                        m.setGrantGroups(userIdAndGroupIdAndGroupNamesDTO.getList());
                    }
                }
            }
        }


        PageInfo<ModelGroupGrantUserDTO> pageInfo = new PageInfo<>(list);


        return pageInfo;
    }

    private Result insertModelGranpGrantUser(AddModelGroupGrantUserVO addModelGroupGrantUserVO) {

        List<ModelGroupUserGrant> modelGroupUserGrants = new ArrayList<>();

        List<ModelUserGrant> modelUserGrants = new ArrayList<>();

        for (String modelGrouName : addModelGroupGrantUserVO.getModelGrantGroupNames()
        ) {
            ModelGroupUserGrant modelGroupUserGrant = new ModelGroupUserGrant();
            modelGroupUserGrant.setGroupName(modelGrouName);
            modelGroupUserGrant.setUserId(addModelGroupGrantUserVO.getUserId());
            modelGroupUserGrants.add(modelGroupUserGrant);
        }

        for (String modelName : addModelGroupGrantUserVO.getModelNames()
        ) {
            ModelUserGrant modelUserGrant = new ModelUserGrant();
            modelUserGrant.setModelName(modelName);
            modelUserGrant.setUserId(addModelGroupGrantUserVO.getUserId());
            modelUserGrants.add(modelUserGrant);
        }

        if (modelGroupUserGrants.size() > 0) {
            int flag = modelGroupUserGrantMapper.insertModelGroupUserGrants(modelGroupUserGrants);
            if (flag <= 0) {
                throw new RuntimeException();
            }
        }

        if (modelUserGrants.size() > 0) {
            int flag = modelUserGrantMapper.insertModelUserGrants(modelUserGrants);
            if (flag <= 0) {
                throw new RuntimeException();
            }
        }
        return Result.success(true);
    }

    @Override
    public List<UserIdAndNamesDTO> selectUserIdAndNames(String userId) {
        List<UserIdAndNamesDTO> list = userGrantMapper.selectAllUserByCreateUserId(userId);
        return list;
    }

    @Override
    public List<AddUserGrantGroupListDTO> selectGroupModelNameAndModelList(String userId, String groupName) {
        List<AddUserGrantGroupListDTO> addUserGrantGroupListDTOS = modelGroupMapper.selectGroupModelNameAndModelList(userId, groupName);

        for (AddUserGrantGroupListDTO a : addUserGrantGroupListDTOS) {
            a.setModelCount(a.getBusinessModelName().size());
        }
        return addUserGrantGroupListDTOS;
    }
}
