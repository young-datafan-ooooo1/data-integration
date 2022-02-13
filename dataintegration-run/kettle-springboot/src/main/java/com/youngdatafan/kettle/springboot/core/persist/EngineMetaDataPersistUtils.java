package com.youngdatafan.kettle.springboot.core.persist;

import com.youngdatafan.kettle.springboot.core.entity.DpDeProjectExecStatus;
import com.youngdatafan.kettle.springboot.core.mapper.DpDeProjectExecStatusMapper;

/**
 * 引擎元数据持久化工具类
 *
 * @author gavin
 */
public class EngineMetaDataPersistUtils {

    /**
     * 查询项目执行状态
     *
     * @param projectId 项目id
     * @param userId    用户id
     * @param stepName  步骤名称
     * @return BusinessScriptExecStatus
     */
    public static DpDeProjectExecStatus selectProjectExecStatus(String projectId, String userId, String stepName) {
        DpDeProjectExecStatusMapper projectExecStatusMapper = DpDeProjectExecStatusMapperHelper.getInstance();
        return projectExecStatusMapper.select(projectId, userId, stepName);
    }

    /**
     * 保存临时表名称
     *
     * @param dpDeProjectExecStatus DpDeProjectExecStatus
     */
    public static void saveTableName(DpDeProjectExecStatus dpDeProjectExecStatus) {
        DpDeProjectExecStatusMapper projectExecStatusMapper = DpDeProjectExecStatusMapperHelper.getInstance();
        projectExecStatusMapper.insert(dpDeProjectExecStatus);
    }

    /**
     * 更新执行状态
     *
     * @param dpDeProjectExecStatus DpDeProjectExecStatus
     */
    public static int updateByPrimaryKeySelective(DpDeProjectExecStatus dpDeProjectExecStatus) {
        DpDeProjectExecStatusMapper projectExecStatusMapper = DpDeProjectExecStatusMapperHelper.getInstance();
        return projectExecStatusMapper.updateByPrimaryKeySelective(dpDeProjectExecStatus);
    }

}
