package com.youngdatafan.dataintegration.file.management.utils;

import io.swagger.annotations.ApiModel;

/**
 * 文件工具类.
 *
 * @author liuhao
 * @since 2022/1/21 10:47
 */

@ApiModel("文件工具类")
public class FileUtil {

    /**
     * 将文件从字节大小转换成KB,MB或GB.
     *
     * @param size 文件大小, 以kb为单位
     * @return 转换后文件的大小
     */
    public static String calculateFileSize(Long size) {
        if (size > 1024 * 1024 * 1024) {
            return size / 1024 * 1024 * 1024 + "GB";
        } else if (size > 1024 * 1024) {
            return size / 1024 * 1024 + "MB";
        } else if (size > 1024) {
            return size / 1024 + "KB";
        } else {
            return size + "B";
        }
    }
}
