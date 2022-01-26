package com.youngdatafan.portal.model.management.modelgrant.mapper;

import com.youngdatafan.portal.model.management.modelgrant.dto.*;
import com.youngdatafan.portal.model.management.modelgrant.entity.UserGrant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserGrantMapper {
    int deleteByPrimaryKey(@Param("userGrantId") String userGrantId);

    int insert(UserGrant record);

    int insertSelective(UserGrant record);

    UserGrant selectByPrimaryKey(String userName);

    int updateByPrimaryKeySelective(UserGrant record);

    int updateByPrimaryKey(UserGrant record);

    /**
     * 通过用户名和创建人id查询对象
     *
     * @param userId   创建人id
     * @param userName 用户名称
     * @return
     */
    UserGrant selectUserGrantByUserIdAndUserName(@Param("userId") String userId, @Param("grantId") String userName);


    List<ModelGroupGrantUserDTO> selectAllByUserIdAndUserNameAndGrantGroupName(@Param("userId") String userId,
                                                                               @Param("userName") String userName,
                                                                               @Param("grantGroupName") String groupName);

    List<UserIdAndNamesDTO> selectAllUserByCreateUserId(@Param("userId") String userId);

    String selectUserIdByUserName(@Param("userName") String userName);


    List<ModelGroupGrantUserDTO> selectUserGrant(@Param("userId") String userId,
                                                 @Param("userName") String userName,
                                                 @Param("grantGroupName") String groupName);


    List<UserIdAndBusinessModelListDTO> selectBusinessModelList(@Param("list") List<String> list);

    List<UserIdAndGroupIdAndGroupNamesDTO> selecGroupIdAndGroupNames(@Param("list") List<String> list);

//    List<GroupIdAndGroupNamesDTO> selectGroupIdAndNames(@Param(""));

}