package com.youngdatafan.portal.model.management.datasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Echo_Sxl on 2020/8/6 14:56
 * @version 1.0
 */
@ApiModel(description = "数据源输入高级参数")
public class AdvancedParametersVO {

    @ApiModelProperty(value = "高级参数")
    private List<parameterVo> advancedParameterVo;

    @ApiModelProperty(value = "选项参数")
    private  List<parameterVo> optionsParameterVo;

    @ApiModelProperty(value = "连接池参数")
    private  List<parameterVo>  poolParameterVo;

    @ApiModelProperty(value = "集群参数")
    private  List<parameterVo>  clusterParameterVo;

    public List<parameterVo> getAdvancedParameterVo() {
        return advancedParameterVo;
    }

    public void setAdvancedParameterVo(List<parameterVo> advancedParameterVo) {
        this.advancedParameterVo = advancedParameterVo;
    }

    public List<parameterVo> getOptionsParameterVo() {
        return optionsParameterVo;
    }

    public void setOptionsParameterVo(List<parameterVo> optionsParameterVo) {
        this.optionsParameterVo = optionsParameterVo;
    }

    public List<parameterVo> getPoolParameterVo() {
        return poolParameterVo;
    }

    public void setPoolParameterVo(List<parameterVo> poolParameterVo) {
        this.poolParameterVo = poolParameterVo;
    }

    public List<parameterVo> getClusterParameterVo() {
        return clusterParameterVo;
    }

    public void setClusterParameterVo(List<parameterVo> clusterParameterVo) {
        this.clusterParameterVo = clusterParameterVo;
    }


    @Override
    public String toString() {
        return "AdvancedParametersVO{" +
                "advancedParameterVo=" + advancedParameterVo +
                ", optionsParameterVo=" + optionsParameterVo +
                ", poolParameterVo=" + poolParameterVo +
                ", clusterParameterVo=" + clusterParameterVo +
                '}';
    }






}
