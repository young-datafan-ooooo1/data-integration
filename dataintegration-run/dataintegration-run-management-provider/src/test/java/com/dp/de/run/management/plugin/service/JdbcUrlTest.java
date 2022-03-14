package com.dp.de.run.management.plugin.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gavin
 * @since 2020/2/28 2:25 下午
 */
public class JdbcUrlTest {

    private static final Pattern URL_PATTERN = Pattern.compile("([\\w.]+):(\\d+)[:/]?([\\w.]+)?");

    public static void main(String[] args) {
        final Matcher matcher = URL_PATTERN.matcher("jdbc:oracle:thin:@//localhost:1521");
        if(matcher.find()){
            System.out.println( matcher.group(1));
            System.out.println( matcher.group(2));
            System.out.println( matcher.group(3));
        }
    }
}
