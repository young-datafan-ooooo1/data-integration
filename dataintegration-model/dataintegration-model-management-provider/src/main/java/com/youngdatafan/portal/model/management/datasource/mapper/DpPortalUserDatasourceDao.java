package com.youngdatafan.portal.model.management.datasource.mapper;

import com.youngdatafan.portal.model.management.datasource.dto.UserListDTO;
import com.youngdatafan.portal.model.management.datasource.entity.DpPortalUserDatasource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (DpPortalUserDatasource)表数据库访问层
 *
 * @author makejava
 * @since 2020-11-30 18:25:06
 */
public interface DpPortalUserDatasourceDao {

    /**
     * 通过ID查询单条数据
     *
     * @param 主键
     * @return 实例对象
     */
    DpPortalUserDatasource queryById();

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DpPortalUserDatasource> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param dpPortalUserDatasource 实例对象
     * @return 对象列表
     */
    List<DpPortalUserDatasource> queryAll(DpPortalUserDatasource dpPortalUserDatasource);

    /**
     * 新增数据
     *
     * @param dpPortalUserDatasource 实例对象
     * @return 影响行数
     */
    int insert(DpPortalUserDatasource dpPortalUserDatasource);

    List<UserListDTO> getUsersByDsId(@Param("dsId") String dsId);

    List<UserListDTO> getAllUser();

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<DpPortalUserDatasource> list);

    /**
     * 根据数据源id删除
     *
     * @param dsId
     * @return
     */
    int deleteByDsId(@Param("dsId") String dsId);

    /**
     * 修改数据
     *
     * @param dpPortalUserDatasource 实例对象
     * @return 影响行数
     */
    int update(DpPortalUserDatasource dpPortalUserDatasource);

    /**
     * 通过主键删除数据
     *
     * @param 主键
     * @return 影响行数
     */
    int deleteById();


}
