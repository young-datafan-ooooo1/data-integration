package com.youngdatafan.di.run.management.steps.javascript.controller;

import com.alibaba.fastjson.JSONObject;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.UUIDUtils;
import com.youngdatafan.di.run.management.steps.javascript.api.JavaScriptServiceApi;
import com.youngdatafan.di.run.management.steps.javascript.vo.JavascriptVO;
import com.youngdatafan.di.run.management.steps.javascript.vo.ScriptFieldVO;
import com.youngdatafan.di.run.management.server.service.ProjectExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.ScriptNode;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.scriptvalues_mod.ScriptValuesMetaMod;
import org.pentaho.di.trans.steps.scriptvalues_mod.ScriptValuesModDummy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.pentaho.di.trans.steps.script.Script.ABORT_TRANSFORMATION;
import static org.pentaho.di.trans.steps.script.Script.ERROR_TRANSFORMATION;
import static org.pentaho.di.trans.steps.scriptvalues_mod.ScriptValuesMod.CONTINUE_TRANSFORMATION;
import static org.pentaho.di.trans.steps.scriptvalues_mod.ScriptValuesMod.SKIP_TRANSFORMATION;


/**
 * @author echo_
 */
@Slf4j
@RestController
@RequestMapping("/javaScript")
public class JavaScriptController implements JavaScriptServiceApi {


    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    private ProjectExecutorService projectExecutorService;


    @Override
    public Result getJavaScriptFunctions() {
        try {
            Resource resource = resourceLoader.getResource("classpath:kettle/java_script.json");
            InputStream resourceAsStream = resource.getInputStream();
            final byte[] data = new byte[resourceAsStream.available()];
            IOUtils.readFully(resourceAsStream, data);
            String string = new String(data);
            JSONObject json = JSONObject.parseObject(string);
            return Result.success(json);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(StatusCode.CODE_10010.getCode(), "", "读取javaScript json异常");

        }

    }

    @Override
    public Result<List<ScriptFieldVO>, Object> getVariate(JavascriptVO javascriptVO) throws Exception {
        TransMeta transMeta = projectExecutorService.buildTransMeta(UUIDUtils.generateUUID32(),javascriptVO.getProjectFile());
        return Result.success(test(transMeta,javascriptVO.getName()));
    }

