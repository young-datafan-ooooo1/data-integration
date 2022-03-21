package com.youngdatafan.portal.common.project.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.portal.common.project.api.ProjectServiceApi;
import com.youngdatafan.portal.common.project.dto.GroupDTO;
import com.youngdatafan.portal.common.project.dto.ProjectDTO;
import com.youngdatafan.portal.common.project.dto.ProjectFileDTO;
import com.youngdatafan.portal.common.project.dto.UserGroupDTO;
import com.youngdatafan.portal.common.project.entity.DpPortalProject;
import com.youngdatafan.portal.common.project.service.DpPortalProjectOnlineService;
import com.youngdatafan.portal.common.project.service.ProjectService;
import com.youngdatafan.portal.common.project.vo.ProjectAddVO;
import com.youngdatafan.portal.common.project.vo.ProjectFileVO;
import com.youngdatafan.portal.common.project.vo.ProjectUpdateVO;
import io.swagger.annotations.ApiOperation;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 项目接口控制器.
 *
 * @author gavin
 * @since 2020/2/12 11:00 上午
 */
@RestController
@RequestMapping("/project")
public class ProjectServiceApiController implements ProjectServiceApi {

    private final ProjectService projectService;

    private DpPortalProjectOnlineService dpPortalProjectOnlineService;

    @Autowired
    public ProjectServiceApiController(ProjectService projectService, DpPortalProjectOnlineService dpPortalProjectOnlineService) {
        this.projectService = projectService;
        this.dpPortalProjectOnlineService = dpPortalProjectOnlineService;
    }

    @Override
    public Result<String, Object> add(@Validated @RequestBody ProjectAddVO projectAddVO, @RequestHeader(value = "authorization-userId") String userId,
                                      @RequestHeader(value = "authorization-userName") String userName) {
        return Result.success(projectService.add(projectAddVO, userId, userName));
    }

    /**
     * 上传项目.
     *
     * @param projectAddVO modelGroupAddVO
     * @return 接口应答
     */
    @ApiOperation(value = "上传项目", notes = "content返回的是项目id", produces = "application/json")
    @PostMapping(value = "/upload")
    Result<String, Object> upload(@Validated ProjectAddVO projectAddVO, @RequestHeader(value = "authorization-userId") String userId,
                                  @RequestHeader(value = "authorization-userName") String userName, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (fileMap.isEmpty()) {
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "文件未上传");
        }
        MultipartFile file = multipartRequest.getFile("file");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String oneLine = "";
            while ((oneLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(oneLine);
            }
            ProjectFileVO projectFileVO = new ProjectFileVO();
            projectFileVO.setProjectFile(stringBuffer.toString());
            projectAddVO.setProjectFileVO(projectFileVO);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return Result.success(projectService.add(projectAddVO, userId, userName));
    }

