package com.youngdatafan.portal.common.project.api;

import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.common.project.dto.GroupDTO;
import com.youngdatafan.portal.common.project.dto.ProjectDTO;
import com.youngdatafan.portal.common.project.dto.ProjectFileDTO;
import com.youngdatafan.portal.common.project.dto.UserGroupDTO;
import com.youngdatafan.portal.common.project.vo.ProjectAddVO;
import com.youngdatafan.portal.common.project.vo.ProjectUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目管理API接口.
 *
 * @author gavin
 * @since 2020/2/11 10:25 上午
 */
@Api(tags = "项目管理API接口")
public interface ProjectServiceApi {

    /**
     * 新增项目.
     *
     * @param userId     userId
     * @param userName   userName
     * @param groupAddVO modelGroupAddVO
     * @return 接口应答
     */
    @ApiOperation(value = "新增项目", notes = "content返回的是项目id", produces = "application/json")
    @PostMapping(value = "/add")
    Result<String, Object> add(@Validated @RequestBody ProjectAddVO groupAddVO, @RequestHeader(value = "authorization-userId") String userId,
                               @RequestHeader(value = "authorization-userName") String userName);

    /**
     * 下载项目.
     *
     * @param projectId projectId
     * @param response  response
     **/
    @ApiOperation(value = "下载项目", notes = "返回的是文件流", produces = "application/json")
    @GetMapping(value = "/download")
    void download(@RequestParam("projectId") String projectId, HttpServletResponse response);

    /**
     * 修改项目.
     *
     * @param userId          userId
     * @param userName        userName
     * @param projectUpdateVO projectUpdateVO
     * @return 接口应答
     */
    @ApiOperation(value = "修改项目", notes = "参数对象中如果有null值，数据库会被更新成null", produces = "application/json")
    @PutMapping(value = "/update")
    Result<Boolean, Object> update(@Validated @RequestBody ProjectUpdateVO projectUpdateVO,
                                   @RequestHeader(value = "authorization-userId") String userId, @RequestHeader(value = "authorization-userName") String userName);

    /**
     * 修改项目,选择性修改.
     *
     * @param userId          userId
     * @param userName        userName
     * @param projectUpdateVO projectUpdateVO
     * @return 接口应答
     */
    @ApiOperation(value = "修改项目，选择性修改", notes = "参数对象中如果有null值，数据库将不会被更新", produces = "application/json")
    @PutMapping(value = "/updateSelective")
    Result<String, Object> updateSelective(@Validated @RequestBody ProjectUpdateVO projectUpdateVO, @RequestHeader(value = "authorization-userId") String userId,
                                           @RequestHeader(value = "authorization-userName") String userName);

    /**
     * 删除项目.
     *
     * @param projectId 项目id
     * @return 接口应答
     */
    @ApiOperation(value = "删除项目", notes = "根据id删除项目", produces = "application/json")
    @DeleteMapping(value = "/delete/{projectId}")
    Result<Boolean, Object> delete(@PathVariable("projectId") String projectId);

    /**
     * 删除项目.
     *
     * @param projectIds 项目id 数组
     * @return 接口应答
     */
    @ApiOperation(value = "删除项目", notes = "根据id删除项目", produces = "application/json")
    @DeleteMapping(value = "/deleteBatch")
    Result<Boolean, Object> deleteBatch(@ApiParam("项目id 数组") @RequestParam("projectIds") String projectIds);


    /**
     * 根据id获取项目.
     *
     * @param projectId 项目id
     * @return 接口应答
     */
    @ApiOperation(value = "获取项目", notes = "根据id获取项目", produces = "application/json")
    @GetMapping(value = "/get/{projectId}")
    Result<ProjectDTO, Object> get(@PathVariable("projectId") String projectId);

    /**
     * 根据id获取项目附件.
     *
     * @param projectId 项目id
     * @return 接口应答
     */
    @ApiOperation(value = "获取项目", notes = "根据id获取项目附件", produces = "application/json")
    @GetMapping(value = "/getProjectFile/{projectId}")
    Result<ProjectFileDTO, Object> getProjectFile(@PathVariable("projectId") String projectId);

