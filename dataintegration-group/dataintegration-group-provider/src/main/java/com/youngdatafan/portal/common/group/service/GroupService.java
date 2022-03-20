package com.youngdatafan.portal.common.group.service;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.exception.FormValidationException;
import com.youngdatafan.dataintegration.core.exception.ValidationException;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.portal.common.group.dto.DpPortalGroupDTO;
import com.youngdatafan.portal.common.group.entity.DpPortalGroup;
import com.youngdatafan.portal.common.group.mapper.DpPortalGroupMapper;
import com.youngdatafan.portal.common.group.vo.GroupAddVO;
import com.youngdatafan.portal.common.group.vo.GroupUpdateVO;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 分组管理.
 *
 * @author gavin
 * @since 2020/2/10 1:29 下午
 */
@Service
public class GroupService {

    private final DpPortalGroupMapper groupMapper;

    @Autowired
    public GroupService(DpPortalGroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    /**
     * 查询联合主键是否重复.
     *
     * @param dpPortalGroup DpPortalGroup
     * @return true or false
     */
    public boolean queryGroupExists(DpPortalGroup dpPortalGroup) {
        // 记录数大于0就是存在
        return groupMapper.queryGroupExists(dpPortalGroup) > 0;
    }

    /**
     * 添加组.
     *
     * @param groupAddVO ModelGroupAddVO
     * @param userId     用户id
     * @return 新增的分组信息.
     */
    public DpPortalGroupDTO addModelGroup(GroupAddVO groupAddVO, String userId) {
        DpPortalGroup dpPortalGroup = new DpPortalGroup();
        // 创建数据库实体对象
        BeanUtils.copyProperties(groupAddVO, dpPortalGroup);
        dpPortalGroup.setCreateTime(new Date());
        dpPortalGroup.setUpdateTime(new Date());
        dpPortalGroup.setCreateUserId(userId);

        // 判断分组是否存在
        if (queryGroupExists(dpPortalGroup)) {
            throw new FormValidationException(StatusCode.CODE_10004.getCode(), "groupName", "分组名称已存在，请切换分组类型或者重命名。");
        }

        // 自动生成分组id
        dpPortalGroup.setGroupId(UUIDUtils.generateUUID32());

        // 保存到数据库
        groupMapper.insert(dpPortalGroup);

        DpPortalGroupDTO dpPortalGroupDto = new DpPortalGroupDTO();
        BeanUtils.copyProperties(dpPortalGroup, dpPortalGroupDto);
        return dpPortalGroupDto;
    }

    /**
     * 修改组.
     *
     * @param groupUpdateVO ModelGroupUpdateVO
     * @return 受影响的记录数.
     */
    public int updateModelGroup(GroupUpdateVO groupUpdateVO) {
        DpPortalGroup dpPortalGroup = new DpPortalGroup();
        // 创建数据库实体对象
        BeanUtils.copyProperties(groupUpdateVO, dpPortalGroup);
        dpPortalGroup.setUpdateTime(new Date());

        // 保存到数据库
        return groupMapper.updateByPrimaryKey(dpPortalGroup);
    }

    /**
     * 修改组，选择性的.
     *
     * @param groupUpdateVO ModelGroupUpdateVO
     * @return 受影响的记录数.
     */
    public int updateModelGroupSelective(GroupUpdateVO groupUpdateVO) {
        DpPortalGroup dpPortalGroup = new DpPortalGroup();
        // 创建数据库实体对象
        BeanUtils.copyProperties(groupUpdateVO, dpPortalGroup);
        dpPortalGroup.setUpdateTime(new Date());

        // 保存到数据库
        return groupMapper.updateByPrimaryKeySelective(dpPortalGroup);
    }

    /**
     * 删除组.
     *
     * @param groupId 分组id
     * @return 删除的记录数.
     */
    public int delete(String groupId) {
        return groupMapper.deleteByPrimaryKey(groupId);
    }

    /**
     * 批量删除组.
     *
     * @param groupIds 分组id
     */
    public void deleteBath(String[] groupIds) {
        if (ArrayUtils.isEmpty(groupIds)) {
            return;
        }
        for (String str : groupIds) {
            DpPortalGroupDTO dpPortalGroupDTO = groupMapper.selectByGroupId(str);
            if (dpPortalGroupDTO != null) {
                throw new ValidationException(StatusCode.CODE_10010.getCode(), dpPortalGroupDTO.getGroupName() + "有项目，不可删除");
            }
        }
        groupMapper.deleteByGrouIds(groupIds);
    }

    /**
     * 查询所有.
     *
     * @param userId 用户id
     * @return 分组明细集合
     */
    public List<DpPortalGroupDTO> selectAll(String userId) {
        return groupMapper.selectAll(userId);
    }

    /**
     * 根据分组名称模糊查询.
     *
     * @param groupName 分组名称
     * @return 分组明细集合
     */
    public List<DpPortalGroupDTO> selectLikeByGroupName(String groupName) {
        return groupMapper.selectLikeByGroupName(groupName);
    }

    /**
     * 查询分组类型下的分组明细.
     *
     * @param userId    用户id
     * @param groupType 分组类型，多个逗号分隔
     * @return 分组明细集合
     */
    public List<DpPortalGroupDTO> selectByGroupType(String userId, String groupType) {
        String[] groupTypes = groupType.split(",");
        if (groupTypes.length > 1) {
            return groupMapper.selectInGroupType(userId, groupTypes);
        } else {
            return groupMapper.selectByGroupType(userId, groupType);
        }
    }

    /**
     * 根据分组名称和分组备注模糊查询，查询分组类型下的分组明细.
     *
     * @param userId    用户id
     * @param groupType 分组类型，多个逗号分隔
     * @param groupName 分组名称
     * @return 分组明细集合
     */
    public List<DpPortalGroupDTO> selectLikeByGroupType(String userId, String groupType, String groupName) {
        String[] groupTypes = groupType.split(",");
        if (groupTypes.length > 1) {
            return groupMapper.selectLikeInGroupType(userId, groupTypes, groupName);
        } else {
            return groupMapper.selectLikeByGroupType(userId, groupType, groupName);
        }
    }
}
