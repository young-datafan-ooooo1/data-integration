package com.youngdatafan.kettle.springboot.core.mapper;

import com.youngdatafan.kettle.springboot.core.entity.DpDeProjectExecStatus;
import org.apache.ibatis.annotations.Param;

public interface DpDeProjectExecStatusMapper {
    int deleteByPrimaryKey(@Param("projectId") String projectId, @Param("userId") String userId);

    int insert(DpDeProjectExecStatus record);

    int insertSelective(DpDeProjectExecStatus record);

    DpDeProjectExecStatus selectByPrimaryKey(@Param("projectId") String projectId, @Param("userId") String userId);

    DpDeProjectExecStatus select(@Param("projectId") String projectId, @Param("userId") String userId, @Param("stepName") String stepName);

    int updateByPrimaryKeySelective(DpDeProjectExecStatus record);

    int updateByPrimaryKey(DpDeProjectExecStatus record);
}
