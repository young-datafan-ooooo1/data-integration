package com.youngdatafan.portal.common.group.mapper;

import com.youngdatafan.portal.common.group.dto.DpPortalGroupDTO;
import com.youngdatafan.portal.common.group.entity.DpPortalGroup;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 模型分组mapper.
 */
public interface DpPortalGroupMapper {
    /**
     * 通过分组id删除.
     *
     * @param groupId 分组Id
     * @return int
     */
    int deleteByPrimaryKey(String groupId);

    /**
     * 分组保存.
     *
     * @param record DpPortalGroup
     * @return int
     */
    int insert(DpPortalGroup record);

    /**
     * 分组保存.
     *
     * @param record DpPortalGroup
     * @return int
     */
    int insertSelective(DpPortalGroup record);

    /**
     * 通过分组Id查询.
     *
     * @param groupId 分组Id
     * @return DpPortalGroup
     */
    DpPortalGroup selectByPrimaryKey(String groupId);

    /**
     * 更新分组.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalGroup record);

    /**
     * 通过DpPortalGroup 更新.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpPortalGroup record);

    /**
     * 查询分组是否存在.
     *
     * @param record DpPortalGroup
     * @return 记录数
     */
    int queryGroupExists(DpPortalGroup record);

    /**
     * 查询所有.
     *
     * @param userId 用户Id
     * @return List
     */
    List<DpPortalGroupDTO> selectAll(@Param("userId") String userId);

    /**
     * 通过分组名称查询.
     *
     * @param groupName groupName
     * @return List
     */
    List<DpPortalGroupDTO> selectLikeByGroupName(@Param("groupName") String groupName);

    /**
     * 通过分组类型查询.
     *
     * @param userId    用户Id
     * @param groupType 分组类型
     * @return List
     */
    List<DpPortalGroupDTO> selectByGroupType(@Param("userId") String userId, @Param("groupType") String groupType);

    /**
     * 通过Id分组类型查询.
     *
     * @param userId     用户Id
     * @param groupTypes 分组类型
     * @return List
     */
    List<DpPortalGroupDTO> selectInGroupType(@Param("userId") String userId, @Param("groupTypes") String[] groupTypes);

    /**
     * 模糊查询.
     *
     * @param userId    用户Id
     * @param groupName 分组名称
     * @param groupType 分组类型
     * @return List
     */
    List<DpPortalGroupDTO> selectLikeByGroupType(@Param("userId") String userId, @Param("groupType") String groupType, @Param("groupName") String groupName);

    /**
     * 查询.
     *
     * @param userId     用户Id
     * @param groupName  分组名称
     * @param groupTypes 分组类型集合
     * @return List
     */
    List<DpPortalGroupDTO> selectLikeInGroupType(@Param("userId") String userId, @Param("groupTypes") String[] groupTypes, @Param("groupName") String groupName);

    /**
     * 批量删除.
     *
     * @param groupIds 分组id
     * @return int
     */
    int deleteByGrouIds(@Param("groupIds") String[] groupIds);

    /**
     * 通过分组id查询.
     *
     * @param groupId id
     * @return DpPortalGroupDTO
     */
    DpPortalGroupDTO selectByGroupId(String groupId);
}
