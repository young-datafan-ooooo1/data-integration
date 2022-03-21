package com.youngdatafan.portal.model.management.businessmodel.service;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.businessmodel.dto.*;
import com.youngdatafan.portal.model.management.businessmodel.vo.AddBusinessModelVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.BusinessPreviewDataVO;
import com.youngdatafan.portal.model.management.businessmodel.vo.UpdateBusinessModelVO;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
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
public interface BusinessModelService {

    /**
     * 查询授权给用户的所有模型
     *
     * @param userId
     * @return
     */
    List<ModelTypeAndGroupListDTO> selectModelAndGroupList(String userId);

    /**
     * 根据模型id查询模型信息
     *
     * @param modelName
     * @return
     */
    BusinessModelDTO selectModelByModelName(String userId, String modelName);

    /**
     * 查询数据源
     *
     * @param userId
     * @param businessModelName
     * @return
     */
    Result<ModelDatasourceDTO, Object> selectDatasource(String userId, String businessModelName);


    /**
     * 查询业务模型元数据
     *
     * @param userId
     * @param businessModelName
     * @return
     */
    Result<List<BusinessModelMetaDataDTO>, Object> selectBusinessModelMetaData(String userId, String businessModelName);


    Result<ModelDatasourceDTO, Object> selectDatasourceByModelName(String userId, String businessModelName, String modeType);

    /**
     * 业务模型新增
     *
     * @param addBusinessModelVO
     * @return
     */
    Result insert(String userId, AddBusinessModelVO addBusinessModelVO);


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
     * @param updateBusinessModelVO
     * @return
     */
    Result<Boolean, Object> update(String userId, UpdateBusinessModelVO updateBusinessModelVO);


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
    PageInfo<BusinessModelDTO> selectAll(String userId, String modelName, String groupName, String basicModelName, Integer curPage, Integer pageSize);


    /**
     * 查询基础模型组
     *
     * @param userId
     * @return
     */
    List<BasicModelGroupDTO> getBasicModelGroup(String userId);

    /**
     * 查询基础模型
     *
     * @param userId
     * @param groupName
     * @return
     */
    List<BasicModelNameAndCnameDTO> getBasicModel(String userId, String groupName);

    /**
     * 获取基础模型字段
     *
     * @param userId
     * @param basicModel
     * @return
     */
    List<BasicModelAndMetaDataDTO> getBasicModelColumns(String userId, String basicModel);


    /**
     * 获取所有分组
     *
     * @param userId
     * @return
     */
    List<GroupDTO> getAllGroup(String userId);

    /**
     * 批量下载模型
     *
     * @param userId
     * @param modelNames
     * @return
     */

    List<BatchDownloadBusinessModelDTO> batchDownloadExcel(String userId, List<String> modelNames);


    Result<Boolean, Object> queryBusinessModelExists(String userId, String businessModelName, String businessModelGroup);

    ModelDTO selectModelAndMetaData(String userId, String modelName);

    Result<List<Map<String, Object>>, Object> testPriview(String userId, BusinessPreviewDataVO businessPreviewDataVO);
}
