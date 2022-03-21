package com.youngdatafan.dataintegration.file.management.mapper;

import com.youngdatafan.dataintegration.file.management.dto.FileGrantDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderGrantDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.dto.GrantUserDTO;
import com.youngdatafan.dataintegration.file.management.dto.UserGrantDTO;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileUserGrant;
import com.youngdatafan.dataintegration.file.management.vo.DpPortalFileUserGrantQueryVO;
import com.youngdatafan.dataintegration.file.management.dto.DpPortalFileUserGrantDTO;
import com.youngdatafan.dataintegration.file.management.vo.UserGrantQueryVO;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件管理表.
 * 
 * @author lizihao
 * @date 2021-12-27 14:53:37
 */
@Mapper
public interface DpPortalFileUserGrantMapper {

    /**
     * 新增.
     *
     * @param addEntity 保存参数
     * @return 返回数据
     */
    int insert(DpPortalFileUserGrant addEntity);

    /**
     * 批量新增授权.
     * @param list 授权列表
     * @return 返回数据
     */
    int batchInsert(List<DpPortalFileUserGrant> list);

    /**
     * 删除.
     *
     * @param id 主键
     * @return 返回数据
     */
    int delete(Integer id);

    /**
     * 批量删除.
     *
     * @param ids 主键
     * @return 返回数据
     */
    int batchDel(Set<Integer> ids);

    /**
     * 修改.
     *
     * @param uptEntity 修改参数
     * @return 返回数据
     */
    int update(DpPortalFileUserGrant uptEntity);

    /**
     * 根据条件查询.
     *
     * @param queryVo 查询参数
     * @return 返回集合数据
     */
    List<DpPortalFileUserGrantDTO> queryListByWrapper(DpPortalFileUserGrantQueryVO queryVo);

    /**
     * 根据主键查询.
     *
     * @param id 主键
     * @return 返回集合数据
     */
    DpPortalFileUserGrantDTO getDetail(Integer id);

    /**
     * 分页查询.
     *
     * @param queryVo 查询参数
     * @return 返回集合数据
     */
    List<DpPortalFileUserGrantDTO> queryPageByWrapper(DpPortalFileUserGrantQueryVO queryVo);

    /**
     * 根据用户id删除用户授权.
     * @param userId 用户id
     * @return 删除成功
     */
    int deleteByUserId(Integer userId);

    /**
     * 根据文件夹删除用户授权.
     * @param folderId 文件家id
     * @return 删除成功
     */
    int deleteByFolderId(String folderId);

    /**
     * 查询用户授权信息.
     * @param queryVO 查询参数
     * @return 用户授权信息
     */
    List<UserGrantDTO> queryUserGrant(UserGrantQueryVO queryVO);

    /**
     * 根据用户id查询文件夹授权.
     * @param userId 用户id
     * @return 返回文件夹授权
     */
    List<FolderGrantDTO> queryFolderGrantByUserId(Integer userId);

    /**
     * 根据用户id查询文件授权.
     * @param userId 用户id
     * @return 返回文件授权
     */
    List<FileGrantDTO> queryFileGrantByUserId(Integer userId);

    /**
     * 查询授权的文件夹.
     * @param userId 用户id
     * @return 返回授权的文件夹
     */
    List<FolderInfoDTO> queryGrantFolder(String userId);

    /**
     * 根据文件夹id查询文件夹授权的用户信息.
     * @param folderId 文件夹id
     * @return 返回用户信息
     */
    List<GrantUserDTO> queryGrantUser(String folderId);

    /**
     * 查询所有可授权的用户.
     * @return 返回用户信息
     */
    List<GrantUserDTO> queryAllUser();
}
