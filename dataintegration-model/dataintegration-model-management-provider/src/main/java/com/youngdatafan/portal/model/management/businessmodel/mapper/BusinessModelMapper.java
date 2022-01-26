package com.youngdatafan.portal.model.management.businessmodel.mapper;

import com.youngdatafan.portal.model.management.businessmodel.dto.*;
import com.youngdatafan.portal.model.management.businessmodel.entity.BusinessModel;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.BusinessModelListDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusinessModelMapper {
    int deleteByPrimaryKey(@Param("userId") String userId, @Param("modelName") String modelName);

    int insert(BusinessModel record);

    int insertSelective(BusinessModel record);

    BusinessModelDTO selectByPrimaryKey(@Param("userId") String userId, @Param("modelName") String modelName, @Param("groupId") String groupId);

    BusinessModelDTO selectModelByModelName(@Param("userId") String userId, @Param("modelName") String modelName);


    int updateByPrimaryKeySelective(BusinessModel record);

    int updateByPrimaryKeyWithBLOBs(BusinessModel record);

    int updateByPrimaryKey(BusinessModel record);

    int insertBusinessModel(BusinessModelAndBasicModelName record);


    /**
     * 通过用户id查询业务模型用户授权组添加模型
     *
     * @param userId
     * @param businessModelName
     * @param modelGroupName
     * @param modelGroupType
     * @return
     */
    List<BusinessModelListDTO> selectBusinessModelByCreatUserId(@Param("userId") String userId,
                                                                @Param("businessModelName") String businessModelName,
                                                                @Param("modelGroupName") String modelGroupName,
                                                                @Param("modelGroupType") String modelGroupType);

    /**
     * 查询模型分组和模型集合
     *
     * @param userId
     * @return
     */
    List<BusinessModelAndGroupListDTO> selectModelAndGroupList(@Param("userId") String userId);

    List<BusinessModelAndGroupListDTO> selectCustomModelAndGroupList(@Param("userId") String userId);

    List<BusinessModelAndGroupListDTO> selectOutModelAndGroupList(@Param("userId") String userId);

    /**
     * 查询数据库
     *
     * @param modelName
     * @return
     */
    ModelDatasourceDTO selecDatasourceByModelName(@Param("modelName") String modelName);

    ModelDatasourceDTO selecDatasourceByZDYModelName(@Param("modelName") String modelName);

    ModelDatasourceDTO selecDatasourceBySCLModelName(@Param("modelName") String modelName);




    /**
     * 查询业务模型元数据
     *
     * @param modelName
     * @return
     */
    List<BusinessModelMetaDataDTO> selectBusinessMetaData(@Param("modelName") String modelName);


    int replaceIntoBatch(List<BusinessModel> record);


    int deleteModelGroup(@Param("modelName") String modelName);

    int insertModelGroup(@Param("modelName") String modelName, @Param("groupId") String groupId);

    List<String> getGroupTypes(@Param("userId") String userId, @Param("list") List<String> list);


    List<BusinessModelDTO> selectBusinessModel(@Param("userId") String userId,
                                               @Param("modelGroup") String modelGroup,
                                               @Param("basicModelName") String basicModelName, @Param("businessModelName") String businessModelName);

    List<SelectBusinessModelNameAndColumnCountDTO> selectModelMetaDatas(@Param("list") List<String> list);

    /**
     * 获取基础模型组
     *
     * @param userId
     * @return
     */
    List<BasicModelGroupDTO> getBasicModelGroup(@Param("userId") String userId, @Param("groupType") String grouptype);


    List<BasicModelNameAndCnameDTO> getBasicModel(@Param("userId") String userId,
                                                  @Param("groupName") String groupName,
                                                  @Param("modelType") String modelType,
                                                  @Param("enabled") String enabled);


    List<BatchDownloadBusinessModelDTO> batchDownloadExcel(@Param("userId") String userId, @Param("list") List<String> modelNames);


    String selectBasicModelIdByModelNameAndGroupName(@Param("userId") String userId, @Param("modelName") String modelName, @Param("groupName") String groupName);

    String selectTableNameByModelNameAndGroupName(@Param("userId") String userId, @Param("modelName") String modelName, @Param("groupName") String groupName);

    Integer queryBusinessModelExists(@Param("userId") String userId, @Param("businessModelName") String businessModelName, @Param("businessModelGroup") String businessModelGroup);

    ModelDTO selectModelAndMetaData(@Param("userId") String userId, @Param("modelName") String modelName);

    DatasourceAndTableNameDTO selectByBasicModelId(@Param("modelId") String modelId);

    List<BusinessModelAndGroupListDTO> selectOutinterfaceModelAndGroupList(String userId);

    ModelDatasourceDTO selecDatasourceBySCJKModelName(@Param("modelName") String modelName);
}
