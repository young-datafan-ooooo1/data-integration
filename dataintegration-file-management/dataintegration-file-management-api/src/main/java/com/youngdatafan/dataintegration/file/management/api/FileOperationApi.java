package com.youngdatafan.dataintegration.file.management.api;

import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.file.management.dto.DpPortalFileManagerDTO;
import com.youngdatafan.dataintegration.file.management.dto.FileInfoDTO;
import com.youngdatafan.dataintegration.file.management.dto.FileSystemInfo;
import com.youngdatafan.dataintegration.file.management.vo.DmDemandFileVO;
import com.youngdatafan.dataintegration.file.management.vo.FileAddVO;
import com.youngdatafan.dataintegration.file.management.vo.FileUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文件管理api.
 *
 * @Author jeremychen
 * @Descripition:
 * @Date 2020/4/7 17:41
 */
@Api(tags = "文件管理API接口")
public interface FileOperationApi {

    /**
     * 测试调用接口.
     */
    @ApiOperation(value = "测试用接口", notes = "测试用接口", produces = "application/json")
    @PostMapping(value = "/fileOperationTest")
    void fileOperationTest();


    /**
     * 新增文件.
     *
     * @param sourceProject 来源项目
     * @param userId        用户id
     * @param userName      用户名称
     * @param fileAddVO     文件增加对象
     * @param request       其他请求参数
     * @return 增加后文件处理DTO对象
     */
    @ApiOperation(value = "新增一个文件", notes = "新增一个文件")
    @PostMapping(value = "/add")
    Result<DpPortalFileManagerDTO, Object> add(
        @ApiParam("来源项目") @RequestParam(value = "sourceProject", required = false) String sourceProject,
        @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @ApiParam("操作用户Id") @RequestHeader("authorization-userName") String userName,
        @Validated FileAddVO fileAddVO, HttpServletRequest request);

    /**
     * 新增文件夹.
     *
     * @param sourceProject 来源项目
     * @param userId        用户id
     * @param userName      用户名
     * @param fileAddVO     文件映射对象
     * @return 处理后文件的DTO对象
     */
    @ApiOperation(value = "新建文件夹", notes = "新建文件夹")
    @PostMapping(value = "/addDir")
    Result<DpPortalFileManagerDTO, Object> addDir(
        @ApiParam("来源项目") @RequestParam(value = "sourceProject", required = false) String sourceProject,
        @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
        @ApiParam("操作用户Id") @RequestHeader("authorization-userName") String userName, @Validated FileAddVO fileAddVO);


    /**
     * 一次性增加多个文件.
     *
     * @param roleCode      角色权限编码
     * @param sourceSystem  来源系统
     * @param sourceWay     来源方式
     * @param sourceProject 来源项目
     * @param userId        用户id
     * @param fileAddVO     增加对象的其他参数
     * @param request       请求中的其他参数
     * @return 处理后文件的DTO对象
     * @throws IOException 处理过程中可能抛出io异常
     */
    @ApiOperation(value = "新增多个文件", notes = "新增多个文件")
    @PostMapping(value = "/addBatch")
    Result<DpPortalFileManagerDTO, Object> addBatch(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                                    @NotBlank @ApiParam("来源系统") @RequestParam("sourceSystem") String sourceSystem,
                                                    @NotBlank @ApiParam("来源方式") @RequestParam("sourceWay") String sourceWay,
                                                    @ApiParam("来源项目") @RequestParam(value = "sourceProject", required = false) String sourceProject,
                                                    @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @Validated FileAddVO fileAddVO, HttpServletRequest request)
        throws IOException;

    /**
     * 删除文件信息.
     *
     * @param roleCode 角色权限编码
     * @param userId   用户id
     * @param fileId   文件id
     * @return 返回是否删除成功地结果
     */
    @ApiOperation(value = "删除文件信息", notes = "删除文件信息")
    @DeleteMapping(value = "/delete")
    Result delete(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                  @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                  @RequestParam("fileId") String fileId);

    /**
     * 一次性批量删除多个文件.
     *
     * @param roleCode 角色权限编码
     * @param userId   用户id
     * @param fileIds  文件id列表
     * @return 是否删除成功的
     */
    @ApiOperation(value = "批量删除文件信息", notes = "批量删除文件信息")
    @DeleteMapping(value = "/batchDelete")
    Result batchDelete(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                       @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                       @RequestParam("fileIds") String fileIds);


    /**
     * 更新文件信息.
     *
     * @param roleCode     角色权限编码
     * @param userId       用户id
     * @param fileUpdateVO 文件更新对象
     * @param request      请求参数
     * @return 是否更新成功结果.
     */
    @ApiOperation(value = "更新文件信息", notes = "更新文件信息")
    @PutMapping(value = "/update")
    Result update(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                  @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                  @Validated FileUpdateVO fileUpdateVO, HttpServletRequest request);

