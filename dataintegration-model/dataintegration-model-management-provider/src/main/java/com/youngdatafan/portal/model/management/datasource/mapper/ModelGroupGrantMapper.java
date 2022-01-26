package com.youngdatafan.portal.model.management.datasource.mapper;

import com.youngdatafan.portal.model.management.datasource.entity.ModelGroupGrant;

public interface ModelGroupGrantMapper {
    int insert(ModelGroupGrant record);

    int insertSelective(ModelGroupGrant record);
}