package com.youngdatafan.portal.model.management.basicmodel.vo;

import java.util.List;
import lombok.Data;

/**
 * BatchInserModelVO.
 */
@Data
public class BatchInserModelVO {

    private String dataSourceName;

    private String group;

    private String modelType;

    private String schemaName;

    private List<TableNameAndModelNameVO> tableNameAndModelNameVOS;

}
