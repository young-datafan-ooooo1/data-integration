package com.youngdatafan.di.run.management.server.util;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * 动态表达式工具类
 *
 * @author gavin
 */
public class AviatorUtils {

    static {
        // 日期函数add操作
        AviatorEvaluator.addFunction(new AbstractFunction() {

            @Override
            public String getName() {
                return "addDate";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
                final Date date = (Date) arg1.getValue(env);
                final String field = String.valueOf(arg2.getValue(env));
                final Number amount = FunctionUtils.getNumberValue(arg3, env);

                int fieldValue;
                if (field.matches("\\d+")) {
                    fieldValue = Integer.parseInt(field);
                } else {
                    fieldValue = CalendarFieldEnum.valueOf(field).getValue();
                }

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(fieldValue, amount.intValue());

                return new AviatorRuntimeJavaType(c.getTime());
            }
        });

        // dateForamt
        AviatorEvaluator.addFunction(new AbstractFunction() {

            @Override
            public String getName() {
                return "dateFormat";
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                final Object value = arg1.getValue(env);
                Date date;
                if (value instanceof String) {
                    try {
                        date = DateUtils.parseDate((String) value, new String[]{"yyyy-MM-dd", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss",});
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    date = (Date) value;
                }

                final String format = FunctionUtils.getStringValue(arg2, env);

                return new AviatorRuntimeJavaType(DateFormatUtils.format(date, format));
            }
        });
    }

    /**
     * 执行表达式
     *
     * @param expression 表达式
     * @return 字符串
     */
    public static String execute(String expression) {
        return AviatorEvaluator.execute(expression).toString();
    }

}
