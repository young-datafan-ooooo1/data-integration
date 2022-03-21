package com.youngdatafan.portal.common.project.service;

import com.youngdatafan.dataintegration.core.exception.FormValidationException;
import com.youngdatafan.dataintegration.core.util.Md5Utils;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.portal.common.project.dto.GroupDTO;
import com.youngdatafan.portal.common.project.dto.ProjectDTO;
import com.youngdatafan.portal.common.project.dto.ProjectFileDTO;
import com.youngdatafan.portal.common.project.dto.UserGroupDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.entity.DpPortalProject;
import com.youngdatafan.portal.common.project.mapper.DpPortalProjectFileMapper;
import com.youngdatafan.portal.common.project.mapper.DpPortalProjectMapper;
import com.youngdatafan.portal.common.project.vo.ProjectAddVO;
import com.youngdatafan.portal.common.project.vo.ProjectFileVO;
import com.youngdatafan.portal.common.project.vo.ProjectUpdateVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目.
 * service, * @author gavin
 *
 * @since 2020/2/12 11:03 上午
 */
@Slf4j
@Service
public class ProjectService {

    private final AsyncProjectService asyncProjectService;

    private final DpPortalProjectMapper projectMapper;

    private final DpPortalProjectFileMapper projectFileMapper;

    private final ProjectUserGrantService projectUserGrantService;

    @Autowired
    public ProjectService(AsyncProjectService asyncProjectService, DpPortalProjectMapper projectMapper, DpPortalProjectFileMapper projectFileMapper, ProjectUserGrantService projectUserGrantService) {
        this.asyncProjectService = asyncProjectService;
        this.projectMapper = projectMapper;
        this.projectFileMapper = projectFileMapper;
        this.projectUserGrantService = projectUserGrantService;
    }

    /**
     * 项目保存.
     *
     * @param projectAddVO 项目add
     * @param userId       用户id
     * @param userName     用户名称
     * @return string
     */
    public String add(ProjectAddVO projectAddVO, String userId, String userName) {
        DpPortalProject project = new DpPortalProject();
        // 创建数据库实体对象
        BeanUtils.copyProperties(projectAddVO, project);

        if (StringUtils.isBlank(projectAddVO.getProjectId())) {
            project.setProjectId(UUIDUtils.generateUUID32());
        } else {
            project.setProjectId(projectAddVO.getProjectId());
        }

        project.setCreateUserId(userId);
        project.setCreateUserName(userName);
        project.setCreateTime(new Date());

        //重名检查
        if (projectMapper.selectExists(project) > 0) {
            throw new FormValidationException(StatusCode.CODE_10004.getCode(), "projectName", "项目名称已存在，请更换项目名称。");
        }

        // 保存到数据库
        projectMapper.insert(project);

        //异步写入项目附件
        asyncProjectService.asyncInsertProjectFile(projectAddVO, project);

        return project.getProjectId();
    }

    /**
     * 项目更新.
     *
     * @param projectUpdateVO 项目更新
     * @param userId          用户id
     * @param userName        用户名称
     * @return int
     */
    public int update(ProjectUpdateVO projectUpdateVO, String userId, String userName) {
        DpPortalProject project = new DpPortalProject();
        // 创建数据库实体对象
        BeanUtils.copyProperties(projectUpdateVO, project);

        project.setUpdateTime(new Date());
        project.setUpdateUserId(userId);
        project.setUpdateUserName(userName);

        int count = projectMapper.updateByPrimaryKeySelective(project);

        //异步更新项目附件
        asyncProjectService.asyncUpdateProjectFile(projectUpdateVO);

        return count;
    }

    /**
     * 更新项目.
     *
     * @param projectUpdateVO 项目更新vo
     * @param userId          用户id
     * @param userName        用户名称
     * @return string
     */
    public String updateSelective(ProjectUpdateVO projectUpdateVO, String userId, String userName) {
        DpPortalProject project = new DpPortalProject();
        // 创建数据库实体对象
        BeanUtils.copyProperties(projectUpdateVO, project);

        project.setUpdateTime(new Date());
        project.setUpdateUserId(userId);
        project.setUpdateUserName(userName);

        // 更新项目基本信息
        projectMapper.updateByPrimaryKeySelective(project);

        ProjectFileVO projectFileVO = projectUpdateVO.getProjectFileVO();
        if (projectFileVO == null || projectFileVO.getProjectFile() == null) {
            return null;
        }

        final String fileMd5 = Md5Utils.encode(projectFileVO.getProjectFile(), "UTF-8", false);
        if (fileMd5.equals(projectFileVO.getMd5())) {
            log.info("文件内容未修改，不需要更新，projectId: {}", projectUpdateVO.getProjectId());

        } else {
            //异步更新项目附件
            asyncProjectService.asyncUpdateProjectFile(projectUpdateVO);
        }

        return fileMd5;
    }

    /**
     * 项目删除.
     *
     * @param projectId 项目id
     * @return int
     */
    public int delete(String projectId) {
        // 删除附件
        projectFileMapper.deleteByPrimaryKey(projectId);

        // 删除用户授权
        projectUserGrantService.deleteByProjectId(projectId);

        // 删除明细
        return projectMapper.deleteByPrimaryKey(projectId);
    }

    /**
     * 批量删除.
     *
     * @param projectIds 项目ids
     */
    public void deleteBath(String[] projectIds) {
        if (ArrayUtils.isEmpty(projectIds)) {
            return;
        }
        // 删除附件
        projectFileMapper.deleteByProjectIds(projectIds);
        // 删除用户授权
        projectUserGrantService.deleteByProjectIds(projectIds);
        // 删除明细
        projectMapper.deleteByProjectIds(projectIds);
    }

