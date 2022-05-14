package com.youngdatafan.dataintegration.file.management.controller;

import com.amazonaws.AmazonServiceException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.Pair;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.dataintegration.core.util.encryption.DefaultEncryptionUtils;
import com.youngdatafan.dataintegration.file.management.api.FileOperationApi;
import com.youngdatafan.dataintegration.file.management.config.FileServerProperties;
import com.youngdatafan.dataintegration.file.management.dto.DpPortalFileManagerDTO;
import com.youngdatafan.dataintegration.file.management.dto.FileInfoDTO;
import com.youngdatafan.dataintegration.file.management.dto.FileSystemInfo;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.mapper.DmDemandFileMapper;
import com.youngdatafan.dataintegration.file.management.model.DmDemandFile;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileManager;
import com.youngdatafan.dataintegration.file.management.model.FileType;
import com.youngdatafan.dataintegration.file.management.service.DpPortalFileManagerService;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import com.youngdatafan.dataintegration.file.management.utils.BaseController;
import com.youngdatafan.dataintegration.file.management.vo.DmDemandFileVO;
import com.youngdatafan.dataintegration.file.management.vo.FileAddVO;
import com.youngdatafan.dataintegration.file.management.vo.FileUpdateVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 文件操作控制器.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/7 5:42 下午
 */
@Slf4j
@RestController
@RequestMapping("/fileOperation")
public class FileOperationApiController extends BaseController<DpPortalFileManagerDTO> implements FileOperationApi {

    @Value("${youngdatafan.file.sourceSystem:集成}")
    private String sourceSystem;

    @Value("${youngdatafan.file.sourceWay:上传}")
    private String sourceWay;

    @Autowired
    private FileSystemManagerService fileSystemManagerService;

    @Autowired
    private DpPortalFileManagerService dpPortalFileManagerService;

    @Autowired
    private FileServerProperties fileServerProperties;

    @Autowired
    private DmDemandFileMapper dmDemandFileMapper;

    @Override
    public void fileOperationTest() {
        fileSystemManagerService.testFtp();
    }

