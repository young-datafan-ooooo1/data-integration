package com.youngdatafan.portal.model.management.basicmodel.mapper;

import com.youngdatafan.portal.model.management.basicmodel.dto.ModelNameAndModelFilterDTO;
import com.youngdatafan.portal.model.management.basicmodel.entity.DpPortalModelFilter;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模型过滤信息(DpPortalModelFilter)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-06 16:06:16
 */
public interface DpPortalModelFilterMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param 主键
     * @return 实例对象
     */
    List<DpPortalModelFilter> queryById(@Param("modelId") String modelId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DpPortalModelFilter> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    List<ModelFilterVO> queryByModelName(@Param("modelId") String modelId);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param dpPortalModelFilter 实例对象
     * @return 对象列表
     */
    List<DpPortalModelFilter> queryAll(DpPortalModelFilter dpPortalModelFilter);

    /**
     * 新增数据
     *
     * @param dpPortalModelFilter 实例对象
     * @return 影响行数
     */
    int insert(DpPortalModelFilter dpPortalModelFilter);

    /**
     * 修改数据
     *
     * @param dpPortalModelFilter 实例对象
     * @return 影响行数
     */
    int update(DpPortalModelFilter dpPortalModelFilter);

    /**
     * 通过主键删除数据
     *
     * @param 主键
     * @return 影响行数
     */
    int deleteById(@Param("modelName") String modelName);

    int insertModelFilter(@Param("list") List<ModelFilterVO> modelFilterVOS);


    List<ModelNameAndModelFilterDTO> selectMdelFilters(@Param("list") List<String> list);
}