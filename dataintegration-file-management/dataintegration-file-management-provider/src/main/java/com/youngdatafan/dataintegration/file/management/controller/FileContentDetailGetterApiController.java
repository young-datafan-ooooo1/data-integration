package com.youngdatafan.dataintegration.file.management.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3Object;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.file.management.api.FileContentDetailGetterApi;
import com.youngdatafan.dataintegration.file.management.dto.FieldInfoDTO;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileManager;
import com.youngdatafan.dataintegration.file.management.service.DpPortalFileManagerService;
import com.youngdatafan.dataintegration.file.management.service.FileSystemManagerService;
import com.youngdatafan.dataintegration.file.management.vo.FileSheetVO;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

/**
 * 文件详细内容获取.
 *
 * @Author: jeremychen
 * @Descripition:
 * @Date 2020/4/15 14：04
 */
@Slf4j
@RestController
@RequestMapping("/fileContentDetailGetter")
public class FileContentDetailGetterApiController implements FileContentDetailGetterApi {

    @Autowired
    private DpPortalFileManagerService dpPortalFileManagerService;

    @Autowired
    private FileSystemManagerService fileSystemManagerService;

    @Override
    public Result<List<String>, Object> getSheetList(String roleCode, String userId, String fileId) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
        if (dpPortalFileManager == null) {
            return Result.fail(StatusCode.CODE_10010, null, "文件未找到");
        }
        if (!dpPortalFileManager.getFileType().equals("xls") && !dpPortalFileManager.getFileType().equals("xlsx")) {
            return Result.fail(StatusCode.CODE_10010, null, "不是excel文件");
        }
        InputStream fileObject = fileSystemManagerService.getFileObject(dpPortalFileManager.getFilePath());
        Workbook workbook = null;

