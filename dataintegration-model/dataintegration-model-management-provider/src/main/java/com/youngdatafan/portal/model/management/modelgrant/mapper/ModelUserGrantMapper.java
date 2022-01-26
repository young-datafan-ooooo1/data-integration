package com.youngdatafan.portal.model.management.modelgrant.mapper;

import com.youngdatafan.portal.model.management.modelgrant.entity.ModelUserGrant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelUserGrantMapper {
    int insert(ModelUserGrant record);

    int insertSelective(ModelUserGrant record);

    int insertModelUserGrants(@Param("list") List<ModelUserGrant> list);

    int deleteModelByUserId(@Param("userId") String userId);

    int deleteModelByUserName(@Param("userName") String userName);
}