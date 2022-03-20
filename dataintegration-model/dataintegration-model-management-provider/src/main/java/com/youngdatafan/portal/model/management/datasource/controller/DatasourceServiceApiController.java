package com.youngdatafan.portal.model.management.datasource.controller;

import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.basicmodel.service.BasiceModelService;
import com.youngdatafan.portal.model.management.datasource.api.DatasourceServiceApi;
import com.youngdatafan.portal.model.management.datasource.dto.*;
import com.youngdatafan.portal.model.management.datasource.entity.Datasource;
import com.youngdatafan.portal.model.management.datasource.service.DatasourceService;
import com.youngdatafan.portal.model.management.datasource.service.DpPortalUserDatasourceService;
import com.youngdatafan.portal.model.management.datasource.vo.DatasourceConnectorVO;
import com.youngdatafan.portal.model.management.datasource.vo.DatasourceVO;
import com.youngdatafan.portal.model.management.datasource.vo.GrantDatasourceVo;
import com.youngdatafan.portal.model.management.datasource.vo.JCDataSourceVO;
import com.youngdatafan.portal.model.management.util.jdbc.DatasourceExplainDTO;
import com.youngdatafan.portal.model.management.util.jdbc.JdbcUtils;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 4:25 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@RestController
@RequestMapping("/datasource")
public class DatasourceServiceApiController implements DatasourceServiceApi {

    Logger logger = LoggerFactory.getLogger(DatasourceServiceApiController.class);

    private final DatasourceService datasourceService;
    private final BasiceModelService basiceModelService;
    private final DpPortalUserDatasourceService dpPortalUserDatasourceService;

    /**
     * 管理员用户id
     */
    private final String ADMIN_USER_ID = "00000000";

    @Autowired
    public DatasourceServiceApiController(DatasourceService datasourceService,
                                          BasiceModelService basiceModelService,
                                          DpPortalUserDatasourceService dpPortalUserDatasourceService) {
        this.datasourceService = datasourceService;
        this.basiceModelService = basiceModelService;
        this.dpPortalUserDatasourceService = dpPortalUserDatasourceService;

    }


    @Override
    public Result<Boolean, Object> add(String userId, DatasourceVO datasourceVO) {
        try {
           /* Result<Boolean, Object> stringObjectResult1 = getBooleanObjectResult(datasourceVO);
            if (stringObjectResult1 != null) {
                return stringObjectResult1;
            }*/

            //todo用户id需要添加

            Datasource datasource = new Datasource();

            BeanUtils.copyProperties(datasourceVO, datasource);
            if (datasourceVO.getAdvancedParametersVO() != null) {
                datasource.setDsConectorSetting(JsonUtils.toString(datasourceVO.getAdvancedParametersVO()));
            }
            if (datasourceService.insert(userId, datasource)) {
                return Result.success(true);
            } else {
                return Result.fail(StatusCode.CODE_10010.getCode(), false, "创建数据源失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "创建数据源失败");
        }
    }

    //jdbc:mysql://10.242.10.170:3306/dp_dev?useUnicode=true&characterEncoding=utf-8&useSSL=false
    //jdbc:clickhouse://10.242.10.170:8123/default
    @Override
    public Result<Boolean, Object> JCAdd(String userId, JCDataSourceVO jcDataSourceVO) {

        Datasource datasource = new Datasource();

        BeanUtils.copyProperties(jcDataSourceVO, datasource);
        try {
            String url;
            if (!jcDataSourceVO.getDsType().isEmpty() && jcDataSourceVO.getDsType().equals("GENERIC")) {
                url = jcDataSourceVO.getCustomUrl();
                if (!url.contains("?")) {
                    datasource.setDsUrl(url + "?access=" + jcDataSourceVO.getAccess() + "&type=GENERIC");
                } else {
                    datasource.setDsUrl(url + "&access=" + jcDataSourceVO.getAccess() + "&type=GENERIC");
                }
                datasource.setDsType(jcDataSourceVO.getDatabaseDialectId());
                datasource.setDriverClassName(jcDataSourceVO.getCustomDriverClass());
            } else {
                url = datasourceService.getUrl(jcDataSourceVO);
                datasource.setDsUrl(url + "?access=" + jcDataSourceVO.getAccess());
            }

            if (jcDataSourceVO.getDsConnectorSetting() != null) {
                datasource.setDsConectorSetting(JsonUtils.toString(jcDataSourceVO.getDsConnectorSetting()));
            }


        } catch (Exception e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "数据源参数构建失败");
        }
        if (datasourceService.insert(userId, datasource)) {
            return Result.success(true);
        } else {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "创建数据源失败");
        }
    }

