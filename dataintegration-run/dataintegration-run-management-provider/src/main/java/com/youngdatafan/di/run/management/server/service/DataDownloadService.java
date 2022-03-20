package com.youngdatafan.di.run.management.server.service;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.exception.DpException;
import com.youngdatafan.di.run.management.datasource.PluginRunDataSourceRepository;
import com.youngdatafan.di.run.management.server.vo.PreviewDownloadVO;
import com.youngdatafan.di.run.management.server.vo.StepFieldVO;
import jxl.write.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import com.youngdatafan.dataintegration.core.util.sql.DataSourceWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 预览数据下载
 *
 * @author gavin
 * @since 2020/2/26 11:34 上午
 */
@Slf4j
@Service
public class DataDownloadService {

    private final PluginRunDataSourceRepository dataSourceRepository;

    /**
     * 最大下载数据行数
     */
    @Value("${dp.preview.downloadMaxRow:600000}")
    private int previewDownloadMaxRow;

    @Autowired
    public DataDownloadService(PluginRunDataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * 下载excle
     *
     * @param userId            用户id
     * @param previewDownloadVO PreviewDownloadVO
     * @param response          http响应对象
     */
    public void downloadExcel(String userId, PreviewDownloadVO previewDownloadVO, boolean useNativeSql, HttpServletResponse response) {
        String fileName = previewDownloadVO.getFileName();
        //设置文件名的编码,将不安全的文件名改为UTF-8
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        //兼容不同浏览器的中文乱码问题
        response.setHeader("Content-disposition", "attachment;filename="
                + fileName + ".xlsx;filename*=utf-8''" + fileName + ".xlsx");
        // 定义输出类型
        response.setContentType("application/msexcel");
        response.setCharacterEncoding("UTF-8");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // 生成excel文件并流式写出
            generateExcelAndWriteStream(userId, previewDownloadVO, useNativeSql, outputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DpException(StatusCode.CODE_10010.getCode(), "excel数据下载失败");
        }
    }

    /**
     * 生成excel并流式写出
     *
     * @param outputStream 输出流
     * @throws SQLException   sql执行错误
     * @throws IOException    excel生成错误
     * @throws WriteException 文件输出错误
     */
    private void generateExcelAndWriteStream(String userId, PreviewDownloadVO previewDownloadVO, boolean useNativeSql, OutputStream outputStream) throws SQLException, IOException, WriteException {
        StepFieldVO[] stepFields = previewDownloadVO.getStepFields();

        // 构件查询sql
        StringBuilder querySqlBuilder = new StringBuilder();
        if (useNativeSql) {
            querySqlBuilder.append(previewDownloadVO.getDataQuerySql());
        } else {
            querySqlBuilder.append("select ");
            for (StepFieldVO stepField : stepFields) {
                querySqlBuilder.append(stepField.getFiledName()).append(",");
            }
            querySqlBuilder.setCharAt(querySqlBuilder.length() - 1, ' ');
            querySqlBuilder.append(" from (").append(previewDownloadVO.getDataQuerySql()).append(")");
        }

        final DataSourceWrap dataSourceWrap = dataSourceRepository.getDataSource(userId, previewDownloadVO.getDatasourceId());
        final DatabaseMeta databaseMeta = new DatabaseMeta(""
                , dataSourceWrap.getDatabaseType().name(), "JDBC", "", "", "", "", "");
        final Database database = new Database(databaseMeta);

        ResultSet resultSet = null;
        SXSSFWorkbook book = null;

        try {
            // 获取数据库连接
            Connection connection = dataSourceWrap.getDataSource().getConnection();
            database.setConnection(connection);

            // 创建一个Excel文件对象
            book = new SXSSFWorkbook();

            SXSSFSheet sheet = book.createSheet("sheet1");

            SXSSFRow sxssfRow = sheet.createRow(0);

            // 设置表头，第一行内容
            for (int i = 0; i < stepFields.length; i++) {
                StepFieldVO stepField = stepFields[i];
                // 标题
                SXSSFCell cell = sxssfRow.createCell(i);
                cell.setCellValue(stepField.getFiledCname());
            }
            log.info("查询sql为：{}", querySqlBuilder);

            resultSet = database.openQuery(querySqlBuilder.toString());
            RowMetaInterface rowMeta = null;
            int rowCount = 0;
            int[] fieldsWidth = new int[0];
            while (true) {
                // 最大行限制
                if (rowCount++ > previewDownloadMaxRow) {
                    log.info("已超过下载最大数据量:{}，文件名:{} 用户id:{}", rowCount, previewDownloadVO.getFileName(), userId);
                    break;
                }

                // 获取字段信息
                if (rowMeta == null) {
                    rowMeta = database.getReturnRowMeta();
                    fieldsWidth = new int[rowMeta.size()];
                }
                SXSSFRow sxssfRow1 = sheet.createRow(rowCount);
                // 获取值
                final Object[] row = database.getRow(resultSet);
                if (row == null || row.length == 0) {
                    break;
                }
                final int fieldSize = rowMeta.size();

                for (int i = 0; i < fieldSize; i++) {
                    // 输出excel
                    final ValueMetaInterface valueMeta = rowMeta.getValueMeta(i);

                    writeExcel(sxssfRow1, i, fieldsWidth, valueMeta, stepFields[i].getFiledCname(), row[i]);
                }
            }
            log.info("总行数:{}", rowCount);
            sheet.trackAllColumnsForAutoSizing();
            // 自动调整列大小
            int nFields = fieldsWidth.length;
            for (int i = 0; i < nFields; i++) {
                sheet.autoSizeColumn(i);
//                sheet.setColumnWidth(i, fieldsWidth[i]);
            }

            // 写入数据到目标文件
            book.write(outputStream);
        } catch (Exception e) {
            log.error("excel数据下载失败", e);

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            database.disconnect();

            if (book != null) {
                try {
                    // 关闭
                    book.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 输出excel
     */
    private void writeExcel(WritableSheet sheet, int row, int i
            , Map<String, WritableCellFormat> formats, int[] fieldsWidth
            , ValueMetaInterface vMeta, String cname, Object v) throws WriteException, KettleValueException {
        final String name = vMeta.getName();
        WritableCellFormat cellFormat = formats.get(name);

        // prepare auto size columns
        int vlen = cname.length();
        if (v != null) {
            vlen = Math.max(v.toString().trim().length(), vlen);
        }
        if (vlen > 0 && vlen > fieldsWidth[i]) {
            fieldsWidth[i] = vlen + 3;
        }

        switch (vMeta.getType()) {
            case ValueMetaInterface.TYPE_DATE: {
                if (v != null && vMeta.getDate(v) != null) {
                    if (cellFormat == null) {
                        cellFormat = new WritableCellFormat(DateFormats.FORMAT9);
                        formats.put(name, cellFormat);
                    }
                    DateTime dateTime = new DateTime(i, row, vMeta.getDate(v), cellFormat);
                    sheet.addCell(dateTime);
                }
                break;
            }
            default:
                // fallthrough
                // Output the data value as a string
            case ValueMetaInterface.TYPE_STRING:
            case ValueMetaInterface.TYPE_BOOLEAN:
            case ValueMetaInterface.TYPE_BINARY: {
                if (cellFormat == null) {
                    cellFormat = new WritableCellFormat();
                    formats.put(name, cellFormat);
                }
                if (v != null) {
                    Label label = new Label(i, row, vMeta.getString(v), cellFormat);
                    sheet.addCell(label);
                }
                break;
            }
            case ValueMetaInterface.TYPE_NUMBER:
            case ValueMetaInterface.TYPE_BIGNUMBER:
            case ValueMetaInterface.TYPE_INTEGER: {
                if (v != null) {
                    if (cellFormat == null) {
                        String format = "###,###.00";
                        NumberFormat numberFormat = new NumberFormat(format);
                        cellFormat = new WritableCellFormat(numberFormat);
                        formats.put(name, cellFormat);
                    }
                    jxl.write.Number number =
                            new jxl.write.Number(i, row, vMeta.getNumber(v), cellFormat);
                    sheet.addCell(number);
                }
                break;
            }
        }
    }

    /**
     * 输出excel
     */
    private void writeExcel(SXSSFRow sxssfRow, int i
            , int[] fieldsWidth
            , ValueMetaInterface vMeta, String cname, Object v) throws WriteException, KettleValueException {
        // 创建Excel第一个选项卡对象

        // prepare auto size columns
        int vlen = cname.length();
        if (v != null) {
            vlen = Math.max(v.toString().trim().length(), vlen);
        }
        if (vlen > 0 && vlen > fieldsWidth[i]) {
            fieldsWidth[i] = vlen + 3;
        }

        switch (vMeta.getType()) {
            case ValueMetaInterface.TYPE_DATE: {
                if (v != null && vMeta.getDate(v) != null) {

                    SXSSFCell cell = sxssfRow.createCell(i);
                    cell.setCellValue(DateFormatUtils.format(vMeta.getDate(v), "yyyy-MM-dd HH:mm:ss"));

                }
                break;
            }
            default:
                // fallthrough
                // Output the data value as a string
            case ValueMetaInterface.TYPE_STRING:
                if (v != null) {
                    SXSSFCell cell = sxssfRow.createCell(i);
                    cell.setCellValue(vMeta.getString(v));
                }
                break;
            case ValueMetaInterface.TYPE_BOOLEAN:
                if (v != null) {
                    SXSSFCell cell = sxssfRow.createCell(i);
                    cell.setCellValue(vMeta.getBoolean(v));
                }
                break;
            case ValueMetaInterface.TYPE_BINARY: {

                if (v != null) {
                    SXSSFCell cell = sxssfRow.createCell(i);
                    cell.setCellValue(vMeta.getString(v));

                }
                break;
            }
            case ValueMetaInterface.TYPE_NUMBER:
                if (v != null) {
                    SXSSFCell cell = sxssfRow.createCell(i);
                    cell.setCellValue(vMeta.getNumber(v));
                }
                break;
            case ValueMetaInterface.TYPE_BIGNUMBER:
                if (v != null) {
                    SXSSFCell cell = sxssfRow.createCell(i);
                    cell.setCellValue(vMeta.getBigNumber(v).doubleValue());
                }
                break;
            case ValueMetaInterface.TYPE_INTEGER: {
                if (v != null) {

                    SXSSFCell cell = sxssfRow.createCell(i);
                    cell.setCellValue(vMeta.getNumber(v));
                }
                break;
            }
        }
    }
}