    @Override
    @Transactional
    public Result<DpPortalFileManagerDTO, Object> add(String sourceProject, String userId, String userName, FileAddVO fileAddVO, HttpServletRequest request) {
        boolean isFolder = fileAddVO.getIsFolder().equals("1");

        if (!isFolder && (fileAddVO.getFileType() == null || fileAddVO.getFileType().equals(""))) {
            return Result.fail(StatusCode.CODE_10010, null, "文件类型为空");

        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取上传文件
        List<MultipartFile> files = multipartRequest.getFiles("file");
        if (!isFolder && CollectionUtils.isEmpty(files)) {
            return Result.fail(StatusCode.CODE_10010, null, "必须选择文件");
        }
        DpPortalFileManager dpPortalFileManager = new DpPortalFileManager();
        dpPortalFileManager.setSourceSystem(sourceSystem);
        dpPortalFileManager.setSourceWay(sourceWay);
        dpPortalFileManager.setSourceProject(sourceProject);
        dpPortalFileManager.setFileId(UUIDUtils.generateUUID32());
        dpPortalFileManager.setFileName(StringEscapeUtils.unescapeHtml4(fileAddVO.getFileName()));
        dpPortalFileManager.setFileType(fileAddVO.getFileType());
        dpPortalFileManager.setNotes(fileAddVO.getNotes());
        dpPortalFileManager.setOrder(fileAddVO.getOrder());
        dpPortalFileManager.setCreateUserId(userId);
        dpPortalFileManager.setUserName(userName);
        dpPortalFileManager.setPid(fileAddVO.getPid());
        dpPortalFileManager.setIsFolder(fileAddVO.getIsFolder());
        dpPortalFileManager.setIsValid("Y");
        dpPortalFileManager.setUploadTime(new Date());
        dpPortalFileManager.setEffectiveDays(fileAddVO.getEffectiveDays());
        dpPortalFileManager.setLastModifiedTime(new Date());
        dpPortalFileManager.setFileServerType(fileServerProperties.getExtendsFileType());
        dpPortalFileManager.setFileSize(String.valueOf(files.get(0).getSize()));
        FolderInfoDTO folderInfoDTO = this.dpPortalFileManagerService.queryOneFolder(fileAddVO.getPid());
        try {
            dpPortalFileManager.setFilePath(fileSystemManagerService.getRootPath() + userName + "/" + folderInfoDTO.getFolderName() + "/" + dpPortalFileManager.getFileName());
            fileSystemManagerService.addFile(dpPortalFileManager.getFilePath(), files.get(0).getInputStream(), files.get(0).getSize());
            // 获取文件名
            String fileId = dpPortalFileManagerService.checkeFileName(fileAddVO.getFileName(), userId, fileAddVO.getPid());
            if (StringUtils.isNotBlank(fileId)) {
                dpPortalFileManager.setFileId(fileId);
                dpPortalFileManagerService.updateByPrimaryKeySelective(dpPortalFileManager);
            } else {
                dpPortalFileManagerService.insert(dpPortalFileManager);
            }
        } catch (IOException e) {
            throw new DpException(StatusCode.CODE_10010, "获取文件失败", e);
        }
        DpPortalFileManagerDTO dpPortalFileManagerDTO = new DpPortalFileManagerDTO();
        BeanUtils.copyProperties(dpPortalFileManager, dpPortalFileManagerDTO);

        return Result.success(dpPortalFileManagerDTO);
    }

    @Override
    @Transactional
    public Result<DpPortalFileManagerDTO, Object> addDir(String sourceProject, String userId, String userName, FileAddVO fileAddVO) {

        // 获取文件名
        if (dpPortalFileManagerService.checkeDirName(fileAddVO.getFileName(), userId) != null) {
            return Result.fail(StatusCode.CODE_10010, null, "文件夹名称已存在");
        }

        DpPortalFileManager dpPortalFileManager = new DpPortalFileManager();
        dpPortalFileManager.setSourceSystem(sourceSystem);
        dpPortalFileManager.setSourceWay(sourceWay);
        dpPortalFileManager.setSourceProject(sourceProject);
        dpPortalFileManager.setFileId(UUIDUtils.generateUUID32());
        dpPortalFileManager.setFileName(StringEscapeUtils.unescapeHtml4(fileAddVO.getFileName()));
        dpPortalFileManager.setFileType(fileAddVO.getFileType());
        dpPortalFileManager.setNotes(fileAddVO.getNotes());
        dpPortalFileManager.setOrder(fileAddVO.getOrder());
        dpPortalFileManager.setCreateUserId(userId);
        dpPortalFileManager.setUserName(userName);
        dpPortalFileManager.setIsFolder("1");
        dpPortalFileManager.setIsValid("Y");
        dpPortalFileManager.setUploadTime(new Date());
        dpPortalFileManager.setLastModifiedTime(new Date());
        dpPortalFileManager.setFileServerType(fileServerProperties.getExtendsFileType());
        dpPortalFileManager.setFilePath(fileSystemManagerService.getRootPath() + userName + "/" + dpPortalFileManager.getFileName() + "/");
        dpPortalFileManagerService.insert(dpPortalFileManager);
        if (fileServerProperties.getS3().getCreateDir()) {
            fileSystemManagerService.addFolder(userName + "/" + dpPortalFileManager.getFileName());
        }

        DpPortalFileManagerDTO dpPortalFileManagerDTO = new DpPortalFileManagerDTO();
        BeanUtils.copyProperties(dpPortalFileManager, dpPortalFileManagerDTO);

        return Result.success(dpPortalFileManagerDTO);
    }

    @Override
    @Transactional
    public Result<DpPortalFileManagerDTO, Object> addBatch(String roleCode, String sourceSystem, String sourceWay, String sourceProject, String userId, FileAddVO fileAddVO, HttpServletRequest request)
        throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取上传文件
        List<MultipartFile> files = multipartRequest.getFiles("file");

        if (files == null || files.size() < 1) {
            return Result.fail(StatusCode.CODE_10010, null, "必须选择文件");
        }
        DpPortalFileManager dpPortalFileManagerDir = dpPortalFileManagerService.selectByFileId(roleCode, fileAddVO.getFileId(), userId);

        String pid = dpPortalFileManagerDir.getFileId();
        for (MultipartFile file : files) {
            String fileStr = dpPortalFileManagerService.checkeFileName(file.getOriginalFilename(), userId, dpPortalFileManagerDir.getFileId());
            // 获取文件名
            if (StringUtils.isNotBlank(fileStr)) {
                //文件已存在则删除
                DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileStr, userId);
                dpPortalFileManagerService.deleteByPrimaryKey(dpPortalFileManager.getFileId(), userId);
                fileSystemManagerService.delFile(dpPortalFileManager.getFilePath());
            }

            DpPortalFileManager dpPortalFileManager = new DpPortalFileManager();
            BeanUtils.copyProperties(dpPortalFileManagerDir, dpPortalFileManager);
            String uid = UUIDUtils.generateUUID32();
            dpPortalFileManager.setSourceSystem(sourceSystem);
            dpPortalFileManager.setSourceWay(sourceWay);
            dpPortalFileManager.setSourceProject(sourceProject);
            dpPortalFileManager.setFileId(uid);
            dpPortalFileManager.setPid(pid);
            dpPortalFileManager.setIsFolder("0");
            dpPortalFileManager.setFileName(StringEscapeUtils.unescapeHtml4(file.getOriginalFilename()));
            dpPortalFileManager.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            dpPortalFileManager.setFilePath(dpPortalFileManager.getFilePath() + file.getOriginalFilename());
            dpPortalFileManager.setUploadTime(new Date());
            dpPortalFileManager.setLastModifiedTime(new Date());
            dpPortalFileManager.setFileSize(String.valueOf(file.getSize()));
            dpPortalFileManagerService.insert(dpPortalFileManager);
            fileSystemManagerService.addFile(dpPortalFileManager.getFilePath(), file.getInputStream(), file.getSize());
        }
        DpPortalFileManagerDTO dpPortalFileManagerDTO = new DpPortalFileManagerDTO();
        BeanUtils.copyProperties(dpPortalFileManagerDir, dpPortalFileManagerDTO);

