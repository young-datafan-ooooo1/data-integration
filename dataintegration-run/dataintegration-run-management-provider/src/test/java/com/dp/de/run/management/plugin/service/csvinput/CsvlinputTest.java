package com.dp.de.run.management.plugin.service.csvinput;

import com.csvreader.CsvReader;
import com.youngdatafan.dataintegration.core.util.encryption.DefaultEncryptionUtils;
import com.dp.de.run.management.plugin.service.JsonToXmlTest;
import com.youngdatafan.di.run.management.steps.csvinput.vo.FieldVO;
import com.youngdatafan.di.run.management.steps.utils.TypeCastUtils;
import com.github.vfss3.FileServerConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.junit.Test;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.plugins.csv.input.step.CsvInputMeta2;
import org.pentaho.di.plugins.textfile.input.step.EncodingType;
import org.pentaho.di.plugins.textfile.input.step.TextFileInputUtils;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author gavin
 * @since 2020/2/15 12:18 下午
 */
public class CsvlinputTest {

    /**
     * 获取字段
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

    public void getFields(Node node) throws KettleException, IOException {
        List<FieldVO> fieldVOList = doScan(node);
        System.out.println(fieldVOList);
    }

    private List<FieldVO> doScan(Node node) throws KettleException, IOException {
        List<FieldVO> fieldVOList = new ArrayList<>();
        CsvInputMeta2 info = new CsvInputMeta2();
        String fileName = info.getFilename().substring(info.getFilename().lastIndexOf("/")+1);
        info.readData(node);
        InputStream inputStream = getInputStream( info );
        InputStreamReader reader = getReader( info, inputStream );
        try {
            CsvReader csvReader = new CsvReader(reader);
            String[] head = null;
            int i=1;
            //循环读取每一个文件的数据
            while (csvReader.readRecord()) {
                if(i==1){
                    head = csvReader.getRawRecord().split(info.getSeparator());
                } else if (i > 2) {
                    break;
                } else {
                    for (int z = 0; z < head.length; z++) {
                        FieldVO fieldVO = new FieldVO();
                        fieldVO.setName(fileName);
                        TypeCastUtils.typeCast(csvReader.get(z),fieldVO);
                        fieldVOList.add(fieldVO);
                    }
                }
                i++;
            }
        }finally {
            if(inputStream!=null){
                inputStream.close();
            }
            if(reader!=null){
                reader.close();
            }
        }
        return  fieldVOList;

    }

    public String typeCast(String str) {
        if(str.matches("^-?\\d+$")){
            return "Integer";
        }else if(str.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$")){
            return "Decimal";
        }else if(str.matches("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$")){
            return "Date";
        }else if(str.matches("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1]?[0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$")){
            return "Datetime";
        }
        return "String";
    }

    public String[] getFieldNames( final CsvInputMeta2 meta ) {
        String[] fieldNames = new String[] {};
        final InputStream inputStream = getInputStream( meta );
        final InputStreamReader reader = getReader( meta, inputStream );
        try {
            fieldNames = getFieldNamesImpl( reader, meta );
        } catch ( final KettleException e ) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch ( Exception e ) {
                // Ignore close errors
            }
        }
        return fieldNames;
    }

    public String[] getFieldNamesImpl( final InputStreamReader reader, final CsvInputMeta2 meta )
            throws KettleException {

        String[] fieldNames = new String[] {};
        if ( reader == null || meta == null ) {
            return fieldNames;
        }
        final String delimiter = meta.getDelimiter();
        final String enclosure = meta.getEnclosure();

        final EncodingType encodingType = EncodingType.guessEncodingType( reader.getEncoding() );

        // Read a line of data to determine the number of rows...
        final String line = TextFileInputUtils.getLine( null, reader, encodingType, 1,
                new StringBuilder( 1000 ));
        if ( !StringUtils.isBlank( line ) ) {
            fieldNames = guessStringsFromLine( line, delimiter, enclosure,
                    meta.getEscapeCharacter() );
        }
        if ( Utils.isEmpty( fieldNames ) ) {
            return fieldNames;
        }

        // Massage field names
        for ( int i = 0; i < fieldNames.length; i++ ) {
            fieldNames[ i ] = Const.trim( fieldNames[ i ] );
            if ( !meta.hasHeader() ) {
                final DecimalFormat df = new DecimalFormat( "000" );
                fieldNames[ i ] = "Field_" + df.format( i );
            } else if ( !Utils.isEmpty( meta.getEnclosure() ) && fieldNames[ i ].startsWith( meta.getEnclosure() )
                    && fieldNames[ i ].endsWith( meta.getEnclosure() ) && fieldNames[ i ].length() > 1 ) {
                fieldNames[ i ] = fieldNames[ i ].substring( 1, fieldNames[ i ].length() - 1 );
            }
            // trim again, now that the enclosure characters have been removed
            fieldNames[ i ] = Const.trim( fieldNames[ i ] );
        }
        return fieldNames;
    }

    public InputStream getInputStream( final CsvInputMeta2 meta ) {
        InputStream inputStream = null;
        try {
            FileObject fileObject = KettleVFS.getFileObject(meta.getFilename(), FileServerConfig.getFileSystemOptions(meta.getFileServerType(),meta.getFtpUsername(), DefaultEncryptionUtils.decrypt(meta.getFtpPassword())));
            inputStream = fileObject.getContent().getInputStream();
        } catch ( final Exception e ) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public InputStreamReader getReader( final CsvInputMeta2 meta, final InputStream inputStream ) {
        InputStreamReader reader = null;
        try {
            String realEncoding = meta.getEncoding();
            if ( Utils.isEmpty( realEncoding ) ) {
                reader = new InputStreamReader( inputStream );
            } else {
                reader = new InputStreamReader( inputStream, realEncoding );
            }
        } catch ( final Exception e ) {
            e.printStackTrace();
        }
        return reader;
    }

    public static String[] guessStringsFromLine(String line, String delimiter,
                                                String enclosure, String escapeCharacter ) throws KettleException {
        List<String> strings = new ArrayList<>();

        String pol; // piece of line

        try {
            if ( line == null ) {
                return null;
            }

            // Split string in pieces, only for CSV!

            int pos = 0;
            int length = line.length();
            boolean dencl = false;

            int len_encl = ( enclosure == null ? 0 : enclosure.length() );
            int len_esc = ( escapeCharacter == null ? 0 : escapeCharacter.length() );

            while ( pos < length ) {
                int from = pos;
                int next;

                boolean encl_found;
                boolean contains_escaped_enclosures = false;
                boolean contains_escaped_separators = false;

                // Is the field beginning with an enclosure?
                // "aa;aa";123;"aaa-aaa";000;...
                if ( len_encl > 0 && line.substring( from, from + len_encl ).equalsIgnoreCase( enclosure ) ) {
                    encl_found = true;
                    int p = from + len_encl;

                    boolean is_enclosure =
                            len_encl > 0
                                    && p + len_encl < length && line.substring( p, p + len_encl ).equalsIgnoreCase( enclosure );
                    boolean is_escape =
                            len_esc > 0
                                    && p + len_esc < length && line.substring( p, p + len_esc ).equalsIgnoreCase( escapeCharacter );

                    boolean enclosure_after = false;

                    // Is it really an enclosure? See if it's not repeated twice or escaped!
                    if ( ( is_enclosure || is_escape ) && p < length - 1 ) {
                        String strnext = line.substring( p + len_encl, p + 2 * len_encl );
                        if ( strnext.equalsIgnoreCase( enclosure ) ) {
                            p++;
                            enclosure_after = true;
                            dencl = true;

                            // Remember to replace them later on!
                            if ( is_escape ) {
                                contains_escaped_enclosures = true;
                            }
                        }
                    }

                    // Look for a closing enclosure!
                    while ( ( !is_enclosure || enclosure_after ) && p < line.length() ) {
                        p++;
                        enclosure_after = false;
                        is_enclosure =
                                len_encl > 0 && p + len_encl < length && line.substring( p, p + len_encl ).equals( enclosure );
                        is_escape =
                                len_esc > 0 && p + len_esc < length && line.substring( p, p + len_esc ).equals( escapeCharacter );

                        // Is it really an enclosure? See if it's not repeated twice or escaped!
                        if ( ( is_enclosure || is_escape ) && p < length - 1 ) {
                            String strnext = line.substring( p + len_encl, p + 2 * len_encl );
                            if ( strnext.equals( enclosure ) ) {
                                p++;
                                enclosure_after = true;
                                dencl = true;

                                // Remember to replace them later on!
                                if ( is_escape ) {
                                    contains_escaped_enclosures = true; // remember
                                }
                            }
                        }
                    }

                    if ( p >= length ) {
                        next = p;
                    } else {
                        next = p + len_encl;
                    }
                } else {
                    encl_found = false;
                    boolean found = false;
                    int startpoint = from;
                    do {
                        next = line.indexOf( delimiter, startpoint );

                        // See if this position is preceded by an escape character.
                        if ( len_esc > 0 && next - len_esc > 0 ) {
                            String before = line.substring( next - len_esc, next );

                            if ( escapeCharacter != null && escapeCharacter.equals( before ) ) {
                                // take the next separator, this one is escaped...
                                startpoint = next + 1;
                                contains_escaped_separators = true;
                            } else {
                                found = true;
                            }
                        } else {
                            found = true;
                        }
                    } while ( !found && next >= 0 );
                }
                if ( next == -1 ) {
                    next = length;
                }

                if ( encl_found ) {
                    pol = line.substring( from + len_encl, next - len_encl );
                } else {
                    pol = line.substring( from, next );
                }

                if ( dencl ) {
                    StringBuilder sbpol = new StringBuilder( pol );
                    int idx = sbpol.indexOf( enclosure + enclosure );
                    while ( idx >= 0 ) {
                        sbpol.delete( idx, idx + ( enclosure == null ? 0 : enclosure.length() ) );
                        idx = sbpol.indexOf( enclosure + enclosure );
                    }
                    pol = sbpol.toString();
                }

                // replace the escaped enclosures with enclosures...
                if ( contains_escaped_enclosures ) {
                    String replace = escapeCharacter + enclosure;
                    pol = Const.replace( pol, replace, enclosure );
                }

                // replace the escaped separators with separators...
                if ( contains_escaped_separators ) {
                    String replace = escapeCharacter + delimiter;
                    pol = Const.replace( pol, replace, delimiter );
                }

                // Now add pol to the strings found!
                strings.add( pol );

                pos = next + delimiter.length();
            }
            if ( pos == length ) {
                strings.add( "" );
            }
        } catch ( Exception e ) {
        }

        return strings.toArray( new String[ strings.size() ] );
    }

}
