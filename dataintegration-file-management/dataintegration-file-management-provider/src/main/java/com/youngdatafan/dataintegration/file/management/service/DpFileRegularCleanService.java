package com.youngdatafan.dataintegration.file.management.service;

import com.youngdatafan.dataintegration.file.management.dto.DpFileRegularCleanDTO;
import com.youngdatafan.dataintegration.file.management.vo.DpFileRegularCleanUptVO;
import java.util.Date;

/**
 * 文件定时清理service.
 *
 * @author songxiaolang
 * @since 2022-01-04 14:59
 */
public interface DpFileRegularCleanService {

    /**
     * 修改文件定时清理设置.
     *
     * @param userId   用户id
     * @param updateVO updateVO
     * @return int
     */
    int update(String userId, DpFileRegularCleanUptVO updateVO);

    /**
     * 获取文件定时清理设置.
     *
     * @return DpFileRegularCleanDTO
     */
    DpFileRegularCleanDTO get();

    /**
     * 获取文件有效天数.
     *
     * @param sysTemEffectiveDay   系统全局有效时间
     * @param businessEffectiveDay 业务文件有效时间
     * @param fileUpdateDay        文件最后修改时间
     * @return int
     */
    int getFileEffectiveDays(Integer sysTemEffectiveDay, Integer businessEffectiveDay, Date fileUpdateDay);

    /**
     * 定时清理文件.
     *
     * @param dpFileRegularCleanDTO dpFileRegularCleanDTO
     */
    void timingCleanFile(DpFileRegularCleanDTO dpFileRegularCleanDTO);

    /**
     * 检查文件定时清理设置表.
     */
    void inspectFileRegularCleanTable();

}
