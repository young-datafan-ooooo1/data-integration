package com.youngdatafan.portal.model.management.modelgrant.service;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelGrantGroupDTO;
import com.youngdatafan.portal.model.management.modelgrant.dto.ModelNameAndGroupNameAndTypes;
import com.youngdatafan.portal.model.management.modelgrant.vo.AddModelGroupVO;
import com.youngdatafan.portal.model.management.modelgrant.vo.UpdateModelGroupVO;
import com.github.pagehelper.PageInfo;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : 模型授权组接口</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/12 11:06 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface ModelGroupService {

    /**
     * 根据模型授权组名称删除模型授权组
     *
     * @param modelGrantGroupName
     * @return
     */
    boolean delete(String userId, String modelGrantGroupName);


    /**
     * 查询所有模型授权组（分页+筛选）
     *
     * @param userId              用户id
     * @param curPage             页码
     * @param pageSize            行数
     * @param modelGrantGroupName 模型授权组名称
     * @return
     */
    PageInfo<ModelGrantGroupDTO> selectAll(String userId, Integer curPage, Integer pageSize, String modelGrantGroupName);


    /**
     * 更新模型授权组
     *
     * @param userId
     * @param updateModelGroupVO
     * @return
     */
    Result update(String userId, UpdateModelGroupVO updateModelGroupVO);


    /**
     * 新增模型授权组
     *
     * @param addModelGroupVO
     * @param userId
     * @return
     */
    boolean insert(String userId, AddModelGroupVO addModelGroupVO);

    /**
     * 查询所有的业务模型
     *
     * @param userId
     * @param modelName
     * @param modelGroupNam
     * @param modelGroupType
     * @return
     */
    ModelNameAndGroupNameAndTypes selectAllBusinessodel(String userId, String modelName, String modelGroupNam, String modelGroupType,Integer curPage,
                                                        Integer pageSize);


    /**
     * 查询授权组名称是否重复
     *
     * @param userId
     * @param modelGroupName
     * @return
     */
    Boolean queryModelGroupNameIsExits(String userId, String modelGroupName);
}
