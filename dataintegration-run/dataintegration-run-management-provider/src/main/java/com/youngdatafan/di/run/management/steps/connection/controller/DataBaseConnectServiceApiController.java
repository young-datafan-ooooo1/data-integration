package com.youngdatafan.di.run.management.steps.connection.controller;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;

import com.youngdatafan.di.run.management.datasource.feign.DatasourceServiceApiClient;
import com.youngdatafan.di.run.management.steps.connect.api.DBConnectServiceApi;

import com.youngdatafan.di.run.management.steps.connect.dto.FieldDTO;
import com.youngdatafan.di.run.management.steps.connect.dto.PreviewDataInfoDTO;
import com.youngdatafan.di.run.management.steps.connect.dto.ProcedureDTO;
import com.youngdatafan.di.run.management.steps.connect.vo.ConnectionDetailVO;
import com.youngdatafan.di.run.management.steps.connect.vo.QueryVO;
import com.youngdatafan.di.run.management.steps.connection.service.DataBaseInputService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * @author echo_
 */
@Slf4j
@RestController
@RequestMapping("/database")
public class DataBaseConnectServiceApiController implements DBConnectServiceApi {
    private final DataBaseInputService dataBaseInputService;
    private final DatasourceServiceApiClient datasourceServiceApiClient;
    @Autowired
    public DataBaseConnectServiceApiController(DataBaseInputService dataBaseInputService, DatasourceServiceApiClient datasourceServiceApiClient) {
        this.dataBaseInputService = dataBaseInputService;
        this.datasourceServiceApiClient = datasourceServiceApiClient;
    }

    @Override
    public Result dbJason() {
        try {
            return Result.success(dataBaseInputService.getDbJson());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取Json数据失败 ,{}", e.toString());
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "获取Json数据失败");
        }

    }

    @Override
    public Result testConnect(ConnectionDetailVO connectionDetailVO) {

        return dataBaseInputService.testConnect(connectionDetailVO);


    }

    @Override
    public Result<PreviewDataInfoDTO, Object> previewData(QueryVO queryVO) {
        return dataBaseInputService.previewData(queryVO);
    }

    @Override
    public Result<List<FieldDTO>, Object> getFieldInfo(QueryVO queryVO) {

        return dataBaseInputService.getFieldInfo(queryVO);
    }



    @Override
    public Result<List<ProcedureDTO>, Object> getProcedure(String userId, String databaseId) {
        Result<String[], Object> procedure = datasourceServiceApiClient.getProcedure(userId, databaseId);
        if(!procedure.getCode().equals(StatusCode.CODE_10000) ){
            return Result.fail(StatusCode.CODE_10010.getCode(), "", procedure.getMsg());
        }else {
            String[] content = procedure.getContent();
            List<ProcedureDTO> procedureDTOS=new ArrayList<>();
            for (String s : content) {
                ProcedureDTO procedureDTO=new  ProcedureDTO();
                procedureDTO.setName(s);
                procedureDTOS.add(procedureDTO);
            }
            return Result.success(procedureDTOS);
        }
        
    }
}
