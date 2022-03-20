package com.youngdatafan.di.run.management.steps.javascript.api;


import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.steps.javascript.vo.JavascriptVO;
import com.youngdatafan.di.run.management.steps.javascript.vo.ScriptFieldVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author Echo_Sxl on 2020/8/6 17:55
 * @version 1.0
 */
@Api(tags = "JavaScripAPI接口")
public interface JavaScriptServiceApi {

    @ApiOperation(value = "获取JavaScriptFunctions")
    @PostMapping(value = "/getJavaScriptFunctions")
    Result getJavaScriptFunctions();

    @ApiOperation(value = "获取变量", produces = "application/json")
    @PostMapping(value = "/getVariate")
    Result<List<ScriptFieldVO>, Object> getVariate(@RequestBody JavascriptVO javascriptVO) throws Exception;

}
