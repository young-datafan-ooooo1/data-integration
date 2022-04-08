package com.youngdatafan.dataintegration.file.management.dto;

import java.util.Date;
import lombok.Data;

/**
 * 文件信息.
 *
 * @author songxiaolang
 * @since 2022-03-30 15:26
 */
@Data
public class FileSummary {
    /**
     * 最后修改时间.
     */
    private Date lastModified;

    /**
     * 文件大小.
     */
    private long size;

    /**
     * 文件key.
     */
    private String key;
}
