package com.youngdatafan.dataintegration.file.management.api;

import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.vo.FileInfoVO;
import com.youngdatafan.dataintegration.file.management.vo.FileQueryVO;
import com.youngdatafan.dataintegration.file.management.vo.FolderRefreshParamVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantDeptVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantFolderUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.ServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 文件夹模式.
 *
 * @Author: lizihao
 * @Descripition:
 * @Date 2020/4/7 5:41 下午
 */
@Api(tags = "文件夹模式API接口")
public interface FolderOperationApi {

    /**
     * 根据用户id查询授权的文件夹信息(Tree结构).
     * @param userId 用户id
     * @return 授权的文件夹信息(Tree结构)
     */
    @ApiOperation(value = "根据用户id查询授权的文件夹信息(Tree结构)", notes = "根据用户id查询授权的文件夹信息(Tree结构)", produces = "application/json")
    @GetMapping("/userGrantTree")
    Result<List<GrantDeptVO>, Object> queryGranDeptTree(@RequestHeader(value = "authorization-userId") String userId);

    /**
     * 查询管理员的文件夹(Tree结构).
     * @return 查询管理员的文件夹(Tree结构)
     */
    @ApiOperation(value = "查询管理员的文件夹(Tree结构)", notes = "查询管理员的文件夹(Tree结构)", produces = "application/json")
    @GetMapping("/folderTree")
    Result<List<GrantDeptVO>, Object> queryAdminFolderTree();

    /**
     * 查询单个文件夹信息.
     * @param folderId 文件夹id.
     * @return 查询单个文件夹信息
     */
    @ApiOperation(value = "查询单个文件夹信息", notes = "查询单个文件夹信息", produces = "application/json")
    @GetMapping("/folder/{folderId}")
    Result<FolderInfoDTO, Object> queryFolderById(@PathVariable("folderId") String folderId);

    /**
     * 查询我的文件夹.
     * @param userId 用户id.
     * @return 查询我的文件夹信息
     */
    @ApiOperation(value = "查询我的文件夹", notes = "查询我的文件夹")
    @GetMapping(value = "/folderList")
    Result<List<FolderInfoDTO>, Object> queryFolder(@ApiParam("操作用户Id") @RequestHeader(value = "authorization-userId") String userId);

    /**
     * 查询我的文件信息.
     * @param userId 用户id
     * @param fileQueryVO 文件查询对象
     * @param servletRequest 请求参数
     * @return 分页数据后的结果集.
     */
    @ApiOperation(value = "查询我的文件信息", notes = "查询我的文件信息")
    @GetMapping(value = "/fileList")
    Result<PageInfo<FileInfoVO>, Object> queryFile(@ApiParam("操作用户Id") @RequestHeader(value = "authorization-userId") String userId,
                                                   FileQueryVO fileQueryVO, @ApiParam("分页参数") ServletRequest servletRequest);

    /**
     * 查询文件信息.
     * @param fileQueryVO 查询参数
     * @param servletRequest 请求参数.
     * @return 返回文件信息
     */
    @ApiOperation(value = "查询文件信息（管理员）", notes = "查询文件信息（管理员）")
    @GetMapping(value = "/fileListAdmin")
    Result<PageInfo<FileInfoVO>, Object> queryFileAdmin(@ApiParam("分页参数") ServletRequest servletRequest, FileQueryVO fileQueryVO);

    /**
     * 刷新单个文件夹.
     * @param folderId 文件夹id.
     * @return 返回刷新成功
     */
    @ApiOperation(value = "刷新单个文件夹", notes = "刷新单个文件夹")
    @PutMapping(value = "/refreshOneFolder/{folderId}")
    Result<String, Object> refreshOneFolder(@PathVariable("folderId") String folderId);

    /**
     * 刷新多个文件夹.
     * @param paramVO  刷新参数
     * @return 返回刷新成功
     */
    @ApiOperation(value = "刷新多个文件夹", notes = "刷新多个文件夹")
    @PutMapping("refreshFolders")
    Result<String, Object> refreshFolders(@RequestBody FolderRefreshParamVO paramVO);

    /**
     * 根据文件夹id查询授权的用户信息.
     * @param folderId 文件夹id
     * @return 返回用户信息
     */
    @ApiOperation(value = "根据文件夹id查询授权的用户信息", notes = "根据文件夹id查询授权的用户信息")
    @GetMapping("/grantUserByFolder/{foldertId}")
    Result<GrantFolderUserVO, Object> queryGrantUserByFolderId(@PathVariable("foldertId") String folderId);
}
