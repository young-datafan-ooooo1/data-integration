package com.youngdatafan.portal.model.management.util.mapper;

import com.youngdatafan.portal.model.management.util.model.ExcelColumns;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExcelColumnsMapper {
    int deleteByPrimaryKey(@Param("tableId") String tableId, @Param("columnIndex") Integer columnIndex);

    int insert(ExcelColumns record);

    int insertSelective(ExcelColumns record);

    ExcelColumns selectByPrimaryKey(@Param("tableId") String tableId, @Param("columnIndex") Integer columnIndex);

    int updateByPrimaryKeySelective(ExcelColumns record);

    int updateByPrimaryKey(ExcelColumns record);

    List<ExcelColumns> selectByTable(@Param("tableId") String tableId);

}