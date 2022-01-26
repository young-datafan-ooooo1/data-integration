package com.youngdatafan.portal.model.management.util.excel;

/**
 * ------
 *
 * @author VincentHsing|e.vincent.hsing@gmail.com
 * @date 2018-08-21 10:47
 */
public class ExcelExportTable {
    private String tableName;

    private String tableDesc;

    public ExcelExportTable(){}
    public ExcelExportTable(String tableName, String tableDesc) {
        this.tableName = tableName;
        this.tableDesc = tableDesc;
    }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTableDesc() {
    return tableDesc;
  }

  public void setTableDesc(String tableDesc) {
    this.tableDesc = tableDesc;
  }
}
