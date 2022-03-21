package com.youngdatafan.dataintegration.file.management.mapper;

import com.youngdatafan.dataintegration.file.management.dto.FileInfoDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileManager;
import com.youngdatafan.dataintegration.file.management.vo.FileQueryVO;
import com.youngdatafan.dataintegration.file.management.vo.FolderQueryVO;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文件管理mapper.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/8 15:41
 */
@Mapper
public interface DpPortalFileManagerMapper {

    /**
     * 根据主键删除文件信息.
     * @param fileId 文件id
     * @param userId 用户id
     * @return 删除信息之后的主键
     */
    int deleteByPrimaryKey(@Param("fileId") String fileId, @Param("userId") String userId);

    /**
     * 插入一条文件信息记录.
     * @param record 文件信息记录
     * @return 插入信息后的主键
     */
    int insert(DpPortalFileManager record);

    /**
     * 选择性插入信息, 只插入传递过来的参数信息.
     * @param record 信息记录
     * @return 插入信息后的主键
     */
    int insertSelective(DpPortalFileManager record);

    /**
     * 根据主键查询文件信息.
     * @param fileId 文件id.
     * @return 文件信息.
     */
    DpPortalFileManager selectByPrimaryKey(String fileId);

    /**
     * 根据主键查询文件信息.
     * @param record 文件信息记录.
     * @return 更新的主键.
     */
    int updateByPrimaryKeySelective(DpPortalFileManager record);

    /**
     * 根据主键更新文件信息.
     * @param record 信息记录.
     * @return 更新主键.
     */
    int updateByPrimaryKey(DpPortalFileManager record);

    /**
     * 根据文件id查询文件信息.
     * @param roleCode 角色权限编码
     * @param fileId 文件id
     * @param userId 用户id
     * @return 文件信息
     */
    DpPortalFileManager selectByFileId(@Param("roleCode") String roleCode, @Param("fileId") String fileId, @Param("userId") String userId);

    /**
     * 根据文件id集合查询文件信息.
     * @param roleCode 角色权限编码
     * @param fileIds 文件id集合.
     * @param userId 用户id.
     * @return 文件信息集合.
     */
    List<DpPortalFileManager> selectByFileIds(@Param("roleCode") String roleCode, @Param("fileIds") String[] fileIds, @Param("userId") String userId);

    /**
     * 根据文件id集合删除文件信息.
     * @param fileIds 文件id集合.
     * @param userId 用户id
     * @param roleCode 角色权限编码
     * @return 删除信息的最后一个主键id
     */
    int deleteByFileIds(@Param("fileIds") String[] fileIds, @Param("userId") String userId, @Param("roleCode") String roleCode);

    /**
     * 根据参数查询文件信息.
     * @param params 参数
     * @return 文件信息集合
     */
    List<DpPortalFileManager> selectByParam(Map<String, Object> params);

    /**
     * 检验文件名.
     * @param fileName 文件名
     * @param userId 用户id
     * @param pid 文件信息pid
     * @return 查询到的文件名.
     */
    String checkeFileName(@Param("fileName") String fileName, @Param("userId") String userId, @Param("pid") String pid);

    /**
     * 检验文件夹名称.
     * @param fileName 文件夹名称.
     * @param userId 用户id
     * @return 文件夹名称.
     */
    String checkeDirName(@Param("fileName") String fileName, @Param("userId") String userId);

    /**
     * 根据文件类型查询文件信息.
     * @param roleCode 角色权限编码
     * @param fileType 文件类型
     * @param userId 用户id
     * @param pid 文件信息pid
     * @return 文件信息集合
     */
    List<DpPortalFileManager> selectFileListByType(@Param("roleCode") String roleCode, @Param("fileTypes") String[] fileType, @Param("userId") String userId, @Param("pid") String pid);

    /**
     * 根据主键删除子文件.
     * @param fileId 文件id
     * @return 删除文件信息的主键
     */
    int deleteSonByPrimaryKey(@Param("fileId") String fileId);

    /**
     * 删除某一文件夹下的主键.
     * @param fileId 文件id
     * @param fileName 文件名称
     * @return 删除之后的文件主键
     */
    int deleteSonFromFolder(@Param("fileId") String fileId, @Param("fileName") String fileName);

    /**
     * 根据文件pid查询文件信息.
     * @param fileId 文件id.
     * @return 文件信息集合
     */
    List<DpPortalFileManager> selectByPid(@Param("fileId") String fileId);

    /**
     * 查询文件夹.
     * @param roleCode 角色权限编码
     * @param userId 用户id
     * @param isFolder 是否是文件夹
     * @return 文件夹信息集合
     */
    List<DpPortalFileManager> selectFileFolderList(@Param("roleCode") String roleCode, @Param("userId") String userId, @Param("isFolder") String isFolder);

    /**
     * 查询文件集合.
     * @param roleCode 角色权限编码
     * @param userId 用户id
     * @param pid 文件pid.
     * @return 文件信息集合
     */
    List<DpPortalFileManager> selectFileList(@Param("roleCode") String roleCode, @Param("userId") String userId, @Param("pid") String pid);

    /**
     * 根据文件id查询文件信息.
     * @param fileId 文件id
     * @return 文件信息
     */
    DpPortalFileManager selectItemByFileId(@Param("fileId") String fileId);

    /**
     * 批量更新.
     * @param list 文件信息集合.
     */
    void batchUpdate(List<DpPortalFileManager> list);

    /**
     * 查询需求任务.
     * @param taskId 任务id
     * @return 任务名称
     */
    String findDemandTaskName(@Param("taskId") Integer taskId);

    /**
     * 根据文件名称查询文件信息.
     * @param fileId 文件id
     * @param fileNames 文件名称
     * @return 文件信息集合
     */
    List<DpPortalFileManager> selectByFileNames(@Param("fileId") String fileId, @Param("fileNames") String[] fileNames);

    /**
     * 根据文件id集合查询文件名称集合.
     * @param strings 文件id集合
     * @return 文件名称集合.
     */
    String[] selectFileNamesByFileIds(@Param("ids") String[] strings);

    /**
     * 查询文件夹.
     *
     * @param userId 用户id
     * @return 返回我的文件信息
     */
    List<FolderInfoDTO> queryFolder(@Param("userId") String userId);

    /**
     * 跟据条件查询文件信息.
     *
     * @param queryVO 查询条件
     * @return 返回文件信息
     */
    List<FileInfoDTO> queryFileByParam(FileQueryVO queryVO);

    /**
     * 根据条件查询文件夹信息.
     *
     * @param queryVO 查询条件
     * @return 根据条件查询文件信息
     */
    List<FolderInfoDTO> queryFolderByParam(FolderQueryVO queryVO);

    /**
     * 获取所有文件夹.
     *
     * @return List
     */
    List<DpPortalFileManager> getAllFileNotDir();

    /**
     * 刷新老的数据，上传文件时候没有将最后修改时间设置为上传时间.
     *
     * @return int
     */
    int refreshOldData();

    /**
     * 刷新新字段的值.
     *
     * @return int
     */
    int refreshUserName();


    /**
     * 通过文件Id集合查询文件信息.
     *
     * @param fileIdList 文件Id集合
     * @return List
     */
    List<DpPortalFileManager> selectByFileIdList(@Param("fileIdList") String[] fileIdList);

    /**
     * 通过文件ID删除出文件信息.
     *
     * @param fileIds 文件Ids
     * @return int
     */
    int deleteFileByIds(@Param("fileIds") List<String> fileIds);
}
