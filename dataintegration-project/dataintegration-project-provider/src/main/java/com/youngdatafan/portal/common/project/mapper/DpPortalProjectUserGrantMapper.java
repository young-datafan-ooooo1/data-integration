package com.youngdatafan.portal.common.project.mapper;

import com.youngdatafan.portal.common.project.dto.ProjectUserGrantDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.entity.DpPortalProjectUserGrant;
import com.youngdatafan.portal.common.project.vo.ProjectUserGrantVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DpPortalProjectUserGrantMapper.
 */
public interface DpPortalProjectUserGrantMapper {
    /**
     * 保存.
     *
     * @param record DpPortalProjectUserGrant
     * @return int
     */
    int insert(DpPortalProjectUserGrant record);

    /**
     * 保存.
     *
     * @param record DpPortalProjectUserGrant
     * @return int
     */
    int insertSelective(DpPortalProjectUserGrant record);

    /**
     * 通过项目Id查询.
     *
     * @param projectId 想Id
     * @return list
     */
    List<ProjectUserGrantDTO> selectUserGrant(@Param("projectId") String projectId);

    /**
     * 通过项目Id 删除.
     *
     * @param projectId 项目id
     * @return int
     */
    int deleteByProjectId(@Param("projectId") String projectId);

    /**
     * 项目授权.
     *
     * @param projectId         项目id
     * @param projectUserGrants 授权用户
     * @return int
     */
    int insertGrant(@Param("projectId") String projectId, @Param("projectUserGrants") List<ProjectUserGrantVO> projectUserGrants);

    /**
     * 通过id删除/.
     *
     * @param projectIds 项目id
     * @return int
     */
    int deleteByProjectIds(@Param("projectIds") String[] projectIds);

    /**
     * 茶轩授权用户.
     *
     * @param projectId 项目id
     * @param userId    用户Id
     * @param describe  描述
     * @return list
     */
    List<UserInfoDTO> selectNotGrantedUser(@Param("projectId") String projectId, @Param("userId") String userId, @Param("describe") String describe);
}