    /**
     * 获取项目信息.
     *
     * @param projectId 项目id
     * @return ProjectDTO
     */
    public ProjectDTO get(String projectId) {
        DpPortalProject project = projectMapper.selectByPrimaryKey(projectId);
        if (project == null) {
            return null;
        }

        ProjectDTO projectDTO = new ProjectDTO();
        BeanUtils.copyProperties(project, projectDTO);
        return projectDTO;
    }

    /**
     * 项目文件获取.
     *
     * @param projectId 项目id
     * @return ProjectFileDTO
     */
    public ProjectFileDTO getProjectFile(String projectId) {
        final ProjectFileDTO projectFileDTO = projectFileMapper.selectByPrimaryKey(projectId);
        if (projectFileDTO != null && projectFileDTO.getProjectFile() != null) {
            projectFileDTO.setMd5(Md5Utils.encode(projectFileDTO.getProjectFile(), "UTF-8", false));
        }
        return projectFileDTO;
    }

    /**
     * 查询项目名称.
     *
     * @param projectName 项目名称
     * @param projectType 项目类型
     * @param userId      用户 id
     * @return list
     */
    public List<ProjectDTO> selectLikeByProjectName(String projectName, String projectType, String userId) {
        return projectMapper.selectLikeByProjectName(projectName, projectType, userId);
    }

    /**
     * 查询项目信息.
     *
     * @param projectName 项目名称
     * @param groupId     分组id
     * @param projectType 项目类型
     * @param userId      用户id
     * @return list
     */
    public List<ProjectDTO> selectLikeByGroupId(String projectName, String groupId, String projectType, String userId) {
        return projectMapper.selectLikeByGroupId(projectName, groupId, projectType, userId);
    }

    /**
     * 查询我的项目.
     *
     * @param projectName 项目名称
     * @param groupName   分组名称
     * @param userId      用户Id
     * @param projectType 项目类型
     * @return list
     */
    public List<GroupDTO> selectMyProject(String projectName, String groupName, String userId, String projectType) {
        List<ProjectDTO> projects = projectMapper.selectMyProject(projectName, groupName, userId, projectType);
        return getGroups(projects);
    }

    /**
     * 查询授权项目信息.
     *
     * @param projectName 项目名称
     * @param userId      用户id
     * @param projectType 项目类型
     * @return list
     */
    public List<UserGroupDTO> selectGrantMyProject(String projectName, String userId, String projectType) {
        List<UserInfoDTO> userInfoDTOS = projectMapper.selectGrantMyProject(projectName, userId, projectType);
        return getUserGroups(userInfoDTOS);
    }

    /**
     * 查询所有项目.
     *
     * @param projectName 项目名称
     * @param userId      用户id
     * @param projectType 项目类型
     * @return list
     */
    public List<GroupDTO> selectAllProject(String projectName, String userId, String projectType) {
        List<ProjectDTO> userInfoDTOS = projectMapper.selectAllProject(projectName, userId, projectType);
        return getGroups(userInfoDTOS);
    }

    /**
     * 获取用户组.
     *
     * @param userInfoDTOS 用户信息
     * @return list
     */
    private List<UserGroupDTO> getUserGroups(List<UserInfoDTO> userInfoDTOS) {
        if (userInfoDTOS == null) {
            return null;
        }
        List<UserGroupDTO> userGroupDTOS = new ArrayList<>(userInfoDTOS.size());
        for (UserInfoDTO userInfoDTO : userInfoDTOS) {
            UserGroupDTO userGroupDTO = new UserGroupDTO();
            userGroupDTO.setUserId(userInfoDTO.getUserId());
            userGroupDTO.setDescribe(userInfoDTO.getDescribe());
            List<GroupDTO> groupDTOS = getGroups(userInfoDTO.getProjectDTOList());
            userGroupDTO.setProjects(groupDTOS);
            userGroupDTOS.add(userGroupDTO);
        }
        return userGroupDTOS;
    }

    /**
     * 获取项目组.
     *
     * @param projects 项目
     * @return list
     */
    private List<GroupDTO> getGroups(List<ProjectDTO> projects) {
        if (projects == null) {
            return null;
        }
        List<GroupDTO> groups = new ArrayList<>(projects.size());
        Map<String, GroupDTO> groupMas = new HashMap<>(projects.size());

        for (ProjectDTO project : projects) {
            if (project == null) {
                continue;
            }

            String groupName = project.getGroupName();
            GroupDTO groupDTO = groupMas.get(groupName);

            if (groupDTO == null) {
                groupDTO = new GroupDTO();
                groupDTO.setGroupId(project.getGroupId());
                groupDTO.setGroupName(groupName);
                groupDTO.setDescribe(project.getGroupDescribe());

                // 添加到临时map
                groupMas.put(groupName, groupDTO);
                // 添加到返回集
                groups.add(groupDTO);
            }
            // 添加到组
            groupDTO.add(project);
        }

        return groups;
    }

    /**
     * 下载项目.
     *
     * @param projectId 项目id
     * @return ProjectFileDTO
     */
    public ProjectFileDTO downloadProjectFile(String projectId) {
        return projectFileMapper.selectByPrimaryKey(projectId);
    }

    /**
     * 查询项目信息.
     *
     * @param projectId 项目id
     * @return DpPortalProject
     */
    public DpPortalProject selectProjectById(String projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }
}