        return Result.success(dpPortalFileManagerDTO);
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
            return Result.fail(StatusCode.CODE_10010, null, "文件信息不存在");
        }
    }

    @Transactional
    @Override
    public Result batchDelete(String roleCode, String userId, String fileIds) {
        String[] ids = fileIds.split(",");
        List<DpPortalFileManager> dpPortalFileManagers = dpPortalFileManagerService.selectByFileIds(roleCode, ids, userId);
        for (DpPortalFileManager dpPortalFileManager : dpPortalFileManagers) {
            int i = dpPortalFileManagerService.deleteByPrimaryKey(dpPortalFileManager.getFileId(), userId);
            boolean isFolder = dpPortalFileManager.getIsFolder().equals("1");
            if (i > 0) {
                if (isFolder) {
                    fileSystemManagerService.delFolder(dpPortalFileManager.getFilePath());
                } else {
                    fileSystemManagerService.delFile(dpPortalFileManager.getFilePath());

                }
            }
        }
        return Result.success(true);
    }

    @Override
    @Transactional
    public Result update(String roleCode, String userId, FileUpdateVO fileUpdateVO, HttpServletRequest request) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileUpdateVO.getFileId(), userId);
        // 获取上传文件
        DpPortalFileManager dpPortalFileManager1 = new DpPortalFileManager();

        dpPortalFileManager1.setFileId(fileUpdateVO.getFileId());
        dpPortalFileManager1.setFileName(StringEscapeUtils.unescapeHtml4(fileUpdateVO.getFileName()));
        dpPortalFileManager1.setFileType(fileUpdateVO.getFileType());
        dpPortalFileManager1.setNotes(fileUpdateVO.getNotes());
        dpPortalFileManager1.setOrder(fileUpdateVO.getOrder());
        dpPortalFileManager1.setCreateUserId(userId);
        dpPortalFileManager1.setIsValid(fileUpdateVO.getIsValid());
        dpPortalFileManager1.setUploadTime(new Date());
        dpPortalFileManager1.setLastModifiedTime(new Date());
        dpPortalFileManager1.setIsFolder(dpPortalFileManager.getIsFolder());
        dpPortalFileManager1.setFileServerType(fileServerProperties.getExtendsFileType());
        dpPortalFileManagerService.updateByPrimaryKeySelective(dpPortalFileManager1);
        DpPortalFileManagerDTO dpPortalFileManagerDTO = new DpPortalFileManagerDTO();
        BeanUtils.copyProperties(dpPortalFileManager1, dpPortalFileManagerDTO);

        return Result.success(dpPortalFileManagerDTO);
    }

    @Override
    @Transactional
    public Result updateDir(String userId, FileUpdateVO fileUpdateVO, HttpServletRequest request) {
        DpPortalFileManager dpPortalFileManager1 = new DpPortalFileManager();
        dpPortalFileManager1.setFileId(fileUpdateVO.getFileId());
        dpPortalFileManager1.setFileName(StringEscapeUtils.unescapeHtml4(fileUpdateVO.getFileName()));
        dpPortalFileManager1.setFileType(fileUpdateVO.getFileType());
        dpPortalFileManager1.setNotes(fileUpdateVO.getNotes());
        dpPortalFileManager1.setOrder(fileUpdateVO.getOrder());
        dpPortalFileManager1.setCreateUserId(userId);
        dpPortalFileManager1.setIsValid(fileUpdateVO.getIsValid());
        dpPortalFileManager1.setLastModifiedTime(new Date());
        dpPortalFileManager1.setIsFolder("1");
        dpPortalFileManager1.setFileServerType(fileServerProperties.getExtendsFileType());
        dpPortalFileManagerService.updateByPrimaryKeySelective(dpPortalFileManager1);
        DpPortalFileManagerDTO dpPortalFileManagerDTO = new DpPortalFileManagerDTO();
        BeanUtils.copyProperties(dpPortalFileManager1, dpPortalFileManagerDTO);
        return Result.success(dpPortalFileManagerDTO);
    }

    @Override
    public Result<PageInfo<DpPortalFileManagerDTO>, Object> selectPage(String roleCode, String userId, String pageSize, String
        curPage, String fileType, String fileName) {
        Page page = this.initPage(pageSize, curPage);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("fileType", fileType);
        params.put("roleCode", roleCode);
        params.put("fileName", fileName.toUpperCase());
        PageInfo<DpPortalFileManagerDTO> portalFileManagerDTOPageInfo = dpPortalFileManagerService.selectByParamPage(params, page);
        return Result.success(portalFileManagerDTOPageInfo);
    }

    @Override
    public void batchDownLoadFile(String roleCode, String userId, String[] fileIds, HttpServletResponse response) {
        String datetime = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String dirName = "文件批量下载_" + datetime;
        List<Pair<String, InputStream>> fileObjectList = new ArrayList<>();
        try (OutputStream outputStream = response.getOutputStream();
             ZipOutputStream zout = new ZipOutputStream(outputStream)) {
            response.setContentType("octets/stream");
            response.setContentType("application/zip;" + "charset = UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(dirName + ".zip", "utf-8"));

            List<DpPortalFileManager> fileManagerList = dpPortalFileManagerService.selectByFileIds(fileIds);
            fileManagerList.forEach(fileManager -> {
                InputStream child = fileSystemManagerService.getFileObject(fileManager.getFilePath());
                fileObjectList.add(new Pair<>(fileManager.getFileName(), child));
            });
            ZipEntry zipEntry = new ZipEntry(dirName + File.separator);
            zout.putNextEntry(zipEntry);
            zout.flush();
            for (Pair<String, InputStream> child : fileObjectList) {
                InputStream inputStream = null;
                if (child.getV() != null) {
                    try {
                        ZipEntry entry = new ZipEntry(dirName + File.separator + child.getK());
                        zout.putNextEntry(entry);
                        int length = 1024 * 1024 * 2;
                        int len;
                        byte[] b = new byte[length];
                        inputStream = child.getV();
                        while ((len = inputStream.read(b)) != -1) {
                            zout.write(b, 0, len);
                            zout.flush();
                        }
                    } catch (Exception e) {
                        log.error("文件下载失败", e);
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                    }
                }
            }
            zout.flush();
        } catch (AmazonServiceException | IOException e) {
            throw new DpException(StatusCode.CODE_10010, "下载文件失败", e);
        } finally {
            for (Pair<String, InputStream> stringS3ObjectPair : fileObjectList) {
                try {
                    InputStream s3Object = stringS3ObjectPair.getV();
                    if (s3Object != null) {
                        s3Object.close();
                    }
                } catch (IOException e) {
                    log.error("file close error", e);
                }
            }
        }
    }

    @Override
    public void downLoadFile(String roleCode, String userId, String fileId, String fileNames, HttpServletResponse response) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
        boolean isFolder = dpPortalFileManager.getIsFolder().equals("1");
        if (isFolder) {
            List<Pair<String, InputStream>> fileObjectList = new ArrayList<>();
            if (fileNames == null || "".equals(fileNames)) {
                throw new DpException(StatusCode.CODE_10010, "至少选择一个文件");
            }

            try (OutputStream outputStream = response.getOutputStream();
                 ZipOutputStream zout = new ZipOutputStream(outputStream)) {

                response.setContentType("octets/stream");
                //设置字符集和文件后缀名
                response.setContentType("application/zip;" + "charset = UTF-8");
                response.addHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(dpPortalFileManager.getFileName() + ".zip", "utf-8"));

                String[] strings = fileNames.split(",");
                for (int i = 0; i < strings.length; i++) {
                    DpPortalFileManager dpPortalFileManager1 = dpPortalFileManagerService.selectByFileId(roleCode, strings[i], userId);
                    InputStream fileObject = fileSystemManagerService.getFileObject(dpPortalFileManager1.getFilePath());
                    fileObjectList.add(new Pair<>(dpPortalFileManager1.getFileName(), fileObject));
                }

                for (Pair<String, InputStream> child : fileObjectList) {
                    ZipEntry entry = new ZipEntry(child.getK());
                    zout.putNextEntry(entry);
                    int length = 1024 * 1024 * 2;
                    int len;
                    byte[] b = new byte[length];
                    InputStream inputStream = child.getV();
                    while ((len = inputStream.read(b)) != -1) {
                        zout.write(b, 0, len);
                        zout.flush();
                    }
                    inputStream.close();
                }

                zout.flush();

            } catch (AmazonServiceException | IOException e) {
                throw new DpException(StatusCode.CODE_10010, "下载文件失败", e);
            } finally {
                for (Pair<String, InputStream> stringS3ObjectPair : fileObjectList) {
                    try {
                        stringS3ObjectPair.getV().close();
                    } catch (IOException e) {
                        log.error("file close error", e);
                    }
                }
            }
        } else {
            InputStream fileObject = fileSystemManagerService.getFileObject(dpPortalFileManager.getFilePath());
            try (InputStream inputStream = fileObject;
                 OutputStream outputStream = response.getOutputStream()) {
                response.setContentType("octets/stream");
                //设置字符集和文件后缀名
                response.setContentType("application/" + dpPortalFileManager.getFileType() + ";" + "charset = UTF-8");
                response.addHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(dpPortalFileManager.getFileName(), "utf-8"));

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
    }

    @Override
    public void downLoadOutputFile(String roleCode, String userId, String fileId, String ids, HttpServletResponse response) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);

        List<Pair<String, InputStream>> fileObjectList = new ArrayList<>();
        if (ids == null || "".equals(ids)) {
            throw new DpException(StatusCode.CODE_10010, "至少选择一个文件");
        }

        try (OutputStream outputStream = response.getOutputStream();
             ZipOutputStream zout = new ZipOutputStream(outputStream)) {
            response.setContentType("octets/stream");
            //设置字符集和文件后缀名
            response.setContentType("application/zip;" + "charset = UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(dpPortalFileManager.getFileName() + ".zip", "utf-8"));

            String[] strings = ids.split(",");
            String[] fileNames = dpPortalFileManagerService.selectFileNamesByFileIds(strings);
            List<DpPortalFileManager> list = dpPortalFileManagerService.selectByFileNames(dpPortalFileManager.getFileId(), fileNames);
            for (DpPortalFileManager dpPortalFileManager1 : list) {
                fileObjectList.add(new Pair<>(dpPortalFileManager1.getFileName(), fileSystemManagerService.getFileObject(dpPortalFileManager1.getFilePath())));
            }

            for (Pair<String, InputStream> child : fileObjectList) {
                ZipEntry entry = new ZipEntry(child.getK());
                zout.putNextEntry(entry);
                int length = 1024 * 1024 * 2;
                int len;
                byte[] b = new byte[length];
                InputStream inputStream = child.getV();
                while ((len = inputStream.read(b)) != -1) {
                    zout.write(b, 0, len);
                    zout.flush();
                }
                inputStream.close();

            }
            zout.flush();

        } catch (AmazonServiceException | IOException e) {
            throw new DpException(StatusCode.CODE_10010, "下载文件失败", e);
        } finally {
            for (Pair<String, InputStream> stringS3ObjectPair : fileObjectList) {
                try {
                    stringS3ObjectPair.getV().close();
                } catch (IOException e) {
                    log.error("file close error", e);
                }
            }
        }
    }

    @Override
    public Result<List<FileInfoDTO>, Object> selectFileListByType(String roleCode, String userId, String fileType, String isFolder) {
        String[] fileTypes = fileType.split(",");
        List<DpPortalFileManager> dpPortalFileManagers = dpPortalFileManagerService.selectFileListByType(roleCode, fileTypes, userId, isFolder);
        List<FileInfoDTO> fileInfoDTOS = new ArrayList<>();
        for (DpPortalFileManager dpPortalFileManager : dpPortalFileManagers) {
            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            BeanUtils.copyProperties(dpPortalFileManager, fileInfoDTO);
            fileInfoDTO.setFileRelativPath(dpPortalFileManager.getFilePath());
            fileInfoDTO.setUserName(fileSystemManagerService.getUserName());
            fileInfoDTO.setPassword(DefaultEncryptionUtils.encrypt(fileSystemManagerService.getPassword()));
            fileInfoDTO.setCreateUserName(dpPortalFileManager.getCreateUserName());
            fileInfoDTOS.add(fileInfoDTO);
        }
        return Result.success(fileInfoDTOS);
    }

    @Override
    public Result<List<FileInfoDTO>, Object> selectFileFolderList(String roleCode, String userId) {
        List<DpPortalFileManager> dpPortalFileManagers = dpPortalFileManagerService.selectFileFolderList(roleCode, userId, "1");
        List<FileInfoDTO> fileInfoDTOS = new ArrayList<>();
        for (DpPortalFileManager dpPortalFileManager : dpPortalFileManagers) {
            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            BeanUtils.copyProperties(dpPortalFileManager, fileInfoDTO);
            fileInfoDTO.setFileRelativPath(dpPortalFileManager.getFilePath());
            fileInfoDTO.setUserName(fileSystemManagerService.getUserName());
            fileInfoDTO.setPassword(DefaultEncryptionUtils.encrypt(fileSystemManagerService.getPassword()));
            fileInfoDTO.setCreateUserName(dpPortalFileManager.getCreateUserName());
            fileInfoDTO.setCreateUserId(dpPortalFileManager.getCreateUserId());
            fileInfoDTOS.add(fileInfoDTO);
        }
        return Result.success(fileInfoDTOS);
    }

    @Override
    public Result<FileInfoDTO, Object> selectFileById(String roleCode, String userId, String fileId) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        BeanUtils.copyProperties(dpPortalFileManager, fileInfoDTO);
        String isFolder = dpPortalFileManager.getIsFolder();
        fileInfoDTO.setFileRelativPath(dpPortalFileManager.getFilePath());
        fileInfoDTO.setUserName(fileSystemManagerService.getUserName());
        fileInfoDTO.setPassword(DefaultEncryptionUtils.encrypt(fileSystemManagerService.getPassword()));
        fileInfoDTO.setCreateUserName(dpPortalFileManager.getCreateUserName());
        return Result.success(fileInfoDTO);
    }

    @Override
    public Result<FileSystemInfo, Object> selectFileSystemInfo() {
        FileSystemInfo fileSystemInfo = new FileSystemInfo();
        fileSystemInfo.setFsBaseDir(fileSystemManagerService.getRootPath());
        fileSystemInfo.setFsUserName(fileSystemManagerService.getUserName());
        fileSystemInfo.setFsPassword(DefaultEncryptionUtils.encrypt(fileSystemManagerService.getPassword()));
        return Result.success(fileSystemInfo);
    }

    @Override
    public Result getFolderFileList(String roleCode, String userId, String fileId, String fileType) throws IOException {
        //查询父目录
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);

        if (dpPortalFileManager == null) {
            return Result.fail(StatusCode.CODE_10010, false, "文件夹不存在");
        }

        //获取子文件元数据信息
        List<DpPortalFileManager> dpPortalFileManagers;
        //子目录下所有元数据
        List<DpPortalFileManager> dpPortalFileManagersAll = dpPortalFileManagerService.selectFileList(roleCode, userId, fileId);
        if (StringUtils.isBlank(fileType)) {
            dpPortalFileManagers = dpPortalFileManagersAll;
        } else {
            String[] fileTypes = fileType.split(",");
            dpPortalFileManagers = dpPortalFileManagerService.selectFileListByType(roleCode, fileTypes, userId, fileId);
        }

        List<DpPortalFileManager> metaUpdateLists = new ArrayList<>();
        List<DpPortalFileManager> metaInsterLists = new ArrayList<>();

        if (StringUtils.isBlank(fileType)) {
            final List<DpPortalFileManager> loopList = dpPortalFileManagers;

            fileSystemManagerService.loopFolder(dpPortalFileManager.getFilePath(), objectSummary -> {
                boolean flag = true;
                for (DpPortalFileManager fileManager : loopList) {
                    String fileName1 = fileManager.getFileName().replaceAll(" ", "");
                    String fileName2 = fileSystemManagerService.getBaseName(objectSummary.getKey()).replaceAll(" ", "");
                    if (fileName1.equals(fileName2)) {
                        flag = false;
                        if (!equalsIgnoreMillisecond(fileManager.getLastModifiedTime(), objectSummary.getLastModified())) {
                            DpPortalFileManager dpPortalFileManagerNew = new DpPortalFileManager();
                            BeanUtils.copyProperties(fileManager, dpPortalFileManagerNew);
                            dpPortalFileManagerNew.setLastModifiedTime(new Date(getDateSeconds(objectSummary.getLastModified())));
                            dpPortalFileManagerNew.setFileSize(String.valueOf(objectSummary.getSize()));
                            metaUpdateLists.add(dpPortalFileManagerNew);
                        }
                    }
                }
                if (flag) {
                    String fileName = fileSystemManagerService.getBaseName(objectSummary.getKey());
                    DpPortalFileManager dpPortalFileManagerNew = new DpPortalFileManager();
                    dpPortalFileManagerNew.setSourceSystem("探索平台");
                    dpPortalFileManagerNew.setSourceWay("探索项目运行");
                    dpPortalFileManagerNew.setFileId(UUIDUtils.generateUUID32());
                    dpPortalFileManagerNew.setFileName(StringEscapeUtils.unescapeHtml4(fileSystemManagerService.getBaseName(objectSummary.getKey())));
                    final int i = fileName.lastIndexOf(".");
                    if (i > 0) {
                        dpPortalFileManagerNew.setFileType(fileName.substring(i + 1));
                    } else {
                        dpPortalFileManagerNew.setFileType("");
                    }

                    dpPortalFileManagerNew.setOrder(99);
                    dpPortalFileManagerNew.setCreateUserId(userId);
                    dpPortalFileManagerNew.setPid(dpPortalFileManager.getFileId());
                    dpPortalFileManagerNew.setIsFolder("0");
                    dpPortalFileManagerNew.setIsValid("Y");
                    dpPortalFileManagerNew.setUploadTime(objectSummary.getLastModified());
                    dpPortalFileManagerNew.setLastModifiedTime(objectSummary.getLastModified());
                    dpPortalFileManagerNew.setFileServerType(fileServerProperties.getExtendsFileType());
                    dpPortalFileManagerNew.setFileSize(String.valueOf(objectSummary.getSize()));
                    dpPortalFileManagerNew.setFilePath(dpPortalFileManager.getFilePath() + dpPortalFileManagerNew.getFileName());
                    metaInsterLists.add(dpPortalFileManagerNew);
                }
                return null;
            });
        }

        dpPortalFileManagerService.batchUpdateAndInsert(metaUpdateLists, metaInsterLists);

        //子目录下所有元数据
        List<DpPortalFileManager> dpPortalFileManagersAllResult = dpPortalFileManagerService.selectFileList(roleCode, userId, fileId);
        if (StringUtils.isBlank(fileType)) {
            dpPortalFileManagers = dpPortalFileManagersAllResult;
        } else {
            String[] fileTypes = fileType.split(",");
            dpPortalFileManagers = dpPortalFileManagerService.selectFileListByType(roleCode, fileTypes, userId, fileId);
        }

        List<FileInfoDTO> fileInfoDTOS = new ArrayList<>();
        for (DpPortalFileManager dpPortalFileManagerChildre : dpPortalFileManagers) {
            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            BeanUtils.copyProperties(dpPortalFileManagerChildre, fileInfoDTO);
            fileInfoDTO.setFileRelativPath(dpPortalFileManagerChildre.getFilePath());
            fileInfoDTO.setUserName(fileSystemManagerService.getUserName());
            fileInfoDTO.setPassword(DefaultEncryptionUtils.encrypt(fileSystemManagerService.getPassword()));
            fileInfoDTO.setCreateUserName(dpPortalFileManagerChildre.getCreateUserName());
            fileInfoDTOS.add(fileInfoDTO);
        }
        return Result.success(fileInfoDTOS);
    }

    @Override
    public Result<Object, Object> uploadFileToFolder(String roleCode, String sourceSystem, String sourceWay, String sourceProject, String userId, String fileId, HttpServletRequest request) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
        if (dpPortalFileManager == null) {
            return Result.fail(StatusCode.CODE_10010, false, "文件信息不存在");
        }
        if (dpPortalFileManager.getIsFolder().equals("0")) {
            return Result.fail(StatusCode.CODE_10010, false, "不是文件夹");
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("file");
        String folderPath = dpPortalFileManager.getFilePath();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String newFilePath = folderPath + "/" + fileName;

            try {
                fileSystemManagerService.addFile(newFilePath, file.getInputStream(), file.getSize());

            } catch (AmazonServiceException | IOException e) {
                log.error(e.getMessage(), e);
                return Result.fail(StatusCode.CODE_10010, false, e.getMessage());
            }
        }

        return Result.success(dpPortalFileManager.getFileName(), "success");
    }

    @Override
    public Result<String, Object> flushFolderTime(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId) {
        Page page = this.initPage("99999999", "1");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("fileType", null);
        params.put("fileName", null);
        PageInfo<DpPortalFileManagerDTO> portalFileManagerDTOPageInfo = dpPortalFileManagerService.selectByParamPage(params, page);
        List<DpPortalFileManagerDTO> list = portalFileManagerDTOPageInfo.getList();

        List<DpPortalFileManager> dpPortalFileManagers = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (DpPortalFileManagerDTO dpPortalFileManagerDTO : list) {
                final String filePath = dpPortalFileManagerDTO.getFilePath();

                final long lastModifyFileTime = fileSystemManagerService.getLastModifyFileTime(filePath);
                if (lastModifyFileTime > 0) {
                    DpPortalFileManager dpPortalFileManager = new DpPortalFileManager();
                    dpPortalFileManager.setFileId(dpPortalFileManagerDTO.getFileId());
                    dpPortalFileManager.setLastModifiedTime(new Date(lastModifyFileTime));
                    dpPortalFileManagers.add(dpPortalFileManager);
                }
            }
        }
        if (dpPortalFileManagers.size() > 0) {
            dpPortalFileManagerService.batchUpdate(dpPortalFileManagers);
        }

        return Result.success("刷新成功");
    }

    @Override
    public Result<Boolean, Object> deleteFileFromFolder(String roleCode, String userId, String fileId, String fileNames) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
        if (dpPortalFileManager == null) {
            return Result.fail(StatusCode.CODE_10010, false, "文件信息不存在");
        }
        if (dpPortalFileManager.getIsFolder().equals("0")) {
            return Result.fail(StatusCode.CODE_10010, false, "不是文件夹");
        }
        String folderPath = dpPortalFileManager.getFilePath();

        String[] ss = fileNames.split(",");
        for (int i = 0; i < ss.length; i++) {
            String fileName = ss[i];
            String filePath = folderPath + "/" + fileName;

            fileSystemManagerService.delFile(filePath);
            dpPortalFileManagerService.deleteSonFromFolder(fileId, fileName);
        }
        return Result.success(true);
    }

    @Override
    public Result<String, Object> checkFileName(String userId, String fileName, String pid) {
        return Result.success(dpPortalFileManagerService.checkeFileName(fileName, userId, pid));
    }

    @Override
    public Result<String, Object> checkDirName(String userId, String fileName) {
        if (dpPortalFileManagerService.checkeDirName(fileName, userId) != null) {
            return Result.success("文件夹已存在");
        }
        return Result.success("文件夹可用");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result demandFileAdd(String sourceSystem, String sourceWay, String sourceProject, String userId, String userName, DmDemandFileVO dmDemandFileVO, HttpServletRequest request) {

        String defaultSourceSystem = "需求协作平台";
        String defaultSourceWay = "上传";
        Integer taskId = dmDemandFileVO.getTaskId();
        String taskName = dpPortalFileManagerService.findDemandTaskName(taskId);
        String defaultSourceProject = taskName;
        //判断目录是否存在
        String fileId = dpPortalFileManagerService.checkeDirName("demand", userId);
        if (fileId == null) {
            //创建文件夹
            FileAddVO fileAddVO = new FileAddVO();
            fileAddVO.setFileFolderName("demand");
            fileAddVO.setFileName("demand");
            fileAddVO.setIsFolder("1");
            fileAddVO.setOrder(99);
            DpPortalFileManagerDTO dpPortalFileManagerDTO = addDir(defaultSourceProject, userId, userName, fileAddVO).getContent();
            fileId = dpPortalFileManagerDTO.getFileId();
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        List<MultipartFile> files = multipartRequest.getFiles("file");
        if (files.size() < 1) {
            throw new DpException(StatusCode.CODE_10010, "必须选择文件");
        }
        String finalFileId = fileId;
        String finalSourceSystem = defaultSourceSystem;
        String finalSourceWay = defaultSourceWay;
        String finalSourceProject = defaultSourceProject;
        files.forEach(file -> {
            String name = file.getOriginalFilename();
            String path = fileSystemManagerService.getRootPath() + userName + "/demand/" + name;
            DmDemandFile demandFile = new DmDemandFile();
            demandFile.setCreateUser(Integer.parseInt(userId));
            demandFile.setFilePath(path);
            DmDemandFile oldDemandFile = dmDemandFileMapper.countByUserIdAndPath(demandFile);
            //检查文件名是否重复
            if (oldDemandFile != null) {

                try {
                    // 获取上传文件
                    String fileid = dpPortalFileManagerService.checkeDirName(name, userId);
                    if (fileid != null) {
                        DpPortalFileManager dpPortalFileManager1 = new DpPortalFileManager();
                        dpPortalFileManager1.setFileId(fileid);
                        dpPortalFileManager1.setFileSize(String.valueOf(file.getSize()));
                        dpPortalFileManagerService.updateByPrimaryKeySelective(dpPortalFileManager1);
                    }

                    addFile(path, file.getInputStream(), file.getSize());
                    oldDemandFile.setUpdateTime(new Date());
                    oldDemandFile.setFileSize((int) file.getSize());
                    oldDemandFile.setUpdateUser(Integer.parseInt(userId));
                    oldDemandFile.setFilePath(path);
                    dmDemandFileMapper.updateByPrimaryKey(oldDemandFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new DpException(StatusCode.CODE_10010, "获取更新失败");
                }

            } else {
                try {
                    DpPortalFileManager dpPortalFileManager = new DpPortalFileManager();
                    dpPortalFileManager.setSourceSystem(finalSourceSystem);
                    dpPortalFileManager.setSourceWay(finalSourceWay);
                    dpPortalFileManager.setSourceProject(finalSourceProject);
                    dpPortalFileManager.setFileId(UUIDUtils.generateUUID32());
                    dpPortalFileManager.setFileName(StringEscapeUtils.unescapeHtml4(name));
                    dpPortalFileManager.setFileType(name.substring(name.lastIndexOf(".") + 1));
                    dpPortalFileManager.setCreateUserId(userId);
                    dpPortalFileManager.setPid(finalFileId);
                    dpPortalFileManager.setIsFolder("0");
                    dpPortalFileManager.setIsValid("Y");
                    dpPortalFileManager.setUploadTime(new Date());
                    dpPortalFileManager.setLastModifiedTime(new Date());
                    dpPortalFileManager.setFilePath(path);
                    dpPortalFileManager.setFileServerType(fileServerProperties.getExtendsFileType());
                    dpPortalFileManager.setFileSize(String.valueOf(file.getSize()));
                    dpPortalFileManagerService.insert(dpPortalFileManager);

                    addFile(path, file.getInputStream(), file.getSize());
                    DmDemandFile newDemandFile = new DmDemandFile();
                    BeanUtils.copyProperties(dmDemandFileVO, newDemandFile);
                    newDemandFile.setCreateTime(new Date());
                    newDemandFile.setFileSize((int) file.getSize());
                    newDemandFile.setCreateUser(Integer.parseInt(userId));
                    newDemandFile.setFileName(StringEscapeUtils.unescapeHtml4(name));
                    newDemandFile.setFilePath(path);
                    dmDemandFileMapper.insert(newDemandFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new DpException(StatusCode.CODE_10010, "获取保存失败");

                }

            }
        });

        return Result.success(true);
    }

    @Override
    public void download(Integer fileId, HttpServletResponse response) {
        DmDemandFile dmDemandFile = dmDemandFileMapper.selectByPrimaryKey(fileId);
        if (dmDemandFile == null) {
            throw new DpException(StatusCode.CODE_10010, "文件不存在");
        }
        InputStream fileObject = fileSystemManagerService.getFileObject(dmDemandFile.getFilePath());
        try (InputStream inputStream = fileObject;
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("octets/stream");
            response.setContentType("charset = UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(dmDemandFile.getFileName(), "utf-8"));

            int length = 1024 * 1024 * 2;
            int len;
            byte[] b = new byte[length];
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            log.error("download error.", e);
            throw new DpException(StatusCode.CODE_10010, "下载文件失败");
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

    /**
     * 增加文件.
     *
     * @param relativePath 文件相对路径
     * @param inputStream  文件输入流
     * @param size         文件大小
     * @throws DpException Stella异常处理
     */
    public void addFile(String relativePath, InputStream inputStream, long size) throws DpException {
        try {
            fileSystemManagerService.addFile(relativePath, inputStream, size);
        } catch (AmazonServiceException e) {
            throw new DpException(StatusCode.CODE_10010, "新建文件失败", e);
        }
    }

    /**
     * 增加文件.
     *
     * @param sourceSystem  系统来源.
     * @param sourceWay     来源方式
     * @param sourceProject 来源项目
     * @param userId        用户id
     * @param userName      用户名
     * @param effectiveDays 有效天数
     * @param fileName      文件名称
     * @param pid           文件pid
     * @param file          文件内容
     * @return 返回结果
     */
    @ApiOperation(value = "新增一个文件", notes = "新增一个文件")
    @PostMapping(value = "/addFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<DpPortalFileManagerDTO, Object> addFile(@NotBlank @ApiParam("来源系统") @RequestParam("sourceSystem") String sourceSystem,
                                                          @NotBlank @ApiParam("来源方式") @RequestParam("sourceWay") String sourceWay,
                                                          @ApiParam("来源项目") @RequestParam(value = "sourceProject", required = false) String sourceProject,
                                                          @ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                                          @ApiParam("操作用户Id") @RequestHeader("authorization-userName") String userName,
                                                          @ApiParam("文件保存天数") @RequestParam(value = "effectiveDays", required = false) Integer effectiveDays,
                                                          @RequestParam("fileName") String fileName,
                                                          @RequestParam("pid") String pid, @RequestPart("file") MultipartFile file) {

        if (file == null) {
            return Result.fail(StatusCode.CODE_10010, null, "文件类型为空");
        }

        DpPortalFileManager dpPortalFileManager = new DpPortalFileManager();
        dpPortalFileManager.setSourceSystem(sourceSystem);
        dpPortalFileManager.setSourceWay(sourceWay);
        dpPortalFileManager.setSourceProject(sourceProject);
        dpPortalFileManager.setFileId(UUIDUtils.generateUUID32());
        dpPortalFileManager.setFileName(StringEscapeUtils.unescapeHtml4(fileName));
        dpPortalFileManager.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
        dpPortalFileManager.setCreateUserId(userId);
        dpPortalFileManager.setPid(pid);
        dpPortalFileManager.setIsFolder("0");
        dpPortalFileManager.setIsValid("Y");
        dpPortalFileManager.setEffectiveDays(effectiveDays);
        dpPortalFileManager.setUploadTime(new Date());
        dpPortalFileManager.setLastModifiedTime(new Date());
        dpPortalFileManager.setFileServerType(fileServerProperties.getExtendsFileType());
        dpPortalFileManager.setFileSize(String.valueOf(file.getSize()));
        DpPortalFileManager dpPortalFileManager1 = dpPortalFileManagerService.selectByFileId(pid);
        try {
            dpPortalFileManager.setFilePath(fileSystemManagerService.getRootPath() + userName + "/" + dpPortalFileManager1.getFileName() + "/" + dpPortalFileManager.getFileName());
            fileSystemManagerService.addFile(dpPortalFileManager.getFilePath(), file.getInputStream(), file.getSize());
            // 获取文件名
            String fileId = dpPortalFileManagerService.checkeFileName(fileName, userId, pid);
            if (StringUtils.isNotBlank(fileId)) {
                dpPortalFileManager.setFileId(fileId);
                dpPortalFileManagerService.updateByPrimaryKeySelective(dpPortalFileManager);
            } else {
                dpPortalFileManagerService.insert(dpPortalFileManager);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new DpException(StatusCode.CODE_10010, "获取文件失败");
        }
        DpPortalFileManagerDTO dpPortalFileManagerDTO = new DpPortalFileManagerDTO();
        BeanUtils.copyProperties(dpPortalFileManager, dpPortalFileManagerDTO);

        return Result.success(dpPortalFileManagerDTO);
    }

    /**
     * 比较两个日期，忽略毫秒单位.
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 两个日期是否相同
     */
    private boolean equalsIgnoreMillisecond(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return getDateSeconds(date1) == getDateSeconds(date2);
    }

    private long getDateSeconds(Date date) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTimeInMillis();
    }

}
