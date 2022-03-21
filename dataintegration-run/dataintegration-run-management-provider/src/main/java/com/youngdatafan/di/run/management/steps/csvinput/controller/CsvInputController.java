package com.youngdatafan.di.run.management.steps.csvinput.controller;

import com.csvreader.CsvReader;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.encryption.DefaultEncryptionUtils;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.youngdatafan.di.run.management.steps.csvinput.api.CsvInputApi;
import com.youngdatafan.di.run.management.steps.csvinput.vo.FieldVO;
import com.youngdatafan.di.run.management.steps.utils.TypeCastUtils;
import com.github.vfss3.FileServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.plugins.csv.input.step.CsvInputMeta2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author echo_
 */
@Slf4j
@RestController
@RequestMapping("/csvinput")
public class CsvInputController implements CsvInputApi {

    @Override
    public Result<List<FieldVO>, Object> getColumns(String userId, String json) throws KettleException, IOException {
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

    private List<FieldVO> doScan(Node node) throws KettleException, IOException {
        List<FieldVO> fieldVOList = new ArrayList<>();
        CsvInputMeta2 info = new CsvInputMeta2();
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
                        fieldVO.setName(head[z]);
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

    private Map<String,Object> doScanData(Node node, String count) throws KettleException, IOException {
        Map<String,Object> map = new HashMap<>();
        List<String[]> resultColum = new ArrayList<>();
        List<String[]> resultRow = new ArrayList<>();
        CsvInputMeta2 info = new CsvInputMeta2();
        info.readData(node);
        InputStream inputStream = getInputStream( info );
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

    public InputStream getInputStream( CsvInputMeta2 meta ) {
        InputStream inputStream = null;
        try {
            FileObject fileObject = KettleVFS.getFileObject(meta.getFilename(), FileServerConfig.getFileSystemOptions(meta.getFileServerType(),meta.getFtpUsername(), DefaultEncryptionUtils.decrypt(meta.getFtpPassword())));
            inputStream = fileObject.getContent().getInputStream();
        } catch ( final Exception e ) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public InputStreamReader getReader( CsvInputMeta2 meta, InputStream inputStream ) {
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
