package com.youngdatafan.portal.model.management.outinterfacemodel.mapper;

import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.BusinessModelListDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.*;
import com.youngdatafan.portal.model.management.outinterfacemodel.entity.OutinterfaceModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutinterfaceModelMapper {
    int deleteByPrimaryKey(@Param("userId") String userId, @Param("modelName") String modelName);

    int insert(OutinterfaceModel record);

    int insertSelective(OutinterfaceModel record);

    OutinterfaceModelDTO selectByPrimaryKey(@Param("userId") String userId, @Param("modelName") String modelName, @Param("groupId") String groupId);

    OutinterfaceModelDTO selectModelByModelName(@Param("userId") String userId, @Param("modelName") String modelName);


    int updateByPrimaryKeySelective(OutinterfaceModel record);

    int updateByPrimaryKeyWithBLOBs(OutinterfaceModel record);

    int updateByPrimaryKey(OutinterfaceModel record);

    int insertOutinterfaceModel(OutinterfaceModelAndBasicModelName record);


    /**
     * 通过用户id查询业务模型用户授权组添加模型
     *
     * @param userId
     * @param outinterfaceModelName
     * @param modelGroupName
     * @param modelGroupType
     * @return
     */
    List<BusinessModelListDTO> selectOutinterfaceModelByCreatUserId(@Param("userId") String userId,
                                                                @Param("outinterfaceModelName") String outinterfaceModelName,
                                                                @Param("modelGroupName") String modelGroupName,
                                                                @Param("modelGroupType") String modelGroupType);

    /**
     * 查询模型分组和模型集合
     *
     * @param userId
     * @return
     */
    List<OutinterfaceModelAndGroupListDTO> selectModelAndGroupList(@Param("userId") String userId);

    List<OutinterfaceModelAndGroupListDTO> selectCustomModelAndGroupList(@Param("userId") String userId);

    List<OutinterfaceModelAndGroupListDTO> selectOutModelAndGroupList(@Param("userId") String userId);

    /**
     * 查询数据库
     *
     * @param modelName
     * @return
     */
    OutinterfaceModelDatasourceDTO selecDatasourceByModelName(@Param("modelName") String modelName);

    OutinterfaceModelDatasourceDTO selecDatasourceByZDYModelName(@Param("modelName") String modelName);

    OutinterfaceModelDatasourceDTO selecDatasourceBySCLModelName(@Param("modelName") String modelName);




    /**
     * 查询业务模型元数据
     *
     * @param modelName
     * @return
     */
    List<OutinterfaceModelMetaDataDTO> selectOutinterfaceMetaData(@Param("modelName") String modelName);


    int replaceIntoBatch(List<OutinterfaceModel> record);


    int deleteModelGroup(@Param("modelName") String modelName);

    int insertModelGroup(@Param("modelName") String modelName, @Param("groupId") String groupId);

    List<String> getGroupTypes(@Param("userId") String userId, @Param("list") List<String> list);


    List<OutinterfaceModelDTO> selectOutinterfaceModel(@Param("userId") String userId,
                                                   @Param("modelGroup") String modelGroup,
                                                   @Param("basicModelName") String basicModelName, @Param("outinterfaceModelName") String outinterfaceModelName);

    List<SelectOutinterfaceModelNameAndColumnCountDTO> selectModelMetaDatas(@Param("list") List<String> list);

    /**
     * 获取基础模型组
     *
     * @param userId
     * @return
     */
    List<OutinterfaceModelGroupDTO> getBasicModelGroup(@Param("userId") String userId, @Param("groupType") String grouptype);


    List<OutinterfaceModelNameAndCnameDTO> getBasicModel(@Param("userId") String userId,
                                                         @Param("groupName") String groupName,
                                                         @Param("modelType") String modelType,
                                                         @Param("enabled") String enabled);


    List<BatchDownloadOutinterfaceModelDTO> batchDownloadExcel(@Param("userId") String userId, @Param("list") List<String> modelNames);


    String selectBasicModelIdByModelNameAndGroupName(@Param("userId") String userId, @Param("modelName") String modelName, @Param("groupName") String groupName);

    String selectTableNameByModelNameAndGroupName(@Param("userId") String userId, @Param("modelName") String modelName, @Param("groupName") String groupName);

    Integer queryOutinterfaceModelExists(@Param("userId") String userId, @Param("outinterfaceModelName") String outinterfaceModelName, @Param("outinterfaceModelGroup") String outinterfaceModelGroup);

    ModelDTO selectModelAndMetaData(@Param("userId") String userId, @Param("modelName") String modelName);

    OutinterfaceDatasourceAndTableNameDTO selectByBasicModelId(@Param("modelId") String modelId);

}
