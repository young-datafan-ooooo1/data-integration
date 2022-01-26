package com.youngdatafan.portal.model.management.util.excel;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;


/**
 * ------
 *
 * @author VincentHsing|e.vincent.hsing@gmail.com
 * @date 2018-08-17 14:59
 */
public class ExcelUploadResult {

    private List<ObjectNode> resultObject;
    private List<ExcelRejectCell> rejectCells;

  public List<ObjectNode> getResultObject() {
    return resultObject;
  }

  public void setResultObject(List<ObjectNode> resultObject) {
    this.resultObject = resultObject;
  }

  public List<ExcelRejectCell> getRejectCells() {
    return rejectCells;
  }

  public void setRejectCells(List<ExcelRejectCell> rejectCells) {
    this.rejectCells = rejectCells;
  }
}
