package com.youngdatafan.portal.system.management.summary.service;

import com.youngdatafan.portal.system.management.summary.dto.LastSixItemDTO;
import com.youngdatafan.portal.system.management.summary.dto.MonSumDTO;
import com.youngdatafan.portal.system.management.summary.model.FileSummary;
import com.youngdatafan.portal.system.management.summary.model.ProjectOnline;
import com.youngdatafan.portal.system.management.summary.model.ProjectStatusSum;
import com.youngdatafan.portal.system.management.summary.model.TaskRunSummary;
import java.util.List;

/**
 * IndexSummaryService.
 */
public interface IndexSummaryService {
    /**
     * 查询任务运行统计信息.
     *
     * @param userId userId
     * @return Map
     */
    List<TaskRunSummary> selectTaskRunSummary(String userId);

    /**
     * 项目上线运行统计信息.
     *
     * @param userId userId
     * @return List
     */
    List<ProjectOnline> selectProjectOnline(String userId);

    /**
     * 文件统计.
     *
     * @param userId userId
     * @return Map
     */
    List<FileSummary> selectFileSummary(String userId);

    /**
     * 项目运行状态统计.
     *
     * @param userId userId
     * @return List
     */
    List<ProjectStatusSum> selectProjectStatus(String userId);

    /**
     * selectProjectSumMonth.
     *
     * @param userId      userId
     * @param projectType projectType
     * @return MonSumDTO
     */
    MonSumDTO selectProjectSumMonth(String userId, String projectType);

    /**
     * selectReportMonthSumDetail.
     *
     * @param userId userId
     * @return MonSumDTO
     */
    MonSumDTO selectReportMonthSumDetail(String userId);

    /**
     * selectReportMonthSumDetail.
     *
     * @param userId userId
     * @return MonSumDTO
     */
    MonSumDTO selectDashboardMonthSumDetail(String userId);

    /**
     * selectBasicModelMonthSumDetail.
     *
     * @param userId userId
     * @return MonSumDTO
     */
    MonSumDTO selectBasicModelMonthSumDetail(String userId);

    /**
     * selectBusinessModelMonthSumDetail.
     *
     * @param userId userId
     * @return MonSumDTO
     */
    MonSumDTO selectBusinessModelMonthSumDetail(String userId);

    /**
     * selectLastSixBasicModel.
     *
     * @param userId userId
     * @return list
     */
    List<LastSixItemDTO> selectLastSixBasicModel(String userId);

    /**
     * selectLastSixBusinessModel.
     *
     * @param userId userId
     * @return list
     */
    List<LastSixItemDTO> selectLastSixBusinessModel(String userId);

    /**
     * selectLastSixPortalDashboard.
     *
     * @param userId userId
     * @return list
     */
    List<LastSixItemDTO> selectLastSixPortalDashboard(String userId);

    /**
     * selectLastSixFileOutPut.
     *
     * @param userId userId
     * @return list
     */
    List<LastSixItemDTO> selectLastSixFileOutPut(String userId);

    /**
     * selectLastSixFileUpload.
     *
     * @param userId userId
     * @return list
     */
    List<LastSixItemDTO> selectLastSixFileUpload(String userId);

    /**
     * selectLastSixTSJB.
     *
     * @param userId userId
     * @return list
     */
    List<LastSixItemDTO> selectLastSixTSJB(String userId);

    /**
     * selectLastSixJCJB.
     *
     * @param userId userId
     * @return list
     */
    List<LastSixItemDTO> selectLastSixJCJB(String userId);

    /**
     * selectLastSixReportRecord.
     *
     * @param userId userId
     * @return list
     */
    List<LastSixItemDTO> selectLastSixReportRecord(String userId);

}
