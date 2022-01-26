package com.youngdatafan.portal.model.management.datasource.service.impl;

import com.youngdatafan.portal.model.management.datasource.dto.AllUserAndGrantUser;
import com.youngdatafan.portal.model.management.datasource.dto.UserListDTO;
import com.youngdatafan.portal.model.management.datasource.entity.DpPortalUserDatasource;
import com.youngdatafan.portal.model.management.datasource.mapper.DpPortalUserDatasourceDao;
import com.youngdatafan.portal.model.management.datasource.service.DpPortalUserDatasourceService;
import com.youngdatafan.portal.model.management.datasource.vo.GrantDatasourceVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (DpPortalUserDatasource)表服务实现类
 *
 * @author makejava
 * @since 2020-11-30 18:25:09
 */
@Service("dpPortalUserDatasourceService")
public class DpPortalUserDatasourceServiceImpl implements DpPortalUserDatasourceService {
    @Resource
    private DpPortalUserDatasourceDao dpPortalUserDatasourceDao;

    /**
     * 通过ID查询单条数据
     *
     * @param 主键
     * @return 实例对象
     */
    @Override
    public DpPortalUserDatasource queryById() {
        return this.dpPortalUserDatasourceDao.queryById();
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<DpPortalUserDatasource> queryAllByLimit(int offset, int limit) {
        return this.dpPortalUserDatasourceDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param dpPortalUserDatasource 实例对象
     * @return 实例对象
     */
    @Override
    public DpPortalUserDatasource insert(DpPortalUserDatasource dpPortalUserDatasource) {
        this.dpPortalUserDatasourceDao.insert(dpPortalUserDatasource);
        return dpPortalUserDatasource;
    }

    /**
     * 修改数据
     *
     * @param dpPortalUserDatasource 实例对象
     * @return 实例对象
     */
    @Override
    public DpPortalUserDatasource update(DpPortalUserDatasource dpPortalUserDatasource) {
        this.dpPortalUserDatasourceDao.update(dpPortalUserDatasource);
        return this.queryById();
    }

    /**
     * 通过主键删除数据
     *
     * @param 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById() {
        return this.dpPortalUserDatasourceDao.deleteById() > 0;
    }

    @Override
    public Boolean grantDatasource(String userId, GrantDatasourceVo grantDatasourceVo) {
        String dsId = grantDatasourceVo.getDsId();

        String[] userIds = grantDatasourceVo.getUserId();

        List<DpPortalUserDatasource> list = new ArrayList<>();

        for (int i = 0, size = userIds.length; i < size; i++) {
            DpPortalUserDatasource dpPortalUserDatasource = new DpPortalUserDatasource();
            dpPortalUserDatasource.setDsId(dsId);

            dpPortalUserDatasource.setUserId(userIds[i]);

            list.add(dpPortalUserDatasource);
        }
        int i = dpPortalUserDatasourceDao.deleteByDsId(dsId);

        if (i < 0) {
            return false;
        }

        int i1 = dpPortalUserDatasourceDao.batchInsert(list);

        if (i1 < 0) {
            throw new RuntimeException("授权失败");
        }
        return true;
    }

    @Override
    public AllUserAndGrantUser getUsersByDsId(String userId, String dsId) {

        List<UserListDTO> allUser = dpPortalUserDatasourceDao.getAllUser();

        List<UserListDTO> grantlUser = dpPortalUserDatasourceDao.getUsersByDsId(dsId);

        AllUserAndGrantUser allUserAndGrantUser = new AllUserAndGrantUser(allUser, grantlUser);

        return allUserAndGrantUser;
    }
}