    /**
     * 更新目录.
     *
     * @param userId       用户id
     * @param fileUpdateVO 文件更新映射对象
     * @param request      其他请求参数信息
     * @return 是否更新成功的相关信息
     */
    @ApiOperation(value = "更新目录", notes = "更新目录")
    @PutMapping(value = "/updateDir")
    Result updateDir(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @Validated FileUpdateVO fileUpdateVO, HttpServletRequest request);

    /**
     * 分页查询.
     *
     * @param roleCode 角色权限编码
     * @param userId   用户id
     * @param pageSize 每页显示数据量
     * @param curPage  当前页码
     * @param fileType 文件类型
     * @param fileName 文件名称
     * @return 查询到的文件信息
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping(value = "/selectPage")
    Result<PageInfo<DpPortalFileManagerDTO>, Object> selectPage(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                                                @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @RequestParam("pageSize") @ApiParam("每页条数") String pageSize,
                                                                @RequestParam("curPage") @ApiParam("当前页") String curPage, @RequestParam("fileType") @ApiParam("文件类型") String fileType,
                                                                @RequestParam("fileName") @ApiParam("文件名称") String fileName);

    /**
     * 下载文件.
     *
     * @param roleCode  角色权限编码
     * @param userId    用户id
     * @param fileId    文件id
     * @param fileNames 文件名称
     * @param response  响应参数
     */
    @ApiOperation(value = "下载文件", notes = "下载文件")
    @GetMapping(value = "/downLoadFile")
    void downLoadFile(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                      @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                      @RequestParam("fileId") String fileId, @RequestParam("fileNames") @ApiParam("需要下载的文件名清单") String fileNames, HttpServletResponse response);

    /**
     * 下载输出文件.
     *
     * @param roleCode 角色权限编码
     * @param userId   用户id
     * @param fileId   文件id
     * @param fileIds  需要下载的文件清单列表
     * @param response 响应参数信息
     */
    @ApiOperation(value = "下载输出文件", notes = "下载文件")
    @GetMapping(value = "/downLoadOutputFile")
    void downLoadOutputFile(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                            @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                            @RequestParam("fileId") String fileId,
                            @RequestParam("fileIds") @ApiParam("需要下载的文件名清单") String fileIds,
                            HttpServletResponse response);


    /**
     * 查询文件清单.
     *
     * @param roleCode 角色权限编码
     * @param userId   用户id.
     * @param fileType 文件类型.
     * @param isFolder 是否是文件夹.
     * @return 查询到的文件列表
     */
    @ApiOperation(value = "查询文件清单", notes = "查询文件清单")
    @GetMapping(value = "/selectFileListByType")
    Result<List<FileInfoDTO>, Object> selectFileListByType(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                                           @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @RequestParam("fileType") String fileType,
                                                           @RequestParam("isFolder") String isFolder);

    /**
     * 查询文件夹清单.
     *
     * @param roleCode 角色权限编码
     * @param userId   用户id.
     * @return 查询到的文件夹清单
     */
    @ApiOperation(value = "查询文件夹清单", notes = "查询文件夹清单")
    @GetMapping(value = "/selectFileFolderList")
    Result<List<FileInfoDTO>, Object> selectFileFolderList(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                                           @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);


    /**
     * 根据id, 查询文件信息.
     *
     * @param roleCode 角色权限编码
     * @param userId   用户id.
     * @param fileId   文件id.
     * @return 查询到的相关文件
     */
    @ApiOperation(value = "根据id查询文件信息", notes = "根据id查询文件信息")
    @GetMapping(value = "/selectFileById")
    Result<FileInfoDTO, Object> selectFileById(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                               @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                               @RequestParam("fileId") String fileId);

    /**
     * 查询文件系统信息.
     *
     * @return 文件系统相关信息
     */
    @ApiOperation(value = "查询文件系统信息", notes = "查询文件系统信息")
    @GetMapping(value = "/selectFileSystemInfo")
    Result<FileSystemInfo, Object> selectFileSystemInfo();

    /**
     * 查询某一文件夹下的文件清单.
     *
     * @param roleCode 角色权限编码
     * @param userId   用户id
     * @param fileId   文件id
     * @param fileType 文件类型
     * @return 查询到的文件列表
     * @throws IOException 在查询过程中可能出现io异常
     */
    @ApiOperation(value = "查询文件夹下文件清单", notes = "查询文件夹下文件清单")
    @GetMapping(value = "/getFolderFileList")
    Result<List<FileInfoDTO>, Object> getFolderFileList(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                                        @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @RequestParam("fileId") String fileId,
                                                        @RequestParam("fileType") String fileType) throws IOException;

