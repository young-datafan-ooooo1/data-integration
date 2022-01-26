package com.youngdatafan.portal.model.management.util.mapper;

import com.youngdatafan.portal.model.management.util.model.ExcelTables;

public interface ExcelTablesMapper {
    int deleteByPrimaryKey(String id);

    int insert(ExcelTables record);

    int insertSelective(ExcelTables record);

    ExcelTables selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ExcelTables record);

    int updateByPrimaryKey(ExcelTables record);
}