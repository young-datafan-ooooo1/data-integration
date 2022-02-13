package com.youngdatafan.di.run.management.server.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtills {


    public static final String VARIABLE_REGEX = "\\$\\{ *(\\w+) *(: *(.+?) *?[^\\\\])?\\}";

    public static String parseSql(String sql) {


        StringBuilder replaceParameterInterfaceJson = new StringBuilder(
                Double.valueOf(sql.length() * 1.5).intValue());
        Pattern compile = Pattern.compile(VARIABLE_REGEX, Pattern.DOTALL);
        Matcher matcher = compile.matcher(sql);
        int lastIndex = 0;
        while (matcher.find()) { // 根据正则匹配变量
            replaceParameterInterfaceJson.append(sql, lastIndex, matcher.start());
            lastIndex = matcher.end();

        }
        replaceParameterInterfaceJson.append(sql.substring(lastIndex));
        return replaceParameterInterfaceJson.toString();


    }

}
