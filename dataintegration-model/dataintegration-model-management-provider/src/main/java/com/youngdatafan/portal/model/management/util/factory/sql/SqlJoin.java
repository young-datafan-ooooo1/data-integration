package com.youngdatafan.portal.model.management.util.factory.sql;

import com.youngdatafan.portal.model.management.common.entity.BasicModelMetaDataVO;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/7/2 5:00 下午</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public  interface SqlJoin {
    /**
     *
     * @param basicModelMetaDataVOS 元数据信息
     * @param modelFilterVOS 过滤条件
     * @param tableName 表名
     * @param isLimit 是否限制条数
     * @return
     */
    StringBuffer getStringBuffer(List<BasicModelMetaDataVO> basicModelMetaDataVOS, List<ModelFilterVO> modelFilterVOS, String tableName, Boolean isLimit);


}
