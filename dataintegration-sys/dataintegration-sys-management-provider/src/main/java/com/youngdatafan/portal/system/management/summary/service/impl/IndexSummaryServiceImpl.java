package com.youngdatafan.portal.system.management.summary.service.impl;

import com.youngdatafan.portal.system.management.summary.dto.LastSixItemDTO;
import com.youngdatafan.portal.system.management.summary.dto.MonSumDTO;
import com.youngdatafan.portal.system.management.summary.mapper.IndexSummaryMapper;
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
import com.youngdatafan.portal.system.management.summary.service.IndexSummaryService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * IndexSummaryServiceImpl.
 */
@Slf4j
@Service
public class IndexSummaryServiceImpl implements IndexSummaryService {

    @Autowired
    private IndexSummaryMapper indexSummaryMapper;

    @Override
    public List<TaskRunSummary> selectTaskRunSummary(String userId) {
        return indexSummaryMapper.selectTaskRunSummary(userId);
    }

    @Override
    public List<ProjectOnline> selectProjectOnline(String userId) {
        return indexSummaryMapper.selectProjectOnline(userId);
    }

    @Override
    public List<FileSummary> selectFileSummary(String userId) {
        return indexSummaryMapper.selectFileSummary(userId);
    }

    @Override
    public List<ProjectStatusSum> selectProjectStatus(String userId) {
        return indexSummaryMapper.selectProjectStatus(userId);
    }

