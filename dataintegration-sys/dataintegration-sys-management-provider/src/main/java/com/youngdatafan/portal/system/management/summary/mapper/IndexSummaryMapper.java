package com.youngdatafan.portal.system.management.summary.mapper;

import com.youngdatafan.portal.system.management.summary.model.*;
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
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: jeremychen
 * @Descripition:
 * @Date:2020/4/18 3:10 下午
 */
public interface IndexSummaryMapper {
    List<TaskRunSummary> selectTaskRunSummary(@Param("userId") String userId);

    List<ProjectOnline> selectProjectOnline(@Param("userId") String userId);

    List<FileSummary> selectFileSummary(@Param("userId") String userId);

    List<ProjectStatusSum> selectProjectStatus(@Param("userId") String userId);

    List<ProjectSumMonth> selectProjectSumMonth(@Param("userId") String userId);

    List<MonthSumDetail> selectDashboardMonthSumDetail(@Param("userId") String userId);

    List<MonthSumDetail> selectReportMonthSumDetail(@Param("userId") String userId);

    List<MonthSumDetail> selectBasicModelMonthSumDetail(@Param("userId") String userId);

    List<MonthSumDetail> selectBusinessModelMonthSumDetail(@Param("userId") String userId);

    List<DpPortalBasicModel> selectLastSixBasicModel(@Param("userId") String userId);

    List<DpPortalBusinessModel> selectLastSixBusinessModel(@Param("userId") String userId);

    List<DpPortalDashboard> selectLastSixPortalDashboard(@Param("userId") String userId);

    List<DpPortalFileManager> selectLastSixFileOutPut(@Param("userId") String userId);

    List<DpPortalFileManager> selectLastSixFileUpload(@Param("userId") String userId);

    List<DpPortalProject> selectLastSixTSJB(@Param("userId") String userId);

    List<DpPortalProject> selectLastSixJCJB(@Param("userId") String userId);

    List<DpPortalReportRecord> selectLastSixReportRecord(@Param("userId") String userId);


}
