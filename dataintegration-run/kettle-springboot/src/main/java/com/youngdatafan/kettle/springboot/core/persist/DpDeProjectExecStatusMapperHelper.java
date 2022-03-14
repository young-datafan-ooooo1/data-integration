package com.youngdatafan.kettle.springboot.core.persist;

import com.youngdatafan.kettle.springboot.core.mapper.DpDeProjectExecStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author gavin
 * @since 2020/2/20 5:24 下午
 */
@Component
public class DpDeProjectExecStatusMapperHelper {

    private static DpDeProjectExecStatusMapperHelper INSTANCE;

    private final DpDeProjectExecStatusMapper projectExecStatusMapper;

    @Autowired
    public DpDeProjectExecStatusMapperHelper(DpDeProjectExecStatusMapper projectExecStatusMapper) {
        this.projectExecStatusMapper = projectExecStatusMapper;
    }

    public DpDeProjectExecStatusMapper getProjectExecStatusMapper() {
        return projectExecStatusMapper;
    }

    @PostConstruct
    public void init() {
        DpDeProjectExecStatusMapperHelper.INSTANCE = this;
    }

    public static DpDeProjectExecStatusMapper getInstance() {
        return DpDeProjectExecStatusMapperHelper.INSTANCE.getProjectExecStatusMapper();
    }

}
