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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 4:31 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface DatasourceServiceApi {

    @ApiOperation(value = "添加数据源信息", notes = "添加数据源的【数据源名称\n" +
            "数据源描述\n" +
            "数据源类型\n" +
            "数据源地址\n" +
            "数据库驱动\n" +
            "数据源账号\n" +
            "数据源密码\n" +
            "是否启用\n" +
            "创建时间\n" +
            "修改时间\n" +
            "创建者】", produces = "application/json")
    @PostMapping(value = "/add")
    Result<Boolean, Object> add(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestBody DatasourceVO datasourceVO);


    @ApiOperation(value = "删除数据源", notes = "根据数据源id删除数据源", produces = "application/json")
    @DeleteMapping(value = "/delete/{datasourceName}")
    Result<Boolean, Object> delete(@RequestHeader(value = "authorization-userId", required = false) String userId,
                                   @PathVariable("datasourceName") @ApiParam("数据源名称") String datasourceName);


    @ApiOperation(value = "获取数据源", notes = "根据用户id获取数据源", produces = "application/json")
    @GetMapping(value = "/selectAll")
    Result<DatasourceWithPageDTO, Object> selectAll(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                    @ApiParam("页码") @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                                                    @ApiParam("行数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                    @ApiParam("数据源名称") @RequestParam(value = "dataSourceName", required = false) String dataSourceName,
                                                    @ApiParam("数据源类型") @RequestParam(value = "datasourceType", required = false) String datasourceType);


    @ApiOperation(value = "更新数据源", notes = "修改数据源的【数据源名称\n" +
            "数据源描述\n" +
            "数据源类型\n" +
            "数据源地址\n" +
            "数据库驱动\n" +
            "数据源账号\n" +
            "数据源密码\n" +
            "是否启用\n" +
            "修改时间\n" +
            "创建者】", produces = "application/json")
    @PutMapping(value = "/update")
    Result update(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestBody DatasourceVO datasourceVO);


    @ApiOperation(value = "获取数据源类型", notes = "从配置文件中获取所有的数据库类型", produces = "application/json")
    @GetMapping(value = "/datasourceType")
    Result getDatasourceType();

    @ApiOperation(value = "数据源测试连接", notes = "测试数据源链接是否成功", produces = "application/json")
    @PostMapping(value = "/connector")
    Result<String, Object> testDatasourceConnector(@RequestBody DatasourceConnectorVO datasourceConnectorVO);

    @ApiOperation(value = "判断此名称的数据源是否存在根据名称查询", notes = "判断此名称的数据源是否存在", produces = "application/json")
    @GetMapping(value = "/datasourceName/{datasourceName}")
    Result<DatasourceDTO, Object> getDatasourceByName(@ApiParam("数据源名称") @PathVariable("datasourceName") String datasourceName);


    @ApiOperation(value = "判断此名称的数据源是否存在根据id查询", notes = "判断此名称的数据源是否存在", produces = "application/json")
    @GetMapping(value = "/datasourceId/{datasourceId}")
    Result<DatasourceDTO, Object> getDatasourceById(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                    @ApiParam("数据源id") @PathVariable("datasourceId") String datasourceId);


    @ApiOperation(value = "判断此名称的数据源是否存在", notes = "判断此名称的数据源是否存在", produces = "application/json")
    @GetMapping(value = "/datasourceIdNotUserId/{datasourceId}")
    Result<DatasourceDTO, Object> datasourceIdNotUserId(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                        @ApiParam("数据源id") @PathVariable("datasourceId") String datasourceId);

    @ApiOperation(value = "获取所有数据源", notes = "获取所有数据源不需要进行分页查询", produces = "application/json")
    @GetMapping(value = "/selectAllNoPage")
    Result<List<DatasourceDTO>, Object> getDatasourceNoPage(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                            @RequestParam(value = "sourcePlatform", required = false) String sourcePlatform);


    @ApiOperation(value = "获取数据库的扩展信息", notes = "获取数据库的扩展信息", produces = "application/json")
    @GetMapping(value = "/getStatement")
    Result<StatementDto, Object> getStatementDto(@ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName
            , @ApiParam("schema") @RequestParam(value = "schema", required = false) String schema,
                                                 @ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);


    @ApiOperation(value = "获取数据库的扩展信息sql", notes = "获取数据库的扩展信息", produces = "application/json")
    @GetMapping(value = "/getStatement_sql")
    Result<String, Object> getStatementSql(@ApiParam("数据库名称") @RequestParam("datasourceName") String datasourceName
            , @ApiParam("schema") @RequestParam(value = "schema", required = false) String schema,
                                           @ApiParam("表名") @RequestParam(value = "tables", required = true) String table,
                                           @ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId);

    @ApiOperation(value = "获取所有数据源根据来源平台", notes = "获取所有数据源根据来源平台不需要进行分页查询", produces = "application/json")
    @GetMapping(value = "/getDatasourceNoPageBySourcePlat")
    Result<List<JCDataSourceDTO>, Object> getDatasourceNoPageBySourcePlat(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId
            , @ApiParam("来源平台") @RequestParam(value = "sourcePlatform") String sourcePlatform);


    @ApiOperation(value = "新增集成数据源", notes = "新增集成数据源", produces = "application/json")
    @PostMapping(value = "/JCAdd")
    Result<Boolean, Object> JCAdd(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestBody JCDataSourceVO jcDataSourceVO);

    @ApiOperation(value = "修改集成数据源", notes = "修改集成数据源", produces = "application/json")
    @PostMapping(value = "/JCUpdate")
    Result<Boolean, Object> JCUpdate(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestBody JCDataSourceVO jcDataSourceVO);


    @ApiOperation(value = "获取存储过程")
    @PostMapping(value = "/getProcedure")
    Result<String[], Object> getProcedure(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId, @RequestParam("databaseId") String databaseId);


    @ApiOperation(value = "授权数据源", notes = "授权数据源", produces = "application/json")
    @PostMapping(value = "/grantDatasource")
    Result<Boolean, Object> grantDatasource(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                            @RequestBody GrantDatasourceVo grantDatasourceVo);


    @ApiOperation(value = "查询此数据源授权的用户", notes = "查询此数据源授权的用户", produces = "application/json")
    @GetMapping(value = "/getUsersByDsId")
    Result<AllUserAndGrantUser, Object> getUsersByDsId(@ApiParam("用户id") @RequestHeader(value = "authorization-userId", required = false) String userId,
                                                       @RequestParam("dsId") String dsId);


}
