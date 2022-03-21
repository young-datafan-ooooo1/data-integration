package com.youngdatafan.dataintegration.file.management.service.impl;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.file.management.dto.DpFileRegularCleanDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.mapper.DpFileRegularCleanMapper;
import com.youngdatafan.dataintegration.file.management.mapper.DpPortalFileManagerMapper;
import com.youngdatafan.dataintegration.file.management.model.DpFileRegularClean;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileManager;
import com.youngdatafan.dataintegration.file.management.service.DpFileRegularCleanService;
import com.youngdatafan.dataintegration.file.management.service.DpPortalFileManagerService;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import com.youngdatafan.dataintegration.file.management.utils.RedisKeyConstants;
import com.youngdatafan.dataintegration.file.management.vo.DpFileRegularCleanUptVO;
import com.youngdatafan.dataintegration.file.management.vo.FolderQueryVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.dataintegration.core.lock.CacheLock;

/**
 * 文件定时清理impl.
 *
 * @author songxiaolang
 * @since 2022-01-04 14:59
 */
@Slf4j
@Service
public class DpFileRegularCleanServiceImpl implements DpFileRegularCleanService {

    @Value("${esCat.file.RegularCleanId:1}")
    private Integer fileRegularCleanId;

    @Value("${esCat.file.RegularCleanDay:360}")
    private Integer fileRegularCleanDay;

    private final DpPortalFileManagerMapper dpPortalFileManagerMapper;

    private final DpFileRegularCleanMapper dpFileRegularCleanMapper;

    @Autowired
    private DpPortalFileManagerService dpPortalFileManagerService;

    @Autowired
    private FileSystemManagerService fileSystemManagerService;

    public DpFileRegularCleanServiceImpl(DpFileRegularCleanMapper dpFileRegularCleanMapper, DpPortalFileManagerMapper dpPortalFileManagerMapper) {
        this.dpFileRegularCleanMapper = dpFileRegularCleanMapper;
        this.dpPortalFileManagerMapper = dpPortalFileManagerMapper;
    }

    @Override
    public int update(String userId, DpFileRegularCleanUptVO updateVO) {
        DpFileRegularCleanDTO detail = dpFileRegularCleanMapper.getDetail(fileRegularCleanId);
        DpFileRegularClean dpFileRegularClean = new DpFileRegularClean();
        if (detail == null) {
            BeanUtils.copyProperties(updateVO, dpFileRegularClean);
            dpFileRegularClean.setFileRegularCleanId(fileRegularCleanId);
            dpFileRegularClean.setCreateTime(new Date());
            dpFileRegularClean.setUpdateTime(new Date());
            dpFileRegularClean.setEffectiveDays(dpFileRegularClean.getEffectiveDays());
            dpFileRegularClean.setCreateUserId(Integer.valueOf(userId));
            dpFileRegularClean.setUpdateUserId(Integer.valueOf(userId));
            return dpFileRegularCleanMapper.insert(dpFileRegularClean);
        } else {
            dpFileRegularClean.setFileRegularCleanId(fileRegularCleanId);
            dpFileRegularClean.setCreateTime(new Date());
            dpFileRegularClean.setUpdateTime(new Date());
            dpFileRegularClean.setIsUseBusiness(updateVO.getIsUseBusiness());
            dpFileRegularClean.setEffectiveDays(updateVO.getEffectiveDays());
            dpFileRegularClean.setCreateUserId(Integer.valueOf(userId));
            dpFileRegularClean.setUpdateUserId(Integer.valueOf(userId));
            return dpFileRegularCleanMapper.update(dpFileRegularClean);
        }

    }

    @Override
    public DpFileRegularCleanDTO get() {
        DpFileRegularCleanDTO detail = dpFileRegularCleanMapper.getDetail(fileRegularCleanId);
        if (detail == null) {
            return new DpFileRegularCleanDTO();
        } else {
            return detail;
        }
    }

    @Override
    public int getFileEffectiveDays(Integer sysTemEffectiveDay, Integer businessEffectiveDay, Date fileUpdateDay) {
        if (sysTemEffectiveDay == null) {
            throw new DpException(StatusCode.CODE_10010, "系统设置保留天数不能为空");
        }
        if (fileUpdateDay == null) {
            throw new DpException(StatusCode.CODE_10010, "文件修改时间不能为空");
        }
        if (businessEffectiveDay != null) {
            return businessEffectiveDay - daysBetween(fileUpdateDay, new Date());
        }
        return sysTemEffectiveDay - daysBetween(fileUpdateDay, new Date());
    }

