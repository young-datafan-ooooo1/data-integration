package com.youngdatafan.di.run.management.steps.utils;

import com.youngdatafan.di.run.management.steps.csvinput.vo.FieldVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * @author gavin
 * @since 2020/2/22 12:04 下午
 */
@Slf4j
public class TypeCastUtils {
    /**
     * 设置字段类型
     *
     */
    public static void typeCast(String str, FieldVO fieldVO) {
        if(StringUtils.isBlank(str)){
            fieldVO.setLen(200);
            fieldVO.setPrecision(0);
            fieldVO.setType(ValueMetaInterface.getTypeDescription(ValueMetaInterface.TYPE_STRING));
        }else if(str.matches("^[1-9]\\d*|0$")){
            fieldVO.setLen(15);
            fieldVO.setPrecision(0);
            fieldVO.setFormat("#");
            fieldVO.setType(ValueMetaInterface.getTypeDescription(ValueMetaInterface.TYPE_INTEGER));
        }else if(str.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$")){
            fieldVO.setLen(38);
            fieldVO.setPrecision(str.split("\\.")[1].length());
            fieldVO.setFormat("#.#");
            fieldVO.setType(ValueMetaInterface.getTypeDescription(ValueMetaInterface.TYPE_BIGNUMBER));

        }else if(str.matches("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$")){
            if(str.contains("-")){
                fieldVO.setFormat("yyyy-MM-dd");
            }else if(str.contains("/")){
                fieldVO.setFormat("yyyy/MM/dd");
            }else {
                fieldVO.setFormat("yyyyMMdd");
            }
            fieldVO.setType(ValueMetaInterface.getTypeDescription(ValueMetaInterface.TYPE_DATE));
        }else if(str.matches("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1]?[0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$")){
            if(str.contains("-")){
                fieldVO.setFormat("yyyy-MM-dd HH:mm:ss");
            }else if(str.contains("/")){
                fieldVO.setFormat("yyyy/MM/dd HH:mm:ss");
            }else {
                fieldVO.setFormat("yyyyMMdd HH:mm:ss");
            }
            fieldVO.setType(ValueMetaInterface.getTypeDescription(ValueMetaInterface.TYPE_TIMESTAMP));
        }else {
            fieldVO.setLen(200);
            fieldVO.setPrecision(0);
            fieldVO.setType(ValueMetaInterface.getTypeDescription(ValueMetaInterface.TYPE_STRING));
        }

    }

}
