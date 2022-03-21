package com.youngdatafan.dataintegration.core.util.json;

import org.junit.Test;

import java.io.InputStream;

/**
 * @author gavin
 * @since 2020/2/25 11:40 上午
 */
public class XmlTest {

    @Test
    public void test() throws Exception {

        final InputStream resourceAsStream = XmlTest.class.getClassLoader().getResourceAsStream("dp_test.ktr");

        final byte[] data = new byte[resourceAsStream.available()];
        resourceAsStream.read(data);

        String xml = new String(data);

        final JSONLinkedObject jsonObject = XML.toJSONObject(xml);

        final long l = System.currentTimeMillis();
        final String s2 = XML.toString(jsonObject);

        System.out.println(s2);
    }


    @Test
    public void test1() {
        String x = "{\"transformation\":{\"info\":{\"name\":\"数据探索 1\",\"description\":\"\",\"extended_description\":\"\",\"trans_version\":\"\",\"trans_type\":\"Normal\",\"directory\":\"/home/admin\",\"parameters\":\"\",\"log\":\"\",\"maxdate\":{\"connection\":\"\",\"table\":\"\",\"field\":\"\",\"offset\":0,\"maxdiff\":0},\"size_rowset\":10000,\"sleep_time_empty\":50,\"sleep_time_full\":50,\"unique_connections\":\"N\",\"feedback_shown\":\"Y\",\"feedback_size\":50000,\"using_thread_priorities\":\"Y\",\"shared_objects_file\":\"\",\"capture_step_performance\":\"N\",\"step_performance_capturing_delay\":1000,\"step_performance_capturing_size_limit\":100,\"dependencies\":\"\",\"partitionschemas\":\"\",\"slaveservers\":\"\",\"clusterschemas\":\"\",\"created_user\":\"00000000\",\"created_date\":\"2020/02/05 13:34:55.102\",\"modified_user\":\"admin\",\"modified_date\":\"2020/02/19 17:08:25.150\",\"key_for_session_key\":\"\",\"is_key_private\":\"N\"},\"notepads\":\"\",\"connection\":{\"name\":\"test\",\"server\":\"\",\"type\":\"GENERIC\",\"access\":\"Native\",\"database\":\"\",\"port\":0,\"username\":\"root\",\"password\":\"123456\",\"servername\":\"\",\"data_tablespace\":\"\",\"index_tablespace\":\"\",\"attributes\":{\"attribute\":[{\"code\":\"PORT_NUMBER\",\"attribute\":0},{\"code\":\"CUSTOM_DRIVER_CLASS\",\"attribute\":\"com.mysql.jdbc.Driver\"},{\"code\":\"CUSTOM_URL\",\"attribute\":\"jdbc:mysql://tencent.vincenthsing.top:50810/dp_dev?useUnicode=true&characterEncoding=utf-8&useSSL=false\"}]}},\"order\":{\"hop\":[]},\"step\":[{\"name\":\"业务测试模型2\",\"type\":\"BasicModelInput\",\"modelName\":\"testBusinessModel22\",\"description\":null,\"distribute\":\"Y\",\"custom_distribution\":\"\",\"copies\":1,\"partitioning\":{\"method\":\"none\",\"schema_name\":\"\"},\"isBusiness\":\"Y\",\"connection\":\"test\",\"sql\":null,\"limit\":0,\"lookup\":\"\",\"execute_each_row\":\"N\",\"variables_active\":\"N\",\"lazy_conversion_active\":\"N\",\"attributes\":\"\",\"cluster_schema\":\"\",\"remotesteps\":{\"input\":\"\",\"output\":\"\"},\"GUI\":{\"xloc\":176,\"yloc\":160,\"draw\":\"Y\"},\"fields\":{\"field\":[{\"name\":\"model_name\",\"nameCn\":\"模型名称做ID(PK)\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":100,\"precision\":null,\"description\":null},{\"name\":\"description\",\"nameCn\":\"模型描述\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":200,\"precision\":null,\"description\":null},{\"name\":\"model_type\",\"nameCn\":\"模型类型\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":20,\"precision\":null,\"description\":null},{\"name\":\"ds_name\",\"nameCn\":\"关联数据源(FK)\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":100,\"precision\":null,\"description\":null},{\"name\":\"table_schema\",\"nameCn\":\"关联表schema\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":100,\"precision\":null,\"description\":null},{\"name\":\"table_name\",\"nameCn\":\"关联表名\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":100,\"precision\":null,\"description\":null},{\"name\":\"table_chinese_name\",\"nameCn\":\"表中文名称\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":100,\"precision\":null,\"description\":null},{\"name\":\"table_description\",\"nameCn\":\"表描述\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":200,\"precision\":null,\"description\":null},{\"name\":\"statistics_time\",\"nameCn\":\"统计信息收集时间\",\"type\":\"\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":null,\"precision\":null,\"description\":null},{\"name\":\"row_cnt\",\"nameCn\":\"表记录行数\",\"type\":\"\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":10,\"precision\":0,\"description\":null},{\"name\":\"enabled\",\"nameCn\":\"是否启用\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":1,\"precision\":null,\"description\":null},{\"name\":\"create_time\",\"nameCn\":\"创建时间\",\"type\":\"\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":null,\"precision\":null,\"description\":null},{\"name\":\"update_time\",\"nameCn\":\"修改时间\",\"type\":\"\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":null,\"precision\":null,\"description\":null},{\"name\":\"create_user_id\",\"nameCn\":\"创建者\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":50,\"precision\":null,\"description\":null},{\"name\":\"model_sort\",\"nameCn\":\"排序\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":3,\"precision\":null,\"description\":null},{\"name\":\"c_name\",\"nameCn\":\"模型中文名\",\"type\":\"String\",\"format\":\"\",\"currency\":\"\",\"decimal\":\".\",\"group\":\"\",\"nullif\":\"\",\"trim_type\":\"both\",\"length\":100,\"precision\":null,\"description\":null}]}}],\"step_error_handling\":\"\",\"slave-step-copy-partition-distribution\":\"\",\"slave_transformation\":\"N\",\"attributes\":\"\"}}";
        final JSONLinkedObject jsonLinkedObject = new JSONLinkedObject(x);
        final String s = XML.toString(jsonLinkedObject);
        System.out.println(s);
    }
}
