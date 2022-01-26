package com.youngdatafan.portal.model.management.outinterfacemodel.mapper;

import com.youngdatafan.portal.model.management.forward.superset.XmlBean;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceBasicModelAndMetaDataDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.OutinterfaceModelMetaDataCopyDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.entity.OutinterfaceModelMetadata;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface OutinterfaceModelMetadataMapper {
    int insert(OutinterfaceModelMetadata record);

    int insertSelective(OutinterfaceModelMetadata record);

    int deleteByModelName(@Param("outinterfaceModelName") String outinterfaceModelName);

    int insertOutinterfaceModelMetaData(@Param("list") List<OutinterfaceModelMetadata> list);

    int insertIntoBatch(List<OutinterfaceModelMetaDataCopyDTO> record);



    List<OutinterfaceBasicModelAndMetaDataDTO> selectBasicModelColumns(@Param("userId") String userId, @Param("basicModel") String basicModel);

    List<XmlBean> findModelMetaDataByName(@Param("outinterfaceModelName") String modelName, @Param("list") Set<String> list);

}
