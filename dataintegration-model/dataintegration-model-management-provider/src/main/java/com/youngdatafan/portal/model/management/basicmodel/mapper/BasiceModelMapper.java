package com.youngdatafan.portal.model.management.basicmodel.mapper;

import com.youngdatafan.portal.model.management.basicmodel.dto.*;
import com.youngdatafan.portal.model.management.basicmodel.entity.BasiceModel;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BasiceModelMapper {
    int deleteByPrimaryKey(@Param("modelName") String modelName, @Param("userId") String userId);

    int insert(BasiceModel record);

    int insertSelective(BasiceModel record);

    BasiceModel selectByPrimaryKey(@Param("modelName") String modelName, @Param("userId") String userId);

    int updateByPrimaryKeySelective(BasiceModel record);

    int updateByPrimaryKey(BasiceModel record);

    List<BasicModelDTO> selectBasicModelByUserId(@Param("userId") String userId,
                                                 @Param("modelName") String modelName,
                                                 @Param("groupName") String groupName,
                                                 @Param("datasourceName") String dateSourceName, @Param("modelType") String modelType);

    /**
     * 获取所有数据源
     *
     * @param userId
     * @return
     */
    List<BasicModelDatasourceDTO> getAllDatasources(@Param("userId") String userId, @Param("sourcePlatform") String  source);

    /**
     * 获取所有分组
     *
     * @param userId
     * @return
     */
    List<GroupDTO> getAllGroup(@Param("userId") String userId, @Param("groupType") String groupType);


    /**
     * 插入模型和分组
     *
     * @param modelName
     * @param groupId
     * @return
     */
    int insertModelGroup(@Param("modelName") String modelName, @Param("groupId") String groupId);

    int deleteModelGroup(@Param("modelName") String modelName);


    int replaceIntoBatch(List<BasiceModel> record);

    List<BasicModelAndMetaDataDTO> selectBasicModelAndMetaData(@Param("list") List<String> list,
                                                               @Param("userId") String userId);


    String selectModelNameByUserIdAndCname(@Param("userId") String userId, @Param("cName") String cName,
                                           @Param("modelType") String modelType, @Param("groupId") String groupId);

    /**
     * 通过业务模型查询schema
     *
     * @param businessName
     * @return
     */
    String selectAdminBasicModelByBusiname(@Param("businessName") String businessName);

    Integer queryBasiceModelExists(@Param("userId") String userId, @Param("basicModelName") String basicModelName, @Param("basicModelGroup") String basicModelGroup);

    CustomModelDTO selectCustomModel(@Param("userId") String userId, @Param("modelName") String modelName, @Param("modelType") String modelType);


    ModelDTO selectModel(@Param("userId") String userId, @Param("modelName") String modelName, @Param("modelType") String modelType);

    int queryModelByModelNameAndType(@Param("groupId") String groupId, @Param("modelType") String modelType, @Param("list") List<String> modelNames);

}