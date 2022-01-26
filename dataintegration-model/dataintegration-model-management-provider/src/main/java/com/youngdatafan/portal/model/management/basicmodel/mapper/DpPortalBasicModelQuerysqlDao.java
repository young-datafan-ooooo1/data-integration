package com.youngdatafan.portal.model.management.basicmodel.mapper;

import com.youngdatafan.portal.model.management.basicmodel.entity.DpPortalBasicModelQuerysql;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基础模型关联sql表(DpPortalBasicModelQuerysql)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-15 11:20:54
 */
public interface DpPortalBasicModelQuerysqlDao {

    /**
     * 通过ID查询单条数据
     *
     * @param basicModelId 主键
     * @return 实例对象
     */
    DpPortalBasicModelQuerysql queryById(String basicModelId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DpPortalBasicModelQuerysql> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param dpPortalBasicModelQuerysql 实例对象
     * @return 对象列表
     */
    List<DpPortalBasicModelQuerysql> queryAll(DpPortalBasicModelQuerysql dpPortalBasicModelQuerysql);

    /**
     * 新增数据
     *
     * @param dpPortalBasicModelQuerysql 实例对象
     * @return 影响行数
     */
    int insert(DpPortalBasicModelQuerysql dpPortalBasicModelQuerysql);

    /**
     * 修改数据
     *
     * @param dpPortalBasicModelQuerysql 实例对象
     * @return 影响行数
     */
    int update(DpPortalBasicModelQuerysql dpPortalBasicModelQuerysql);

    int replaceInto(DpPortalBasicModelQuerysql dpPortalBasicModelQuerysql);
    /**
     * 通过主键删除数据
     *
     * @param basicModelId 主键
     * @return 影响行数
     */
    int deleteById(String basicModelId);

}