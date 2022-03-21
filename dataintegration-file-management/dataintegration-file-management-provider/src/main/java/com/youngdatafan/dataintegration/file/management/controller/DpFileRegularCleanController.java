package com.youngdatafan.dataintegration.file.management.controller;

import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.file.management.api.DpFileRegularCleanApi;
import com.youngdatafan.dataintegration.file.management.dto.DpFileRegularCleanDTO;
import com.youngdatafan.dataintegration.file.management.service.DpFileRegularCleanService;
import com.youngdatafan.dataintegration.file.management.vo.DpFileRegularCleanUptVO;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件定时清理设置.
 *
 * @author songxiaolang
 * @since 2022-01-04 14:58
 */
@Api(tags = "文件定时清理")
@RestController
@RequestMapping("/dpFileRegularClean")
public class DpFileRegularCleanController implements DpFileRegularCleanApi {

    private final DpFileRegularCleanService dpFileRegularCleanService;

    @Autowired
    public DpFileRegularCleanController(DpFileRegularCleanService dpFileRegularCleanService) {
        this.dpFileRegularCleanService = dpFileRegularCleanService;
    }

    @Override
    public Result<String, Object> update(String userId, @Valid DpFileRegularCleanUptVO updateVO) {
        dpFileRegularCleanService.update(userId, updateVO);
        return Result.success("修改成功");
    }

    @Override
    public Result<DpFileRegularCleanDTO, Object> get() {
        DpFileRegularCleanDTO dpFileRegularCleanDTO = dpFileRegularCleanService.get();
        return Result.success(dpFileRegularCleanDTO);
    }

    @Override
    public Result<String, Object> immediately(Integer effectiveDays, Integer isUseBusiness) {
        DpFileRegularCleanDTO dpFileRegularCleanDTO = new DpFileRegularCleanDTO();
        dpFileRegularCleanDTO.setIsUseBusiness(isUseBusiness);
        dpFileRegularCleanDTO.setEffectiveDays(effectiveDays);
        dpFileRegularCleanService.timingCleanFile(dpFileRegularCleanDTO);
        return Result.success("清理成功");
    }
}
