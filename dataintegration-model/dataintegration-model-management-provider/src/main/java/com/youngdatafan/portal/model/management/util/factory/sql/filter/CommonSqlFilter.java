package com.youngdatafan.portal.model.management.util.factory.sql.filter;

import com.youngdatafan.portal.model.management.common.DataTypeUtil;
import com.youngdatafan.portal.model.management.common.entity.ModelFilterVO;
import org.springframework.util.StringUtils;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/7/14 11:31 上午</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class CommonSqlFilter {

    public void appendSql(StringBuffer stringBuffer, ModelFilterVO modelFilterVO) {
        if (!StringUtils.isEmpty(modelFilterVO.getModelColumnVal())) {
            stringBuffer.append(modelFilterVO.getModelColumn() + modelFilterVO.getOpertationSign() + modelFilterVO.getModelColumnVal());
            return;
        }

        if(org.apache.commons.lang.StringUtils.isBlank(modelFilterVO.getRelation())){
            // 默认 and
            modelFilterVO.setRelation("AND");
        }

        String fieldType = modelFilterVO.getColumnType().toLowerCase();

        if (DataTypeUtil.stringValid(fieldType)) {
            if (!StringUtils.isEmpty(modelFilterVO.getFixedValue())) {
                stringBuffer.append(" ").append(modelFilterVO.getRelation()).append(" ")
                        .append(modelFilterVO.getModelColumn() + modelFilterVO.getOpertationSign() + "'" + modelFilterVO.getFixedValue() + "'");
                return;
            }
        } else if (DataTypeUtil.numberValid(fieldType)) {
            if (!StringUtils.isEmpty(modelFilterVO.getFixedValue())) {
                stringBuffer.append(" ").append(modelFilterVO.getRelation()).append(" ")
                        .append(modelFilterVO.getModelColumn() + modelFilterVO.getOpertationSign() + modelFilterVO.getFixedValue());
                return;
            }
        }
    }
}
