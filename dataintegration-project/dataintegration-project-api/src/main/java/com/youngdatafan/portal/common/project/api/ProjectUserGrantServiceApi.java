package com.youngdatafan.portal.common.project.api;

import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.common.project.dto.ProjectUserGrantDTO;
import com.youngdatafan.portal.common.project.dto.UserInfoDTO;
import com.youngdatafan.portal.common.project.vo.ProjectUserGrantInsertVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目用户授权管理API接口.
 *
 * @author gavin
 * @since 2020/2/11 10:25 上午
 */
@Api(tags = "项目用户授权管理API接口")
public interface ProjectUserGrantServiceApi {

    /**
     * 查询项目的用户授权信息.
     *
     * @param projectId 项目编号
     * @param pageNum   页数
     * @param pageSize  每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据项目名称模糊查询指项目信息", produces = "application/json")
    @GetMapping(value = "/selectUserGrant/{projectId}")
    Result<PageInfo<ProjectUserGrantDTO>, Object> selectUserGrant(@ApiParam("项目编号") @PathVariable("projectId") String projectId,
                                                                  @ApiParam("页数") @RequestParam int pageNum, @ApiParam("每页记录数") @RequestParam int pageSize);

    /**
     * 重新授权.
     *
     * @param projectUserGrantInsertVO projectUserGrantInsertVO
     * @return Boolean
     */
    @ApiOperation(value = "根据项目名称模糊查询指项目信息", produces = "application/json")
    @PostMapping(value = "/reGrant")
    Result<Boolean, Object> reGrant(@Validated @RequestBody ProjectUserGrantInsertVO projectUserGrantInsertVO);

    /**
     * 查询出没授权给项目的用户.
     *
     * @param userId    userId
     * @param projectId projectId
     * @param describe  describe
     * @return UserInfoDTO
     */
    @ApiOperation(value = "查询出没授权给项目的用户", produces = "application/json")
    @GetMapping(value = "/selectNotGrantedUser/{projectId}")
    Result<List<UserInfoDTO>, Object> selectNotGrantedUser(@RequestHeader("authorization-userId") String userId, @ApiParam("项目编号") @PathVariable("projectId") String projectId,
                                                           @RequestParam String describe);

}
