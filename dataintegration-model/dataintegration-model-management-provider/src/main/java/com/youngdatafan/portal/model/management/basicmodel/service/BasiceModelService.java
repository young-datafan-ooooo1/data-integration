package com.youngdatafan.portal.model.management.basicmodel.service;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.basicmodel.dto.*;
import com.youngdatafan.portal.model.management.basicmodel.vo.AddBasicModelVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.BatchInserModelVO;
import com.youngdatafan.portal.model.management.basicmodel.vo.UpdateBasicModelVO;
import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/12 10:43 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface BasiceModelService {
    /**
     * 基础模型上传
     *
     * @param addBasicModelVO
     * @return
     */
    Result insert(String userId, AddBasicModelVO addBasicModelVO);


    /**
     * 根据模型名称删除模型
     *
     * @param modelName
     * @return
     */
    Result delete(String userId, String modelName);


    /**
     * 基础模型更新
     *
     * @param updateBasicModelVO
     * @return
     */
    Result update(String userId, UpdateBasicModelVO updateBasicModelVO);


    /**
     * 查询所有模型
     *
     * @param userId     用户id
     * @param groupName  分组名称
     * @param modelName  模型名称
     * @param datasource 数据源名称
     * @param curPage    页码
     * @param pageSize   行数
     * @return
     */
    PageInfo<BasicModelDTO> selectAll(String userId, String modelName, String groupName, String datasource, Integer curPage, Integer pageSize, String modelType);

    /**
     * 查询所有数据源
     *
     * @param userId
     * @param datasourceName
     * @return
     */
    List<BasicModelDatasourceDTO> getAllDatasources(String userId);

    /**
     * 获取选择数据库下所有的表
     *
     * @param datasourceName
     * @param userId
     * @return
     */
    Result getAllTables(String datasourceName, String userId, String schema);

    /**
     * 查询schema下的表和视图
     *
     * @param userId
     * @param datasourceName
     * @param schema
     * @return
     */
    Result<TablesAndViewDTO, Object> getAllTablesAndViews(String userId, String datasourceName, String schema);

    /**
     * 获取选择数据库下所有schema
     *
     * @param datasourceName
     * @param userId
     * @return
     */
    Result getAllSchema(String datasourceName, String userId);


    /**
     * 获取此表的所有字段
     *
     * @param tables
     * @param datasourceName
     * @return
     */
    Result<List<AllColumnDTO>, Object> getAllColumns(String userId, String tables, String datasourceName, String schema);

    /**
     * 获取表的查询sql
     *
     * @param userId
     * @param datasourceName
     * @param table
     * @param schema
     * @return
     */
    Result<String, Object> getTableQuerySql(String userId, String datasourceName, String table, String schema);

    List<GroupDTO> getAllGroup(String userId, String groupType);

    List<BasicModelAndMetaDataDTO> getBasicModelAndMetaData(String userId, List<String> list);

    Result batchInsertModel(String userId, BatchInserModelVO batchInserModelVO);

    Result<Boolean, Object> queryBasiceModelExists(String userId, String basicModelName, String basicModelGroup);

    Result<CustomModelDTO, Object> selectCustomModel(String userId, String modelName);

    ModelDTO selectModelsByModelName(String userId, String modelName, String modelType);

    Result<List<Map<String, Object>>, Object> testPreview(String userId, String datasourceId,
                                                          List<BasicModelMetaDataVO> basicModelMetaDataVOS,
                                                          List<ModelFilterVO> modelFilterVOS, String tableName);


    List<AllColumnDTO> getAllColumnsBySql(String userId, String datasourceName, String sql);
}
