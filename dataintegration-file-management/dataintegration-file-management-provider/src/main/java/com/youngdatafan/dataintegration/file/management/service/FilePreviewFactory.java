package com.youngdatafan.dataintegration.file.management.service;

import com.youngdatafan.dataintegration.file.management.model.FileType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * FilePreviewFactory.
 *
 * @author songxiaolang
 */
@Service
public class FilePreviewFactory {

    private final ApplicationContext context;

    public FilePreviewFactory(ApplicationContext context) {
        this.context = context;
    }

    /**
     * 通过文件类型获取实现类.
     *
     * @param type 文件类型
     * @return FilePreview
     */
    public FilePreview get(FileType type) {
        return context.getBean(type.getInstanceName(), FilePreview.class);
    }
}
