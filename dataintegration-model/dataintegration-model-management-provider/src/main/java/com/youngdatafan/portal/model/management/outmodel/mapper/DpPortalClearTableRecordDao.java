package com.youngdatafan.portal.model.management.outmodel.mapper;

import com.youngdatafan.portal.model.management.outmodel.entity.DpPortalClearTableRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 清空临时表记录表(DpPortalClearTableRecord)表数据库访问层
 *
 * @author makejava
 * @since 2020-08-03 15:05:47
 */
public interface DpPortalClearTableRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param modelId 主键
     * @return 实例对象
     */
    DpPortalClearTableRecord queryById(String modelId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DpPortalClearTableRecord> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param dpPortalClearTableRecord 实例对象
     * @return 对象列表
     */
    List<DpPortalClearTableRecord> queryAll(DpPortalClearTableRecord dpPortalClearTableRecord);

    /**
     * 新增数据
     *
     * @param dpPortalClearTableRecord 实例对象
     * @return 影响行数
     */
    int insert(DpPortalClearTableRecord dpPortalClearTableRecord);

    int batchInsert(List<DpPortalClearTableRecord> dpPortalClearTableRecord);

    /**
     * 修改数据
     *
     * @param dpPortalClearTableRecord 实例对象
     * @return 影响行数
     */
    int update(DpPortalClearTableRecord dpPortalClearTableRecord);

    /**
     * 通过主键删除数据
     *
     * @param modelId 主键
     * @return 影响行数
     */
    int deleteById(String modelId);

}