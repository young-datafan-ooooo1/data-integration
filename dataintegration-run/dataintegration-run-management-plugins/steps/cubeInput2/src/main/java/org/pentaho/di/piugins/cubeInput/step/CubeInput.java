/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
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

package org.pentaho.di.piugins.cubeInput.step;

import com.youngdatafan.dataintegration.core.util.encryption.DefaultEncryptionUtils;
import com.github.vfss3.FileServerConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.provider.local.LocalFile;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.ResultFile;
import org.pentaho.di.core.exception.KettleEOFException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.util.UUIDUtil;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.zip.GZIPInputStream;

public class CubeInput extends BaseStep implements StepInterface {
  private static Class<?> PKG = CubeInputMeta.class; // for i18n purposes, needed by Translator2!!

  private CubeInputMeta meta;
  private CubeInputData data;
  private int realRowLimit;

  public CubeInput( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
    Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {

    if ( first ) {
      first = false;
      meta = (CubeInputMeta) smi;
      data = (CubeInputData) sdi;
      realRowLimit = Const.toInt( environmentSubstitute( meta.getRowLimit() ), 0 );
    }


    try {
      Object[] r = data.meta.readData( data.dis );
      putRow( data.meta, r ); // fill the rowset(s). (sleeps if full)
      incrementLinesInput();

      if ( realRowLimit > 0 && getLinesInput() >= realRowLimit ) { // finished!
        setOutputDone();
        return false;
      }
    } catch ( KettleEOFException eof ) {
      setOutputDone();
      return false;
    } catch ( SocketTimeoutException e ) {
      throw new KettleException( e ); // shouldn't happen on files
    }

    if ( checkFeedback( getLinesInput() ) ) {
      if ( log.isBasic() ) {
        logBasic( BaseMessages.getString( PKG, "CubeInput.Log.LineNumber" ) + getLinesInput() );
      }
    }

    return true;
  }

  @Override public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    meta = (CubeInputMeta) smi;
    data = (CubeInputData) sdi;

    //替换文件路径
    try {
      // 处理远程文件，将远程文件下载到本地目录
      String fileName = meta.getFilename().substring(meta.getFilename().lastIndexOf("/")+1);
      String tmpFilePath = "./tmp/"+ UUIDUtil.getUUIDAsString() + fileName.substring(fileName.lastIndexOf("."));
      File tmpFile = new File(tmpFilePath);
      if(!tmpFile.getParentFile().exists()){
        tmpFile.getParentFile().mkdirs();
      }

      FileObject fileObject = KettleVFS.getFileObject( meta.getFilename(), getTransMeta(), FileServerConfig.getFileSystemOptions(meta.getFileServerType(),meta.getFtpUsername(), DefaultEncryptionUtils.decrypt(meta.getFtpPassword())));
      if ( !( fileObject instanceof LocalFile) ) {
        // 先将远程文件写入到本地临时目录
        try(InputStream inputStream = fileObject.getContent().getInputStream();
            FileOutputStream outputStream = new FileOutputStream(tmpFilePath)) {
          IOUtils.copy(inputStream, outputStream);
          outputStream.flush();
          IOUtils.closeQuietly(outputStream);
        }
        // 保存临时文件
        meta.setFilename(tmpFilePath);
        data.tmpFiles.add(tmpFilePath);
      }else {
        meta.setFilename(meta.getFilename());
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


    if ( super.init( smi, sdi ) ) {
      try {
        String filename = environmentSubstitute( meta.getFilename() );

        // Add filename to result filenames ?
        if ( meta.isAddResultFile() ) {
          ResultFile resultFile =
            new ResultFile(
              ResultFile.FILE_TYPE_GENERAL, KettleVFS.getFileObject( filename, getTransMeta() ),
              getTransMeta().getName(), toString() );
          resultFile.setComment( "File was read by a Cube Input step" );
          addResultFile( resultFile );
        }

        data.fis = KettleVFS.getInputStream( filename, this );
        data.zip = new GZIPInputStream( data.fis );
        data.dis = new DataInputStream( data.zip );

        try {
          data.meta = new RowMeta( data.dis );
          return true;
        } catch ( KettleFileException kfe ) {
          logError( BaseMessages.getString( PKG, "CubeInput.Log.UnableToReadMetadata" ), kfe );
          return false;
        }
      } catch ( Exception e ) {
        logError( BaseMessages.getString( PKG, "CubeInput.Log.ErrorReadingFromDataCube" ), e );
      }
    }
    return false;
  }

  @Override public void dispose( StepMetaInterface smi, StepDataInterface sdi ) {
    meta = (CubeInputMeta) smi;
    data = (CubeInputData) sdi;

    try {
      if ( data.dis != null ) {
        data.dis.close();
        data.dis = null;
      }
      if ( data.zip != null ) {
        data.zip.close();
        data.zip = null;
      }
      if ( data.fis != null ) {
        data.fis.close();
        data.fis = null;
      }
    } catch ( IOException e ) {
      logError( BaseMessages.getString( PKG, "CubeInput.Log.ErrorClosingCube" ) + e.toString() );
      setErrors( 1 );
      stopAll();
    }

    super.dispose( smi, sdi );
  }
}
