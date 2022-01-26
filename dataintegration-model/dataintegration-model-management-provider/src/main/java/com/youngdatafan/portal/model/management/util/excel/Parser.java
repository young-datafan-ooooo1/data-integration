package com.youngdatafan.portal.model.management.util.excel;

import com.youngdatafan.portal.model.management.util.model.ExcelColumns;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Pattern;


/**
 * ------
 *
 * @author VincentHsing|e.vincent.hsing@gmail.com
 * @date 2018-08-17 16:46
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class Parser implements IParser {
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);


    private static final String DATA_TYPE_STRING = "STRING";
    private static final String DATA_TYPE_NUMBER = "NUMBER";
    private static final String DATA_TYPE_INTEGER = "INTEGER";
    private static final String DATA_TYPE_DATE = "DATE";

    @Override
    @Async
    public Future<ObjectNode> parseRow(String tableId, Row row, List<ExcelRejectCell> rejectCells, List<ExcelColumns> excelColumns) {
        DecimalFormat df = new DecimalFormat("###.####");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        boolean isRowBreak = false;
        for (ExcelColumns excelColumn : excelColumns) {
            System.out.println("table" + excelColumn.getColumnDesc());
            int realIndex = excelColumn.getColumnIndex() - 1;
            String cellContent = "";
            Cell cell = row.getCell(realIndex);
            if (null == cell) {
                continue;
            }
            switch (cell.getCellTypeEnum()) {
                case STRING:
                    cellContent = cell.getStringCellValue();
                    break;
                case NUMERIC:
//                    if (DATA_TYPE_INTEGER.equals(excelColumns.getDataType())) {
//                        cellContent = String.valueOf((int) cell.getNumericCellValue());
//                    } else {
//                        cellContent = String.valueOf(cell.getNumericCellValue());
//                    }
                    cellContent = df.format(cell.getNumericCellValue());
                    break;
                case BLANK:
                    break;
                default:
                    ExcelRejectCell excelRejectCell = new ExcelRejectCell();
                    excelRejectCell.setColumn(excelColumn.getColumnIndex());
                    excelRejectCell.setRow(row.getRowNum() + 1);
                    excelRejectCell.setMsg("单元格格式只接受字符和数值,当前为:" + cell.getCellTypeEnum().name());
                    rejectCells.add(excelRejectCell);
                    isRowBreak = true;
                    continue;
            }
            if (!StringUtils.isEmpty(excelColumn.getRegexpCheck())) {
                Pattern pattern = Pattern.compile(excelColumn.getRegexpCheck());
                if (!pattern.matcher(cellContent).find()) {
                    ExcelRejectCell excelRejectCell = new ExcelRejectCell();
                    excelRejectCell.setColumn(excelColumn.getColumnIndex());
                    excelRejectCell.setRow(row.getRowNum() + 1);
                    excelRejectCell.setMsg("不满足正则规则:" + excelColumn.getRegexpCheck() + " ,单元格内容:" + cellContent);
                    rejectCells.add(excelRejectCell);
                    isRowBreak = true;
                    continue;
                }
            }

            objectNode.put(excelColumn.getColumnName(), cellContent);
        }
        if (isRowBreak) {
            return null;
        }
        return new AsyncResult<>(objectNode);
    }
}
