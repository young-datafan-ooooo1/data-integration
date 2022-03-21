package com.youngdatafan.dataintegration.file.management.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.youngdatafan.dataintegration.file.management.dto.DpPortalFileManagerDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileManager;
import com.youngdatafan.dataintegration.file.management.vo.FileInfoVO;
import com.youngdatafan.dataintegration.file.management.vo.FileQueryVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantDeptVO;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

/**
 * 门户网站文件管理服务.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/8 15:41
 */
public interface DpPortalFileManagerService {


    /**
     * 根据主键删除文件.
     *
     * @param fileId 文件id
     * @param userId 用户id
     * @return 主键id
     */
    int deleteByPrimaryKey(String fileId, String userId);

    /**
     * 插入文件信息.
     *
     * @param record 文件信息记录
     * @return 插入之后主键id.
     */
    int insert(DpPortalFileManager record);

    /**
     * 插入记录中含有的相关信息.
     * @param record 信息记录
     * @return 插入之后主键信息id.
     */
    int insertSelective(DpPortalFileManager record);

    /**
     * 文件主键查询文件管理信息.
     * @param fileId 文件id.
     * @return 查询到的系统信息.
     */
    DpPortalFileManager selectByPrimaryKey(String fileId);

    /**
     * 根据主键更新相关信息.
     * @param record 信息记录.
     * @return 更新之后的id.
     */
    int updateByPrimaryKeySelective(DpPortalFileManager record);

    /**
     * 根据主键更新全部信息.
     * @param record 包含全部信息的记录.
     * @return 更新之后信息主键id.
     */
    int updateByPrimaryKey(DpPortalFileManager record);

    /**
     * 根据用户角色, 文件id, 用户id查询文件管理信息.
     * @param roleCode 角色权限编码.
     * @param fileId 文件id.
     * @param userId 用户id.
     * @return 文件管理信息.
     */
    DpPortalFileManager selectByFileId(String roleCode, String fileId, String userId);

    /**
     * 根据文件id查询文件管理信息.
     * @param fileId 文件id
     * @return 文件管理信息.
     */
    DpPortalFileManager selectByFileId(String fileId);

    /**
     * 根据相关参数与分页信息查询文件管理信息.
     * @param params 相关参数
     * @param page 分页信息
     * @return 分页后的文件管理信息
     */
    PageInfo<DpPortalFileManagerDTO> selectByParamPage(Map<String, Object> params, Page page);

    /**
     * 根据多个id查询文件管理信息集合.
     * @param roleCode 角色权限编码
     * @param fileIds 文件id集合
     * @param userId 用户id
     * @return 文件管理信息集合.
     */
    List<DpPortalFileManager> selectByFileIds(String roleCode, String[] fileIds, String userId);


    /**
     * 通过文件id查询文件信息.
     *
     * @param fileIdList 文件id集合
     * @return 文件信息集合
     */
    List<DpPortalFileManager> selectByFileIds(String[] fileIdList);

    /**
     * 根据文件id集合批量删除文件.
     * @param fileIds 文件id集合.
     * @param userId 用户id.
     * @param roleCode 角色权限编码
     * @return 删除文件之后最后一个id
     */
    int deleteByFileIds(String[] fileIds, String userId, String roleCode);

    /**
     * 检查文件名称.
     * @param fileName 文件名.
     * @param userId 用户id.
     * @param pid 文件管理信息pid
     * @return 是否重名.
     */
    String checkeFileName(String fileName, String userId, String pid);

    /**
     * 检查文件夹名称.
     * @param fileName 文件名称
     * @param userId 用户id.
     * @return 是否重名的信息.
     */
    String checkeDirName(String fileName, String userId);

    /**
     * 根据文件类型查询文件集合.
     * @param roleCode 角色权限编码
     * @param fileTypes 文件类型集合.
     * @param userId 用户id
     * @param pid 文件信息管理的pid
     * @return 文件管理信息的集合
     */
    List<DpPortalFileManager> selectFileListByType(String roleCode, String[] fileTypes, String userId, String pid);

    /**
     * 文件基础查询.
     * @param roleCode 用户角色编码
     * @param userId 用户id
     * @param pid 文件挂你信息pid
     * @return 文件管理信息集合.
     */
    List<DpPortalFileManager> selectFileList(String roleCode, String userId, String pid);

    /**
     * 文件夹基础查询.
     * @param roleCode 用户角色编码
     * @param userId 用户id
     * @param isFolder 是否是文件夹
     * @return 文件夹集合
     */
    List<DpPortalFileManager> selectFileFolderList(String roleCode, String userId, String isFolder);

    /**
     * 根据主键删除子文件.
     * @param fileId 文件id.
     * @return 删除文件夹之后的id.
     */
    int deleteSonByPrimaryKey(String fileId);

    /**
     * 删除某一文件夹下的子文件.
     * @param fileId 文件id.
     * @param fileName 文件名称.
     * @return 删除之后文件id
     */
    int deleteSonFromFolder(String fileId, String fileName);

    /**
     * 根据文件pid查询文件列表.
     * @param fileId 文件id.
     * @return 文件信息集合.
     */
    List<DpPortalFileManager> selectByPid(String fileId);

    /**
     * 批量更新.
     * @param dpPortalFileManagers 文件管理信息对象集合.
     */
    void batchUpdate(List<DpPortalFileManager> dpPortalFileManagers);

    /**
     * 发现需求任务.
     * @param taskId 任务id
     * @return 任务名称.
     */
    String findDemandTaskName(Integer taskId);

    /**
     * 根据文件名称查询文件管理信息.
     * @param fileId 文件id.
     * @param strings 文件名称元组
     * @return 文件信息集合
     */
    List<DpPortalFileManager> selectByFileNames(String fileId, String[] strings);

    /**
     * 根据文件id集合查询文件名称.
     * @param strings 文件id集合.
     * @return 文件名称集合.
     */
    String[] selectFileNamesByFileIds(String[] strings);

    /**
     * 批量更新和增加文件信息.
     * @param metaUpdateLists 需要更新的文件信息集合
     * @param metaInsterLists 需要增加的文件信息集合
     */
    void batchUpdateAndInsert(List<DpPortalFileManager> metaUpdateLists, List<DpPortalFileManager> metaInsterLists);

    /**
     * 查询我的文件夹.
     *
     * @param userId 用户id
     * @return 返回我的文件信息
     */
    List<FolderInfoDTO> queryFolder(String userId);

    /**
     * 跟据条件查询文件信息.
     *
     * @param queryVO 查询条件
     * @param page    分页信息
     * @return 返回文件信息
     */
    PageInfo<FileInfoVO> queryFileByParam(FileQueryVO queryVO, Pageable page);

    /**
     * 根据文件夹id查询文件夹信息.
     *
     * @param folderId 文件夹id
     * @return 根据条件查询文件信息
     */
    FolderInfoDTO queryOneFolder(String folderId);

    /**
     * 查询管理员文件夹信息.
     *
     * @return 返回管理员的文件夹信息
     */
    List<GrantDeptVO> queryAdminFolder();

    /**
     * 刷新单个文件夹.
     *
     * @param folderId 文件夹id
     */
    void refreshOneFolder(String folderId);


    /**
     * 清理文件：查文件是否存在，不存在则删除元数据.
     *
     * @param dpPortalFileManagers 检查的文件信息.
     */
    void clearFiles(List<DpPortalFileManager> dpPortalFileManagers);
}
