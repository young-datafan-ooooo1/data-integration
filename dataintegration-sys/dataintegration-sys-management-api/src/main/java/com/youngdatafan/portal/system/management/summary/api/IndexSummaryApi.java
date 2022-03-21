package com.youngdatafan.portal.system.management.summary.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.summary.dto.FileSummaryDTO;
import com.youngdatafan.portal.system.management.summary.dto.LastSixItemDTO;
import com.youngdatafan.portal.system.management.summary.dto.MonSumDTO;
import com.youngdatafan.portal.system.management.summary.dto.ProjectOnlineDTO;
import com.youngdatafan.portal.system.management.summary.dto.ProjectStatusSumDTO;
import com.youngdatafan.portal.system.management.summary.dto.TaskRunSummaryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页统计信息.
 */
@Api(tags = "首页统计信息")
public interface IndexSummaryApi {

    /**
     * 查询任务运行统计信息.
     *
     * @param userId userId
     * @return Map
     */
    @ApiOperation(value = "查询任务运行统计信息", notes = "查询任务运行统计信息", produces = "application/json")
    @GetMapping("/selectTaskRunSummary")
    Result<Map<String, List<TaskRunSummaryDTO>>, Object> selectTaskRunSummary(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 项目上线运行统计信息.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "项目上线运行统计信息", notes = "项目上线运行统计信息", produces = "application/json")
    @GetMapping("/selectProjectOnline")
    Result<List<ProjectOnlineDTO>, Object> selectProjectOnline(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 文件统计.
     *
     * @param userId userId
     * @return Map
     */
    @ApiOperation(value = "文件统计", notes = "文件统计", produces = "application/json")
    @GetMapping("/selectFileSummary")
    Result<Map<String, FileSummaryDTO>, Object> selectFileSummary(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 项目运行状态统计.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "项目运行状态统计", notes = "项目运行状态统计", produces = "application/json")
    @GetMapping("/selectProjectStatus")
    Result<List<ProjectStatusSumDTO>, Object> selectProjectStatus(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 项目数量统计.
     *
     * @param userId      userId
     * @param projectType userId
     * @return MonSumDTO
     */
    @ApiOperation(value = "项目数量统计", notes = "项目数量统计", produces = "application/json")
    @GetMapping("/selectProjectSumMonth")
    Result<MonSumDTO, Object> selectProjectSumMonth(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId,
                                                    @ApiParam("项目类别（TSJB:探索脚本，JCJB：集成脚本）") @RequestParam("projectType") String projectType);

    /**
     * 获取报表图表统计.
     *
     * @param userId 获取报表图表统计
     * @return MonSumDTO
     */
    @ApiOperation(value = "获取报表图表统计", notes = "获取报表图表统计", produces = "application/json")
    @GetMapping("/selectReportMonthSumDetail")
    Result<MonSumDTO, Object> selectReportMonthSumDetail(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取报表看板统计.
     *
     * @param userId userId
     * @return MonSumDTO
     */
    @ApiOperation(value = "获取报表看板统计", notes = "获取报表看板统计", produces = "application/json")
    @GetMapping("/selectDashboardMonthSumDetail")
    Result<MonSumDTO, Object> selectDashboardMonthSumDetail(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取基本模型统计.
     *
     * @param userId userId
     * @return MonSumDTO
     */
    @ApiOperation(value = "获取基本模型统计", notes = "获取基本模型统计", produces = "application/json")
    @GetMapping("/selectBasicModelMonthSumDetail")
    Result<MonSumDTO, Object> selectBasicModelMonthSumDetail(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取业务模型统计.
     *
     * @param userId userId
     * @return MonSumDTO
     */
    @ApiOperation(value = "获取业务模型统计", notes = "获取业务模型统计", produces = "application/json")
    @GetMapping("/selectBusinessModelMonthSumDetail")
    Result<MonSumDTO, Object> selectBusinessModelMonthSumDetail(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取业务模型统计.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取业务模型统计", notes = "获取业务模型统计", produces = "application/json")
    @GetMapping("/selectLastSixBasicModel")
    Result<List<LastSixItemDTO>, Object> selectLastSixBasicModel(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * selectLastSixBusinessModel.
     *
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取业务模型统计", notes = "获取业务模型统计", produces = "application/json")
    @GetMapping("/selectLastSixBusinessModel")
    Result<List<LastSixItemDTO>, Object> selectLastSixBusinessModel(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取最近六个仪表盘.
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取最近六个仪表盘", notes = "获取最近六个仪表盘", produces = "application/json")
    @GetMapping("/selectLastSixPortalDashboard")
    Result<List<LastSixItemDTO>, Object> selectLastSixPortalDashboard(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取最近六个输出文件.
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取最近六个输出文件", notes = "获取最近六个输出文件", produces = "application/json")
    @GetMapping("/selectLastSixFileOutPut")
    Result<List<LastSixItemDTO>, Object> selectLastSixFileOutPut(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取最近六个上传的文件.
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取最近六个上传的文件", notes = "获取最近六个上传的文件", produces = "application/json")
    @GetMapping("/selectLastSixFileUpload")
    Result<List<LastSixItemDTO>, Object> selectLastSixFileUpload(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取最近六个探索脚本.
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取最近六个探索脚本", notes = "获取最近六个探索脚本", produces = "application/json")
    @GetMapping("/selectLastSixTSJB")
    Result<List<LastSixItemDTO>, Object> selectLastSixTSJB(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取最近六个集成脚本.
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取最近六个集成脚本", notes = "获取最近六个集成脚本", produces = "application/json")
    @GetMapping("/selectLastSixJCJB")
    Result<List<LastSixItemDTO>, Object> selectLastSixJCJB(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

    /**
     * 获取最近六个图表.
     * @param userId userId
     * @return List
     */
    @ApiOperation(value = "获取最近六个图表", notes = "获取最近六个图表", produces = "application/json")
    @GetMapping("/selectLastSixReportRecord")
    Result<List<LastSixItemDTO>, Object> selectLastSixReportRecord(@ApiParam("操作用户Id") @RequestHeader("authorization-userId") String userId);

}
