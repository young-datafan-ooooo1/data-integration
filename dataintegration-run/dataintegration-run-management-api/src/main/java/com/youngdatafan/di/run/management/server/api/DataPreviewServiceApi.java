package com.youngdatafan.di.run.management.server.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.di.run.management.server.vo.PreviewDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author gavin
 * @since 2020-02-26 12:23:17
 */
@Api(tags = "数据预览API接口")
public interface DataPreviewServiceApi {

    @ApiOperation(value = "数据预览", produces = "application/json")
    @PostMapping(value = "/previewData")
    Result<List<Map<String, Object>>, Object> previewData(@RequestHeader("authorization-userId") String userId
            , @Validated @RequestBody PreviewDataVO previewDataVO);

    @ApiOperation(value = "excel数据下载")
    @RequestMapping(value = "/excelDownload")
    void excelDownload(@RequestHeader("authorization-userId") String userId
            , @Validated @RequestParam("paramsJson") String paramsJson
            , HttpServletResponse response) throws IOException;

    @ApiOperation(value = "excel数据下载根据原生sql")
    @RequestMapping(value = "/excelDownloadByNativeSql")
    void excelDownloadByNativeSql(@RequestHeader("authorization-userId") String userId
            , @Validated @RequestParam("paramsJson") String paramsJson
            , HttpServletResponse response) throws IOException;

}
