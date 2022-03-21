package com.youngdatafan.portal.common.project.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.common.project.dto.ProjectUserGrantDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.vo.ProjectUserGrantInsertVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gavin
 * @since 2020/2/11 10:25 上午
 */
@Api(tags = "项目用户授权管理API接口")
public interface ProjectUserGrantServiceApi {

    /**
     * 查询项目的用户授权信息
     *
     * @param projectId 项目编号
     * @param pageNum   页数
     * @param pageSize  每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据项目名称模糊查询指项目信息", produces = "application/json")
    @GetMapping(value = "/selectUserGrant/{projectId}")
    Result<PageInfo<ProjectUserGrantDTO>, Object> selectUserGrant(@ApiParam("项目编号") @PathVariable("projectId") String projectId
            , @ApiParam("页数") @RequestParam int pageNum
            , @ApiParam("每页记录数") @RequestParam int pageSize);

    /**
     * 重新授权
     *
     * @param projectUserGrantInsertVO ProjectUserGrantInsertVO
     * @return 接口应答
     */
    @ApiOperation(value = "根据项目名称模糊查询指项目信息", produces = "application/json")
    @PostMapping(value = "/reGrant")
    Result<Boolean, Object> reGrant(@Validated @RequestBody ProjectUserGrantInsertVO projectUserGrantInsertVO);

    @ApiOperation(value = "查询出没授权给项目的用户", produces = "application/json")
    @GetMapping(value = "/selectNotGrantedUser/{projectId}")
    Result<List<UserInfoDTO>, Object> selectNotGrantedUser(@RequestHeader("authorization-userId") String userId,@ApiParam("项目编号") @PathVariable("projectId") String projectId,@RequestParam String describe);

}
