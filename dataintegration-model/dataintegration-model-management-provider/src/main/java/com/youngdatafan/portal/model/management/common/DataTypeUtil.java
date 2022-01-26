package com.youngdatafan.portal.model.management.common;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : 判断字段类型</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/5/18 4:52 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class DataTypeUtil {
    private static List<String> numberList = new ArrayList<String>() {{
        add("number");
        add("bigNumber");
        add("integer");
    }};
    private static List<String> stringList = new ArrayList<String>() {{
        add("string");
    }};

    private static List<String> datesList = new ArrayList<String>() {{
        add("date");
        add("datetime");
        add("timestamp");
    }};


    public static Boolean stringValid(String fieldType) {
        return stringList.contains(fieldType.toLowerCase());
    }

    public static Boolean dateValid(String fieldType) {
        return datesList.contains(fieldType.toLowerCase());
    }

    public static Boolean numberValid(String fieldType) {
        return numberList.contains(fieldType.toLowerCase());
    }

    public static Boolean isDateValid(String fieldType) {
        return "date".contains(fieldType.toLowerCase());
    }

    public static Boolean isDatetimeValid(String fieldType) {
        return "datetime".contains(fieldType.toLowerCase());
    }

    public static Boolean isTimestampValid(String fieldType) {
        return "timestamp".contains(fieldType.toLowerCase());
    }


}
