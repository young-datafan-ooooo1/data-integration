package com.youngdatafan.portal.system.management.summary.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.summary.api.IndexSummaryApi;
import com.youngdatafan.portal.system.management.summary.dto.FileSumDetailDTO;
import com.youngdatafan.portal.system.management.summary.dto.FileSummaryDTO;
import com.youngdatafan.portal.system.management.summary.dto.LastSixItemDTO;
import com.youngdatafan.portal.system.management.summary.dto.MonSumDTO;
import com.youngdatafan.portal.system.management.summary.dto.OperationStatusEnum;
import com.youngdatafan.portal.system.management.summary.dto.ProjectOnlineDTO;
import com.youngdatafan.portal.system.management.summary.dto.ProjectStatusSumDTO;
import com.youngdatafan.portal.system.management.summary.dto.TaskRunSummaryDTO;
import com.youngdatafan.portal.system.management.summary.model.FileSummary;
import com.youngdatafan.portal.system.management.summary.model.ProjectOnline;
import com.youngdatafan.portal.system.management.summary.model.ProjectStatusSum;
import com.youngdatafan.portal.system.management.summary.model.TaskRunSummary;
import com.youngdatafan.portal.system.management.summary.service.IndexSummaryService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexSummaryApiController.
 */
@Slf4j
@RestController
@RequestMapping("/indexSummary")
public class IndexSummaryApiController implements IndexSummaryApi {

    @Autowired
    private IndexSummaryService indexSummaryService;

    @Override
    public Result<Map<String, List<TaskRunSummaryDTO>>, Object> selectTaskRunSummary(String userId) {
        List<TaskRunSummaryDTO> taskRunSummaryDTOList = new ArrayList<>();
        List<TaskRunSummary> taskRunSummaries = indexSummaryService.selectTaskRunSummary(userId);

        Map<String, List<TaskRunSummary>> typeGroup = taskRunSummaries.stream().collect(Collectors.groupingBy(o -> o.getProjectType()));
        List<String> last7Days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Map<String, List<TaskRunSummaryDTO>> result = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String daystring = year + "-" + month + "-" + day;
            log.info(daystring);
            last7Days.add(daystring);
        }

