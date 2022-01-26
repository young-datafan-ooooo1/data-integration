package com.youngdatafan.portal.model.management.modelgrant.mapper;

import com.youngdatafan.portal.model.management.modelgrant.dto.AddUserGrantGroupListDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.GroupIdBusinessModelListDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelGrantGroupDTO;
import com.youngdatafan.portal.model.management.modelgrant.entity.ModelGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelGroupMapper {
    int deleteByPrimaryKey(@Param("groupName") String groupName, @Param("userId") String userId);

    int insert(ModelGroup record);

    int insertSelective(ModelGroup record);

    ModelGroup selectByPrimaryKey(String groupName);

    int updateByPrimaryKeySelective(ModelGroup record);

    int updateByPrimaryKey(ModelGroup record);

    /**
     * 获取所有授权组信息
     *
     * @param userId
     * @param modelGrantGroupName
     * @return
     */
    List<ModelGrantGroupDTO> selectAllModelGrantGroup(@Param("userId") String userId,
                                                      @Param("name") String modelGrantGroupName);


    List<GroupIdBusinessModelListDTO> selectAllBusinessModelByGroupIds(@Param("list") List<String> groupIds);

    /**
     * 新增用户授权时查询的授权组名和表数
     *
     * @param userId
     * @return
     */
    List<AddUserGrantGroupListDTO> selectGroupModelNameAndModelList(@Param("userId") String userId,
                                                                    @Param("groupName") String groupName);


    int deleteModelGroupBusinessModel(@Param("modelGroupName") String modelGroupName);

    int selectModelGroupByGroupName(@Param("userId") String userId, @Param("modelGroupName") String modelGroupName);
}