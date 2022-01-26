package com.youngdatafan.portal.model.management.outinterfacemodel.mapper;

import com.youngdatafan.portal.model.management.outinterfacemodel.entity.DpPortalOutinterfaceModelQuerysql;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务模型关联sql表(DpPortalBusinessModelQuerysql)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-15 11:22:11
 */
public interface DpPortalOutinterfaceModelQuerysqlDao {

    /**
     * 通过ID查询单条数据
     *
     * @param businessModelId 主键
     * @return 实例对象
     */
    DpPortalOutinterfaceModelQuerysql queryById(String businessModelId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DpPortalOutinterfaceModelQuerysql> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param dpPortalBusinessModelQuerysql 实例对象
     * @return 对象列表
     */
    List<DpPortalOutinterfaceModelQuerysql> queryAll(DpPortalOutinterfaceModelQuerysql dpPortalBusinessModelQuerysql);

    /**
     * 新增数据
     *
     * @param dpPortalBusinessModelQuerysql 实例对象
     * @return 影响行数
     */
    int insert(DpPortalOutinterfaceModelQuerysql dpPortalBusinessModelQuerysql);

    /**
     * 修改数据
     *
     * @param dpPortalBusinessModelQuerysql 实例对象
     * @return 影响行数
     */
    int update(DpPortalOutinterfaceModelQuerysql dpPortalBusinessModelQuerysql);


    int replaceIntoItem(DpPortalOutinterfaceModelQuerysql dpPortalBusinessModelQuerysql);

    /**
     * 通过主键删除数据
     *
     * @param businessModelId 主键
     * @return 影响行数
     */
    int deleteById(String businessModelId);

}
