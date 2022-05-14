package com.youngdatafan.dataintegration.file.management.controller;

import com.amazonaws.AmazonServiceException;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.dataintegration.file.management.api.ModelFileOperationApi;
import com.youngdatafan.dataintegration.file.management.config.FileServerProperties;
import com.youngdatafan.dataintegration.file.management.dto.DpPortalFileManagerDTO;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileManager;
import com.youngdatafan.dataintegration.file.management.service.DpPortalFileManagerService;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import com.youngdatafan.dataintegration.file.management.utils.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作模型控制器.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/7 5:42 下午
 */
@Slf4j
@RestController
@RequestMapping("/fileOperationtomodel")
public class FileOperationToModelApiController extends BaseController<DpPortalFileManagerDTO> implements ModelFileOperationApi {

    @Autowired
    private DpPortalFileManagerService dpPortalFileManagerService;

    @Autowired
    private FileSystemManagerService fileSystemManagerService;

    @Autowired
    private FileServerProperties fileServerProperties;

    @ApiOperation(value = "新增一个文件", notes = "新增一个文件")
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    Result<DpPortalFileManagerDTO, Object> add(@NotBlank @ApiParam("来源系统") String sourceSystem,
                                               @NotBlank @ApiParam("来源方式") String sourceWay,
                                               @ApiParam("来源项目") String sourceProject,
                                               @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                               @ApiParam("操作用户Id") @RequestHeader("authorization-userName") String userName,
                                               @RequestParam("id") String id, @RequestPart(value = "file") MultipartFile file) {
        String fileName = id;
        // 获取文件名
        if (dpPortalFileManagerService.checkeFileName(fileName, userId, "ai_model") != null) {
            return Result.fail(StatusCode.CODE_10010, null, "文件名称已存在");
        }
        DpPortalFileManager dpPortalFileManager = new DpPortalFileManager();
        dpPortalFileManager.setFileId(UUIDUtils.generateUUID32());
        dpPortalFileManager.setFileName(fileName);
        dpPortalFileManager.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
        //dpPortalFileManager.setNotes(fileAddVO.getNotes());
        dpPortalFileManager.setOrder(99);
        dpPortalFileManager.setCreateUserId(userId);
        dpPortalFileManager.setIsFolder("0");
        dpPortalFileManager.setIsValid("Y");
        dpPortalFileManager.setPid("ai_model");
        dpPortalFileManager.setUploadTime(new Date());
        dpPortalFileManager.setCreateChannel("AIMODEL");
        dpPortalFileManager.setFileServerType(fileServerProperties.getExtendsFileType());
        dpPortalFileManager.setFilePath(fileSystemManagerService.getRootPath() + userName + "/ai_model/" + dpPortalFileManager.getFileName());
        try {
            fileSystemManagerService.addFile(dpPortalFileManager.getFilePath(), file.getInputStream(), file.getSize());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new DpException(StatusCode.CODE_10010, "获取文件失败");
        }
        dpPortalFileManagerService.insert(dpPortalFileManager);
        DpPortalFileManagerDTO dpPortalFileManagerDTO = new DpPortalFileManagerDTO();
        BeanUtils.copyProperties(dpPortalFileManager, dpPortalFileManagerDTO);

        return Result.success(dpPortalFileManagerDTO);
    }

    @Override
    public void downLoadFile(String roleCode, String userId, String fileId, String fileNames, HttpServletResponse response) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
        if (dpPortalFileManager == null) {
            throw new DpException(StatusCode.CODE_10010, "文件不存在");
        }
        InputStream fileObject = fileSystemManagerService.getFileObject(dpPortalFileManager.getFilePath());
        try (InputStream inputStream = fileObject;
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("octets/stream");
            //设置字符集和文件后缀名
            response.setContentType("application/" + dpPortalFileManager.getFileType() + ";" + "charset = UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(dpPortalFileManager.getFileId() + "." + dpPortalFileManager.getFileType(), "utf-8"));

            int length = 1024 * 1024 * 2;
            int len;
            byte[] b = new byte[length];
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
                outputStream.flush();
            }
            outputStream.flush();
        } catch (AmazonServiceException | IOException e) {
            throw new DpException(StatusCode.CODE_10010, "下载文件失败", e);
        } finally {
            if (fileObject != null) {
                try {
                    fileObject.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    @Transactional
    public Result update(String roleCode, String userId, String userName, String fileId, MultipartFile file) {
        try {
            //获取老的文件信息并删除
            DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
            if (dpPortalFileManager == null) {
                dpPortalFileManager = new DpPortalFileManager();
            }
            String fileName = file.getOriginalFilename();
            /*// 获取文件名
            if (!fileName.equals(dpPortalFileManager.getFileName()) && dpPortalFileManagerService.checkeFileName(fileName,userId) !=null) {
                return Result.fail(StatusCode.CODE_10010, "", "文件名称已存在");
            }*/

            dpPortalFileManager.setCreateChannel("AIMODEL");
            dpPortalFileManager.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));

            // 获取上传文件
            String newPath = fileSystemManagerService.getRootPath() + userName + "/ai_model/" + fileName.substring(fileName.lastIndexOf(".") + 1);
            if (file != null) {
                fileSystemManagerService.delFile(dpPortalFileManager.getFilePath());
                fileSystemManagerService.addFile(newPath, file.getInputStream(), file.getSize());
            }
            DpPortalFileManager dpPortalFileManager1 = new DpPortalFileManager();
            dpPortalFileManager1.setFileId(fileId);
            dpPortalFileManager1.setFileName(fileName);
            dpPortalFileManager1.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
            //dpPortalFileManager1.setNotes(fileUpdateVO.getNotes());
            dpPortalFileManager1.setOrder(99);
            dpPortalFileManager1.setCreateUserId(userId);
            dpPortalFileManager1.setIsValid("Y");
            dpPortalFileManager1.setUploadTime(new Date());
            dpPortalFileManager1.setIsFolder(dpPortalFileManager.getIsFolder());

            dpPortalFileManagerService.updateByPrimaryKeySelective(dpPortalFileManager1);
            DpPortalFileManagerDTO dpPortalFileManagerDTO = new DpPortalFileManagerDTO();
            BeanUtils.copyProperties(dpPortalFileManager1, dpPortalFileManagerDTO);

            return Result.success(dpPortalFileManagerDTO);
        } catch (AmazonServiceException | IOException e) {
            throw new DpException(StatusCode.CODE_10010, "获取新文件失败", e);
        }
    }

    @Override
    @Transactional
    public Result delete(String roleCode, String userId, String fileId) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
        if (dpPortalFileManager != null) {
            boolean isFolder = dpPortalFileManager.getIsFolder().equals("1");
            int i = dpPortalFileManagerService.deleteByPrimaryKey(fileId, userId);
            if (i > 0) {
                if (isFolder) {
                    fileSystemManagerService.delFolder(dpPortalFileManager.getFilePath());
                } else {
                    fileSystemManagerService.delFile(dpPortalFileManager.getFilePath());
                }
            }

            return Result.success(i > 0);
        } else {
            return Result.fail(StatusCode.CODE_10010, "", "文件信息不存在");
        }
    }
}