    @Override
    public MonSumDTO selectProjectSumMonth(String userId, String projectType) {

        List<ProjectSumMonth> list = indexSummaryMapper.selectProjectSumMonth(userId);
        List<ProjectSumMonth> afterFilter = list.stream().filter(o -> o.getProjectType().equals(projectType)).collect(Collectors.toList());
        MonSumDTO monSumDTO = new MonSumDTO();
        int total = 0;

        //计算总和
        for (ProjectSumMonth projectSumMonth : afterFilter) {
            total = total + projectSumMonth.getCnt();
        }
        int avg = afterFilter.size() == 0 ? 0 : total / afterFilter.size();
        monSumDTO.setTotal(total);
        monSumDTO.setAvgMon(avg);

        //补全最近12个月项目明细
        Map<String, List<ProjectSumMonth>> map = afterFilter.stream().collect(Collectors.groupingBy(o -> o.getMon()));
        Map<String, Integer> details = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            calendar.add(Calendar.MONTH, -1);
            String s = year + "-" + String.format("%02d", month);
            List<ProjectSumMonth> list1 = map.get(s);
            details.put(s, list1 == null || list1.size() == 0 ? 0 : list1.get(0).getCnt());
        }
        monSumDTO.setDetails(details);
        return monSumDTO;
    }

    @Override
    public MonSumDTO selectReportMonthSumDetail(String userId) {
        List<MonthSumDetail> list = indexSummaryMapper.selectReportMonthSumDetail(userId);
        return generateMonSumDTO(list);
    }

    @Override
    public MonSumDTO selectDashboardMonthSumDetail(String userId) {
        List<MonthSumDetail> list = indexSummaryMapper.selectDashboardMonthSumDetail(userId);
        return generateMonSumDTO(list);
    }

    @Override
    public MonSumDTO selectBasicModelMonthSumDetail(String userId) {
        List<MonthSumDetail> list = indexSummaryMapper.selectBasicModelMonthSumDetail(userId);

        return generateMonSumDTO(list);
    }

    @Override
    public MonSumDTO selectBusinessModelMonthSumDetail(String userId) {
        List<MonthSumDetail> list = indexSummaryMapper.selectBusinessModelMonthSumDetail(userId);
        return generateMonSumDTO(list);
    }

    @Override
    public List<LastSixItemDTO> selectLastSixBasicModel(String userId) {
        List<DpPortalBasicModel> dpPortalBasicModels = indexSummaryMapper.selectLastSixBasicModel(userId);
        List<LastSixItemDTO> list = new ArrayList<>();
        dpPortalBasicModels.forEach(o -> {
            LastSixItemDTO lastSixItemDTO = new LastSixItemDTO();
            lastSixItemDTO.setItemId(o.getModelName());
            lastSixItemDTO.setItemName(o.getCName());
            list.add(lastSixItemDTO);
        });
        return list;
    }

    @Override
    public List<LastSixItemDTO> selectLastSixBusinessModel(String userId) {
        List<DpPortalBusinessModel> list = indexSummaryMapper.selectLastSixBusinessModel(userId);
        List<LastSixItemDTO> list1 = new ArrayList<>();
        list.forEach(o -> {
            LastSixItemDTO lastSixItemDTO = new LastSixItemDTO();
            lastSixItemDTO.setItemId(o.getModelName());
            lastSixItemDTO.setItemName(o.getChineseName());
            list1.add(lastSixItemDTO);
        });
        return list1;
    }

    @Override
    public List<LastSixItemDTO> selectLastSixPortalDashboard(String userId) {
        List<DpPortalDashboard> list = indexSummaryMapper.selectLastSixPortalDashboard(userId);
        List<LastSixItemDTO> list1 = new ArrayList<>();
        list.forEach(o -> {
            LastSixItemDTO lastSixItemDTO = new LastSixItemDTO();
            lastSixItemDTO.setItemId(o.getDashboardId());
            lastSixItemDTO.setItemName(o.getDashboardName());
            list1.add(lastSixItemDTO);
        });
        return list1;
    }

    @Override
    public List<LastSixItemDTO> selectLastSixFileOutPut(String userId) {
        List<DpPortalFileManager> list = indexSummaryMapper.selectLastSixFileOutPut(userId);
        List<LastSixItemDTO> list1 = new ArrayList<>();
        list.forEach(o -> {
            LastSixItemDTO lastSixItemDTO = new LastSixItemDTO();
            lastSixItemDTO.setItemId(o.getFileId());
            lastSixItemDTO.setItemName(o.getFileName());
            list1.add(lastSixItemDTO);
        });
        return list1;
    }

    @Override
    public List<LastSixItemDTO> selectLastSixFileUpload(String userId) {

        List<DpPortalFileManager> list = indexSummaryMapper.selectLastSixFileUpload(userId);
        List<LastSixItemDTO> list1 = new ArrayList<>();
        list.forEach(o -> {
            LastSixItemDTO lastSixItemDTO = new LastSixItemDTO();
            lastSixItemDTO.setItemId(o.getFileId());
            lastSixItemDTO.setItemName(o.getFileName());
            list1.add(lastSixItemDTO);
        });
        return list1;
    }

    @Override
    public List<LastSixItemDTO> selectLastSixTSJB(String userId) {
        List<DpPortalProject> list = indexSummaryMapper.selectLastSixTSJB(userId);
        List<LastSixItemDTO> list1 = new ArrayList<>();
        list.forEach(o -> {
            LastSixItemDTO lastSixItemDTO = new LastSixItemDTO();
            lastSixItemDTO.setItemId(o.getProjectId());
            lastSixItemDTO.setItemName(o.getProjectName());
            list1.add(lastSixItemDTO);
        });
        return list1;
    }

    @Override
    public List<LastSixItemDTO> selectLastSixJCJB(String userId) {
        List<DpPortalProject> list = indexSummaryMapper.selectLastSixJCJB(userId);
        List<LastSixItemDTO> list1 = new ArrayList<>();
        list.forEach(o -> {
            LastSixItemDTO lastSixItemDTO = new LastSixItemDTO();
            lastSixItemDTO.setItemId(o.getProjectId());
            lastSixItemDTO.setItemName(o.getProjectName());
            list1.add(lastSixItemDTO);
        });
        return list1;
    }

    @Override
    public List<LastSixItemDTO> selectLastSixReportRecord(String userId) {
        List<DpPortalReportRecord> list = indexSummaryMapper.selectLastSixReportRecord(userId);
        List<LastSixItemDTO> list1 = new ArrayList<>();
        list.forEach(o -> {
            LastSixItemDTO lastSixItemDTO = new LastSixItemDTO();
            lastSixItemDTO.setItemId(o.getReportId());
            lastSixItemDTO.setItemName(o.getReportTittle());
            list1.add(lastSixItemDTO);
        });
        return list1;
    }

    private MonSumDTO generateMonSumDTO(List<MonthSumDetail> list) {
        int total = 0;
        int avg = 0;
        Map<String, Integer> details = new HashMap<>();

        for (MonthSumDetail monthSumDetail : list) {
            total = total + monthSumDetail.getCnt();
        }
        avg = list.size() == 0 ? 0 : total / list.size();
        Calendar calendar = Calendar.getInstance();
        Map<String, List<MonthSumDetail>> map = list.stream().collect(Collectors.groupingBy(o -> o.getMon()));

        for (int i = 0; i < 12; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            calendar.add(Calendar.MONTH, -1);
            String s = year + "-" + String.format("%02d", month);
            List<MonthSumDetail> list1 = map.get(s);
            details.put(s, list1 == null || list1.size() == 0 ? 0 : list1.get(0).getCnt());
        }
        MonSumDTO monSumDTO = new MonSumDTO();
        monSumDTO.setTotal(total);
        monSumDTO.setAvgMon(avg);
        monSumDTO.setDetails(details);
        return monSumDTO;
    }

}
