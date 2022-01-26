package com.youngdatafan.portal.model.management.outmodel.mapper;

import com.youngdatafan.portal.model.management.outmodel.dto.ModelIdAndModelMetaDataDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelProjectIdAndNamesDTO;
import com.youngdatafan.portal.model.management.outmodel.entity.OutModelMetaData;
import com.youngdatafan.portal.model.management.outmodel.vo.OutModelMetaDataVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutModelMetaDataMapper {
    int insert(OutModelMetaData record);

    int insertSelective(OutModelMetaData record);

    int deleteOutModelMetaDataByModelName(@Param("list") List<String> modelName);

    int deleteOutModelMetaDataByOne(@Param("modelName")String modelName);

    int batchInsertOutModelMeta(@Param("list") List<OutModelMetaDataVO> list);

    List<ModelIdAndModelMetaDataDTO> selectModelMeataDataList(@Param("list") List<String> modeIds);

    List<OutModelProjectIdAndNamesDTO> selectProjects(@Param("userId") String userId);


}