    @Override
    public Result<Boolean, Object> JCUpdate(String userId, JCDataSourceVO jcDataSourceVO) {

        Datasource datasource = new Datasource();

        BeanUtils.copyProperties(jcDataSourceVO, datasource);
        try {
            String url;
            if (!jcDataSourceVO.getDsType().isEmpty() && jcDataSourceVO.getDsType().equals("GENERIC")) {
                url = jcDataSourceVO.getCustomUrl();
                if (!url.contains("?")) {
                    if (!url.contains("access")) {
                        url = url + "?access=" + jcDataSourceVO.getAccess();
                    }
                    if (!url.contains("type=GENERIC")) {
                        url = url + "&type=GENERI";
                    }
                    datasource.setDsUrl(url);
                } else {
                    if (!url.contains("access")) {
                        url = url + "?access=" + jcDataSourceVO.getAccess();
                    }
                    if (!url.contains("type=GENERIC")) {
                        url = url + "&type=GENERI";
                    }
                    datasource.setDsUrl(url);
                }
                datasource.setDsType(jcDataSourceVO.getDatabaseDialectId());
                datasource.setDriverClassName(jcDataSourceVO.getCustomDriverClass());
            } else {
                url = datasourceService.getUrl(jcDataSourceVO);
                datasource.setDsUrl(url + "?access=" + jcDataSourceVO.getAccess());
            }

            if (jcDataSourceVO.getDsConnectorSetting() != null) {
                datasource.setDsConectorSetting(JsonUtils.toString(jcDataSourceVO.getDsConnectorSetting()));
            }

        } catch (Exception e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "数据源参数构建失败");
        }

