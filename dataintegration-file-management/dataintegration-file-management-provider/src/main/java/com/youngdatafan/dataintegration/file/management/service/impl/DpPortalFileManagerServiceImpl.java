package com.youngdatafan.dataintegration.file.management.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.util.PageableHelpUtil;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.dataintegration.file.management.config.FileServerProperties;
import com.youngdatafan.dataintegration.file.management.dto.DpFileRegularCleanDTO;
import com.youngdatafan.dataintegration.file.management.dto.DpPortalFileManagerDTO;
import com.youngdatafan.dataintegration.file.management.dto.FileInfoDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.mapper.DpPortalFileManagerMapper;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileManager;
import com.youngdatafan.dataintegration.file.management.service.DpFileRegularCleanService;
import com.youngdatafan.dataintegration.file.management.service.DpPortalFileManagerService;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import com.youngdatafan.dataintegration.file.management.utils.BeanConvertUtil;
import com.youngdatafan.dataintegration.file.management.utils.DateUtil;
import com.youngdatafan.dataintegration.file.management.utils.PageInfoUtil;
import com.youngdatafan.dataintegration.file.management.vo.FileInfoVO;
import com.youngdatafan.dataintegration.file.management.vo.FileQueryVO;
import com.youngdatafan.dataintegration.file.management.vo.FolderQueryVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantDeptVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 文件管理服务.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/8 3:41 下午
 */
@Slf4j
@Service
public class DpPortalFileManagerServiceImpl implements DpPortalFileManagerService {

    @Autowired
    private FileServerProperties fileServerProperties;

    @Resource
    private DpPortalFileManagerMapper dpPortalFileManagerMapper;

    @Resource
    private FileSystemManagerService fileSystemManagerService;

    @Resource
    private DpFileRegularCleanService dpFileRegularCleanService;

    private final SqlSessionTemplate sqlSessionTemplate;

