package com.youngdatafan.portal.common.project.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.common.project.api.ProjectUserGrantServiceApi;
import com.youngdatafan.portal.common.project.dto.ProjectUserGrantDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.service.ProjectUserGrantService;
import com.youngdatafan.portal.common.project.vo.ProjectUserGrantInsertVO;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目哦用户授权控制器.
 *
 * @author gavin
 * @since 2020/2/12 1:05 下午
 */
@RestController
@RequestMapping("/projectUserGrant")
public class ProjectUserGrantServiceApiController implements ProjectUserGrantServiceApi {

    private final ProjectUserGrantService projectUserGrantService;

    @Autowired
    public ProjectUserGrantServiceApiController(ProjectUserGrantService projectUserGrantService) {
        this.projectUserGrantService = projectUserGrantService;
    }

    @Override
    public Result<PageInfo<ProjectUserGrantDTO>, Object> selectUserGrant(@PathVariable("projectId") String projectId, @ApiParam("页数") @RequestParam int pageNum,
                                                                         @ApiParam("每页记录数") @RequestParam int pageSize) {
        // 设置分页规则
        PageHelper.startPage(pageNum, pageSize);
        // 返回所有分页信息参数为查询所有记录的信息
        PageInfo<ProjectUserGrantDTO> pageInfo = new PageInfo<>(projectUserGrantService.selectUserGrant(projectId));

        return Result.success(pageInfo);
    }

    @Override
    public Result<Boolean, Object> reGrant(@Validated @RequestBody ProjectUserGrantInsertVO projectUserGrantInsertVO) {
        projectUserGrantService.reGrant(projectUserGrantInsertVO);
        return Result.success(true);
    }

    @Override
    public Result<List<UserInfoDTO>, Object> selectNotGrantedUser(String userId, String projectId, String describe) {
        List<UserInfoDTO> userInfoDTOS = projectUserGrantService.selectNotGrantedUser(userId, projectId, describe);
        return Result.success(userInfoDTOS);
    }

}
