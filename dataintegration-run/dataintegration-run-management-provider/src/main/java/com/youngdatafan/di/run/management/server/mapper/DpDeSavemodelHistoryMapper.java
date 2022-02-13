package com.youngdatafan.di.run.management.server.mapper;

import com.youngdatafan.di.run.management.server.dto.SavemodelHistoryDTO;
import com.youngdatafan.di.run.management.server.entity.DpDeSavemodelHistory;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DpDeSavemodelHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByPrimaryKeys(@Param("ids") Long[] ids);

    int insert(DpDeSavemodelHistory record);

    int insertSelective(DpDeSavemodelHistory record);

    DpDeSavemodelHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DpDeSavemodelHistory record);

    int updateByPrimaryKey(DpDeSavemodelHistory record);

    List<SavemodelHistoryDTO> selectHistory(@Param("userId") String userId, @Param("modelName") String modelName
            , @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
