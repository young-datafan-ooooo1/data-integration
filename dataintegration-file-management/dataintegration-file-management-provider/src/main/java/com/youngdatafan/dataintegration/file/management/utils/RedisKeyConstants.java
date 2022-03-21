package com.youngdatafan.dataintegration.file.management.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * redis key命名规范：[业务模块]:[业务key]，长度控制在64以内
 * redis key常量类.
 */

@ApiModel("redis分布式锁常量类")
public class RedisKeyConstants {

    /**
     * 分布式锁后缀.
     */
    @ApiModelProperty
    public static final String ESCAT_FILE_SUFFIX = "esCat-file:";
}
