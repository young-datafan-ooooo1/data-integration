package com.youngdatafan.portal.model.management.outinterfacemodel.vo;

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
public class AddOutinterfaceModelVO {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "所属分组")
    private String group;

    @ApiModelProperty(value = "基础模型组")
    private String basicModelGroupName;

    @ApiModelProperty(value = "基础模型")
    private String basicModelTableNameValue;

    @ApiModelProperty(value = "字段数量")
    private String columnCount;

    @ApiModelProperty(value = "排序")
    private String sortNum = "0";

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "基础模型管理元数据")
    private List<OutinterfaceBasicModelMetaDataCopyDTO> basicModelMetaDataCopyDTOS;

    @ApiModelProperty(value = "预过滤条件")
    private List<ModelFilterVO> modelFilterVOS;

    public List<ModelFilterVO> getModelFilterVOS() {
        return modelFilterVOS;
    }

    public void setModelFilterVOS(List<ModelFilterVO> modelFilterVOS) {
        this.modelFilterVOS = modelFilterVOS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getBasicModelGroupName() {
        return basicModelGroupName;
    }

    public void setBasicModelGroupName(String basicModelGroupName) {
        this.basicModelGroupName = basicModelGroupName;
    }

    public String getBasicModelTableNameValue() {
        return basicModelTableNameValue;
    }

    public void setBasicModelTableNameValue(String basicModelTableNameValue) {
        this.basicModelTableNameValue = basicModelTableNameValue;
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

    public List<OutinterfaceBasicModelMetaDataCopyDTO> getBasicModelMetaDataCopyDTOS() {
        return basicModelMetaDataCopyDTOS;
    }

    public void setBasicModelMetaDataCopyDTOS(List<OutinterfaceBasicModelMetaDataCopyDTO> basicModelMetaDataCopyDTOS) {
        this.basicModelMetaDataCopyDTOS = basicModelMetaDataCopyDTOS;
    }
}
