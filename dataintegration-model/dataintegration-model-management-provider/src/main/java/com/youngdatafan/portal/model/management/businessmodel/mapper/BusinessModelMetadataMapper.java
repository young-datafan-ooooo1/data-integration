package com.youngdatafan.portal.model.management.businessmodel.mapper;

import com.youngdatafan.portal.model.management.businessmodel.dto.BasicModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.businessmodel.dto.BusinessModelMetaDataCopyDTO;
import com.youngdatafan.portal.model.management.businessmodel.entity.BusinessModelMetadata;
import com.youngdatafan.portal.model.management.forward.superset.XmlBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface BusinessModelMetadataMapper {
    int insert(BusinessModelMetadata record);

    int insertSelective(BusinessModelMetadata record);

    int deleteByModelName(@Param("businessModelName") String businessModelName);

    int insertBusinessModelMetaData(@Param("list") List<BusinessModelMetadata> list);

    int insertIntoBatch(List<BusinessModelMetaDataCopyDTO> record);



    List<BasicModelAndMetaDataDTO> selectBasicModelColumns(@Param("userId") String userId, @Param("basicModel") String basicModel);

    List<XmlBean> findModelMetaDataByName(@Param("businessModelName") String modelName, @Param("list") Set<String> list);

}