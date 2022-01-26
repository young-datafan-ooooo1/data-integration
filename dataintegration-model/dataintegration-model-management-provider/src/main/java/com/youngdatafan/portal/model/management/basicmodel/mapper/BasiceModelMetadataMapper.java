package com.youngdatafan.portal.model.management.basicmodel.mapper;

import com.youngdatafan.portal.model.management.basicmodel.dto.AllColumnDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.BasicModelMetaDataDTO;
import com.youngdatafan.portal.model.management.basicmodel.dto.SelectModelNameAndColumnCountDTO;
import com.youngdatafan.portal.model.management.basicmodel.entity.BasiceModelMetadata;
import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BasiceModelMetadataMapper {
    int insert(BasiceModelMetadata record);

    int insertSelective(BasiceModelMetadata record);

    int deleteByModelName(String modelName);

    List<SelectModelNameAndColumnCountDTO> selectBusinessModelNameAndColumnCount(@Param("list") List<String> modelName);

    int insertBasicModelMetaData(@Param("list") List<BasicModelMetaDataVO> list);


    int insertIntoBatch(@Param("list") List<BasicModelMetaDataDTO> record);

    int insertAllColunms(@Param("list") List<AllColumnDTO> list);
}