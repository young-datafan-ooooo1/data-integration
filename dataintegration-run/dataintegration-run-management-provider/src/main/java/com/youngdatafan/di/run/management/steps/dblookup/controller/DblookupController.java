package com.youngdatafan.di.run.management.steps.dblookup.controller;

import com.alipay.sofa.common.utils.StringUtil;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.di.run.management.steps.dblookup.api.DbLookupApi;
import com.youngdatafan.di.run.management.steps.dblookup.vo.TableFieldVO;
import com.youngdatafan.di.run.management.server.service.ProjectExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author echo_
 */
@Slf4j
@RestController
@RequestMapping("/dblookup")
public class DblookupController implements DbLookupApi {

    @Autowired
    private ProjectExecutorService projectExecutorService;

    @Override
    public Result<List<TableFieldVO>, Object> getTableColumns(String userId, String json, String tableName,String connectName,String schema) {
        List<TableFieldVO> list = new ArrayList<>();
        Database db = null;
        try {
            TransMeta transMeta = projectExecutorService.buildTransMeta(UUIDUtils.generateUUID32(),json);
            DatabaseMeta ci = transMeta.findDatabase(connectName);
            db = new Database( ci );
            db.shareVariablesWith( transMeta );
            db.connect();
            String schemaTable="" ;
            if(!StringUtil.isEmpty(tableName)&&tableName.contains(".")){
                schemaTable=tableName;
            }else{
                schemaTable =
                        ci.getQuotedSchemaTableCombination( db.environmentSubstitute( schema ), db
                                .environmentSubstitute( tableName ) );
            }

            RowMetaInterface r = db.getTableFields( schemaTable );
            for (ValueMetaInterface valueMetaInterface : r.getValueMetaList()){
                TableFieldVO tableFieldVO = new TableFieldVO();
                tableFieldVO.setName(valueMetaInterface.getName());
                tableFieldVO.setType(valueMetaInterface.getTypeDesc());
                list.add(tableFieldVO);
            }
        } catch (KettleMissingPluginsException | KettleXMLException | IOException | KettleDatabaseException e) {
            e.printStackTrace();
        } finally {
            if(db!=null){
                db.disconnect();
            }
        }
        return Result.success(list);
    }
}
