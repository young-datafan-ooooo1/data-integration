package com.youngdatafan.dataintegration.file.management.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.file.management.dto.FieldInfoDTO;
import com.youngdatafan.dataintegration.file.management.vo.FileSheetVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

/**
 * 文件内容的api文档.
 *
 * @Author jeremychen
 * @Date 2020/4/15 13:59
 */
@Api(tags = "文件内容API接口")
public interface FileContentDetailGetterApi {

    /**
     * 获取单个文件的sheet名称.
     *
     * @param roleCode 角色的权限编码
     * @param userId   用户id
     * @param fileId   文件id
     * @return 获取到的单个文件名称
     */
    @GetMapping(value = "/getSheetList")
    @ApiOperation(value = "获取单个文件的sheet名称", notes = "获取sheet名称", produces = "application/json")
    Result<List<String>, Object> getSheetList(@ApiParam("最高权限编码")
                                              @RequestHeader(value = "authorization-highestLevelRoleCode") String roleCode,
                                              @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                              @ApiParam("文件路径") @RequestParam("fileId") String fileId);


    /**
     * 测试接口.
     *
     * @param roleCode  角色权限编码
     * @param userId    用户id
     * @param fileId    文件id
     * @param sheetName 脚本页码名称
     * @param split     分割符
     * @return 文件的相关信息
     */
    @ApiOperation(value = "测试用接口", notes = "测试用接口", produces = "application/json")
    @GetMapping(value = "/fileHeader")
    Result<List<FieldInfoDTO>, Object> fileHeader(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                                  @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                                  @ApiParam("文件路径") @RequestParam("fileId") String fileId,
                                                  @ApiParam("sheet页名称") @RequestParam("sheetName") String sheetName,
                                                  @ApiParam("分隔符") @RequestParam("split") String split);


    /**
     * 获取多个文件的sheet名称.
     *
     * @param roleCode    用户角色权限编码
     * @param userId      用户id
     * @param fileSheetVO 文件脚本对象
     * @return 获取的文件名称集合
     */
    @ApiOperation(value = "获取多个文件的sheet名称", notes = "获取多个文件的sheet名称", produces = "application/json")
    @PostMapping(value = "/getMoreSheetList")
    Result<Set<String>, Object> getMoreSheetList(@ApiParam("最高权限编码") @RequestHeader(value = "authorization-highestLevelRoleCode", required = false) String roleCode,
                                                 @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                                 @RequestBody FileSheetVO fileSheetVO);

}
