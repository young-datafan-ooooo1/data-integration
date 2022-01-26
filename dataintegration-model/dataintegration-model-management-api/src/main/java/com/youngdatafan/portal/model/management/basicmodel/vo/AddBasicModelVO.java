package com.youngdatafan.portal.model.management.basicmodel.vo;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/18 3:35 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@ApiModel(description = "基础模型返回对象")
public class AddBasicModelVO {

    @ApiModelProperty(value = "名称")
    private String cName;

    @ApiModelProperty(value = "所属分组")
    private String group;

    @ApiModelProperty(value = "数据源名称")
    private String datasourceName;

    @ApiModelProperty(value = "schema名称")
    private String schemaName;

    @ApiModelProperty(value = "表名称")
    private String tableName;

    @ApiModelProperty(value = "字段数量")
    private String columnCount;

    @ApiModelProperty(value = "排序")
    private String sortNum;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "模型类型")
    private String modelType;

    @ApiModelProperty(value = "基础模型管理元数据")
    private List<BasicModelMetaDataVO> basicModelMetaDataVOS;

    @ApiModelProperty(value = "模型预过滤处理条件")
    private List<ModelFilterVO> modelFilter;

    public List<ModelFilterVO> getModelFilter() {
        return modelFilter;
    }

    public void setModelFilter(List<ModelFilterVO> modelFilter) {
        this.modelFilter = modelFilter;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getcName() {
        return cName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(String columnCount) {
        this.columnCount = columnCount;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public List<BasicModelMetaDataVO> getBasicModelMetaDataVOS() {
        return basicModelMetaDataVOS;
    }

    public void setBasicModelMetaDataVOS(List<BasicModelMetaDataVO> basicModelMetaDataVOS) {
        this.basicModelMetaDataVOS = basicModelMetaDataVOS;
    }
}
