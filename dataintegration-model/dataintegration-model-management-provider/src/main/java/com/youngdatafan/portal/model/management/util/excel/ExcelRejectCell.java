package com.youngdatafan.portal.model.management.util.excel;


/**
 * ------
 *
 * @author VincentHsing|e.vincent.hsing@gmail.com
 * @date 2018-08-17 13:53
 */
public class ExcelRejectCell {
    private int row;
    private int column;
    private String msg;

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
