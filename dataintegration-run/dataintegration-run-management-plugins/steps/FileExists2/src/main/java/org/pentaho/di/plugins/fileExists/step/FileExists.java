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

package org.pentaho.di.plugins.fileExists.step;

import com.youngdatafan.dataintegration.core.util.encryption.DefaultEncryptionUtils;
import com.github.vfss3.FileServerConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.local.LocalFile;
import org.pentaho.di.core.ResultFile;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.util.UUIDUtil;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

import java.io.*;

/**
 * Check if a table exists in a Database *
 *
 * @author Samatar
 * @since 03-Juin-2008
 *
 */

public class FileExists extends BaseStep implements StepInterface {
  private static Class<?> PKG = FileExistsMeta.class; // for i18n purposes, needed by Translator2!!

  private FileExistsMeta meta;
  private FileExistsData data;

  public FileExists( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
    Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    meta = (FileExistsMeta) smi;
    data = (FileExistsData) sdi;

    boolean sendToErrorRow = false;
    String errorMessage = null;

    Object[] r = getRow(); // Get row from input rowset & set row busy!
    if ( r == null ) { // no more input to be expected...

      setOutputDone();
      return false;
    }

    boolean fileexists = false;
    String filetype = null;

    try {
      if ( first ) {
        first = false;
        // get the RowMeta
        data.previousRowMeta = getInputRowMeta().clone();
        data.NrPrevFields = data.previousRowMeta.size();
        data.outputRowMeta = data.previousRowMeta;
        meta.getFields( data.outputRowMeta, getStepname(), null, null, this, repository, metaStore );

        // Check is tablename field is provided
        if ( Utils.isEmpty( meta.getDynamicFilenameField() ) ) {
          logError( BaseMessages.getString( PKG, "FileExists.Error.FilenameFieldMissing" ) );
          throw new KettleException( BaseMessages.getString( PKG, "FileExists.Error.FilenameFieldMissing" ) );
        }

        // cache the position of the field
        if ( data.indexOfFileename < 0 ) {
          data.indexOfFileename = data.previousRowMeta.indexOfValue( meta.getDynamicFilenameField() );
          if ( data.indexOfFileename < 0 ) {
            // The field is unreachable !
            logError( BaseMessages.getString( PKG, "FileExists.Exception.CouldnotFindField" )
              + "[" + meta.getDynamicFilenameField() + "]" );
            throw new KettleException( BaseMessages.getString( PKG, "FileExists.Exception.CouldnotFindField", meta
              .getDynamicFilenameField() ) );
          }
        }
      } // End If first

      Object[] outputRow = RowDataUtil.allocateRowData( data.outputRowMeta.size() );
      for ( int i = 0; i < data.NrPrevFields; i++ ) {
        outputRow[i] = r[i];
      }
      // get filename
      //替换文件路径
      try {
        // 处理远程文件，将远程文件下载到本地目录
        String sqlpath=r[data.indexOfFileename].toString();
        String fileName =  sqlpath.substring(sqlpath.lastIndexOf("/")+1);
        String tmpFilePath = "./tmp/"+ UUIDUtil.getUUIDAsString() + fileName.substring(fileName.lastIndexOf("."));
        File tmpFile = new File(tmpFilePath);
        if(!tmpFile.getParentFile().exists()){
          tmpFile.getParentFile().mkdirs();
        }

        FileObject fileObject = KettleVFS.getFileObject(sqlpath, getTransMeta(), FileServerConfig.getFileSystemOptions(meta.getFileServerType(),meta.getFtpUsername(), DefaultEncryptionUtils.decrypt(meta.getFtpPassword())));
        if ( !( fileObject instanceof LocalFile) ) {
          // 先将远程文件写入到本地临时目录
          try(InputStream inputStream = fileObject.getContent().getInputStream();
              FileOutputStream outputStream = new FileOutputStream(tmpFilePath)) {
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
            IOUtils.closeQuietly(outputStream);
          }
          // 保存临时文件
          r[data.indexOfFileename]=tmpFilePath;
          data.tmpFiles.add(tmpFilePath);
        }else {
          //  meta.setFilename(meta.getFilename());
        }
      } catch (KettleFileException e) {
        e.printStackTrace();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (FileSystemException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

      String filename = data.previousRowMeta.getString( r, data.indexOfFileename );
      if ( !Utils.isEmpty( filename ) ) {
        data.file = KettleVFS.getFileObject( filename, getTransMeta() );

        // Check if file
        fileexists = data.file.exists();

        // include file type?
        if ( meta.includeFileType() && fileexists && !Utils.isEmpty( meta.getFileTypeFieldName() ) ) {
          filetype = data.file.getType().toString();
        }

        // add filename to result filenames?
        if ( meta.addResultFilenames() && fileexists && data.file.getType() == FileType.FILE ) {
          // Add this to the result file names...
          ResultFile resultFile =
            new ResultFile( ResultFile.FILE_TYPE_GENERAL, data.file, getTransMeta().getName(), getStepname() );
          resultFile.setComment( BaseMessages.getString( PKG, "FileExists.Log.FileAddedResult" ) );
          addResultFile( resultFile );

          if ( log.isDetailed() ) {
            logDetailed( BaseMessages.getString( PKG, "FileExists.Log.FilenameAddResult", data.file.toString() ) );
          }
        }
      }

      // Add result field to input stream
      outputRow[data.NrPrevFields] = fileexists;
      int rowIndex = data.NrPrevFields;
      rowIndex++;

      if ( meta.includeFileType() && !Utils.isEmpty( meta.getFileTypeFieldName() ) ) {
        outputRow[rowIndex] = filetype;
      }

      // add new values to the row.
      putRow( data.outputRowMeta, outputRow ); // copy row to output rowset(s);

      if ( log.isRowLevel() ) {
        logRowlevel( BaseMessages.getString( PKG, "FileExists.LineNumber", getLinesRead()
          + " : " + getInputRowMeta().getString( r ) ) );
      }
    } catch ( Exception e ) {
      if ( getStepMeta().isDoingErrorHandling() ) {
        sendToErrorRow = true;
        errorMessage = e.toString();
      } else {
        logError( BaseMessages.getString( PKG, "FileExists.ErrorInStepRunning" ) + e.getMessage() );
        setErrors( 1 );
        stopAll();
        setOutputDone(); // signal end to receiver(s)
        return false;
      }
      if ( sendToErrorRow ) {
        // Simply add this row to the error row
        putError( getInputRowMeta(), r, 1, errorMessage, meta.getResultFieldName(), "FileExistsO01" );
      }
    }

    return true;
  }

  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    meta = (FileExistsMeta) smi;
    data = (FileExistsData) sdi;

    if ( super.init( smi, sdi ) ) {
      if ( Utils.isEmpty( meta.getResultFieldName() ) ) {
        logError( BaseMessages.getString( PKG, "FileExists.Error.ResultFieldMissing" ) );
        return false;
      }
      return true;
    }
    return false;
  }

  public void dispose( StepMetaInterface smi, StepDataInterface sdi ) {
    meta = (FileExistsMeta) smi;
    data = (FileExistsData) sdi;
    if ( data.file != null ) {
      try {
        data.file.close();
        data.file = null;
      } catch ( Exception e ) {
        // Ignore close errors
      }

    }
    super.dispose( smi, sdi );
  }

}