    /**
     * 根据项目名称模糊查询指项目信息.
     *
     * @param projectType projectType
     * @param userId      userId
     * @param projectName 项目名称
     * @param pageNum     页数
     * @param pageSize    每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据项目名称模糊查询指项目信息", produces = "application/json")
    @GetMapping(value = "/selectLikeByProjectName")
    Result<PageInfo<ProjectDTO>, Object> selectLikeByProjectName(@ApiParam("项目名称，模糊查询") @RequestParam(value = "projectName", required = false) String projectName,
                                                                 @ApiParam("项目类型，（使用拼音简写）：探索脚本(TSJB)、集成脚本(JSJB)、报表(BB)") @RequestParam(value = "projectType") String projectType,
                                                                 @RequestHeader(value = "authorization-userId") String userId, @ApiParam("页数") @RequestParam("pageNum") int pageNum,
                                                                 @ApiParam("每页记录数") @RequestParam("pageSize") int pageSize);

    /**
     * 根据项目名称模糊查询指定分组下的项目信息.
     *
     * @param projectType projectType
     * @param userId      userId
     * @param projectName 分组名称
     * @param groupId     分组编号
     * @param pageNum     页数
     * @param pageSize    每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据分组名称注模糊查询指定分组下的项目信息", produces = "application/json")
    @GetMapping(value = "/selectLikeByProjectName/{groupId}")
    Result<PageInfo<ProjectDTO>, Object> selectLikeByGroupId(@ApiParam("项目名称，模糊查询") @RequestParam(value = "projectName", required = false) String projectName,
                                                             @ApiParam("分组编号") @PathVariable("groupId") String groupId,
                                                             @ApiParam("项目类型，（使用拼音简写）：探索脚本(TSJB)、集成脚本(JSJB)、报表(BB)") @RequestParam(value = "projectType") String projectType,
                                                             @RequestHeader(value = "authorization-userId") String userId, @ApiParam("页数") @RequestParam("pageNum") int pageNum,
                                                             @ApiParam("每页记录数") @RequestParam("pageSize") int pageSize);

    /**
     * 根据项目名称模糊查询 我的项目.
     *
     * @param groupName   groupName
     * @param projectType projectType
     * @param userId      userId
     * @param projectName 分组名称
     * @param pageNum     页数
     * @param pageSize    每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据项目名称模糊查询 我的项目", produces = "application/json")
    @GetMapping(value = "/selectMyProject")
    Result<PageInfo<GroupDTO>, Object> selectMyProject(@ApiParam("项目名称，模糊查询") @RequestParam(value = "projectName", required = false) String projectName,
                                                       @RequestParam(value = "groupName", required = false) String groupName,
                                                       @RequestHeader(value = "authorization-userId") String userId,
                                                       @ApiParam("项目类型，（使用拼音简写）：探索脚本(TSJB)、集成脚本(JSJB)、报表(BB)") @RequestParam(value = "projectType") String projectType,
                                                       @ApiParam("页数") @RequestParam("pageNum") int pageNum, @ApiParam("每页记录数") @RequestParam("pageSize") int pageSize);

    /**
     * 根据项目名称模糊查询 我的项目.
     *
     * @param projectType projectType
     * @param userId      userId
     * @param projectName 分组名称
     * @param pageNum     页数
     * @param pageSize    每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据项目名称模糊查询 我的项目", produces = "application/json")
    @GetMapping(value = "/selectAllProject")
    Result<PageInfo<GroupDTO>, Object> selectAllProject(@ApiParam("项目名称，模糊查询") @RequestParam(value = "projectName", required = false) String projectName,
                                                        @RequestHeader(value = "authorization-userId") String userId, @ApiParam("项目类型，（使用拼音简写）：探索脚本(TSJB)、集成脚本(JSJB)、报表(BB)")
                                                        @RequestParam(value = "projectType") String projectType,
                                                        @ApiParam("页数") @RequestParam("pageNum") int pageNum, @ApiParam("每页记录数") @RequestParam("pageSize") int pageSize);

    /**
     * 根据项目名称模糊查询 授权给我的项目.
     *
     * @param projectType projectType
     * @param userId      userId
     * @param projectName 分组名称
     * @param pageNum     页数
     * @param pageSize    每页记录数
     * @return 接口应答
     */
    @ApiOperation(value = "根据项目名称模糊查询 授权给我的项目", produces = "application/json")
    @GetMapping(value = "/selectGrantMyProject")
    Result<PageInfo<UserGroupDTO>, Object> selectGrantMyProject(@ApiParam("项目名称，模糊查询") @RequestParam(value = "projectName", required = false) String projectName,
                                                                @RequestHeader(value = "authorization-userId") String userId,
                                                                @ApiParam("项目类型，（使用拼音简写）：探索脚本(TSJB)、集成脚本(JSJB)、报表(BB)") @RequestParam(value = "projectType") String projectType,
                                                                @ApiParam("页数") @RequestParam("pageNum") int pageNum,
                                                                @ApiParam("每页记录数") @RequestParam("pageSize") int pageSize);

}
