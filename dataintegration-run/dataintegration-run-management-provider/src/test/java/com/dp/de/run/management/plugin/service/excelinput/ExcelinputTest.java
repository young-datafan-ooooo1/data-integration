package com.dp.de.run.management.plugin.service.excelinput;

import com.dp.de.run.management.plugin.service.JsonToXmlTest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.junit.Test;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
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
import org.w3c.dom.Node;

/**
 * @author gavin
 * @since 2020/2/15 12:18 下午
 */
public class ExcelinputTest {

    /**
     * 获取来自头部数据的字段
     */
    @Test
    public void getSheetHeaderColumn() throws Exception {
        KettleEnvironment.init();

        final InputStream resourceAsStream = JsonToXmlTest.class.getClassLoader().getResourceAsStream("1.xml");
        final byte[] data = new byte[resourceAsStream.available()];
        IOUtils.readFully(resourceAsStream, data);
        String xml = new String(data);
        getFields(XMLHandler.getSubNode( XMLHandler.loadXMLString( xml ), "step" ));
    }

    public void getFields(Node node) throws KettleXMLException, KettleFileException {
        RowMetaInterface fields = new RowMeta();

        ExcelInputMeta2 info = new ExcelInputMeta2();
        info.readData(node);

        List<FileObject> fileObjectList = new ArrayList<>();
        for (String fileName : info.getFileName()){
            FileObject fileObject = KettleVFS.getFileObject(fileName,getFileSystemOptions(info));
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
        System.out.println("aaa");

    }


    private void processingWorkbook( RowMetaInterface fields, ExcelInputMeta2 info, KWorkbook workbook )
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

    private FileSystemOptions getFileSystemOptions(ExcelInputMeta2 meta2) {
        StaticUserAuthenticator auth = new StaticUserAuthenticator("username", meta2.getFtpUsername(), meta2.getFtpPassword());
        FileSystemOptions opts = new FileSystemOptions();
        // 账号信息
        try {
            DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        // 被动模式
        FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
        return  opts;
    }
}
