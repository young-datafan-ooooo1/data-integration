package com.youngdatafan.portal.common.project.mapper;

import com.youngdatafan.portal.common.project.dto.ProjectFileDTO;
import com.youngdatafan.portal.common.project.entity.DpPortalProjectFile;
import org.apache.ibatis.annotations.Param;

/**
 * 项目管理mapper.
 */
public interface DpPortalProjectFileMapper {
    /**
     * 通过id删除.
     *
     * @param projectId id
     * @return int
     */
    int deleteByPrimaryKey(String projectId);

    /**
     * 保存.
     *
     * @param record record
     * @return int
     */
    int insert(DpPortalProjectFile record);

    /**
     * 保存.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpPortalProjectFile record);

    /**
     * 通过id查询.
     *
     * @param projectId id
     * @return ProjectFileDTO
     */
    ProjectFileDTO selectByPrimaryKey(String projectId);

    /**
     * 更新项目.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalProjectFile record);


    /**
     * 更新项目.
     *
     * @param record record
     * @return int
     */
    int updateByPrimaryKey(DpPortalProjectFile record);

    /**
     * 批量删除.
     *
     * @param projectIds 项目ids
     * @return int
     */
    int deleteByProjectIds(@Param("projectIds") String[] projectIds);
}