    public DpPortalFileManagerServiceImpl(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public int deleteByPrimaryKey(String fileId, String userId) {
        return dpPortalFileManagerMapper.deleteByPrimaryKey(fileId, userId);
    }

    @Override
    public int insert(DpPortalFileManager record) {
        return dpPortalFileManagerMapper.insert(record);
    }

    @Override
    public int insertSelective(DpPortalFileManager record) {
        return dpPortalFileManagerMapper.insertSelective(record);
    }

    @Override
    public DpPortalFileManager selectByPrimaryKey(String fileId) {
        return dpPortalFileManagerMapper.selectByPrimaryKey(fileId);
    }

    @Override
    public int updateByPrimaryKeySelective(DpPortalFileManager record) {
        return dpPortalFileManagerMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(DpPortalFileManager record) {
        return dpPortalFileManagerMapper.updateByPrimaryKey(record);
    }

    @Override
    public DpPortalFileManager selectByFileId(String roleCode, String fileId, String userId) {
        return dpPortalFileManagerMapper.selectByFileId(roleCode, fileId, userId);
    }

    @Override
    public DpPortalFileManager selectByFileId(String fileId) {
        return dpPortalFileManagerMapper.selectItemByFileId(fileId);
    }

    @Override
    public PageInfo<DpPortalFileManagerDTO> selectByParamPage(Map<String, Object> params, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<DpPortalFileManager> dpPortalFileManagers = dpPortalFileManagerMapper.selectByParam(params);
        PageInfo<DpPortalFileManager> dpPortalFileManagerPageInfo = new PageInfo<>(dpPortalFileManagers);
        return PageInfoUtil.pageInfo2PageInfoDTO(dpPortalFileManagerPageInfo, DpPortalFileManagerDTO.class);
    }

    @Override
    public List<DpPortalFileManager> selectByFileIds(String roleCode, String[] fileIds, String userId) {
        return dpPortalFileManagerMapper.selectByFileIds(roleCode, fileIds, userId);
    }

    @Override
    public List<DpPortalFileManager> selectByFileIds(String[] fileIdList) {

        return dpPortalFileManagerMapper.selectByFileIdList(fileIdList);
    }

    @Override
    public int deleteByFileIds(String[] fileIds, String userId, String roleCode) {
        return dpPortalFileManagerMapper.deleteByFileIds(fileIds, userId, roleCode);
    }

    @Override
    public String checkeFileName(String fileName, String userId, String pid) {
        return dpPortalFileManagerMapper.checkeFileName(fileName, userId, pid);
    }

    @Override
    public String checkeDirName(String fileName, String userId) {
        return dpPortalFileManagerMapper.checkeDirName(fileName, userId);
    }

    @Override
    public List<DpPortalFileManager> selectFileListByType(String roleCode, String[] fileType, String userId, String pid) {
        return dpPortalFileManagerMapper.selectFileListByType(roleCode, fileType, userId, pid);
    }

    @Override
    public List<DpPortalFileManager> selectFileList(String roleCode, String userId, String pid) {
        return dpPortalFileManagerMapper.selectFileList(roleCode, userId, pid);
    }

    @Override
    public List<DpPortalFileManager> selectFileFolderList(String roleCode, String userId, String isFolder) {
        return dpPortalFileManagerMapper.selectFileFolderList(roleCode, userId, isFolder);
    }

    @Override
    public int deleteSonByPrimaryKey(String fileId) {
        return dpPortalFileManagerMapper.deleteSonByPrimaryKey(fileId);
    }

    @Override
    public int deleteSonFromFolder(String fileId, String fileName) {
        return dpPortalFileManagerMapper.deleteSonFromFolder(fileId, fileName);
    }

    @Override
    public List<DpPortalFileManager> selectByPid(String fileId) {
        return dpPortalFileManagerMapper.selectByPid(fileId);
    }

    @Override
    public void batchUpdate(List<DpPortalFileManager> dpPortalFileManagers) {
        dpPortalFileManagerMapper.batchUpdate(dpPortalFileManagers);
    }

    @Override
    public String findDemandTaskName(Integer taskId) {
        return dpPortalFileManagerMapper.findDemandTaskName(taskId);
    }

    @Override
    public List<DpPortalFileManager> selectByFileNames(String fileId, String[] strings) {
        return dpPortalFileManagerMapper.selectByFileNames(fileId, strings);
    }

    @Override
    public String[] selectFileNamesByFileIds(String[] strings) {
        return dpPortalFileManagerMapper.selectFileNamesByFileIds(strings);
    }

    @Override
    public void batchUpdateAndInsert(List<DpPortalFileManager> metaUpdateLists, List<DpPortalFileManager> metaInsterLists) {
        // 批量新增资产项向量落地表
        try (SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)) {
            DpPortalFileManagerMapper dpPortalFileManager = sqlSession.getMapper(DpPortalFileManagerMapper.class);
            metaUpdateLists.forEach(assetInfo -> {
                dpPortalFileManager.updateByPrimaryKeySelective(assetInfo);
            });
            metaInsterLists.forEach(assetInfo -> {
                dpPortalFileManager.insertSelective(assetInfo);
            });
            sqlSession.commit();
        }
    }

    @Override
    public List<FolderInfoDTO> queryFolder(String userId) {
        return this.dpPortalFileManagerMapper.queryFolder(userId);
    }

    @Override
    public PageInfo<FileInfoVO> queryFileByParam(FileQueryVO queryVO, Pageable pageable) {
        //数据库文件大小单位为b，传递过来的参数单位是kb，做个转换
        Integer fileSizeMin = queryVO.getFileSizeMin();
        if (fileSizeMin != null) {
            fileSizeMin = fileSizeMin == 1 ? 0 : fileSizeMin;
            fileSizeMin = fileSizeMin * 1024;
            queryVO.setFileSizeMin(fileSizeMin);
        }
        Integer fileSizeMax = queryVO.getFileSizeMax();
        if (fileSizeMax != null) {
            fileSizeMax = fileSizeMax * 1024;
            queryVO.setFileSizeMax(fileSizeMax);
        }
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize(), PageableHelpUtil.getOrders(pageable.getSort()));
        List<FileInfoDTO> fileInfoDTOS = this.dpPortalFileManagerMapper.queryFileByParam(queryVO);
        DpFileRegularCleanDTO dpFileRegularCleanDTO = dpFileRegularCleanService.get();
        // 根据条件获取结果
        PageInfo pageInfo = new PageInfo(fileInfoDTOS);
        List<FileInfoVO> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(fileInfoDTOS)) {
            result = fileInfoDTOS.stream().map(t -> {
                FileInfoVO fileInfoVO = new FileInfoVO();
                BeanUtils.copyProperties(t, fileInfoVO);
                if (fileInfoVO.getDeptId() == null) {
                    fileInfoVO.setDeptId(-1);
                    fileInfoVO.setDeptName("未分配部门");
                }
                if (StringUtils.isBlank(fileInfoVO.getUserName())) {
                    fileInfoVO.setUserName("用户已删除(" + t.getAccountOld() + ")");
                }
                //计算剩余时间
                int fileEffectiveDays = dpFileRegularCleanService.getFileEffectiveDays(dpFileRegularCleanDTO.getEffectiveDays(), t.getEffectiveDays(), t.getLastModifiedTime());
                fileInfoVO.setRemainingDay(fileEffectiveDays);
                Integer effectiveDay = t.getEffectiveDays() == null ? dpFileRegularCleanDTO.getEffectiveDays() : t.getEffectiveDays();
                fileInfoVO.setEffectiveDay(effectiveDay);
                fileInfoVO.setFilePath(t.getFileRelativPath());
                //文件大小换算
                String fileSize = fileInfoVO.getFileSize();
                if (StringUtils.isNotBlank(fileSize)) {
                    fileSize = fileSize.trim();
                    BigDecimal fileSizeBig;
                    if ("0".equals(fileSize)) {
                        fileSizeBig = BigDecimal.ZERO;
                    } else {
                        fileSizeBig = new BigDecimal(fileSize);
                        fileSizeBig = fileSizeBig.divide(new BigDecimal("1024"), 0, BigDecimal.ROUND_HALF_UP);
                        fileSizeBig = fileSizeBig.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : fileSizeBig;
                    }
                    fileInfoVO.setFileSize(fileSizeBig.toPlainString());
                }
                return fileInfoVO;
            }).collect(Collectors.toList());
        }
        pageInfo.setList(result);
        return pageInfo;
    }

    @Override
    public FolderInfoDTO queryOneFolder(String folderId) {
        FolderQueryVO folderQueryVO = new FolderQueryVO();
        folderQueryVO.setFolderId(folderId);
        List<FolderInfoDTO> folderInfoDTOS = this.dpPortalFileManagerMapper.queryFolderByParam(folderQueryVO);
        if (CollectionUtils.isEmpty(folderInfoDTOS)) {
            throw new DpException(StatusCode.CODE_10010, "文件夹信息不存在!");
        } else if (folderInfoDTOS.size() > 1) {
            throw new DpException(StatusCode.CODE_10010, "文件夹信息异常，查询出多条文件夹!");
        }
        return folderInfoDTOS.get(0);
    }

    @Override
    public List<GrantDeptVO> queryAdminFolder() {
        List<FolderInfoDTO> folderInfoDTOS = this.dpPortalFileManagerMapper.queryFolderByParam(new FolderQueryVO());
        return BeanConvertUtil.convertFolderToTree(folderInfoDTOS);
    }

    @Override
    public void refreshOneFolder(String folderId) {
        FolderInfoDTO folderInfoDTO = this.queryOneFolder(folderId);
        List<DpPortalFileManager> metaUpdateLists = new ArrayList<>();
        List<DpPortalFileManager> metaInsterLists = new ArrayList<>();
        //子目录下所有元数据
        List<DpPortalFileManager> dpPortalFileManagers = this.selectFileList(null, String.valueOf(folderInfoDTO.getUserId()), folderInfoDTO.getFolderId());
        //检查s3文件是否存在
        clearFiles(dpPortalFileManagers);
        fileSystemManagerService.loopFolder(folderInfoDTO.getFilePath(), objectSummary -> {
            boolean flag = true;
            for (DpPortalFileManager fileManager : dpPortalFileManagers) {
                String fileName1 = fileManager.getFileName().replaceAll(" ", "");
                String fileName2 = fileSystemManagerService.getBaseName(objectSummary.getKey()).replaceAll(" ", "");
                if (fileName1.equals(fileName2)) {
                    flag = false;
                    if (!DateUtil.equalsIgnoreMillisecond(fileManager.getLastModifiedTime(), objectSummary.getLastModified())) {
                        DpPortalFileManager dpPortalFileManagerNew = new DpPortalFileManager();
                        BeanUtils.copyProperties(fileManager, dpPortalFileManagerNew);
                        dpPortalFileManagerNew.setLastModifiedTime(new Date(DateUtil.getDateSeconds(objectSummary.getLastModified())));
                        dpPortalFileManagerNew.setFileSize(String.valueOf(objectSummary.getSize()));
                        metaUpdateLists.add(dpPortalFileManagerNew);
                    }
                }
            }
            if (flag) {
                //默认新建的文件都是来自探索平台
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
                dpPortalFileManagerNew.setCreateUserId(String.valueOf(folderInfoDTO.getUserId()));
                dpPortalFileManagerNew.setPid(folderId);
                dpPortalFileManagerNew.setIsFolder("0");
                dpPortalFileManagerNew.setIsValid("Y");
                dpPortalFileManagerNew.setUploadTime(objectSummary.getLastModified());
                dpPortalFileManagerNew.setLastModifiedTime(objectSummary.getLastModified());
                dpPortalFileManagerNew.setFileServerType(fileServerProperties.getExtendsFileType());
                dpPortalFileManagerNew.setFileSize(String.valueOf(objectSummary.getSize()));
                dpPortalFileManagerNew.setFilePath(folderInfoDTO.getFilePath() + dpPortalFileManagerNew.getFileName());
                metaInsterLists.add(dpPortalFileManagerNew);
            }
            return null;
        });

        this.batchUpdateAndInsert(metaUpdateLists, metaInsterLists);
    }

    @Override
    public void clearFiles(List<DpPortalFileManager> dpPortalFileManagers) {
        final long startTimeMillis = System.currentTimeMillis();
        int count = 0;
        log.info("开始检查文件是否存在s3");
        //记录需要删除的文件Id
        List<String> deleteFileIds = new ArrayList<>();
        for (DpPortalFileManager file : dpPortalFileManagers) {
            //文件路径
            String filePath = file.getFilePath();
            //通过文件路径查看文件s3文件系统是否存在此文件
            boolean exists = fileSystemManagerService.exists(filePath);
            if (!exists) {
                deleteFileIds.add(file.getFileId());
            }
        }
        if (CollectionUtils.isNotEmpty(deleteFileIds)) {
            count = dpPortalFileManagerMapper.deleteFileByIds(deleteFileIds);
        }
        long time = System.currentTimeMillis() - startTimeMillis;
        log.info("初始化总时间：{}ms,删除元数据{}条", time, count);
    }

}
