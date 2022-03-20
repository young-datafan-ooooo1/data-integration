package com.youngdatafan.di.run.management.server.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.di.run.management.datasource.PluginRunDataSourceRepository;
import com.youngdatafan.di.run.management.server.api.DataPreviewServiceApi;
import com.youngdatafan.di.run.management.server.service.DataDownloadService;
import com.youngdatafan.di.run.management.server.service.DataPreviewService;
import com.youngdatafan.di.run.management.server.vo.PreviewDataVO;
import com.youngdatafan.di.run.management.server.vo.PreviewDownloadVO;
import com.youngdatafan.dataintegration.core.util.sql.DataSourceWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author gavin
 * @since 2020/2/26 12:26 下午
 */
@RestController
@RequestMapping("/preview")
public class DataPreviewServiceApiController implements DataPreviewServiceApi {

    private final DataPreviewService dataPreviewService;
    private final DataDownloadService dataDownloadService;
    private final PluginRunDataSourceRepository pluginRunDataSourceRepository;

    @Autowired
    public DataPreviewServiceApiController(DataPreviewService dataPreviewService, DataDownloadService dataDownloadService, PluginRunDataSourceRepository pluginRunDataSourceRepository) {
        this.dataPreviewService = dataPreviewService;
        this.dataDownloadService = dataDownloadService;
        this.pluginRunDataSourceRepository = pluginRunDataSourceRepository;
    }

    @Override
    public Result<List<Map<String, Object>>, Object> previewData(String userId, PreviewDataVO previewDataVO) {
        final DataSourceWrap dataSource = pluginRunDataSourceRepository.getDataSource(userId, previewDataVO.getDataSourceId());
        // 查询明细
        final List<Map<String, Object>> previewData = dataPreviewService.previewData(dataSource, previewDataVO);

        return Result.success(previewData);
    }

    @Override
    public void excelDownload(String userId, String paramsJson, HttpServletResponse response) throws IOException {
        PreviewDownloadVO previewDownloadVO = JsonUtils.parseObject(paramsJson, PreviewDownloadVO.class);
        dataDownloadService.downloadExcel(userId, previewDownloadVO, false, response);
    }

    @Override
    public void excelDownloadByNativeSql(String userId, String paramsJson, HttpServletResponse response) throws IOException {
        PreviewDownloadVO previewDownloadVO = JsonUtils.parseObject(paramsJson, PreviewDownloadVO.class);
        dataDownloadService.downloadExcel(userId, previewDownloadVO, true, response);
    }


}
