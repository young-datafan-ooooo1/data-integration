package com.youngdatafan.portal.common.group.mapper;

import com.youngdatafan.portal.common.group.dto.DpPortalGroupDTO;
import com.youngdatafan.portal.common.group.entity.DpPortalGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DpPortalGroupMapper {
    int deleteByPrimaryKey(String groupId);

    int insert(DpPortalGroup record);

    int insertSelective(DpPortalGroup record);

    DpPortalGroup selectByPrimaryKey(String groupId);

    int updateByPrimaryKeySelective(DpPortalGroup record);

    int updateByPrimaryKey(DpPortalGroup record);

    /**
     * 查询分组是否存在
     *
     * @param record DpPortalGroup
     * @return 记录数
     */
    int queryGroupExists(DpPortalGroup record);

    List<DpPortalGroupDTO> selectAll(@Param("userId") String userId);

    List<DpPortalGroupDTO> selectLikeByGroupName(@Param("groupName") String groupName);

    List<DpPortalGroupDTO> selectByGroupType(@Param("userId") String userId, @Param("groupType") String groupType);

    List<DpPortalGroupDTO> selectInGroupType(@Param("userId") String userId, @Param("groupTypes") String[] groupTypes);

    List<DpPortalGroupDTO> selectLikeByGroupType(@Param("userId") String userId, @Param("groupType") String groupType, @Param("groupName") String groupName);

    List<DpPortalGroupDTO> selectLikeInGroupType(@Param("userId") String userId, @Param("groupTypes") String[] groupTypes, @Param("groupName") String groupName);

    int deleteByGrouIds(@Param("groupIds") String[] groupIds);

    DpPortalGroupDTO selectByGroupId(String groupId);
}