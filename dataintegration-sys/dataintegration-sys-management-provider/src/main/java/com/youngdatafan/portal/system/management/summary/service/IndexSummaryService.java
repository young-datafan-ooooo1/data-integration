package com.youngdatafan.portal.system.management.summary.service;

import com.youngdatafan.portal.system.management.summary.dto.LastSixItemDTO;
import com.youngdatafan.portal.system.management.summary.dto.MonSumDTO;
import com.youngdatafan.portal.system.management.summary.model.FileSummary;
import com.youngdatafan.portal.system.management.summary.model.ProjectOnline;
import com.youngdatafan.portal.system.management.summary.model.ProjectStatusSum;
import com.youngdatafan.portal.system.management.summary.model.TaskRunSummary;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/18 3:29 下午
 */
public interface IndexSummaryService {
    List<TaskRunSummary> selectTaskRunSummary(String userId);

    List<ProjectOnline> selectProjectOnline(String userId);

    List<FileSummary> selectFileSummary(String userId);

    List<ProjectStatusSum> selectProjectStatus(String userId);

    MonSumDTO selectProjectSumMonth(String userId, String projectType);

    MonSumDTO selectReportMonthSumDetail(String userId);

    MonSumDTO selectDashboardMonthSumDetail(String userId);

    MonSumDTO selectBasicModelMonthSumDetail(String userId);

    MonSumDTO selectBusinessModelMonthSumDetail(String userId);

    List<LastSixItemDTO> selectLastSixBasicModel(String userId);

    List<LastSixItemDTO> selectLastSixBusinessModel(String userId);

    List<LastSixItemDTO> selectLastSixPortalDashboard(String userId);

    List<LastSixItemDTO> selectLastSixFileOutPut(String userId);

    List<LastSixItemDTO> selectLastSixFileUpload(String userId);

    List<LastSixItemDTO> selectLastSixTSJB(String userId);

    List<LastSixItemDTO> selectLastSixJCJB(String userId);

    List<LastSixItemDTO> selectLastSixReportRecord(String userId);


}
