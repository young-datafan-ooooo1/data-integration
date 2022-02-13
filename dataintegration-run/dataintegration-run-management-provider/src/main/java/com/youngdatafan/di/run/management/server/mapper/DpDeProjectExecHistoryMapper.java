package com.youngdatafan.di.run.management.server.mapper;


import com.youngdatafan.di.run.management.server.dto.ProjectHistoryExecuteDTO;
import com.youngdatafan.di.run.management.server.entity.DpDeProjectExecHistory;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DpDeProjectExecHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DpDeProjectExecHistory record);

    int insertSelective(DpDeProjectExecHistory record);

    DpDeProjectExecHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DpDeProjectExecHistory record);

    int updateByPrimaryKey(DpDeProjectExecHistory record);

    List<ProjectHistoryExecuteDTO> selectUserHistoryExecute(@Param("userId") String userId, @Param("projectName") String projectName
            , @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}