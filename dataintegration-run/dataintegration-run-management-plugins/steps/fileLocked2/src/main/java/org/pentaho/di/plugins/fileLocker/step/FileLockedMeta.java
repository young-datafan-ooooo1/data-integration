/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.plugins.fileLocker.step;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBoolean;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.shared.SharedObjectInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Check if a file is locked *
 *
 * @author Samatar
 * @since 03-Juin-2009
 *
 */

public class FileLockedMeta extends BaseStepMeta implements StepMetaInterface {
  private static Class<?> PKG = FileLockedMeta.class; // for i18n purposes, needed by Translator2!!

  private boolean addresultfilenames;

  /** dynamic filename */
  private String filenamefield;

  /** function result: new value name */
  private String resultfieldname;

  private String ftpUsername;
  private String ftpPassword;
  private String fileServerType;

  public String getFtpUsername() {
    return ftpUsername;
  }

  public void setFtpUsername(String ftpUsername) {
    this.ftpUsername = ftpUsername;
  }

  public String getFtpPassword() {
    return ftpPassword;
  }

  public void setFtpPassword(String ftpPassword) {
    this.ftpPassword = ftpPassword;
  }

  public String getFileServerType() {
    return fileServerType;
  }

  public void setFileServerType(String fileServerType) {
    this.fileServerType = fileServerType;
  }

  public FileLockedMeta() {
    super(); // allocate BaseStepMeta
  }

  /**
   * @return Returns the filenamefield.
   */
  public String getDynamicFilenameField() {
    return filenamefield;
  }

  /**
   * @param filenamefield
   *          The filenamefield to set.
   */
  public void setDynamicFilenameField( String filenamefield ) {
    this.filenamefield = filenamefield;
  }

  /**
   * @return Returns the resultName.
   */
  public String getResultFieldName() {
    return resultfieldname;
  }

  /**
   * @param resultfieldname
   *          The resultfieldname to set.
   */
  public void setResultFieldName( String resultfieldname ) {
    this.resultfieldname = resultfieldname;
  }

  public boolean addResultFilenames() {
    return addresultfilenames;
  }

  public void setaddResultFilenames( boolean addresultfilenames ) {
    this.addresultfilenames = addresultfilenames;
  }

  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    readData( stepnode, databases );
  }

  public Object clone() {
    FileLockedMeta retval = (FileLockedMeta) super.clone();

    return retval;
  }

  public void setDefault() {
    resultfieldname = "result";
    addresultfilenames = false;
  }

  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
    VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    if ( !Utils.isEmpty( resultfieldname ) ) {
      ValueMetaInterface v = new ValueMetaBoolean( resultfieldname );
      v.setOrigin( name );
      inputRowMeta.addValueMeta( v );
    }
  }

  public String getXML() {
    StringBuilder retval = new StringBuilder();

    retval.append( "    " + XMLHandler.addTagValue( "filenamefield", filenamefield ) );
    retval.append( "    " + XMLHandler.addTagValue( "resultfieldname", resultfieldname ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "addresultfilenames", addresultfilenames ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "FTP_USERNAME", ftpUsername ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "FTP_PASSWORD", ftpPassword ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "FILE_SERVER_TYPE", fileServerType ) );
    return retval.toString();
  }

  private void readData( Node stepnode, List<? extends SharedObjectInterface> databases ) throws KettleXMLException {
    try {
      filenamefield = XMLHandler.getTagValue( stepnode, "filenamefield" );
      resultfieldname = XMLHandler.getTagValue( stepnode, "resultfieldname" );
      addresultfilenames = "Y".equalsIgnoreCase( XMLHandler.getTagValue( stepnode, "addresultfilenames" ) );
      ftpUsername = XMLHandler.getTagValue(stepnode, "FTP_USERNAME");
      ftpPassword = XMLHandler.getTagValue(stepnode, "FTP_PASSWORD");
      fileServerType = XMLHandler.getTagValue(stepnode, "FILE_SERVER_TYPE");
    } catch ( Exception e ) {
      throw new KettleXMLException(
        BaseMessages.getString( PKG, "FileLockedMeta.Exception.UnableToReadStepInfo" ), e );
    }
  }

  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases ) throws KettleException {
    try {
      filenamefield = rep.getStepAttributeString( id_step, "filenamefield" );
      resultfieldname = rep.getStepAttributeString( id_step, "resultfieldname" );
      addresultfilenames = rep.getStepAttributeBoolean( id_step, "addresultfilenames" );
      ftpUsername = rep.getStepAttributeString(id_step, "FTP_USERNAME");
      ftpPassword = rep.getStepAttributeString(id_step, "FTP_PASSWORD");
      fileServerType = rep.getStepAttributeString(id_step, "FILE_SERVER_TYPE");
    } catch ( Exception e ) {
      throw new KettleException( BaseMessages.getString(
        PKG, "FileLockedMeta.Exception.UnexpectedErrorReadingStepInfo" ), e );
    }
  }

  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step ) throws KettleException {
    try {
      rep.saveStepAttribute( id_transformation, id_step, "filenamefield", filenamefield );
      rep.saveStepAttribute( id_transformation, id_step, "resultfieldname", resultfieldname );
      rep.saveStepAttribute( id_transformation, id_step, "addresultfilenames", addresultfilenames );
      rep.saveStepAttribute(id_transformation, id_step, "FTP_USERNAME", ftpUsername);
      rep.saveStepAttribute(id_transformation, id_step, "FTP_PASSWORD", ftpPassword);
      rep.saveStepAttribute(id_transformation, id_step, "FILE_SERVER_TYPE", fileServerType);
    } catch ( Exception e ) {
      throw new KettleException( BaseMessages.getString( PKG, "FileLockedMeta.Exception.UnableToSaveStepInfo" )
        + id_step, e );
    }
  }

  public void check( List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta,
    RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info, VariableSpace space,
    Repository repository, IMetaStore metaStore ) {
    CheckResult cr;
    String error_message = "";

    if ( Utils.isEmpty( resultfieldname ) ) {
      error_message = BaseMessages.getString( PKG, "FileLockedMeta.CheckResult.ResultFieldMissing" );
      cr = new CheckResult( CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta );
      remarks.add( cr );
    } else {
      error_message = BaseMessages.getString( PKG, "FileLockedMeta.CheckResult.ResultFieldOK" );
      cr = new CheckResult( CheckResult.TYPE_RESULT_OK, error_message, stepMeta );
      remarks.add( cr );
    }
    if ( Utils.isEmpty( filenamefield ) ) {
      error_message = BaseMessages.getString( PKG, "FileLockedMeta.CheckResult.FileFieldMissing" );
      cr = new CheckResult( CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta );
      remarks.add( cr );
    } else {
      error_message = BaseMessages.getString( PKG, "FileLockedMeta.CheckResult.FileFieldOK" );
      cr = new CheckResult( CheckResult.TYPE_RESULT_OK, error_message, stepMeta );
      remarks.add( cr );
    }
    // See if we have input streams leading to this step!
    if ( input.length > 0 ) {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "FileLockedMeta.CheckResult.ReceivingInfoFromOtherSteps" ), stepMeta );
      remarks.add( cr );
    } else {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "FileLockedMeta.CheckResult.NoInpuReceived" ), stepMeta );
      remarks.add( cr );
    }

  }

  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr,
    TransMeta transMeta, Trans trans ) {
    return new FileLocked( stepMeta, stepDataInterface, cnr, transMeta, trans );
  }

  public StepDataInterface getStepData() {
    return new FileLockedData();
  }

  public boolean supportsErrorHandling() {
    return true;
  }
}
