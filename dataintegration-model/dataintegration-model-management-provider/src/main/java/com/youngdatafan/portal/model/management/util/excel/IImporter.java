package com.youngdatafan.portal.model.management.util.excel;


import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.portal.model.management.util.model.ExcelColumns;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface IImporter {
    Result importBasicModel(String userId, XSSFWorkbook workbook, String modelType);

    List<ExcelRejectCell> importBasicModelMeta(XSSFWorkbook workbook);

    Result importBusinessModel(String userId, XSSFWorkbook workbook);

    List<ExcelRejectCell> importBusinessModelMeta(XSSFWorkbook workbook);

    ExcelUploadResult parseExcel(String tableId, XSSFWorkbook workbook, List<ExcelColumns> excelColumns);
}
