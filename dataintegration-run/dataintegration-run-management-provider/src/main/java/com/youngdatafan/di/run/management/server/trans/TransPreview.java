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

package com.youngdatafan.di.run.management.server.trans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.step.RowAdapter;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;

/**
 * 数据预览捕获对象
 *
 * @author gavin
 */
public class TransPreview {

    protected Map<String, List<RowMetaAndData>> previewDataMap;
    private int previewSize = 100;
    private PreviewMode previewMode;

    /**
     *
     */
    public TransPreview() {
        previewDataMap = new HashMap<>();
        previewMode = PreviewMode.FIRST;
    }

    /**
     * 捕获预览数据
     */
    public void capturePreviewData(final Trans trans, List<StepMeta> stepMetas) {
        previewDataMap.clear();

        try {
            for (final StepMeta stepMeta : stepMetas) {
                final List<RowMetaAndData> rowsData;
                if (previewMode == PreviewMode.LAST) {
                    rowsData = new LinkedList<>();
                } else {
                    rowsData = new ArrayList<>();
                }

                previewDataMap.put(stepMeta.getName(), rowsData);

                StepInterface step = trans.findRunThread(stepMeta.getName());

                if (step != null) {
                    if (previewMode == PreviewMode.LAST) {
                        step.addRowListener(new RowAdapter() {
                            @Override
                            public void rowWrittenEvent(RowMetaInterface rowMeta, Object[] row) throws KettleStepException {
                                try {
                                    rowsData.add(new RowMetaAndData(rowMeta, rowMeta.cloneRow(row)));
                                    if (rowsData.size() > previewSize) {
                                        rowsData.remove(0);
                                    }
                                } catch (Exception e) {
                                    throw new KettleStepException("Unable to clone row for metadata : " + rowMeta, e);
                                }
                            }
                        });
                    } else {
                        step.addRowListener(new RowAdapter() {

                            @Override
                            public void rowWrittenEvent(RowMetaInterface rowMeta, Object[] row) throws KettleStepException {
                                if (rowsData.size() < previewSize) {
                                    try {
                                        rowsData.add(new RowMetaAndData(rowMeta, rowMeta.cloneRow(row)));
                                    } catch (Exception e) {
                                        throw new KettleStepException("Unable to clone row for metadata : " + rowMeta, e);
                                    }
                                }
                            }
                        });
                    }
                }

            }
        } catch (Exception e) {
            trans.getLogChannel().logError("数据预览缓存数据失败.", e.getMessage());
        }
    }

    /**
     * 根据步骤获取预览数据
     */
    public List<String[]> getData(String stepName) {
        List<RowMetaAndData> rowData = previewDataMap.get(stepName);
        if (rowData == null) {
            return null;
        }

        List<String[]> result = new ArrayList<>(rowData.size());

        for (RowMetaAndData rowDatum : rowData) {
            RowMetaInterface dataRowMeta = rowDatum.getRowMeta();
            Object[] data = rowDatum.getData();
            String[] row = new String[dataRowMeta.size()];
            result.add(row);

            for (int dataIndex = 0; dataIndex < dataRowMeta.size(); dataIndex++) {
                String string;
                ValueMetaInterface valueMetaInterface;
                try {
                    valueMetaInterface = dataRowMeta.getValueMeta(dataIndex);
                    if (valueMetaInterface.isStorageBinaryString()) {
                        Object nativeType = valueMetaInterface.convertBinaryStringToNativeType((byte[]) data[dataIndex]);
                        string = valueMetaInterface.getStorageMetadata().getString(nativeType);
                    } else {
                        string = dataRowMeta.getString(data, dataIndex);
                    }
                } catch (Exception e) {
                    string = "Conversion error: " + e.getMessage();
                }

                row[dataIndex] = string;
            }
        }

        return result;
    }

    /**
     * 获取预览字段名集合
     */
    public List<String> getFieldNames(String stepName) {
        List<RowMetaAndData> rowData = previewDataMap.get(stepName);
        if (rowData == null || rowData.isEmpty()) {
            return null;
        }

        final RowMetaInterface rowMeta = rowData.get(0).getRowMeta();
        List<String> previewFieldNames = new ArrayList<>(rowMeta.size());

        for (int i = 0; i < rowMeta.size(); i++) {
            final ValueMetaInterface valueMeta = rowMeta.getValueMeta(i);
            previewFieldNames.add(valueMeta.getName());
        }

        return previewFieldNames;
    }

    /**
     * 获取字段元数据
     */
    public RowMetaInterface getRowMeta(String stepName) {
        List<RowMetaAndData> rowData = previewDataMap.get(stepName);
        if (rowData == null || rowData.isEmpty()) {
            return null;
        }

        return rowData.get(0).getRowMeta();
    }

    public PreviewMode getPreviewMode() {
        return previewMode;
    }

    public void setPreviewMode(PreviewMode previewMode) {
        this.previewMode = previewMode;
    }

    /**
     * 删除步骤数据
     */
    public void remove(String stepName) {
        previewDataMap.remove(stepName);
    }

    public void setPreviewSize(int previewSize) {
        if (previewSize > 0 && previewSize < 10000) {
            this.previewSize = previewSize;
        }
    }

    public enum PreviewMode {
        FIRST, LAST, OFF,
    }
}
