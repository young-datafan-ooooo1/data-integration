package com.youngdatafan.di.run.management.steps.gzipCsvFileInput.controller;

import com.csvreader.CsvReader;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.encryption.DefaultEncryptionUtils;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.github.vfss3.FileServerConfig;
import com.youngdatafan.di.run.management.steps.csvinput.vo.FieldVO;
import com.youngdatafan.di.run.management.steps.gzipCsvInput.api.GzipCsvInputApi;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.plugins.parallelGzipCsvInput.step.ParGzipCsvInputData;
import org.pentaho.di.plugins.parallelGzipCsvInput.step.ParGzipCsvInputMeta;
import org.pentaho.metastore.api.exceptions.MetaStoreException;
import org.pentaho.metastore.stores.xml.XmlMetaStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Node;


/**
 * @author echo_
 */
@Slf4j
@RestController
@RequestMapping("/gzipcsvinput")
public class GzipCsvInputInputController implements GzipCsvInputApi {

    @Override
    public Result<List<FieldVO>, Object> getColumns(String userId, String json) throws KettleException, IOException, MetaStoreException {
        if(StringUtils.isBlank(json)){
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "step内容不能为空");
        }
        JSONLinkedObject jsonLinkedObject = new JSONLinkedObject(StringEscapeUtils.unescapeXml(json));
        String xml = XML.toString(jsonLinkedObject);
        List<FieldVO> fieldVOList = doScan(XMLHandler.getSubNode( XMLHandler.loadXMLString( xml ), "step" ));
        return Result.success(fieldVOList);
    }

    @Override
    public Result<Map<String,Object>, Object> getData(String userId, String json,String count) throws KettleException, IOException {
        if(StringUtils.isBlank(json)){
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "step内容不能为空");
        }
        JSONLinkedObject jsonLinkedObject = new JSONLinkedObject(StringEscapeUtils.unescapeXml(json));
        String xml = XML.toString(jsonLinkedObject);
        Map<String,Object> map = doScanData(XMLHandler.getSubNode( XMLHandler.loadXMLString( xml ), "step" ), count);
        return Result.success(map);
    }

    private List<FieldVO> doScan(Node node) throws KettleException, IOException, MetaStoreException {
        List<FieldVO> fieldVOList = new ArrayList<>();
        ParGzipCsvInputMeta info = new ParGzipCsvInputMeta();
        CompressorInputStream cis;
        TarArchiveInputStream tis;
        info.loadXML(node,null,new XmlMetaStore());
        ParGzipCsvInputData data = new ParGzipCsvInputData();

        InputStream inputStream = getInputStream( info ,data);
        cis = new GzipCompressorInputStream(inputStream, true);
        tis = new TarArchiveInputStream(cis);
        TarArchiveEntry entry = tis.getNextTarEntry();
        InputStreamReader reader = new InputStreamReader( inputStream );
        while (entry != null) {
            FieldVO fieldVO = new FieldVO();
            fieldVO.setName(entry.getName());
            fieldVO.setType("String");
            entry = tis.getNextTarEntry();
            fieldVOList.add(fieldVO);
        }


/*
        EncodingType encodingType = EncodingType.guessEncodingType( reader.getEncoding() );
        String line;
        line = TextFileInput.getLine(
                log, reader, encodingType, TextFileInputMeta.FILE_FORMAT_MIXED, new StringBuilder( 1000 ) );

        // Split the string, header or data into parts...
        //
        String[] fieldNames = Const.splitString( line, info.getDelimiter() );*/
        return  fieldVOList;

    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    private Map<String,Object> doScanData(Node node, String count) throws KettleException, IOException {
        Map<String,Object> map = new HashMap<>();
        List<String[]> resultColum = new ArrayList<>();
        List<String[]> resultRow = new ArrayList<>();
        ParGzipCsvInputMeta info = new ParGzipCsvInputMeta();
        ParGzipCsvInputData data = new ParGzipCsvInputData();

        InputStream inputStream = getInputStream( info ,data);
        InputStreamReader reader = getReader( info, inputStream );
        try {
            CsvReader csvReader = new CsvReader(reader);
            String[] colunm =null;
            String[] temp =null;
            int tempCount=0;
            int i=1;
            //循环读取每一个文件的数据
            while (csvReader.readRecord()) {
                if(i-1>Integer.parseInt(count)){
                    break;
                }
                if(i==1){
                    colunm = csvReader.getRawRecord().split(info.getSeparator());
                    tempCount = colunm.length;
                    resultColum.add(colunm);
                    map.put("column",resultColum);
                }else {
                    temp = new String[tempCount];
                    for (int z = 0; z < colunm.length; z++) {
                        temp[z]=csvReader.get(z);
                    }
                    resultRow.add(temp);
                }
                i++;
            }
            map.put("data",resultRow);
        }finally {
            if(inputStream!=null){
                inputStream.close();
            }
            if(reader!=null){
                reader.close();
            }
        }
        return  map;

    }

    public InputStream getInputStream( ParGzipCsvInputMeta meta,ParGzipCsvInputData data) {

        try {
            FileObject fileObject = KettleVFS.getFileObject(meta.getFilename(), FileServerConfig.getFileSystemOptions(meta.getFileServerType(),meta.getFtpUsername(), DefaultEncryptionUtils.decrypt(meta.getFtpPassword())));
               return fileObject.getContent().getInputStream();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }

    public InputStreamReader getReader( ParGzipCsvInputMeta meta, InputStream inputStream ) {
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

}
