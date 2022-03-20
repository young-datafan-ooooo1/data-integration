package com.youngdatafan.di.run.management.steps.regex.api;


import com.youngdatafan.dataintegration.core.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "正则表达式接口")
public interface RegexServiceApi {


    @ApiOperation(value = "是否匹配数据", produces = "application/json")
    @PostMapping(value = "/ismatch")
    Result ismatch(@ApiParam(value = "正则", required = true) @RequestParam String regex, @ApiParam(value = "匹配值", required = true) @RequestParam String matcherValue);


    @ApiOperation(value = "匹配数据分组", produces = "application/json")
    @PostMapping(value = "/groupCount")
    Result<List<String>,Object> groupCount(@ApiParam(value = "正则", required = true) @RequestParam String regex, @ApiParam(value = "匹配值", required = true) @RequestParam String matcherValue);




}
