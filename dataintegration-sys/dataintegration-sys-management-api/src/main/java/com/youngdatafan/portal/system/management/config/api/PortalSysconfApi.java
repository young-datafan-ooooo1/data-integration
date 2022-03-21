package com.youngdatafan.portal.system.management.config.api;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.system.management.config.dto.PortalSysconfDTO;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 租户环境.
 */
public interface PortalSysconfApi {

    /**
     * 获取租户环境参数信息.
     *
     * @param paramType paramType
     * @return list
     */
    @ApiOperation(value = "获取租户环境参数信息", notes = "获取租户环境参数信息", produces = "application/json")
    @GetMapping(value = "/getPortalSysconfByType")
    Result<List<PortalSysconfDTO>, Object> getPortalSysconfByType(@Param("paramType") String paramType);
}
