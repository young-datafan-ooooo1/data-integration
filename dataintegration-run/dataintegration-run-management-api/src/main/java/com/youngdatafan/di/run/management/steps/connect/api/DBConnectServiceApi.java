package com.youngdatafan.di.run.management.steps.connect.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.steps.connect.dto.FieldDTO;
import com.youngdatafan.di.run.management.steps.connect.dto.PreviewDataInfoDTO;
import com.youngdatafan.di.run.management.steps.connect.dto.ProcedureDTO;
import com.youngdatafan.di.run.management.steps.connect.vo.ConnectionDetailVO;
import com.youngdatafan.di.run.management.steps.connect.vo.QueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author Echo_Sxl on 2020/8/6 17:55
 * @version 1.0
 */
@Api(tags = "表输入API接口")
public interface DBConnectServiceApi {

    @ApiOperation(value = "表输入数据类型Json数据")
    @PostMapping(value = "/dbTypeJson")
    Result dbJason();


    @ApiOperation(value = "测试连接")
    @PostMapping(value = "/testConnect")
    Result testConnect(@Validated @RequestBody ConnectionDetailVO connectionDetailVO);

    @ApiOperation(value = "预览数据")
    @PostMapping(value = "/previewData")
    Result<PreviewDataInfoDTO,Object> previewData(@Validated @RequestBody QueryVO queryVO);



    @ApiOperation(value = "获取字段信息")
    @PostMapping(value = "/getFieldInfo")
    Result<List<FieldDTO>,Object> getFieldInfo(@Validated @RequestBody QueryVO queryVO);



    @ApiOperation(value = "获取存取过程")
    @PostMapping(value = "/getProcedure")
    Result<List<ProcedureDTO>,Object> getProcedure(@RequestHeader("authorization-userId") String userId, @RequestParam("databaseId") String databaseId);
}