    /**
     * 向某一文件夹中添加文件.
     *
     * @param roleCode      角色权限编码
     * @param sourceSystem  来源系统
     * @param sourceWay     来源方式
     * @param sourceProject 来源项目
     * @param userId        用户id
     * @param fileId        文件id
     * @param request       请求参数
     * @return 是否添加成功地相关信息
     */
    @ApiOperation(value = "向文件夹中添加文件", notes = "向文件夹中添加文件")
    @PutMapping(value = "/uploadFileToFolder")
    Result<Object, Object> uploadFileToFolder(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                              @NotBlank @ApiParam("来源系统") @RequestParam("sourceSystem") String sourceSystem, @NotBlank @ApiParam("来源方式") @RequestParam("sourceWay") String sourceWay,
                                              @ApiParam("来源项目") @RequestParam("sourceProject") String sourceProject, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                              @RequestParam("fileId") String fileId, HttpServletRequest request);

    /**
     * 刷新文件时间.
     *
     * @param userId 用户id.
     * @return 刷新是否成功的结果
     */
    @ApiOperation(value = "刷新文件夹时间", notes = "刷新文件夹时间")
    @PutMapping(value = "/flushFolderTime")
    Result<String, Object> flushFolderTime(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);


    /**
     * 从某一文件夹中删除文件.
     *
     * @param roleCode  角色权限编码.
     * @param userId    用户id.
     * @param fileId    文件id.
     * @param fileNames 文件名称.
     * @return 是否删除成功的结果信息
     */
    @ApiOperation(value = "从文件夹中删除文件", notes = "从文件夹中删除文件")
    @DeleteMapping(value = "/deleteFileFromFolder")
    Result<Boolean, Object> deleteFileFromFolder(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                                 @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @RequestParam("fileId") String fileId,
                                                 @RequestParam("fileNames") @ApiParam("文件清单") String fileNames);

    /**
     * 检查文件名称.
     *
     * @param userId   用户id
     * @param fileName 文件名称.
     * @param pid      文件pid
     * @return 返回文件是否存在的结果信息
     */
    @ApiOperation(value = "检查文件名", notes = "检查文件名")
    @PostMapping(value = "/checkFileName")
    Result<String, Object> checkFileName(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @RequestParam("fileName") String fileName, @RequestParam("pid") String pid);

    /**
     * 检查文件夹.
     *
     * @param userId   用户id
     * @param fileName 文件名称.
     * @return 文件夹是否存在的结果信息
     */
    @ApiOperation(value = "检查文件夹", notes = "检查文件夹")
    @PostMapping(value = "/checkDirName")
    Result<String, Object> checkDirName(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId, @RequestParam("fileName") String fileName);


    /**
     * 需求协作平台表新增.
     *
     * @param sourceSystem  来源系统
     * @param sourceWay     来源方式
     * @param sourceProject 来源项目
     * @param userId        用户id
     * @param userName      用户名称
     * @param fileAddVO     文件增加对象
     * @param request       请求参数
     * @return 新增是之后的相关结果信息
     */
    @ApiOperation(value = "需求协作平台表样新增", notes = "需求协作平台表样新增")
    @PostMapping(value = "/demandFileAdd")
    Result demandFileAdd(@RequestParam(value = "sourceSystem", required = false) String sourceSystem, @ApiParam("来源方式") @RequestParam(value = "sourceWay", required = false) String sourceWay,
                         @ApiParam("来源项目") @RequestParam(value = "sourceProject", required = false) String sourceProject, @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                         @ApiParam("操作用户Id") @RequestHeader("authorization-userName") String userName, @Validated DmDemandFileVO fileAddVO, HttpServletRequest request);


    /**
     * 需求协作平台表样下载文件.
     *
     * @param fileId   文件id.
     * @param response 响应参数
     */
    @ApiOperation(value = "需求协作平台表样下载文件", notes = "下载文件")
    @GetMapping(value = "/downLoadFile/{fileId}")
    void download(@ApiParam("文件") @PathVariable Integer fileId, HttpServletResponse response);


    /**
     * 批量下载文件.
     *
     * @param roleCode 角色编码
     * @param userId   用户Id
     * @param fileIds  文件Ids
     * @param response response
     */
    @ApiOperation(value = "批量下载文件", notes = "批量下载文件")
    @GetMapping(value = "/batchDownLoadFile")
    void batchDownLoadFile(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                           @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                           @RequestParam("fileIds") String[] fileIds, HttpServletResponse response);

}
