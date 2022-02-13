package com.youngdatafan.di.run.management.server.bean;

import com.youngdatafan.di.run.management.server.trans.TransExecutor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目执行对象
 *
 * @author gavin
 * @since 2020/2/13 4:47 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectExecutor {

    /**
     * {@link TransExecutor}
     */
    private TransExecutor transExecutor;

    /**
     * 获取该步骤名称的运行状态，多个逗号分隔
     */
    private volatile String stepNameStr;

}
