package com.youngdatafan.portal.model.management.outmodel.mapper;

import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelAndMetaDataListDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelGroupDTO;
import com.youngdatafan.portal.model.management.outmodel.entity.OutModel;
import com.youngdatafan.portal.model.management.outmodel.vo.UpdateOutModelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutModelMapper {
    int deleteByPrimaryKey(@Param("userId") String userId, @Param("modelName") String modelName);

    int batchDeletePrimaryKey(@Param("list") List<String> list, @Param("userId") String userId);

    int insert(OutModel record);

    int insertSelective(OutModel record);

    OutModel selectByPrimaryKey(String modelName);

    List<OutModel> selectItems(@Param("list") List<String> list);

    int updateByPrimaryKeySelective(OutModel record);

    int updateSelective(UpdateOutModelVO updateOutModelVO);

    int updateByPrimaryKey(OutModel record);

    int updateAllByPrimaryKey(UpdateOutModelVO updateOutModelVO);

    List<OutModelGroupDTO> selectGroupIdByGroupTypeAndUserId(@Param("userId") String userId, @Param("groupType") String groupType);

    List<OutModelAndMetaDataDTO> selectAllGroupByUserIdAndProjectIdAndGroupID(@Param("userId") String userId,
                                                                              @Param("groupId") String groupId,
                                                                              @Param("projectId") String projectId,
                                                                              @Param("modelName") String modelName,
                                                                              @Param("enabled") String enabled);


    int insertModelGroup(@Param("modelName") String modelName, @Param("groupId") String groupId);

    int deleteModelGroup(@Param("modelName") String modelName);

    int deleteModelGroupByModelNames(@Param("list") List<String> modelNames);

    OutModelAndMetaDataListDTO selectOutModelAndMetaData(@Param("userId") String userId,
                                                         @Param("modelName") String modelName);

    ModelDTO selectModel(@Param("userId") String userId, @Param("modelName") String modelName);

    int selectModelByModelId(@Param("userId") String userId, @Param("modelId") String modelId);

}
