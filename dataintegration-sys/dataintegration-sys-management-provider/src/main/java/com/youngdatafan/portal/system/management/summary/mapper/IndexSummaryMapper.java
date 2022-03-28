package com.youngdatafan.portal.system.management.summary.mapper;

import com.youngdatafan.portal.system.management.summary.model.DpPortalBasicModel;
import com.youngdatafan.portal.system.management.summary.model.DpPortalBusinessModel;
import com.youngdatafan.portal.system.management.summary.model.DpPortalDashboard;
import com.youngdatafan.portal.system.management.summary.model.DpPortalFileManager;
import com.youngdatafan.portal.system.management.summary.model.DpPortalProject;
import com.youngdatafan.portal.system.management.summary.model.DpPortalReportRecord;
import com.youngdatafan.portal.system.management.summary.model.FileSummary;
import com.youngdatafan.portal.system.management.summary.model.MonthSumDetail;
import com.youngdatafan.portal.system.management.summary.model.ProjectOnline;
import com.youngdatafan.portal.system.management.summary.model.ProjectStatusSum;
import com.youngdatafan.portal.system.management.summary.model.ProjectSumMonth;
import com.youngdatafan.portal.system.management.summary.model.TaskRunSummary;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * IndexSummaryMapper.
 */
public interface IndexSummaryMapper {

    /**
     * selectTaskRunSummary.
     *
     * @param userId userId
     * @return List
     */
    List<TaskRunSummary> selectTaskRunSummary(@Param("userId") String userId);

    /**
     * selectProjectOnline.
     *
     * @param userId userId
     * @return list
     */
    List<ProjectOnline> selectProjectOnline(@Param("userId") String userId);

    /**
     * selectFileSummary.
     *
     * @param userId id
     * @return List
     */
    List<FileSummary> selectFileSummary(@Param("userId") String userId);

    /**
     * selectProjectStatus.
     *
     * @param userId id
     * @return list
     */
    List<ProjectStatusSum> selectProjectStatus(@Param("userId") String userId);

    /**
     * selectProjectSumMonth.
     *
     * @param userId id
     * @return list
     */
    List<ProjectSumMonth> selectProjectSumMonth(@Param("userId") String userId);

    /**
     * selectDashboardMonthSumDetail.
     *
     * @param userId id
     * @return list
     */
    List<MonthSumDetail> selectDashboardMonthSumDetail(@Param("userId") String userId);

    /**
     * selectReportMonthSumDetail.
     *
     * @param userId userId
     * @return list
     */
    List<MonthSumDetail> selectReportMonthSumDetail(@Param("userId") String userId);

    /**
     * selectBasicModelMonthSumDetail.
     *
     * @param userId userId
     * @return list
     */
    List<MonthSumDetail> selectBasicModelMonthSumDetail(@Param("userId") String userId);

    /**
     * selectBusinessModelMonthSumDetail.
     *
     * @param userId id
     * @return list
     */
    List<MonthSumDetail> selectBusinessModelMonthSumDetail(@Param("userId") String userId);

    /**
     * selectLastSixBasicModel.
     *
     * @param userId userId
     * @return List
     */
    List<DpPortalBasicModel> selectLastSixBasicModel(@Param("userId") String userId);

    /**
     * selectLastSixBusinessModel.
     *
     * @param userId id
     * @return List
     */
    List<DpPortalBusinessModel> selectLastSixBusinessModel(@Param("userId") String userId);

    /**
     * selectLastSixPortalDashboard.
     *
     * @param userId userId
     * @return list
     */
    List<DpPortalDashboard> selectLastSixPortalDashboard(@Param("userId") String userId);

    /**
     * selectLastSixFileOutPut.
     *
     * @param userId userId
     * @return list
     */
    List<DpPortalFileManager> selectLastSixFileOutPut(@Param("userId") String userId);

    /**
     * selectLastSixFileUpload.
     *
     * @param userId id
     * @return list
     */
    List<DpPortalFileManager> selectLastSixFileUpload(@Param("userId") String userId);

    /**
     * selectLastSixTSJB.
     *
     * @param userId id
     * @return list
     */
    List<DpPortalProject> selectLastSixTSJB(@Param("userId") String userId);

    /**
     * selectLastSixJCJB.
     *
     * @param userId userId
     * @return list
     */
    List<DpPortalProject> selectLastSixJCJB(@Param("userId") String userId);

    /**
     * selectLastSixReportRecord.
     *
     * @param userId id
     * @return list
     */
    List<DpPortalReportRecord> selectLastSixReportRecord(@Param("userId") String userId);

}
