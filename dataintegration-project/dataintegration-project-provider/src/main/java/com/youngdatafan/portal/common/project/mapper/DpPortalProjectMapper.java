package com.youngdatafan.portal.common.project.mapper;

import com.youngdatafan.portal.common.project.dto.ProjectDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.entity.DpPortalProject;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalProjectMapper.
 */
public interface DpPortalProjectMapper {
    /**
     * 通过Id删除.
     *
     * @param projectId id
     * @return int
     */
    int deleteByPrimaryKey(String projectId);

    /**
     * 项目保存.
     *
     * @param record record
     * @return int
     */
    int insert(DpPortalProject record);

    /**
     * 项目保存.
     *
     * @param record record
     * @return int
     */
    int insertSelective(DpPortalProject record);

    /**
     * 通过id查询.
     *
     * @param projectId 项目Id
     * @return DpPortalProject
     */
    DpPortalProject selectByPrimaryKey(String projectId);

    /**
     * 更新.
     *
     * @param record DpPortalProject
     * @return int
     */
    int updateByPrimaryKeySelective(DpPortalProject record);

    /**
     * 更新.
     *
     * @param record DpPortalProject
     * @return int
     */
    int updateByPrimaryKey(DpPortalProject record);

    /**
     * 查询.
     *
     * @param projectName 项目名称
     * @param projectType 项目分类
     * @param userId      用户Id
     * @return list
     */
    List<ProjectDTO> selectLikeByProjectName(@Param("projectName") String projectName, @Param("projectType") String projectType, @Param("userId") String userId);

    /**
     * 查询.
     *
     * @param groupId     分组Id
     * @param projectName 项目名称
     * @param projectType 项目分类
     * @param userId      用户Id
     * @return list
     */
    List<ProjectDTO> selectLikeByGroupId(@Param("projectName") String projectName, @Param("groupId") String groupId, @Param("projectType") String projectType, @Param("userId") String userId);

    /**
     * 查询.
     *
     * @param groupName   分组名称
     * @param projectName 项目名称
     * @param projectType 项目分类
     * @param userId      用户Id
     * @return list
     */
    List<ProjectDTO> selectMyProject(@Param("projectName") String projectName, @Param("groupName") String groupName, @Param("userId") String userId, @Param("projectType") String projectType);

    /**
     * 查询.
     *
     * @param projectName 项目名称
     * @param projectType 项目分类
     * @param userId      用户Id
     * @return list
     */
    List<UserInfoDTO> selectGrantMyProject(@Param("projectName") String projectName, @Param("userId") String userId, @Param("projectType") String projectType);

    /**
     * 查询.
     *
     * @param projectName 项目名称
     * @param projectType 项目分类
     * @param userId      用户Id
     * @return list
     */
    List<ProjectDTO> selectAllProject(@Param("projectName") String projectName, @Param("userId") String userId, @Param("projectType") String projectType);

    /**
     * 项目已经存在.
     *
     * @param record DpPortalProject
     * @return int
     */
    int selectExists(DpPortalProject record);

    /**
     * 批量删除.
     *
     * @param projectIds 项目ids
     * @return int
     */
    int deleteByProjectIds(@Param("projectIds") String[] projectIds);
}
