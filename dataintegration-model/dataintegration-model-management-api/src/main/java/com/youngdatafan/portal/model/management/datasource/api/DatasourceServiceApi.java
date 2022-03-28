package com.youngdatafan.portal.model.management.datasource.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.datasource.dto.AllUserAndGrantUser;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceWithPageDTO;
import com.youngdatafan.portal.model.management.datasource.dto.JCDataSourceDTO;
import com.youngdatafan.portal.model.management.datasource.dto.StatementDto;
import com.youngdatafan.portal.model.management.datasource.vo.DatasourceConnectorVO;
import com.youngdatafan.portal.model.management.datasource.vo.DatasourceVO;
import com.youngdatafan.portal.model.management.datasource.vo.GrantDatasourceVo;
import com.youngdatafan.portal.model.management.datasource.vo.JCDataSourceVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 添加数据源信息.
 */
public interface DatasourceServiceApi {
    /**
     * 添加数据源信息.
     *
     * @param userId       userId
     * @param datasourceVO datasourceVO
     * @return Boolean
     */
    @PostMapping(value = "/add")
    Result<Boolean, Object> add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestBody DatasourceVO datasourceVO);

    /**
     * 删除数据源.
     *
     * @param userId         userId
     * @param datasourceName datasourceName
     * @return Boolean
     */
    @ApiOperation(value = "删除数据源", notes = "根据数据源id删除数据源", produces = "application/json")
    @DeleteMapping(value = "/delete/{datasourceName}")
    Result<Boolean, Object> delete(@RequestHeader(value = "authorization-userId", required = false) String userId,
                                   @PathVariable("datasourceName") @ApiParam("数据源名称") String datasourceName);

    /**
     * 获取数据源.
     *
     * @param userId         userId
     * @param curPage        curPage
     * @param pageSize       pageSize
     * @param dataSourceName dataSourceName
     * @param datasourceType datasourceType
     * @return DatasourceWithPageDTO
     */
    @ApiOperation(value = "获取数据源", notes = "根据用户id获取数据源", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result<DatasourceWithPageDTO, Object> selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                    @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                                                    @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                    @ApiParam("数据源名称") @RequestParam(value = "dataSourceName", required = false) String dataSourceName,
                                                    @ApiParam("数据源类型") @RequestParam(value = "datasourceType", required = false) String datasourceType);

    /**
     * 更新数据源.
     *
     * @param userId       userId
     * @param datasourceVO datasourceVO
     * @return Result
     */
    @PutMapping(value = "/update")
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestBody DatasourceVO datasourceVO);

    /**
     * 获取数据源类型.
     *
     * @return Result
     */
    @ApiOperation(value = "获取数据源类型", notes = "从配置文件中获取所有的数据库类型", produces = "application/json")
    @GetMapping(value = "/datasourceType")
    Result getDatasourceType();

    /**
     * 数据源测试连接.
     *
     * @param datasourceConnectorVO datasourceConnectorVO
     * @return String
     */
    @ApiOperation(value = "数据源测试连接", notes = "测试数据源链接是否成功", produces = "application/json")
    @PostMapping(value = "/connector")
    Result<String, Object> testDatasourceConnector(@RequestBody DatasourceConnectorVO datasourceConnectorVO);

    /**
     * 判断此名称的数据源是否存在根据名称查询.
     *
     * @param datasourceName datasourceName
     * @return DatasourceDTO
     */
    @ApiOperation(value = "判断此名称的数据源是否存在根据名称查询", notes = "判断此名称的数据源是否存在", produces = "application/json")
    @GetMapping(value = "/datasourceName/{datasourceName}")
    Result<DatasourceDTO, Object> getDatasourceByName(@ApiParam("数据源名称") @PathVariable("datasourceName") String datasourceName);

    /**
     * 判断此名称的数据源是否存在根据id查询.
     *
     * @param userId       userId
     * @param datasourceId datasourceId
     * @return DatasourceDTO
     */
    @ApiOperation(value = "判断此名称的数据源是否存在根据id查询", notes = "判断此名称的数据源是否存在", produces = "application/json")
    @GetMapping(value = "/datasourceId/{datasourceId}")
    Result<DatasourceDTO, Object> getDatasourceById(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                    @ApiParam("数据源id") @PathVariable("datasourceId") String datasourceId);

    /**
     * 判断此名称的数据源是否存在.
     *
     * @param userId       userId
     * @param datasourceId datasourceId
     * @return DatasourceDTO
     */
    @ApiOperation(value = "判断此名称的数据源是否存在", notes = "判断此名称的数据源是否存在", produces = "application/json")
    @GetMapping(value = "/datasourceIdNotUserId/{datasourceId}")
    Result<DatasourceDTO, Object> datasourceIdNotUserId(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                        @ApiParam("数据源id") @PathVariable("datasourceId") String datasourceId);

    /**
     * 获取所有数据源.
     *
     * @param userId         userId
     * @param sourcePlatform sourcePlatform
     * @return 获取所有数据源
     */
    @ApiOperation(value = "获取所有数据源", notes = "获取所有数据源不需要进行分页查询", produces = "application/json")
    @GetMapping(value = "/selectAllNoPage")
    Result<List<DatasourceDTO>, Object> getDatasourceNoPage(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                            @RequestParam(value = "sourcePlatform", required = false) String sourcePlatform);

    /**
     * 获取数据库的扩展信息.
     *
     * @param datasourceName datasourceName
     * @param schema         schema
     * @param userId         userId
     * @return StatementDto
     */
    @ApiOperation(value = "获取数据库的扩展信息", notes = "获取数据库的扩展信息", produces = "application/json")
    @GetMapping(value = "/getStatement")
    Result<StatementDto, Object> getStatementDto(@ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName,
                                                 @ApiParam("schema") @RequestParam(value = "schema", required = false) String schema,
                                                 @ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 获取数据库的扩展信息sql.
     *
     * @param datasourceName datasourceName
     * @param schema         schema
     * @param table          table
     * @param userId         userId
     * @return String
     */
    @ApiOperation(value = "获取数据库的扩展信息sql", notes = "获取数据库的扩展信息", produces = "application/json")
    @GetMapping(value = "/getStatement_sql")
    Result<String, Object> getStatementSql(@ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName,
                                           @ApiParam("schema") @RequestParam(value = "schema", required = false) String schema,
                                           @ApiParam("表名") @RequestParam(value = "tables", required = true) String table,
                                           @ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    /**
     * 获取所有数据源根据来源平台.
     *
     * @param userId         userId
     * @param sourcePlatform sourcePlatform
     * @return List
     */
    @ApiOperation(value = "获取所有数据源根据来源平台", notes = "获取所有数据源根据来源平台不需要进行分页查询", produces = "application/json")
    @GetMapping(value = "/getDatasourceNoPageBySourcePlat")
    Result<List<JCDataSourceDTO>, Object> getDatasourceNoPageBySourcePlat(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                                          @ApiParam("来源平台") @RequestParam(value = "sourcePlatform") String sourcePlatform);

    /**
     * 新增集成数据源.
     *
     * @param userId         userId
     * @param jcDataSourceVO jcDataSourceVO
     * @return Boolean
     */
    @ApiOperation(value = "新增集成数据源", notes = "新增集成数据源", produces = "application/json")
    @PostMapping(value = "/JCAdd")
    Result<Boolean, Object> jcAdd(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestBody JCDataSourceVO jcDataSourceVO);

    /**
     * 修改集成数据源.
     *
     * @param userId         userId
     * @param jcDataSourceVO jcDataSourceVO
     * @return Boolean
     */
    @ApiOperation(value = "修改集成数据源", notes = "修改集成数据源", produces = "application/json")
    @PostMapping(value = "/JCUpdate")
    Result<Boolean, Object> jcUpdate(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestBody JCDataSourceVO jcDataSourceVO);

    /**
     * 获取存储过程.
     *
     * @param userId     userId
     * @param databaseId databaseId
     * @return String
     */
    @ApiOperation(value = "获取存储过程")
    @PostMapping(value = "/getProcedure")
    Result<String[], Object> getProcedure(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestParam("databaseId") String databaseId);

    /**
     * 授权数据源.
     *
     * @param userId            userId
     * @param grantDatasourceVo grantDatasourceVo
     * @return Boolean
     */
    @ApiOperation(value = "授权数据源", notes = "授权数据源", produces = "application/json")
    @PostMapping(value = "/grantDatasource")
    Result<Boolean, Object> grantDatasource(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                            @RequestBody GrantDatasourceVo grantDatasourceVo);

    /**
     * 查询此数据源授权的用户.
     *
     * @param userId userId
     * @param dsId   dsId
     * @return AllUserAndGrantUser
     */
    @ApiOperation(value = "查询此数据源授权的用户", notes = "查询此数据源授权的用户", produces = "application/json")
    @GetMapping(value = "/getUsersByDsId")
    Result<AllUserAndGrantUser, Object> getUsersByDsId(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                       @RequestParam("dsId") String dsId);

}