        return Result.success(datasourceService.update(datasource));
    }

    @Override
    public Result<String[], Object> getProcedure(String userId, String databaseId) {
        DatasourceDTO datasourceDTO = datasourceService.getDatasourceByDatasourceIdAndUserId(userId, databaseId);
        if (datasourceDTO == null) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, databaseId + "   数据源不存在");

        }
        try {
            String[] procedure = JdbcUtils.getProcedure(datasourceDTO);
            return Result.success(procedure);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, "数据源参数构建失败");

        }
    }

    @Override
    public Result delete(String userId, String datasourceName) {
        try {
            return Result.success(datasourceService.delete(userId, datasourceName));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), false, e.getMessage());

        }
    }

    @Override
    public Result selectAll(String userId, Integer curPage, Integer pageSize, String dataSourceName, String datasourceType) {

        PageInfo<DataSourceListDTO> datasourcePageInfo = datasourceService.selectAllByUserId(userId, curPage, pageSize, StringUtils.isEmpty(dataSourceName) ? null : "%" + dataSourceName + "%", datasourceType);

        List<DataSourceListDTO> list = datasourcePageInfo.getList();

        List<JCDataSourceDTO> jcDataSourceDTOS = getJcDataSourceListDTOS(list);

        for (DataSourceListDTO datasourceDTO : list) {

            for (JCDataSourceDTO jcDataSourceDTO : jcDataSourceDTOS) {

                if (datasourceDTO.getDatasourceId().equals(jcDataSourceDTO.getDatasourceId())) {

                    datasourceDTO.setAccess(jcDataSourceDTO.getAccess());

                    datasourceDTO.setDbhost(jcDataSourceDTO.getDsHost());

                    datasourceDTO.setDatabse(jcDataSourceDTO.getDatabase());

                    datasourceDTO.setPort(jcDataSourceDTO.getDsPort());

                    datasourceDTO.setCUSTOM_DRIVER_CLASS(jcDataSourceDTO.getCUSTOM_DRIVER_CLASS());

                    datasourceDTO.setCUSTOM_URL(jcDataSourceDTO.getCUSTOM_URL());

                    datasourceDTO.setDATABASE_DIALECT_ID(jcDataSourceDTO.getDATABASE_DIALECT_ID());

                    break;
                }
            }

            if (userId.equals(datasourceDTO.getRealyUserId()) || ADMIN_USER_ID.equals(userId)) {
                datasourceDTO.setGrantFLag(Boolean.FALSE);
            } else {
                datasourceDTO.setGrantFLag(Boolean.TRUE);
            }
        }
        datasourcePageInfo.setList(list);
        return Result.success(datasourcePageInfo);


    }

    @Override
    public Result update(String userId, DatasourceVO datasourceVO) {
        Datasource datasource = new Datasource();
        BeanUtils.copyProperties(datasourceVO, datasource);
        if (datasourceVO.getAdvancedParametersVO() != null) {
            datasource.setDsConectorSetting(JsonUtils.toString(datasourceVO.getAdvancedParametersVO()));
        }
        return Result.success(datasourceService.update(datasource));
    }

    @Override
    public Result getDatasourceType() {
        List<DatasourceTypeDTO> list = datasourceService.getAllDatasourceTypes();
        return Result.success(list);
    }

    @Override
    public Result<String, Object> testDatasourceConnector(DatasourceConnectorVO datasourceConnectorVO) {
        Connection connection = null;
        try {
            connection = datasourceService.connectorDatasource(datasourceConnectorVO);
            return Result.success("连接成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, e.getMessage());
        } finally {
            if (!StringUtils.isEmpty(connection)) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Result<DatasourceDTO, Object> getDatasourceByName(String datasourceName) {
        DatasourceDTO datasource = datasourceService.getDatasourceByDatasourceName(datasourceName);
        return Result.success(datasource);
    }

    @Override
    public Result<DatasourceDTO, Object> getDatasourceById(String userId, String datasourceId) {
        DatasourceDTO datasource = datasourceService.getDatasourceByDatasourceIdAndUserId(userId, datasourceId);
        return Result.success(datasource);
    }


    @Override
    public Result<DatasourceDTO, Object> datasourceIdNotUserId(String userId, String datasourceId) {
        DatasourceDTO datasource = datasourceService.getDatasourceByIdNotUserID(datasourceId);
        return Result.success(datasource);
    }


    @Override
    public Result<List<DatasourceDTO>, Object> getDatasourceNoPage(String userId, String sourcePlatform) {
        return datasourceService.getDatasourceNoPageBySourcePlat(userId, sourcePlatform);
    }

    @Override
    public Result<StatementDto, Object> getStatementDto(String datasourceName, String schema, String userId) {


        return datasourceService.getStatementDto(datasourceName, schema, userId);
    }

    @Override
    public Result<String, Object> getStatementSql(String datasourceName, String schema, String table, String userId) {

        return datasourceService.getStatementSql(datasourceName, schema, table, userId);
    }

    @Override
    public Result<List<JCDataSourceDTO>, Object> getDatasourceNoPageBySourcePlat(String userId, String sourcePlatform) {
        List<DatasourceDTO> datasourceNoPage = datasourceService.getDatasourceNoPage(userId, sourcePlatform);
        List<JCDataSourceDTO> jcDataSourceDTOList = getJcDataSourceDTOS(datasourceNoPage);
        return Result.success(jcDataSourceDTOList);

    }

    private List<JCDataSourceDTO> getJcDataSourceDTOS(List<DatasourceDTO> datasourceNoPage) {
        List<JCDataSourceDTO> jcDataSourceDTOList = new ArrayList<>();
        datasourceNoPage.forEach(datasourceDTO -> {
            JCDataSourceDTO jcDataSourceDTO = new JCDataSourceDTO();
            BeanUtils.copyProperties(datasourceDTO, jcDataSourceDTO);
            if (JdbcUtils.getParamByUrl(datasourceDTO.getDsUrl(), "type") != null && JdbcUtils.getParamByUrl(datasourceDTO.getDsUrl(), "type").equals("GENERIC")) {
                jcDataSourceDTO.setDsType(JdbcUtils.getParamByUrl(datasourceDTO.getDsUrl(), "type"));
                jcDataSourceDTO.setCUSTOM_URL(datasourceDTO.getDsUrl());
                jcDataSourceDTO.setCUSTOM_DRIVER_CLASS(datasourceDTO.getDriverClassName());
                jcDataSourceDTO.setDATABASE_DIALECT_ID(datasourceDTO.getDsType());
            }
            DatasourceExplainDTO datasourceExplainDTO = JdbcUtils.explainURL(datasourceDTO.getDsUrl());
            jcDataSourceDTO.setDsPort(datasourceExplainDTO.getPort());
            jcDataSourceDTO.setDsHost(datasourceExplainDTO.getHost());
            jcDataSourceDTO.setDatabase(datasourceExplainDTO.getDb());
            jcDataSourceDTO.setAccess(JdbcUtils.getParamByUrl(datasourceDTO.getDsUrl(), "access"));
            jcDataSourceDTO.setDsConnectorSetting(datasourceDTO.getDsConectorSetting());
            List<DatasourceParamsDTO> paramsDTOS = new ArrayList<>();

            String[] split = datasourceExplainDTO.getDb().split("\\?");
            datasourceService.annalysUrl(paramsDTOS, split);

            jcDataSourceDTO.setParamsDTOS(paramsDTOS);

            jcDataSourceDTOList.add(jcDataSourceDTO);
        });
        return jcDataSourceDTOList;
    }

    /**
     * 为了防止修改datasourcedto,然后造成其他微服务需要重新build,这个类添加的冗余代码
     *
     * @param datasourceNoPage
     * @return
     */

    private List<JCDataSourceDTO> getJcDataSourceListDTOS(List<DataSourceListDTO> datasourceNoPage) {
        List<JCDataSourceDTO> jcDataSourceDTOList = new ArrayList<>();
        datasourceNoPage.forEach(datasourceDTO -> {
            JCDataSourceDTO jcDataSourceDTO = new JCDataSourceDTO();
            BeanUtils.copyProperties(datasourceDTO, jcDataSourceDTO);
            if (JdbcUtils.getParamByUrl(datasourceDTO.getDsUrl(), "type") != null && JdbcUtils.getParamByUrl(datasourceDTO.getDsUrl(), "type").equals("GENERIC")) {
                jcDataSourceDTO.setDsType(JdbcUtils.getParamByUrl(datasourceDTO.getDsUrl(), "type"));
                jcDataSourceDTO.setCUSTOM_URL(datasourceDTO.getDsUrl());
                jcDataSourceDTO.setCUSTOM_DRIVER_CLASS(datasourceDTO.getDriverClassName());
                jcDataSourceDTO.setDATABASE_DIALECT_ID(datasourceDTO.getDsType());
            }
            DatasourceExplainDTO datasourceExplainDTO = JdbcUtils.explainURL(datasourceDTO.getDsUrl());
            jcDataSourceDTO.setDsPort(datasourceExplainDTO.getPort());
            jcDataSourceDTO.setDsHost(datasourceExplainDTO.getHost());
            jcDataSourceDTO.setDatabase(datasourceExplainDTO.getDb());
            jcDataSourceDTO.setAccess(JdbcUtils.getParamByUrl(datasourceDTO.getDsUrl(), "access"));
            jcDataSourceDTO.setDsConnectorSetting(datasourceDTO.getDsConectorSetting());
            List<DatasourceParamsDTO> paramsDTOS = new ArrayList<>();

            String[] split = datasourceExplainDTO.getDb().split("\\?");
            datasourceService.annalysUrl(paramsDTOS, split);

            jcDataSourceDTO.setParamsDTOS(paramsDTOS);

            jcDataSourceDTOList.add(jcDataSourceDTO);
        });
        return jcDataSourceDTOList;
    }


    private Result<Boolean, Object> getBooleanObjectResult(DatasourceVO datasourceVO) {
        DatasourceConnectorVO datasourceConnectorVO = new DatasourceConnectorVO();

        BeanUtils.copyProperties(datasourceVO, datasourceConnectorVO);

        Result<String, Object> stringObjectResult = testDatasourceConnector(datasourceConnectorVO);

        if (stringObjectResult.getCode() != "10000") {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, stringObjectResult.getMsg());
        }
        return null;
    }

    @Override
    public Result<Boolean, Object> grantDatasource(String userId, GrantDatasourceVo grantDatasourceVo) {
        try {
            Boolean aBoolean = dpPortalUserDatasourceService.grantDatasource(userId, grantDatasourceVo);
            if (aBoolean) {
                return Result.success(aBoolean, "授权成功");
            } else {
                return Result.fail(StatusCode.CODE_10010.getCode(), false, "授权失败");
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.CODE_10010.getCode(), false, e.getMessage());
        }
    }

    @Override
    public Result<AllUserAndGrantUser, Object> getUsersByDsId(String userId, String dsId) {

        AllUserAndGrantUser usersByDsId = dpPortalUserDatasourceService.getUsersByDsId(userId, dsId);
        return Result.success(usersByDsId);
    }


}