    @Override
    @CacheLock(key = RedisKeyConstants.ESCAT_FILE_SUFFIX + "timingCleanFile")
    public void timingCleanFile(DpFileRegularCleanDTO dpFileRegularCleanDTO) {
        //刷新所有文件
        refreshAllFile();
        //获取所有文件
        List<DpPortalFileManager> dpPortalFileManagers = dpPortalFileManagerMapper.getAllFileNotDir();
        Integer sysTemEffectiveDays = dpFileRegularCleanDTO.getEffectiveDays();
        Integer isUseBusiness = dpFileRegularCleanDTO.getIsUseBusiness();
        //系统规则的文件
        List<DpPortalFileManager> needDeleteFiles = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dpPortalFileManagers)) {
            dpPortalFileManagers.forEach(dpPortalFileManager -> {
                //文件业务设置效天数
                Integer effectiveDays = dpPortalFileManager.getEffectiveDays();
                //如果不同时按照业务设置保留时长，则将effectiveDay滞空
                if (isUseBusiness == 0) {
                    effectiveDays = null;
                }
                //文件最后修改时间
                Date lastModifiedTime = dpPortalFileManager.getLastModifiedTime();
                int fileEffectiveDays = getFileEffectiveDays(sysTemEffectiveDays, effectiveDays, lastModifiedTime);
                if (fileEffectiveDays <= 0) {
                    log.info("文件有效期为{}", fileEffectiveDays);
                    needDeleteFiles.add(dpPortalFileManager);
                }
            });

        }
        deleteFiles(needDeleteFiles);
    }

    @Override
    @CacheLock(key = RedisKeyConstants.ESCAT_FILE_SUFFIX + "inspectFileRegularCleanTable")
    public void inspectFileRegularCleanTable() {
        try {
            //刷新老的数据，上传文件时候没有将最后修改时间设置为上传时间
            dpPortalFileManagerMapper.refreshOldData();
            //刷新用户账号
            dpPortalFileManagerMapper.refreshUserName();
            DpFileRegularCleanDTO dpFileRegularCleanDTO = this.get();
            if (dpFileRegularCleanDTO == null || dpFileRegularCleanDTO.getFileRegularCleanId() == null) {
                DpFileRegularClean dpFileRegularClean = new DpFileRegularClean();
                dpFileRegularClean.setEffectiveDays(fileRegularCleanDay);
                dpFileRegularClean.setIsUseBusiness(1);
                dpFileRegularClean.setFileRegularCleanId(fileRegularCleanId);
                dpFileRegularClean.setUpdateUserId(1);
                dpFileRegularClean.setCreateUserId(1);
                dpFileRegularClean.setCreateTime(new Date());
                dpFileRegularClean.setUpdateTime(new Date());
                dpFileRegularCleanMapper.insert(dpFileRegularClean);
            }
        } catch (Exception e) {
            log.error("检查文件定时清理规则表异常", e);
        }
    }

    /**
     * 删除文件.
     *
     * @param needDeleteFiles 需要删除的文件
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteFiles(List<DpPortalFileManager> needDeleteFiles) {
        List<String> deleteFileIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(needDeleteFiles)) {
            needDeleteFiles.forEach(file -> {
                String filePath = file.getFilePath();
                String fileId = file.getFileId();
                fileSystemManagerService.delFile(filePath);
                deleteFileIds.add(fileId);
            });
            if (CollectionUtils.isNotEmpty(deleteFileIds)) {
                dpPortalFileManagerMapper.deleteFileByIds(deleteFileIds);
            }
        }

    }

    /**
     * 刷新所有文件.
     */
    public void refreshAllFile() {
        //获取所有文件夹信息
        List<FolderInfoDTO> folderInfoDTOS = dpPortalFileManagerMapper.queryFolderByParam(new FolderQueryVO());
        if (CollectionUtils.isNotEmpty(folderInfoDTOS)) {
            folderInfoDTOS.forEach(folderInfoDTO -> {
                //文件夹Id
                String folderId = folderInfoDTO.getFolderId();
                dpPortalFileManagerService.refreshOneFolder(folderId);
            });
        }

    }

    /**
     * 计算两个日期之间相差的天数.
     *
     * @param sDate 较小的时间
     * @param bDate 较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date sDate, Date bDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date smallDate;
        Date bigDate;
        try {
            smallDate = sdf.parse(sdf.format(sDate));
            bigDate = sdf.parse(sdf.format(bDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smallDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bigDate);
            long time2 = cal.getTimeInMillis();
            long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(betweenDays));
        } catch (ParseException e) {
            throw new DpException(StatusCode.CODE_10010, "date parseException ", e);
        }
    }
}
