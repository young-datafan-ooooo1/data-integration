package com.youngdatafan.portal.model.management.businessmodel.mapper;

import com.youngdatafan.portal.model.management.businessmodel.dto.GroupDTO;
import com.youngdatafan.portal.model.management.businessmodel.entity.Group;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupMapper {
    int deleteByPrimaryKey(String groupId);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(String groupId);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);

    List<GroupDTO> selectAllGroup(@Param("userId") String userId, @Param("groupName") String groupName);

    String selectGroupIdByGroupNameAndUserId(@Param("userId") String userId, @Param("groupName") String groupName, @Param("groupType") String groupType);

}