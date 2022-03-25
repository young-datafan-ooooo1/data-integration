package com.youngdatafan.dataintegration.file.management.api;

import com.youngdatafan.dataintegration.core.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件管理接口.
 *
 * @Author jeremychen
 * @Date 2020/4/7 17:41
 */
@Api(tags = "文件管理API接口(ai模型调用)")
public interface ModelFileOperationApi {

    /**
     * 下载文件.
     * @param roleCode 角色权限编码
     * @param userId 用户id
     * @param fileId 文件id
     * @param fileNames 文件名称
     * @param response 响应参数
     */
    @ApiOperation(value = "下载文件", notes = "下载文件")
    @GetMapping(value = "/downLoadFile")
    void downLoadFile(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                      @ApiParam("操作用户Id") @RequestHeader(value = "authorization-userId") String userId,
                      @RequestParam("fileId") String fileId,
                      @RequestParam("fileNames") @ApiParam("需要下载的文件名清单") String fileNames,
                      @ApiParam("响应参数") HttpServletResponse response);

    /**
     * 更新文件信息.
     * @param roleCode 角色权限编码
     * @param userId 用户id
     * @param userName 用户名称
     * @param fileId 文件id
     * @param file 更新文件时上传的文件
     * @return 更新文件结果信息
     */
    @ApiOperation(value = "更新文件信息", notes = "更新文件信息")
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result update(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                  @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                  @ApiParam("操作用户Id") @RequestHeader("authorization-userName") String userName,
                  @RequestParam("fileId") String fileId,
                  @ApiParam("更新上传的文件") @RequestPart(value = "file") MultipartFile file);


    /**
     * 删除文件信息.
     *
     * @param roleCode 角色权限编码
     * @param userId 用户id
     * @param fileId 文件id
     * @return 删除文件后的结果信息.
     */
    @ApiOperation(value = "删除文件信息", notes = "删除文件信息")
    @DeleteMapping(value = "/delete")
    Result delete(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                  @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                  @ApiParam("删除的文件id") @RequestParam("fileId") String fileId);
}
