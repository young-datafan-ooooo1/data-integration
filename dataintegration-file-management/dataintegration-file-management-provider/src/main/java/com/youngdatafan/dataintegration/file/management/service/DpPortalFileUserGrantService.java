package com.youngdatafan.dataintegration.file.management.service;

import com.github.pagehelper.Page;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.dto.UserGrantDTO;
import com.youngdatafan.dataintegration.file.management.vo.DpPortalFileUserGrantQueryVO;
import com.youngdatafan.dataintegration.file.management.vo.DpPortalFileUserGrantAddVO;
import com.youngdatafan.dataintegration.file.management.vo.DpPortalFileUserGrantUptVO;
import com.youngdatafan.dataintegration.file.management.dto.DpPortalFileUserGrantDTO;
import com.youngdatafan.dataintegration.file.management.vo.DpProtalFileUserGrantBatchAddVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantDeptVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantFolderUserVO;
import com.youngdatafan.dataintegration.file.management.vo.UserGrantQueryVO;
import java.util.List;
import java.util.Set;
import com.github.pagehelper.PageInfo;

/**
 * 文件管理表.
 *
 * @author lizihao
 * @date 2021-12-27 14:53:37
 */
public interface DpPortalFileUserGrantService {

    /**
     * 新增.
     *
     * @param userId    用户id
     * @param addVO 保存参数
     * @return 返回数据
     */
    int insert(String userId, DpPortalFileUserGrantAddVO addVO);

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
     * @param userId    用户id
     * @param updateVO 修改参数
     * @return 返回数据
     */
    int update(String userId, DpPortalFileUserGrantUptVO updateVO);

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
     * @param page 分页排序实体
     * @return 返回集合数据
     */
    PageInfo<DpPortalFileUserGrantDTO> queryPageByWrapper(DpPortalFileUserGrantQueryVO queryVo, Page page);

    /**
     * 根据用户id删除用户授权.
     * @param userId 用户id
     * @return 删除成功
     */
    int deleteByUserId(Integer userId);

    /**
     * 批量新增授权列表.
     * @param batchAddVO 批量授权
     * @param createUser 创建人
     * @return 返回数据
     */
    int batchInsert(DpProtalFileUserGrantBatchAddVO batchAddVO, String createUser);

    /**
     * 查询用户授权信息.
     * @param queryVO 查询参数
     * @param page 分页参数
     * @return 用户授权信息
     */
    PageInfo<UserGrantDTO> queryUserGrant(UserGrantQueryVO queryVO, Page page);

    /**
     * 根据用户id查询用户授权详情.
     * @param userId 用户id
     * @return 授权详情
     */
    DpPortalFileUserGrantUptVO getUserGrant(Integer userId);

    /**
     * 查询授权的文件夹.
     * @param userId 用户id
     * @return 返回授权的文件夹
     */
    List<FolderInfoDTO> queryGrantFolder(String userId);

    /**
     * 查询授权的文件夹信息.
     * @param userId 用户id
     * @return 返回授权的文件夹
     */
    List<GrantDeptVO> queryGrantDeptFolder(String userId);

    /**
     * 根据文件夹id查询文件夹授权的用户信息.
     * @param folderId 文件夹id
     * @return 返回用户信息
     */
    GrantFolderUserVO queryGrantUser(String folderId);
}
