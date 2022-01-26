package com.youngdatafan.portal.model.management.util.excel;

import com.youngdatafan.portal.model.management.util.model.ExcelColumns;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.concurrent.Future;

/**
 * ------
 *
 * @author VincentHsing|e.vincent.hsing@gmail.com
 * @date 2018-08-17 16:46
 */
public interface IParser {
    Future<ObjectNode> parseRow(String tableId, Row row, List<ExcelRejectCell> rejectCells, List<ExcelColumns> excelColumns);
}
