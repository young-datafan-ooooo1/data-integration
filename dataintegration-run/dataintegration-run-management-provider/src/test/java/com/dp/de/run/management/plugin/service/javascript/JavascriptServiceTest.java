package com.dp.de.run.management.plugin.service.javascript;

import com.dp.de.run.management.plugin.service.JsonToXmlTest;
import com.youngdatafan.di.run.management.DiRunManagementApplication;
import com.youngdatafan.di.run.management.server.service.ProjectExecutorService;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.NodeTransformer;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ast.ScriptNode;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.scriptvalues_mod.ScriptValuesMetaMod;
import org.pentaho.di.trans.steps.scriptvalues_mod.ScriptValuesModDummy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.pentaho.di.trans.steps.script.Script.ABORT_TRANSFORMATION;
import static org.pentaho.di.trans.steps.script.Script.ERROR_TRANSFORMATION;
import static org.pentaho.di.trans.steps.scriptvalues_mod.ScriptValuesMod.CONTINUE_TRANSFORMATION;
import static org.pentaho.di.trans.steps.scriptvalues_mod.ScriptValuesMod.SKIP_TRANSFORMATION;

/**
 * @author gavin
 * @since 2020/1/16 7:21 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DiRunManagementApplication.class, JavascriptServiceTest.class}
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JavascriptServiceTest {

    @Autowired
    private ProjectExecutorService projectExecutorService;

    private final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    @Test
    public void execute() throws Exception {
        final InputStream resourceAsStream = JsonToXmlTest.class.getClassLoader().getResourceAsStream("1.xml");
        final byte[] data = new byte[resourceAsStream.available()];
        IOUtils.readFully(resourceAsStream, data);
        String xml = new String(data);
        TransMeta transMeta = projectExecutorService.buildTransMeta("b38b3b6f5a5c462dae6d517803f4b13f11",xml);
        test(transMeta,"JavaScript代码");
    }
    private  List<ScriptFieldVO> test( TransMeta transMeta ,String componentName) {
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
                jsscope.put( "_TransformationName_", jsscope, "JavaScript代码" );

                try {
                    RowMetaInterface rowMeta = transMeta.getPrevStepFields( "JavaScript代码" );
                    if ( rowMeta != null ) {

                        ScriptValuesModDummy dummyStep = new ScriptValuesModDummy( rowMeta, transMeta.getStepFields( "JavaScript代码" ) );
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