    private List<ScriptFieldVO> test(TransMeta transMeta , String componentName) {
        List<ScriptFieldVO> list = new ArrayList<>();
        for (StepMeta sm :transMeta.getSteps()){
            if(componentName.equals(sm.getName())){
                Context jscx;
                Scriptable jsscope;
                // Script jsscript;

                jscx = ContextFactory.getGlobal().enterContext();
                jscx.setOptimizationLevel( -1 );
                jsscope = jscx.initStandardObjects( null, false );


                // Adding the Name of the Transformation to the Context
                jsscope.put( "_TransformationName_", jsscope, componentName );

                try {
                    RowMetaInterface rowMeta = transMeta.getPrevStepFields( componentName );
                    if ( rowMeta != null ) {

                        ScriptValuesModDummy dummyStep = new ScriptValuesModDummy( rowMeta, transMeta.getStepFields( componentName ) );
                        Scriptable jsvalue = Context.toObject( dummyStep, jsscope );
                        jsscope.put( "_step_", jsscope, jsvalue );
                        jsscope.put( "SKIP_TRANSFORMATION", jsscope, Integer.valueOf( SKIP_TRANSFORMATION ) );
                        jsscope.put( "ABORT_TRANSFORMATION", jsscope, Integer.valueOf( ABORT_TRANSFORMATION ) );
                        jsscope.put( "ERROR_TRANSFORMATION", jsscope, Integer.valueOf( ERROR_TRANSFORMATION ) );
                        jsscope.put( "CONTINUE_TRANSFORMATION", jsscope, Integer.valueOf( CONTINUE_TRANSFORMATION ) );

                        Object[] row = new Object[rowMeta.size()];
                        Scriptable jsRowMeta = Context.toObject( rowMeta, jsscope );
                        jsscope.put( "rowMeta", jsscope, jsRowMeta );
                        for ( int i = 0; i < rowMeta.size(); i++ ) {
                            ValueMetaInterface valueMeta = rowMeta.getValueMeta( i );
                            Object valueData = null;

                            // Set date and string values to something to simulate real thing
                            //
                            if ( valueMeta.isDate() ) {
                                valueData = new Date();
                            }
                            if ( valueMeta.isString() ) {
                                valueData = "test value test value test value test value test value "
                                        + "test value test value test value test value test value";
                            }
                            if ( valueMeta.isInteger() ) {
                                valueData = Long.valueOf( 0L );
                            }
                            if ( valueMeta.isNumber() ) {
                                valueData = new Double( 0.0 );
                            }
                            if ( valueMeta.isBigNumber() ) {
                                valueData = BigDecimal.ZERO;
                            }
                            if ( valueMeta.isBoolean() ) {
                                valueData = Boolean.TRUE;
                            }
                            if ( valueMeta.isBinary() ) {
                                valueData = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, };
                            }

                            if ( valueMeta.isStorageBinaryString() ) {
                                valueMeta.setStorageType( ValueMetaInterface.STORAGE_TYPE_NORMAL );
                            }

                            row[i] = valueData;
                            Scriptable jsarg = Context.toObject( valueData, jsscope );
                            jsscope.put( valueMeta.getName(), jsscope, jsarg );
                        }

                        // Add support for Value class (new Value())
                        Scriptable jsval = Context.toObject( Value.class, jsscope );
                        jsscope.put( "Value", jsscope, jsval );

                        Script evalScript = jscx.compileString( (((ScriptValuesMetaMod)sm.getStepMetaInterface()).getJSScripts())[0].getScript(), "script", 1, null );
                        evalScript.exec( jscx, jsscope );
                        // Object tranScript = jscx.evaluateString(jsscope, scr, "script", 1, null);

                        if ( true ) {
                            ScriptNode tree = parseVariables( jscx, jsscope, (((ScriptValuesMetaMod)sm.getStepMetaInterface()).getJSScripts())[0].getScript(), "script", 1, null );
                            for ( int i = 0; i < tree.getParamAndVarCount(); i++ ) {
                                String varname = tree.getParamOrVarName( i );
                                if ( !varname.equalsIgnoreCase( "row" ) && !varname.equalsIgnoreCase( "trans_Status" ) ) {
                                    int type = ValueMetaInterface.TYPE_STRING;
                                    int length = -1, precision = -1;
                                    Object result = jsscope.get( varname, jsscope );
                                    if ( result != null ) {
                                        String classname = result.getClass().getName();
                                        if ( classname.equalsIgnoreCase( "java.lang.Byte" ) ) {
                                            // MAX = 127
                                            type = ValueMetaInterface.TYPE_INTEGER;
                                            length = 3;
                                            precision = 0;
                                        } else if ( classname.equalsIgnoreCase( "java.lang.Integer" ) ) {
                                            // MAX = 2147483647
                                            type = ValueMetaInterface.TYPE_INTEGER;
                                            length = 9;
                                            precision = 0;
                                        } else if ( classname.equalsIgnoreCase( "java.lang.Long" ) ) {
                                            // MAX = 9223372036854775807
                                            type = ValueMetaInterface.TYPE_INTEGER;
                                            length = 18;
                                            precision = 0;
                                        } else if ( classname.equalsIgnoreCase( "java.lang.Double" ) ) {
                                            type = ValueMetaInterface.TYPE_NUMBER;
                                            length = 16;
                                            precision = 2;

                                        } else if ( classname.equalsIgnoreCase( "org.mozilla.javascript.NativeDate" )
                                                || classname.equalsIgnoreCase( "java.util.Date" ) ) {
                                            type = ValueMetaInterface.TYPE_DATE;
                                        } else if ( classname.equalsIgnoreCase( "java.lang.Boolean" ) ) {
                                            type = ValueMetaInterface.TYPE_BOOLEAN;
                                        }
                                    }

                                    ScriptFieldVO ti = new ScriptFieldVO();
                                    ti.setName(varname);
                                    ti.setReName("");
                                    ti.setType(ValueMetaFactory.getValueMetaName( type ));
                                    ti.setLen(length >= 0 ? ( "" + length ) : "");
                                    ti.setPrecision(precision >= 0 ? ( "" + precision ) : "");
                                    ti.setReplace(( rowMeta.indexOfValue( varname ) >= 0 ) ? "是" : "否");
                                    list.add(ti);
                                }
                            }
                        }
                    }
                } catch ( KettleException ke ) {
                    ke.printStackTrace();
                } finally {
                    if ( jscx != null ) {
                        Context.exit();
                    }
                }
            }
        }

        return list;
    }

    public static ScriptNode parseVariables( Context cx, Scriptable scope, String source, String sourceName,
                                             int lineno, Object securityDomain ) {
        // Interpreter compiler = new Interpreter();
        CompilerEnvirons evn = new CompilerEnvirons();
        // evn.setLanguageVersion(Context.VERSION_1_5);
        evn.setOptimizationLevel( -1 );
        evn.setGeneratingSource( true );
        evn.setGenerateDebugInfo( true );
        ErrorReporter errorReporter = new ToolErrorReporter( false );
        Parser p = new Parser( evn, errorReporter );
        ScriptNode tree = p.parse( source, "", 0 ); // IOException
        new NodeTransformer().transform( tree );
        // Script result = (Script)compiler.compile(scope, evn, tree, p.getEncodedSource(),false, null);
        return tree;
    }
}
