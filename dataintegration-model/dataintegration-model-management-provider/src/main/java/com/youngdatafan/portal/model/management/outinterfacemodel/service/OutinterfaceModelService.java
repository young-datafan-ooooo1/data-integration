package com.youngdatafan.portal.model.management.outinterfacemodel.service;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.outinterfacemodel.dto.*;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.AddOutinterfaceModelVO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.OutinterfacePreviewDataVO;
import com.youngdatafan.portal.model.management.outinterfacemodel.vo.UpdateOutinterfaceModelVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/19 10:58 AM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface OutinterfaceModelService {

    /**
     * 查询授权给用户的所有模型
     *
     * @param userId
     * @return
     */
    List<OutinterfaceModelTypeAndGroupListDTO> selectModelAndGroupList(String userId);

    /**
     * 根据模型id查询模型信息
     *
     * @param modelName
     * @return
     */
    OutinterfaceModelDTO selectModelByModelName(String userId, String modelName);

    /**
     * 查询数据源
     *
     * @param userId
     * @param outinterfaceModelName
     * @return
     */
    Result<OutinterfaceModelDatasourceDTO, Object> selectDatasource(String userId, String outinterfaceModelName);


    /**
     * 查询业务模型元数据
     *
     * @param userId
     * @param outinterfaceModelName
     * @return
     */
    Result<List<OutinterfaceModelMetaDataDTO>, Object> selectOutinterfaceModelMetaData(String userId, String outinterfaceModelName);


    Result<OutinterfaceModelDatasourceDTO, Object> selectDatasourceByModelName(String userId, String outinterfaceModelName, String modeType);

    /**
     * 业务模型新增
     *
     * @param addOutinterfaceModelVO
     * @return
     */
    Result insert(String userId, AddOutinterfaceModelVO addOutinterfaceModelVO);


    /**
     * 根据模型名称删除业务模型
     *
     * @param modelName
     * @return
     */
    Result<Boolean, Object> delete(String userId, String modelName);


    /**
     * 业务模型更新
     *
     * @param updateOutinterfaceModelVO
     * @return
     */
    Result<Boolean, Object> update(String userId, UpdateOutinterfaceModelVO updateOutinterfaceModelVO);


    /**
     * 查询所有业务模型
     *
     * @param userId         用户id
     * @param groupName      分组名称
     * @param modelName      模型名称
     * @param basicModelName 数据源名称
     * @param curPage        页码
     * @param pageSize       行数
     * @return
     */
    PageInfo<OutinterfaceModelDTO> selectAll(String userId, String modelName, String groupName, String basicModelName, Integer curPage, Integer pageSize);


    /**
     * 查询基础模型组
     *
     * @param userId
     * @return
     */
    List<OutinterfaceModelGroupDTO> getBasicModelGroup(String userId);

    /**
     * 查询基础模型
     *
     * @param userId
     * @param groupName
     * @return
     */
    List<OutinterfaceModelNameAndCnameDTO> getBasicModel(String userId, String groupName);

    /**
     * 获取基础模型字段
     *
     * @param userId
     * @param basicModel
     * @return
     */
    List<OutinterfaceBasicModelAndMetaDataDTO> getBasicModelColumns(String userId, String basicModel);


    /**
     * 获取所有分组
     *
     * @param userId
     * @return
     */
    List<OutinterfaceGroupDTO> getAllGroup(String userId);

    /**
     * 批量下载模型
     *
     * @param userId
     * @param modelNames
     * @return
     */

    List<BatchDownloadOutinterfaceModelDTO> batchDownloadExcel(String userId, List<String> modelNames);


    Result<Boolean, Object> queryOutinterfaceModelExists(String userId, String outinterfaceModelName, String outinterfaceModelGroup);

    ModelDTO selectModelAndMetaData(String userId, String modelName);

    Result<List<Map<String, Object>>, Object> testPriview(String userId, OutinterfacePreviewDataVO outinterfacePreviewDataVO);
}