    @Override
    public void download(@RequestParam("projectId") String projectId, HttpServletResponse response) {
        DpPortalProject project = projectService.selectProjectById(projectId);
        ProjectFileDTO fileStr = projectService.downloadProjectFile(projectId);
        OutputStream outputSream = null;
        ByteArrayInputStream in = null;
        if (fileStr != null && StringUtils.isNoneBlank(fileStr.getProjectFile())) {
            try {
                byte[] bytes = fileStr.getProjectFile().getBytes();
                in = new ByteArrayInputStream(bytes);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(project.getProjectName() + ".json", "UTF-8"))));
                outputSream = response.getOutputStream();
                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf, 0, 1024)) != -1) {
                    outputSream.write(buf, 0, len);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    outputSream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public Result<Boolean, Object> update(@Validated @RequestBody ProjectUpdateVO projectUpdateVO, @RequestHeader(value = "authorization-userId") String userId,
                                          @RequestHeader(value = "authorization-userName") String userName) {
        return Result.success(projectService.update(projectUpdateVO, userId, userName) > 0);
    }

    @Override
    public Result<String, Object> updateSelective(@Validated @RequestBody ProjectUpdateVO projectUpdateVO, @RequestHeader(value = "authorization-userId") String userId,
                                                  @RequestHeader(value = "authorization-userName") String userName) {
        return Result.success(projectService.updateSelective(projectUpdateVO, userId, userName));
    }

    @Transactional
    @Override
    public Result<Boolean, Object> delete(@PathVariable("projectId") String projectId) {
        int ret = projectService.delete(projectId);
        if (ret > 0) {
            dpPortalProjectOnlineService.deleteByPrimaryKey(projectId);
        }
        return Result.success(ret > 0);
    }

    @Transactional
    @Override
    public Result<Boolean, Object> deleteBatch(@RequestParam("projectIds") String projectIds) {
        String[] ids = projectIds.split(",");
        projectService.deleteBath(ids);
        dpPortalProjectOnlineService.batchDelete(ids);
        return Result.success(true);
    }

    @Override
    public Result<ProjectDTO, Object> get(@PathVariable("projectId") String projectId) {
        return Result.success(projectService.get(projectId));
    }

    @Override
    public Result<ProjectFileDTO, Object> getProjectFile(@PathVariable("projectId") String projectId) {
        return Result.success(projectService.getProjectFile(projectId));
    }

    @Override
    public Result<PageInfo<ProjectDTO>, Object> selectLikeByProjectName(@RequestParam(value = "projectName", required = false) String projectName,
                                                                        @RequestParam(value = "projectType") String projectType, @RequestHeader(value = "authorization-userId") String userId,
                                                                        @RequestParam int pageNum, @RequestParam int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<ProjectDTO> pageInfo = new PageInfo<>(projectService.selectLikeByProjectName(projectName, projectType, userId));

        return Result.success(pageInfo);
    }

    @Override
    public Result<PageInfo<ProjectDTO>, Object> selectLikeByGroupId(@RequestParam(value = "projectName", required = false) String projectName,
                                                                    @PathVariable("groupId") String groupId, @RequestParam(value = "projectType") String projectType,
                                                                    @RequestHeader(value = "authorization-userId") String userId,
                                                                    @RequestParam int pageNum, @RequestParam int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<ProjectDTO> pageInfo = new PageInfo<>(projectService.selectLikeByGroupId(projectName, groupId, projectType, userId));

        return Result.success(pageInfo);
    }

    @Override
    public Result<PageInfo<GroupDTO>, Object> selectMyProject(@RequestParam(value = "projectName", required = false) String projectName,
                                                              @RequestParam(value = "groupName", required = false) String groupName, @RequestHeader(value = "authorization-userId") String userId,
                                                              @RequestParam(value = "projectType") String projectType, @RequestParam int pageNum, @RequestParam int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<GroupDTO> pageInfo = new PageInfo<>(projectService.selectMyProject(projectName, groupName, userId, projectType));

        return Result.success(pageInfo);
    }

    @Override
    public Result<PageInfo<GroupDTO>, Object> selectAllProject(@RequestParam(value = "projectName", required = false) String projectName, @RequestHeader(value = "authorization-userId") String userId,
                                                               @RequestParam(value = "projectType") String projectType, @RequestParam int pageNum, @RequestParam int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<GroupDTO> pageInfo = new PageInfo<>(projectService.selectAllProject(projectName, userId, projectType));

        return Result.success(pageInfo);
    }

    @Override
    public Result<PageInfo<UserGroupDTO>, Object> selectGrantMyProject(@RequestParam(value = "projectName", required = false) String projectName,
                                                                       @RequestHeader(value = "authorization-userId") String userId,
                                                                       @RequestParam(value = "projectType") String projectType,
                                                                       @RequestParam int pageNum, @RequestParam int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<UserGroupDTO> pageInfo = new PageInfo<>(projectService.selectGrantMyProject(projectName, userId, projectType));

        return Result.success(pageInfo);
    }

}
