package com.youngdatafan.portal.model.management.datasource.service;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.datasource.dto.*;
import com.youngdatafan.portal.model.management.datasource.entity.Datasource;
import com.youngdatafan.portal.model.management.datasource.vo.DatasourceConnectorVO;
import com.youngdatafan.portal.model.management.datasource.vo.JCDataSourceVO;
import com.github.pagehelper.PageInfo;

import java.sql.Connection;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 4:04 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface DatasourceService {

    /**
     * 新增数据源
     *
     * @param datasource
     * @return
     */
    boolean insert(String userId, Datasource datasource);

    /**
     * 根据数据源名称删除数据源
     *
     * @param datasourceName
     * @return
     */
    boolean delete(String userId,String datasourceName);

    /**
     * 更新数据源
     *
     * @param datasource
     * @return
     */
    boolean update(Datasource datasource);

    /**
     * 获取用户创建的数据源
     *
     * @param userId
     * @param curPage  页数
     * @param pageSize 行数
     * @return
     */
    PageInfo<DataSourceListDTO> selectAllByUserId(String userId, Integer curPage, Integer pageSize, String dataSourceName, String datasourceType);

    /**
     * 获取数据库类型
     *
     * @return
     */
    List<DatasourceTypeDTO> getAllDatasourceTypes();


    /**
     * 根据数据源名称查询数据源
     *
     * @param userId
     * @param datasourceName
     * @return
     */
    DatasourceDTO getDatasourceByDatasourceName(String datasourceName);


    /**
     * @param userId
     * @param datasourceId
     * @return
     */
    DatasourceDTO getDatasourceByDatasourceIdAndUserId(String userId, String datasourceId);


    DatasourceDTO getDatasourceByIdNotUserID(String datasourceId);

    /**
     * 连接数据库
     *
     * @return
     */
    Connection connectorDatasource(DatasourceConnectorVO datasourceConnectorVO);


    /**
     * 获取所有数据源不需要分页+筛选
     *
     * @param userId
     * @return
     */
    Result<List<DatasourceDTO>, Object> getDatasourceNoPageBySourcePlat(String userId,String sourcePlatform);

    void annalysUrl(List<DatasourceParamsDTO> datasourceParamsDTOS, String[] split);
    /**
     * @param datasourceName 数据库
     * @param schema         schema
     * @param userId         userId
     * @return Result
     */
    Result<StatementDto, Object> getStatementDto(String datasourceName, String schema, String userId);

    Result<String, Object> getStatementSql(String datasourceName, String schema, String table, String userId);


    List<DatasourceDTO> getDatasourceNoPage(String userId ,String sourcePlatform);

    String  getUrl(JCDataSourceVO jcDataSourceVO) throws  Exception;
}