        for (Map.Entry<String, List<TaskRunSummary>> entry : typeGroup.entrySet()) {
            List<TaskRunSummary> list = entry.getValue();

            Map<String, List<TaskRunSummary>> groupMap = list.stream().collect(Collectors.groupingBy(o -> o.getStartDate()));

            for (String key : last7Days) {

                TaskRunSummaryDTO taskRunSummaryDTO = new TaskRunSummaryDTO();
                taskRunSummaryDTO.setDay(key);
                List<TaskRunSummary> value = groupMap.get(key);

                if (value != null) {
                    int total = value.stream().collect(Collectors.summingInt(TaskRunSummary::getCnt));
                    taskRunSummaryDTO.setTotal(total);
                    value.forEach(o -> {
                        switch (OperationStatusEnum.valueOf(o.getStatus()).getCode()) {
                            case "error":
                                taskRunSummaryDTO.setError(o.getCnt());
                                break;
                            case "warn":
                                taskRunSummaryDTO.setWarn(o.getCnt());
                                break;
                            case "waitingShift":
                                taskRunSummaryDTO.setWaitingShift(o.getCnt());
                                break;
                            case "idle":
                                taskRunSummaryDTO.setIdle(o.getCnt());
                                break;
                            case "pending":
                                taskRunSummaryDTO.setPending(o.getCnt());
                                break;
                            case "dependent":
                                taskRunSummaryDTO.setDependent(o.getCnt());
                                break;
                            case "running":
                                taskRunSummaryDTO.setRunning(o.getCnt());
                                break;
                            case "retrying":
                                taskRunSummaryDTO.setRetrying(o.getCnt());
                                break;
                            case "rerunning":
                                taskRunSummaryDTO.setRerunning(o.getCnt());
                                break;
                            case "restarting":
                                taskRunSummaryDTO.setRestarting(o.getCnt());
                                break;
                            case "runningWithError":
                                taskRunSummaryDTO.setRunningWithError(o.getCnt());
                                break;
                            case "runningWithWarn":
                                taskRunSummaryDTO.setRunningWithWarn(o.getCnt());
                                break;
                            case "runningWithCanceled":
                                taskRunSummaryDTO.setRunningWithCanceled(o.getCnt());
                                break;
                            case "canceling":
                                taskRunSummaryDTO.setCanceling(o.getCnt());
                                break;
                            case "canceled":
                                taskRunSummaryDTO.setCanceled(o.getCnt());
                                break;
                            case "success":
                                taskRunSummaryDTO.setSuccess(o.getCnt());
                                break;
                            case "successWithIgnore":
                                taskRunSummaryDTO.setSuccessWithIgnore(o.getCnt());
                                break;
                            default:
                                break;
                        }
                    });
                }
                taskRunSummaryDTOList.add(taskRunSummaryDTO);

            }
            result.put(entry.getKey(), taskRunSummaryDTOList);
        }
        for (String key : last7Days) {
            TaskRunSummaryDTO taskRunSummaryDTO = new TaskRunSummaryDTO();
            taskRunSummaryDTO.setDay(key);
            taskRunSummaryDTOList.add(taskRunSummaryDTO);
        }
        if (result.get("TSJB") == null) {
            result.put("TSJB", taskRunSummaryDTOList);
        }
        if (result.get("JCJB") == null) {
            result.put("JCJB", taskRunSummaryDTOList);
        }
        return Result.success(result);
    }

    @Override
    public Result<List<ProjectOnlineDTO>, Object> selectProjectOnline(String userId) {
        List<ProjectOnline> projectOnlines = indexSummaryService.selectProjectOnline(userId);
        List<ProjectOnlineDTO> projectOnlineDTOS = new ArrayList<>();
        projectOnlines.forEach(o -> {
            ProjectOnlineDTO projectOnlineDTO = new ProjectOnlineDTO();
            BeanUtils.copyProperties(o, projectOnlineDTO);
            projectOnlineDTOS.add(projectOnlineDTO);
        });
        return Result.success(projectOnlineDTOS);
    }

    @Override
    public Result<Map<String, FileSummaryDTO>, Object> selectFileSummary(String userId) {
        List<FileSummary> fileSummaries = indexSummaryService.selectFileSummary(userId);
        Map<String, List<FileSummary>> map = fileSummaries
            .stream()
            .collect(Collectors.groupingBy(o -> o.getCreateChannel()));
        Map<String, FileSummaryDTO> map1 = new HashMap<>();

        for (Map.Entry<String, List<FileSummary>> entry : map.entrySet()) {
            List<FileSummary> list = entry.getValue();
            List<FileSumDetailDTO> fileSumDetailDTOS = new ArrayList<>();
            int total = 0;
            int monAvg = 0;
            for (FileSummary o : list) {
                total = total + o.getCnt();
            }
            Map<String, Integer> sum = list.stream().collect(Collectors.groupingBy(FileSummary::getFileType, Collectors.summingInt(FileSummary::getCnt)));
            for (Map.Entry<String, Integer> entry1 : sum.entrySet()) {
                FileSumDetailDTO fileSumDetailDTO = new FileSumDetailDTO();
                fileSumDetailDTO.setFileType(entry1.getKey());
                fileSumDetailDTO.setCnt(entry1.getValue());
                fileSumDetailDTOS.add(fileSumDetailDTO);
            }
            FileSummaryDTO fileSummaryDTO = new FileSummaryDTO();
            monAvg = list.size() == 0 ? 0 : total / list.size();
            fileSummaryDTO.setFileSumDetailDTOS(fileSumDetailDTOS);
            fileSummaryDTO.setAvg(monAvg);
            fileSummaryDTO.setTotal(total);
            map1.put(entry.getKey(), fileSummaryDTO);
        }
        return Result.success(map1);
    }

    @Override
    public Result<List<ProjectStatusSumDTO>, Object> selectProjectStatus(String userId) {
        List<ProjectStatusSum> projectStatusSums = indexSummaryService.selectProjectStatus(userId);
        List<ProjectStatusSumDTO> projectStatusSumDTOS = new ArrayList<>();
        projectStatusSums.forEach(o -> {
            ProjectStatusSumDTO projectStatusSumDTO = new ProjectStatusSumDTO();
            BeanUtils.copyProperties(o, projectStatusSumDTO);
            projectStatusSumDTOS.add(projectStatusSumDTO);
        });
        return Result.success(projectStatusSumDTOS);
    }

    @Override
    public Result<MonSumDTO, Object> selectProjectSumMonth(String userId, String projectType) {
        MonSumDTO monSumDTO = indexSummaryService.selectProjectSumMonth(userId, projectType);
        return Result.success(monSumDTO);
    }

    @Override
    public Result<MonSumDTO, Object> selectReportMonthSumDetail(String userId) {
        MonSumDTO monSumDTO = indexSummaryService.selectReportMonthSumDetail(userId);
        return Result.success(monSumDTO);
    }

    @Override
    public Result<MonSumDTO, Object> selectDashboardMonthSumDetail(String userId) {
        MonSumDTO monSumDTO = indexSummaryService.selectDashboardMonthSumDetail(userId);
        return Result.success(monSumDTO);
    }

    @Override
    public Result<MonSumDTO, Object> selectBasicModelMonthSumDetail(String userId) {
        MonSumDTO monSumDTO = indexSummaryService.selectBasicModelMonthSumDetail(userId);
        return Result.success(monSumDTO);
    }

    @Override
    public Result<MonSumDTO, Object> selectBusinessModelMonthSumDetail(String userId) {
        MonSumDTO monSumDTO = indexSummaryService.selectBusinessModelMonthSumDetail(userId);
        return Result.success(monSumDTO);
    }

    @Override
    public Result<List<LastSixItemDTO>, Object> selectLastSixBasicModel(String userId) {
        return Result.success(indexSummaryService.selectLastSixBasicModel(userId));
    }

    @Override
    public Result<List<LastSixItemDTO>, Object> selectLastSixBusinessModel(String userId) {
        return Result.success(indexSummaryService.selectLastSixBusinessModel(userId));
    }

    @Override
    public Result<List<LastSixItemDTO>, Object> selectLastSixPortalDashboard(String userId) {
        return Result.success(indexSummaryService.selectLastSixPortalDashboard(userId));
    }

    @Override
    public Result<List<LastSixItemDTO>, Object> selectLastSixFileOutPut(String userId) {
        return Result.success(indexSummaryService.selectLastSixFileOutPut(userId));
    }

    @Override
    public Result<List<LastSixItemDTO>, Object> selectLastSixFileUpload(String userId) {
        return Result.success(indexSummaryService.selectLastSixFileUpload(userId));
    }

    @Override
    public Result<List<LastSixItemDTO>, Object> selectLastSixTSJB(String userId) {
        return Result.success(indexSummaryService.selectLastSixTSJB(userId));
    }

    @Override
    public Result<List<LastSixItemDTO>, Object> selectLastSixJCJB(String userId) {
        return Result.success(indexSummaryService.selectLastSixJCJB(userId));
    }

    @Override
    public Result<List<LastSixItemDTO>, Object> selectLastSixReportRecord(String userId) {
        return Result.success(indexSummaryService.selectLastSixReportRecord(userId));
    }

}
