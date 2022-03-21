package com.dp.de.run.management.plugin.service;

import com.alibaba.fastjson.JSONArray;
import com.youngdatafan.dataintegration.core.util.json.JSONLinkedObject;
import com.youngdatafan.dataintegration.core.util.json.XML;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.Locale;

/**
 * @author gavin
 * @since 2020/2/15 12:18 下午
 */
public class JsonToXmlTest {

    @Test
    public void getlocal() {
        Locale[] locale = Locale.getAvailableLocales();
        String[] dateLocale = new String[locale.length];
        for (int i = 0; i < locale.length; i++) {
            dateLocale[i] = locale[i].toString();
        }
        System.out.println(JSONArray.toJSON(dateLocale));
    }

    @Test
    public void xmlToJson() throws Exception {
        final InputStream resourceAsStream = JsonToXmlTest.class.getClassLoader().getResourceAsStream("spoon6.ktr");

        final byte[] data = new byte[resourceAsStream.available()];
        IOUtils.readFully(resourceAsStream, data);

        String xml = new String(data);

        final JSONLinkedObject jsonObject = XML.toJSONObject(xml);

        final long l = System.currentTimeMillis();
        final String s2 = XML.toString(jsonObject);
        System.out.println(s2);
        System.out.println(System.currentTimeMillis() - l);

        System.out.println("--------------");
        final String s = jsonObject.toString();
        System.out.println(s);

        System.out.println("--------------");

        JSONLinkedObject jsonObject1 = new JSONLinkedObject(s);
        System.out.println(jsonObject1.toString());

        System.out.println("--------------");

        final String s1 = XML.toString(jsonObject1);
        System.out.println(s1);
    }

    @Test
    public void xmlToJson1() throws Exception {
//        final InputStream resourceAsStream = JsonToXmlTest.class.getClassLoader().getResourceAsStream("many-datasource.ktr");
//
//        final byte[] data = new byte[resourceAsStream.available()];
//        IOUtils.readFully(resourceAsStream, data);
//
//        String xml = new String(data);
//
       /* final JSONLinkedObject jsonObject = XML.toJSONObject("");

        final long l = System.currentTimeMillis();
        final String s2 = XML.toString(jsonObject);
        System.out.println(s2);*/

        final JSONLinkedObject jsonLinkedObject = new JSONLinkedObject("{\"step\":{\"showFileName\":\"111.csv\",\"name\":\"CSV文件输入\",\"type\":\"CsvInput2\",\"filename\":\"s3://192.168.124.166:19000/dp-s3/a57333d9-cb1d-4ab9-afaf-742f7e9df40a/02906f1c513f495fa4cb87963c51d801/85de152d5b114d329584e63bdc6c052f.csv\",\"file_server_type\":\"s3\",\"ftp_username\":\"prime\",\"ftp_password\":\"IYrEJt2R4oMID0nRnavE2A==\",\"description\":\"\",\"distribute\":\"Y\",\"copies\":1,\"partitioning\":{\"method\":\"none\",\"schema_name\":\"\"},\"include_filename\":\"N\",\"separator\":\",\",\"enclosure\":\"\\\"\",\"header\":\"Y\",\"buffer_size\":\"5000\",\"lazy_conversion\":\"Y\",\"add_filename_result\":\"N\",\"parallel\":\"N\",\"newline_possible\":\"N\",\"encoding\":\"UTF-8\",\"format\":\"mixed\",\"fields\":{\"field\":[]},\"remotesteps\":{\"input\":\"\",\"output\":\"\"},\"GUI\":{\"xloc\":176,\"yloc\":160,\"draw\":\"Y\"},\"initFlag\":\"Y\",\"model_name\":\"\",\"delimiter\":\",\",\"fileId\":\"85de152d5b114d329584e63bdc6c052f\",\"tableName\":\"CsvInput04552c4f9ca4\"}}");


        final String s = XML.toString(jsonLinkedObject);
        System.out.println(s);

    }

}
