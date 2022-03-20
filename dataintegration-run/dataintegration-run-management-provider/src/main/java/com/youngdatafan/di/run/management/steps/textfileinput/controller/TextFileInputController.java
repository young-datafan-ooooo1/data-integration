package com.youngdatafan.di.run.management.steps.textfileinput.controller;

import com.csvreader.CsvReader;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.util.encryption.DefaultEncryptionUtils;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import com.github.vfss3.FileServerConfig;
import com.youngdatafan.di.run.management.steps.csvinput.vo.FieldVO;
import com.youngdatafan.di.run.management.steps.textfileinput.api.TextFileInputApi;
import com.youngdatafan.di.run.management.steps.utils.TypeCastUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.plugins.textfile.input.step.TextFileInputMeta2;
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
@RequestMapping("/textfileinput")
public class TextFileInputController implements TextFileInputApi {

    @Override
    public Result<List<FieldVO>, Object> getColumns(String userId, String json) throws KettleException, IOException, MetaStoreException {
        if(StringUtils.isBlank(json)){
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "step内容不能为空");
        }
        JSONLinkedObject jsonLinkedObject = new JSONLinkedObject(StringEscapeUtils.unescapeXml(json));
        String xml = XML.toString(jsonLinkedObject);
        List<FieldVO> fieldVOList = doScanField(XMLHandler.getSubNode( XMLHandler.loadXMLString( xml ), "step" ));
        return Result.success(fieldVOList);
    }

    @Override
    public Result<Map<String,Object>, Object> getData(String userId, String json,String count) throws KettleException, IOException, MetaStoreException {
        if(StringUtils.isBlank(json)){
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "step内容不能为空");
        }
        JSONLinkedObject jsonLinkedObject = new JSONLinkedObject(StringEscapeUtils.unescapeXml(json));
        String xml = XML.toString(jsonLinkedObject);
        Map<String,Object> map = doScanData(XMLHandler.getSubNode( XMLHandler.loadXMLString( xml ), "step" ), count);
        return Result.success(map);
    }

    private List<FieldVO> doScanField(Node node) throws KettleException, IOException, MetaStoreException {
        List<FieldVO> fieldVOList = new ArrayList<>();
        TextFileInputMeta2 info = new TextFileInputMeta2();
        info.loadXML(node,null,new XmlMetaStore());
        InputStream inputStream = getInputStream( info, info.inputFiles.fileName[0]);
        InputStreamReader reader = getReader( info, inputStream );
        try {
            CsvReader csvReader = new CsvReader(reader);
            String[] head = null;
            int i=1;
            //循环读取每一个文件的数据
            while (csvReader.readRecord()) {
                if(i==1){
                    head = csvReader.getRawRecord().split(info.content.separator);
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

    private Map<String,Object> doScanData(Node node, String count) throws KettleException, IOException, MetaStoreException {
        Map<String,Object> map = new HashMap<>();
        List<String[]> resultColum = new ArrayList<>();
        List<String[]> resultRow = new ArrayList<>();
        TextFileInputMeta2 info = new TextFileInputMeta2();
        info.loadXML(node,null,new XmlMetaStore());
        Boolean isHead = true;
        for(String fileName:info.getFileName()){
            InputStream inputStream = getInputStream( info,fileName );
            InputStreamReader reader = getReader( info, inputStream );
            try {
                CsvReader csvReader = new CsvReader(reader);
                String[] colunm =null;
                String[] temp =null;
                int tempCount=0;
                int i=resultRow.size()+1;
                //循环读取每一个文件的数据
                while (csvReader.readRecord()) {
                    if(i-1>Integer.parseInt(count)){
                        break;
                    }
                    if(isHead){
                        colunm = csvReader.getRawRecord().split(info.content.separator);
                        tempCount = colunm.length;
                        if(resultColum.size()==0){
                            resultColum.add(colunm);
                            map.put("column",resultColum);
                        }
                        isHead = false;
                    }else {
                        temp = new String[tempCount];
                        for (int z = 0; z < colunm.length; z++) {
                            temp[z]=csvReader.get(z);
                        }
                        resultRow.add(temp);
                    }
                    i++;
                }
                isHead = true;
                map.put("data",resultRow);
            }finally {
                if(inputStream!=null){
                    inputStream.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
        }

        return  map;

    }


    public InputStream getInputStream( TextFileInputMeta2 meta,String fileName) {
        InputStream inputStream = null;
        try {
            FileObject fileObject = KettleVFS.getFileObject(fileName, FileServerConfig.getFileSystemOptions(meta.getFileServerType(),meta.getFtpUsername(), DefaultEncryptionUtils.decrypt(meta.getFtpPassword())));
            inputStream = fileObject.getContent().getInputStream();
        } catch ( final Exception e ) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public InputStreamReader getReader( TextFileInputMeta2 meta, InputStream inputStream ) {
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
