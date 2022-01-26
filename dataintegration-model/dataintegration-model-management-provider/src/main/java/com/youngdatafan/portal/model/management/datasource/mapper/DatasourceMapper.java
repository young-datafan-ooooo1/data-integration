package com.youngdatafan.portal.model.management.datasource.mapper;

import com.youngdatafan.portal.model.management.config.DefaultDatasource;
import com.youngdatafan.portal.model.management.datasource.dto.DataSourceListDTO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.datasource.entity.Datasource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DatasourceMapper {
    int deleteByPrimaryKey(String dsName);


    Datasource selectOneByUserIdAndDsName(@Param("userId") String userId,
                                          @Param("dsId") String datasourceId);

    int insert(Datasource record);

    int insertSelective(Datasource record);

    Datasource selectByPrimaryKey(String dsName);

    int updateByPrimaryKeySelective(Datasource record);

    int updateByPrimaryKey(Datasource record);

    /**
     * 获取此用户创建的数据源
     *
     * @param userId
     * @return
     */
    List<DataSourceListDTO> selectAllByUserId(@Param("userId") String userId, @Param("datasourceName") String datasourceName, @Param("datasourceType") String datasourceType);

    List<DatasourceDTO> selectAll(@Param("userId") String userId, @Param("sourcePlat") String sourcePlat);

    List<DatasourceDTO> selectAllByPlat(@Param("userId") String userId, @Param("sourcePlatform") String sourcePlatform);

    DatasourceDTO getDatasourceByIdAndUserId(@Param("userId") String userId, @Param("datasourceName") String datasourceName);

    DatasourceDTO getDatasourceById(@Param("datasourceName") String datasourceName);

    DatasourceDTO getDatasourceByNameAndUserId(@Param("datasourceName") String datasourceName);

    DatasourceDTO getDatasourceByNameAndSourcePlat(@Param("source") String source, @Param("datasourceName") String datasourceName);

    List<String> getEngineDatasourceIds(@Param("source") String source);

    int insertDefaultDataSource(DefaultDatasource defaultDatasource);

    Datasource selectDataSourceByBusinessName(@Param("businessModelName") String businessModelName);

}
