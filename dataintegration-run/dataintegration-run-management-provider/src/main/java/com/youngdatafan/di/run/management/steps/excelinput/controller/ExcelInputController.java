package com.youngdatafan.di.run.management.steps.excelinput.controller;

import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.encryption.DefaultEncryptionUtils;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.youngdatafan.di.run.management.steps.excelinput.api.ExcelInputApi;
import com.youngdatafan.di.run.management.steps.excelinput.vo.SheetFieldVO;
import com.github.vfss3.FileServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.spreadsheet.KCell;
import org.pentaho.di.core.spreadsheet.KCellType;
import org.pentaho.di.core.spreadsheet.KSheet;
import org.pentaho.di.core.spreadsheet.KWorkbook;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.plugins.excel.input.step.ExcelInputMeta2;
import org.pentaho.di.plugins.excel.input.step.WorkbookFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author echo_
 */
@Slf4j
@RestController
@RequestMapping("/excelinput")
public class ExcelInputController implements ExcelInputApi {

    @Override
    public Result<List<SheetFieldVO>, Object> getSheetHeaderColumn(String userId, String json) throws KettleXMLException, IOException, KettleFileException {
        if(StringUtils.isBlank(json)){
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "step内容不能为空");
        }
        JSONLinkedObject jsonLinkedObject = new JSONLinkedObject(StringEscapeUtils.unescapeXml(json));
        String xml = XML.toString(jsonLinkedObject);
        List<SheetFieldVO> sheetFieldVOS = new ArrayList<>();
        RowMetaInterface fields = new RowMeta();
        getFields(XMLHandler.getSubNode( XMLHandler.loadXMLString( xml ), "step" ),fields);
        if ( fields.size() > 0 ) {
            for ( int j = 0; j < fields.size(); j++ ) {
                ValueMetaInterface field = fields.getValueMeta( j );
                SheetFieldVO sheetFieldVO = new SheetFieldVO();
                sheetFieldVO.setName(field.getName());
                sheetFieldVO.setType(field.getTypeDesc());
                sheetFieldVO.setCurrencySymbol(field.getCurrencySymbol());
                sheetFieldVO.setDecimalSymbol(field.getDecimalSymbol());
                sheetFieldVO.setGroupingSymbol(field.getGroupingSymbol());
                sheetFieldVO.setLength(field.getLength());
                sheetFieldVO.setPrecision(field.getPrecision());
                sheetFieldVO.setTrimType(field.getTrimType());
                sheetFieldVOS.add(sheetFieldVO);
            }
        }
        return Result.success(sheetFieldVOS);
    }

    @Override
    public Result<List<String>, Object> getSheetName(String userId, String json) throws KettleXMLException, IOException, KettleFileException {
        if(StringUtils.isBlank(json)){
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "step内容不能为空");
        }
        JSONLinkedObject jsonLinkedObject = new JSONLinkedObject(StringEscapeUtils.unescapeXml(json));
        String xml = XML.toString(jsonLinkedObject);
        ExcelInputMeta2 info = new ExcelInputMeta2();
        info.readData(XMLHandler.getSubNode( XMLHandler.loadXMLString( xml ), "step" ));
        List<String> sheetnames = new ArrayList<>();
        List<FileObject> fileObjectList = new ArrayList<>();
        for (String fileName : info.getFileName()){
            FileObject fileObject = KettleVFS.getFileObject(fileName,FileServerConfig.getFileSystemOptions(info.getFileServerType(),info.getFtpUsername(), DefaultEncryptionUtils.decrypt(info.getFtpPassword())));
            fileObjectList.add(fileObject);
        }
        for ( FileObject fileObject : fileObjectList) {
            try {
                KWorkbook workbook =
                        WorkbookFactory.getWorkbook( info.getSpreadSheetType(), fileObject.getContent().getInputStream(), info
                                .getEncoding() );
                int nrSheets = workbook.getNumberOfSheets();
                for ( int j = 0; j < nrSheets; j++ ) {
                    KSheet sheet = workbook.getSheet( j );
                    String sheetname = sheet.getName();

                    if ( Const.indexOfString( sheetname, sheetnames ) < 0 ) {
                        sheetnames.add( sheetname );
                    }
                }

                workbook.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        return Result.success(sheetnames);
    }

    public void getFields(Node node,RowMetaInterface fields) throws KettleXMLException, KettleFileException, FileSystemException {

        ExcelInputMeta2 info = new ExcelInputMeta2();
        info.readData(node);

        List<FileObject> fileObjectList = new ArrayList<>();
        for (String fileName : info.getFileName()){
            FileObject fileObject = KettleVFS.getFileObject(fileName, FileServerConfig.getFileSystemOptions(info.getFileServerType(),info.getFtpUsername(),DefaultEncryptionUtils.decrypt(info.getFtpPassword())));
            fileObjectList.add(fileObject);
        }
        for ( FileObject file : fileObjectList) {

            try {
                KWorkbook workbook =
                        WorkbookFactory.getWorkbook( info.getSpreadSheetType(), file.getContent().getInputStream(), info
                                .getEncoding() );
                processingWorkbook( fields, info, workbook );
                workbook.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }


    public void processingWorkbook( RowMetaInterface fields, ExcelInputMeta2 info, KWorkbook workbook )
            throws KettlePluginException {
        int nrSheets = workbook.getNumberOfSheets();
        for ( int j = 0; j < nrSheets; j++ ) {
            KSheet sheet = workbook.getSheet( j );

            // See if it's a selected sheet:
            int sheetIndex;
            if ( info.readAllSheets() ) {
                sheetIndex = 0;
            } else {
                sheetIndex = Const.indexOfString( sheet.getName(), info.getSheetName() );
            }
            if ( sheetIndex >= 0 ) {
                // We suppose it's the complete range we're looking for...
                //
                int rownr = 0;
                int startcol = 0;

                if ( info.readAllSheets() ) {
                    if ( info.getStartColumn().length == 1 ) {
                        startcol = info.getStartColumn()[ 0 ];
                    }
                    if ( info.getStartRow().length == 1 ) {
                        rownr = info.getStartRow()[ 0 ];
                    }
                } else {
                    rownr = info.getStartRow()[ sheetIndex ];
                    startcol = info.getStartColumn()[ sheetIndex ];
                }

                boolean stop = false;
                for ( int colnr = startcol; !stop; colnr++ ) {
                    try {
                        String fieldname = null;
                        int fieldtype = ValueMetaInterface.TYPE_NONE;

                        KCell cell = sheet.getCell( colnr, rownr );
                        if ( cell == null ) {
                            stop = true;
                        } else {
                            if ( cell.getType() != KCellType.EMPTY ) {
                                // We found a field.
                                fieldname = cell.getContents();
                            }

                            // System.out.println("Fieldname = "+fieldname);

                            KCell below = sheet.getCell( colnr, rownr + 1 );

                            if ( below != null ) {
                                if ( below.getType() == KCellType.BOOLEAN ) {
                                    fieldtype = ValueMetaInterface.TYPE_BOOLEAN;
                                } else if ( below.getType() == KCellType.DATE ) {
                                    fieldtype = ValueMetaInterface.TYPE_DATE;
                                } else if ( below.getType() == KCellType.LABEL ) {
                                    fieldtype = ValueMetaInterface.TYPE_STRING;
                                } else if ( below.getType() == KCellType.NUMBER ) {
                                    fieldtype = ValueMetaInterface.TYPE_NUMBER;
                                } else {
                                    fieldtype = ValueMetaInterface.TYPE_STRING;
                                }
                            } else {
                                fieldtype = ValueMetaInterface.TYPE_STRING;
                            }

                            if ( Utils.isEmpty( fieldname ) ) {
                                stop = true;
                            } else {
                                if ( fieldtype != ValueMetaInterface.TYPE_NONE ) {
                                    ValueMetaInterface field = ValueMetaFactory.createValueMeta( fieldname, fieldtype );
                                    fields.addValueMeta( field );
                                }
                            }
                        }
                    } catch ( ArrayIndexOutOfBoundsException aioobe ) {
                        // System.out.println("index out of bounds at column "+colnr+" : "+aioobe.toString());
                        stop = true;
                    }
                }
            }
        }
    }
}
