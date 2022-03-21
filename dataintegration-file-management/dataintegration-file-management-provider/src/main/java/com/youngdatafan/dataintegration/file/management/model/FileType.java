package com.youngdatafan.dataintegration.file.management.model;

import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型.
 * Created by echo
 * Content :文件类型
 *
 * @author songxiaolang
 */
@ApiModel("文件类型")
public enum FileType {
    /**
     * Excel类型.
     */
    OFFICE("officeFilePreviewImpl"),
    /**
     * WORD类型.
     */
    PDF("pdfFilePreviewImpl"),

    /**
     * 其他类型.
     */
    OTHER("otherFilePreviewImpl");


    private static final String[] OFFICE_TYPES = {"docx", "wps", "doc", "xls", "xlsx", "ppt", "pptx", "jpeg", "png", "gif", "bmp", "ico", "raw"};

    private static final String PDF_TYPE = "pdf";

    private static final Map<String, FileType> FILE_TYPE_MAPPER = new HashMap<>();

    static {
        for (String office : OFFICE_TYPES) {
            FILE_TYPE_MAPPER.put(office, FileType.OFFICE);
        }
        FILE_TYPE_MAPPER.put(PDF_TYPE, FileType.PDF);
    }

    private final String instanceName;

    FileType(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * 获取instanceName.
     *
     * @return String
     */
    public String getInstanceName() {
        return instanceName;
    }

    private static FileType to(String fileType) {
        return FILE_TYPE_MAPPER.getOrDefault(fileType, OTHER);
    }

    /**
     * 查看文件类型(防止参数中存在.点号或者其他特殊字符，所以先抽取文件名，然后再获取文件类型).
     *
     * @param url url
     * @return 文件类型
     */
    public static FileType typeFromUrl(String url) {
        String nonPramStr = url.substring(0, url.contains("?") ? url.indexOf("?") : url.length());
        String fileName = nonPramStr.substring(nonPramStr.lastIndexOf("/") + 1);
        return typeFromFileName(fileName);
    }

    /**
     * 通过文件名获取文件类型.
     *
     * @param fileName 文件名称
     * @return FileType
     */
    public static FileType typeFromFileName(String fileName) {
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        String lowerCaseFileType = fileType.toLowerCase();
        return FileType.to(lowerCaseFileType);
    }

    /**
     * 获取文件后缀.
     *
     * @param url url
     * @return String
     */
    public static String getType(String url) {
        String fileType = url.substring(url.lastIndexOf(".") + 1);
        return fileType.toLowerCase();
    }

}