        try {
            List<String> sheetList = new ArrayList<>();
            if (fileObject == null ) {
                return Result.fail(StatusCode.CODE_10010, null, "文件不存在");
            }

            if (dpPortalFileManager.getFileType().equalsIgnoreCase("xls")) {
                workbook = new HSSFWorkbook(fileObject);
            } else if (dpPortalFileManager.getFileType().equalsIgnoreCase("xlsx")) {
                workbook = new XSSFWorkbook(fileObject);
            }

            int sheetCnt = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetCnt; i++) {
                sheetList.add(workbook.getSheetName(i));
            }
            return Result.success(sheetList);

        } catch (AmazonServiceException | IOException e) {
            log.error(e.getMessage(), e);
            return Result.fail(StatusCode.CODE_10010, null, "获取文件信息异常");

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fileObject != null) {
                try {
                    fileObject.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Result<List<FieldInfoDTO>, Object> fileHeader(String roleCode, String userId, String fileId, String sheetName, String split) {
        DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
        if (dpPortalFileManager == null) {
            return Result.fail(StatusCode.CODE_10010, null, "文件未找到");
        }

        InputStream fileObject = fileSystemManagerService.getFileObject(dpPortalFileManager.getFilePath());
        Workbook workbook = null;

        try {
            List<FieldInfoDTO> fieldInfoDTOS = new ArrayList<>();
            if (fileObject == null ) {
                return Result.fail(StatusCode.CODE_10010, null, "文件不存在");
            }

            if (dpPortalFileManager.getFileType().equalsIgnoreCase("xls") || dpPortalFileManager.getFileType().equalsIgnoreCase("xlsx")) {
                if (dpPortalFileManager.getFileType().equalsIgnoreCase("xls")) {
                    workbook = new HSSFWorkbook(fileObject);
                } else if (dpPortalFileManager.getFileType().equalsIgnoreCase("xlsx")) {
                    workbook = new XSSFWorkbook(fileObject);
                }
                Sheet sheet = workbook.getSheet(sheetName);
                Row row = sheet.getRow(0);
                Row row1 = sheet.getRow(1);
                if (row != null && row1 != null) {
                    Iterator<Cell> iterator = row.cellIterator();
                    while (iterator.hasNext()) {
                        FieldInfoDTO fieldInfoDTO = new FieldInfoDTO();
                        Cell cell = iterator.next();
                        int index = cell.getColumnIndex();
                        fieldInfoDTO.setFieldName(cell.getStringCellValue());

                        Cell cell1 = row1.getCell(index);
                        if (cell1 != null) {
                            CellType cellType = cell1.getCellTypeEnum();
                            switch (cellType) {
                                case _NONE:
                                    fieldInfoDTO.setFileType("String");
                                    break;

                                case STRING:
                                    fieldInfoDTO.setFileType("String");
                                    break;

                                case NUMERIC:
                                    fieldInfoDTO.setFileType("Number");
                                    break;

                                case BOOLEAN:
                                    fieldInfoDTO.setFileType("Boolean");
                                    break;

                                case BLANK:
                                    fieldInfoDTO.setFileType("String");
                                    break;

                                case FORMULA:
                                    fieldInfoDTO.setFileType("String");
                                    break;

                                case ERROR:
                                    fieldInfoDTO.setFileType("String");
                                    break;
                                default:
                                    fieldInfoDTO.setFileType("String");

                            }
                        } else {
                            fieldInfoDTO.setFileType("String");
                        }
                        fieldInfoDTOS.add(fieldInfoDTO);
                    }

                }
            } else {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileObject));
                String firstLine = bufferedReader.readLine();
                String[] fields = firstLine.split(split);
                for (int i = 0; i < fields.length; i++) {
                    FieldInfoDTO fieldInfoDTO = new FieldInfoDTO();
                    fieldInfoDTO.setFieldName(fields[i]);
                    fieldInfoDTO.setFileType("String");
                    fieldInfoDTOS.add(fieldInfoDTO);
                }
            }

            return Result.success(fieldInfoDTOS);
        } catch (AmazonServiceException | IOException e) {
            log.error(e.getMessage(), e);
            return Result.fail(StatusCode.CODE_10010, null, "获取文件信息异常");
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fileObject != null) {
                try {
                    fileObject.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Result<Set<String>, Object> getMoreSheetList(String roleCode, String userId, FileSheetVO fileSheetVO) {
        Set<String> sheetList = new HashSet<>();

        if (fileSheetVO == null || fileSheetVO.getFileId() == null || fileSheetVO.getFileId().size() == 0) {
            return Result.fail(StatusCode.CODE_10010, null, "请选择文件");
        }
        try {
            for (String fileId : fileSheetVO.getFileId()) {
                DpPortalFileManager dpPortalFileManager = dpPortalFileManagerService.selectByFileId(roleCode, fileId, userId);
                if (dpPortalFileManager == null) {
                    return Result.fail(StatusCode.CODE_10010, null, "文件未找到");
                }
                if (!dpPortalFileManager.getFileType().equals("xls") && !dpPortalFileManager.getFileType().equals("xlsx")) {
                    return Result.fail(StatusCode.CODE_10010, null, "不是excel文件");
                }

                InputStream fileObject = fileSystemManagerService.getFileObject(dpPortalFileManager.getFilePath());
                Workbook workbook = null;

                try {
                    if (fileObject == null ) {
                        return Result.fail(StatusCode.CODE_10010, null, "文件不存在");
                    }

                    if (dpPortalFileManager.getFileType().equalsIgnoreCase("xls")) {
                        workbook = new HSSFWorkbook(fileObject);
                    } else if (dpPortalFileManager.getFileType().equalsIgnoreCase("xlsx")) {
                        workbook = new XSSFWorkbook(fileObject);
                    }

                    int sheetCnt = workbook.getNumberOfSheets();
                    for (int i = 0; i < sheetCnt; i++) {
                        sheetList.add(workbook.getSheetName(i));
                    }
                } finally {
                    if (workbook != null) {
                        workbook.close();
                    }
                    if (fileObject != null) {
                        fileObject.close();
                    }
                }
            }
            return Result.success(sheetList);
        } catch (AmazonServiceException | IOException e) {
            log.error(e.getMessage(), e);
            return Result.fail(StatusCode.CODE_10010, null, "获取文件信息异常");
        }
    }
}
