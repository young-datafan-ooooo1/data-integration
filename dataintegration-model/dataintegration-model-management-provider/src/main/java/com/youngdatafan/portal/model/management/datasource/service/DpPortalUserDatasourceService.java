package com.youngdatafan.portal.model.management.datasource.service;

import com.youngdatafan.portal.model.management.datasource.dto.AllUserAndGrantUser;
import com.youngdatafan.portal.model.management.datasource.entity.DpPortalUserDatasource;
import com.youngdatafan.portal.model.management.datasource.vo.GrantDatasourceVo;

import java.util.List;

/**
 * (DpPortalUserDatasource)表服务接口
 *
 * @author makejava
 * @since 2020-11-30 18:25:09
 */
public interface DpPortalUserDatasourceService {

    /**
     * 通过ID查询单条数据
     *
     * @param 主键
     * @return 实例对象
     */
    DpPortalUserDatasource queryById();

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DpPortalUserDatasource> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param dpPortalUserDatasource 实例对象
     * @return 实例对象
     */
    DpPortalUserDatasource insert(DpPortalUserDatasource dpPortalUserDatasource);

    /**
     * 修改数据
     *
     * @param dpPortalUserDatasource 实例对象
     * @return 实例对象
     */
    DpPortalUserDatasource update(DpPortalUserDatasource dpPortalUserDatasource);

    /**
     * 通过主键删除数据
     *
     * @param 主键
     * @return 是否成功
     */
    boolean deleteById();

    /**
     * 授权数据源
     *
     * @param userId
     * @param grantDatasourceVo
     * @return
     */
    Boolean grantDatasource(String userId, GrantDatasourceVo grantDatasourceVo);


    AllUserAndGrantUser getUsersByDsId(String userId, String dsId);

}
