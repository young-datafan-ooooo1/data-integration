/*
package com.youngdatafan.portal.model.management.datasource.api;

import ModelManagerApplication;
import DatasourceExplainDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import com.datafan.dataintegration.core.util.sql.DatabaseType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*/
/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/10 9:03 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ModelManagerApplication.class, DatasourceControllerTests.class})
public class DatasourceControllerTests {


    private static final Pattern URL_PATTERN = Pattern.compile("([\\w.]+):(\\d+)[:/]?([\\w.]+)?");


    private static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();

    //    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Before
//    public void setUp() throws Exception {
//        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
//    }
//
//    @Test
//    public void testAdd() throws Exception {
//        DatasourceVO datasourceVO = new DatasourceVO();
//
//        datasourceVO.setCreateTime(new Date());
//        datasourceVO.setCreateUserId("1");
//        datasourceVO.setDsName("test");
//
//        datasourceVO.setDriverClassName("com.mysql.jdbc.Driver");
//        datasourceVO.setDescribe("测试");
//        datasourceVO.setDsType("mysql");
//        datasourceVO.setEnabled("1");
//        datasourceVO.setDsPassword("123456");
//        datasourceVO.setDsUsername("root");
//        datasourceVO.setUpdateTime(new Date());
//        datasourceVO.setDsUrl("jdbc:mysql://ddns.hmshzx2403.top:50826/dp_dev?useUnicode=true&characterEncoding=utf-8&useSSL=false");
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/datasource/add")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtils.toString(datasourceVO)))
//                .andReturn();
//        int status = mvcResult.getResponse().getStatus();
//        String content = mvcResult.getResponse().getContentAsString();
//        System.out.println("----------------------");
//        System.out.println(content);
//
//        Assert.assertEquals(200, status);
//    }
//
//    @Test
//    public void testUpdate() throws Exception {
//        DatasourceVO datasourceVO = new DatasourceVO();
//
//        datasourceVO.setCreateTime(new Date());
//        datasourceVO.setCreateUserId("1");
//        datasourceVO.setDsName("test");
//
//        datasourceVO.setDriverClassName("com.mysql.jdbc.Driver");
//        datasourceVO.setDescribe("测试22");
//
//        datasourceVO.setDsUsername("root");
//        datasourceVO.setUpdateTime(new Date());
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/datasource/update")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtils.toString(datasourceVO)))
//                .andReturn();
//        int status = mvcResult.getResponse().getStatus();
//        String content = mvcResult.getResponse().getContentAsString();
//        System.out.println("----------------------");
//        System.out.println(content);
//
//        Assert.assertEquals(200, status);
//    }
//
//    @Test
//    public void testDelete() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/datasource/delete/test"))
//                .andReturn();
//
//        int status = mvcResult.getResponse().getStatus();
//        String content = mvcResult.getResponse().getContentAsString();
//        System.out.println("----------------------");
//        System.out.println(content);
//
//        Assert.assertEquals(200, status);
//    }
//
//    @Test
//    public void testSelect() throws Exception {
//        DatasourceVO datasourceVO = new DatasourceVO();
//
//        datasourceVO.setCreateTime(new Date());
//        datasourceVO.setCreateUserId("1");
//        datasourceVO.setDsName("test");
//
//        datasourceVO.setDriverClassName("com.mysql.jdbc.Driver");
//        datasourceVO.setDescribe("测试22");
//
//        datasourceVO.setDsUsername("root");
//        datasourceVO.setUpdateTime(new Date());
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/datasource/selectAll").header("userId", "1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//        int status = mvcResult.getResponse().getStatus();
//        String content = mvcResult.getResponse().getContentAsString();
//        System.out.println("----------------------");
//        System.out.println(content);
//
//        Assert.assertEquals(200, status);
//    }
//
//
//    @Test
//    public void testGetDatasourcetype() throws Exception {
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/datasource/datasourceType")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//        int status = mvcResult.getResponse().getStatus();
//        String content = mvcResult.getResponse().getContentAsString();
//        System.out.println("----------------------");
//        System.out.println(content);
//
//        Assert.assertEquals(200, status);
//    }
//
//    @Test
//    public void testConnectorDatasource() throws Exception {
//        DatasourceConnectorVO datasourceVO = new DatasourceConnectorVO();
//
//        datasourceVO.setDsType("mysql");
//
//        datasourceVO.setDsPassword("123456");
//
//        datasourceVO.setDsUsername("root");
//
//        datasourceVO.setDsUrl("jdbc:mysql://ddns.hmshzx2403.top:50826/dp_dev?useUnicode=true&characterEncoding=utf-8&useSSL=false");
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/datasource/connector")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtils.toString(datasourceVO)))
//                .andReturn();
//        int status = mvcResult.getResponse().getStatus();
//        String content = mvcResult.getResponse().getContentAsString();
//        System.out.println("----------------------");
//        System.out.println(content);
//
//        Assert.assertEquals(200, status);
//    }
//
//    private static final Pattern URL_PATTERN = Pattern.compile("([\\w.]+):(\\d+)[:/]?([\\w.]+)?");
//
//    @Test
//    public void getTables() {
//        final Matcher matcher = URL_PATTERN.matcher("jdbc:clickhouse://tencent.vincenthsing.top:50812/default");
//        if (matcher.find()) {
//            System.out.println(matcher.group(1));
//            System.out.println(matcher.group(2));
//            System.out.println(matcher.group(3));
//
//        }
//
//    }
    @Test
    public void geDatabaseInfo() throws Exception {

        Map map = new HashMap();
        // 初始化kettle环境，只需要初始化一次
        KettleEnvironment.init(false);

        DatabaseMeta databaseMeta = new DatabaseMeta(UUID.randomUUID().toString()
                , DatabaseType.CLICKHOUSE.name(), "JDBC", "tencent.vincenthsing.top"
                , "default", "50812", "default", "jRtXD8F8");
        final Database database = new Database(databaseMeta);

        database.connect();

        // 模式schema
        final String[] schemas = database.getSchemas();
        for (String schema : schemas) {
            System.out.println("----------");

            // 表
            final String[] tablenames = database.getTablenames(schema, true);
            for (String tablename : tablenames) {
                System.out.println(tablename);
                System.out.println("####");

                final RowMetaInterface tableFields = database.getTableFields("pdi.pdi_ccard_model_data");
                final int size = tableFields.size();
                for (int i = 0; i < size; i++) {
                    final ValueMetaInterface valueMeta = tableFields.getValueMeta(i);

                    // 字段信息
                    final String fieldName = valueMeta.getName();
                    final String fieldType = ValueMetaInterface.getTypeDescription(valueMeta.getType());
                    final int length = valueMeta.getLength();
                    final int precision = valueMeta.getPrecision();
                    System.out.println(new StringJoiner(":").add(fieldName).add(fieldType)
                            .add(String.valueOf(length)).add(String.valueOf(precision)));
                }

                break;
            }
        }

        database.disconnect();
    }

    @Test
    public void obj() {
        String s = "{ \"tests\": {  \"DeviceID\": \"11539982000501\", \"DeviceName\": \"温度1\" }}";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Tests t = objectMapper.readValue(s, Tests.class);
            System.out.println(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Tests {
        private String deviceID;
        private String deviceName;

        public Tests() {
        }


        public String getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(String deviceID) {
            this.deviceID = deviceID;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        @Override
        public String toString() {
            return "Tests{" +
                    "deviceID='" + deviceID + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    '}';
        }
    }

    @Test
    public void getmax() {
        System.out.println(lengthOfLongestSubstring("abcowdods"));
    }

    //  查字符串最长不重复长度
    public int lengthOfLongestSubstring(String s) {

        Map<Character, Integer> map = new HashMap<>();

        int left = 0;
        int max = 0;
        for (int i = 0, j = s.length(); i < j; i++) {

            if (map.containsKey(s.charAt(i))) {
                left = Math.max(left, map.get(s.charAt(i)) + 1);
            }

            char c = s.charAt(i);
            map.put(c, i);

            max = Math.max(max, i - left + 1);
        }
        return max;
    }

    @Test
    public void findMedianSortedArrays() throws JsonProcessingException {
        String json = "{\"code\":\"10000\",\"msg\":null,\"content\":[{\"country\":\"中华人民共和国\",\"etl_src\":\"人事系统\",\"person_sex\":\"女\",\"identity_id\":\"32068119910311442X\",\"nation\":\"汉族\",\"graduate_ym\":\" \",\"person_type\":\"教职工\",\"person_name\":\"陈晓波\",\"person_type_detail\":\"编制类人员\",\"person_status\":\"在职\",\"person_pid\":\"10010241\",\"person_school_years\":\"2017-10\",\"etl_src_detail\":\"人事系统\",\"person_sid\":\"20073\",\"identity_type\":\"身份证\"},{\"country\":\"中华人民共和国\",\"etl_src\":\"一体化系统\",\"person_sex\":\"女\",\"identity_id\":\"32068119910311442X\",\"nation\":\"汉族\",\"graduate_ym\":\"2016-06-03\",\"person_type\":\"学生\",\"person_name\":\"陈晓波\",\"person_type_detail\":\"硕士研究生\",\"person_status\":\"离校\",\"person_pid\":\"10010241\",\"person_school_years\":\"2013-09-01\",\"etl_src_detail\":\"研究生系统\",\"person_sid\":\"1336288\",\"identity_type\":\"身份证\"},{\"country\":\"中华人民共和国\",\"etl_src\":\"人事系统\",\"person_sex\":\"女\",\"identity_id\":\"32068119910311442X\",\"nation\":\"汉族\",\"graduate_ym\":\"2020-06\",\"person_type\":\"教职工\",\"person_name\":\"陈晓波\",\"person_type_detail\":\"派遣人员\",\"person_status\":\"离校\",\"person_pid\":\"10010241\",\"person_school_years\":\"2017-10\",\"etl_src_detail\":\"人事系统\",\"person_sid\":\"17666554\",\"identity_type\":\"身份证\"}]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode info = mapper.readTree(json);
        String[] cardNos = new String[info.get("content").size()];
        int isTrue = -1;
        for (int i = 0; i < info.get("content").size(); i++) {
            if ("20073".equals(info.get("content").get(i).get("person_sid").asText())) {
                isTrue = 1;
            }
        }


        System.out.println(isTrue);


    }

    @Test
    public void main() {
        System.out.println(smallArrary());
    }

    @Test
    public void smallArrary1() {
        int[] array = new int[]{1, 3, -1, 3, 6, 9, 10};
        Integer length = array.length - 1;

        int min = 0;
        int start = 0;
        int max = 0;
        Integer s = 0;
        Integer e = 0;

        for (int i = 0; i < length; i++) {
            if (array[i] > array[i + 1]) {
                min = i + 1;
                //找到降序点，并且存差距最大的两个降序点
                if (min - start > max) {
                    max = min - start;
                    e = min;
                    s = start;
                }
                start = min;
            }
        }

        //这里判断没有降序点的情况
        if (max == 0) {
            for (int i = 0; i < array.length; i++) {
                System.out.println(array[i]);
            }
        }

        if (start == min) {
            //这里判断只有一个降序点的情况 如果降序点大于中位数就取前面的，小于中位数就取后面的，等于中位数就取两边
            if (array.length / 2 > start) {

            } else {

            }
        }

        //这里判断有大于2个降序点的情况
        for (int j = s; j < e; j++) {
            System.out.println(array[j]);
        }
    }

    private int[] smallArrary() {
        int[] array = new int[]{1, 3, -1, 5, 8, 7, 3, 6, 9, 10, -1};
        List<Integer> result = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();
        Integer length = array.length - 1;
        for (int i = 0; i < length; i++) {
            System.out.println("i----->" + i);
            if (array[i] > array[i + 1]) {
                indexList.add(i + 1);
            }
        }
        Integer size = indexList.size();
        if (size == 0) {
            return array;
        } else if (size == 1) {
            Integer index = indexList.get(0);
            if (index <= (length + 2) / 2) {
                for (int i = 0; i < length; i++) {
                    if (i >= index) {
                        result.add(array[i]);
                    }
                }
            } else {
                for (int i = 0; i < length; i++) {
                    if (i < index) {
                        result.add(array[i]);
                    }
                }
            }
            return result.stream().mapToInt(Integer::intValue).toArray();
        } else {
            System.out.println("indexList" + indexList.toString());
            Integer min = 0;
            Integer max = 0;
            Integer maxLength = 0;
            for (int i = 0; i < size - 1; i++) {
                Integer temp;
                temp = indexList.get(i + 1) - indexList.get(i);
                if (maxLength < temp) {
                    maxLength = temp;
                    min = indexList.get(i);
                    max = indexList.get(i + 1);
                }
            }
            System.out.println("min" + min);
            System.out.println("max" + max);
            for (int i = 0; i < length; i++) {
                if (min <= i && i < max) {
                    result.add(array[i]);
                }

            }
            System.out.println(result.toString());
            return result.stream().mapToInt(Integer::intValue).toArray();
        }

    }

    @Test
    public void dropTable() throws Exception {

        KettleEnvironment.init(false);

        DatasourceExplainDTO datasourceExplainDTO = explainURL("jdbc:clickhouse://10.242.10.170:8123/default");

        if (StringUtils.isEmpty(datasourceExplainDTO)) {
            throw new RuntimeException();
        }

        String password = PASSWORD_ENCODER.decode("Encrypted 2be98afc86aa7f2e4a12bba48faca8982");

        DatabaseMeta databaseMeta = new DatabaseMeta("1"
                , DatabaseType.CLICKHOUSE.name(), "JDBC", datasourceExplainDTO.getHost()
                , datasourceExplainDTO.getDb(), datasourceExplainDTO.getPort(), "default", password);

        final Database database = new Database(databaseMeta);

        database.connect();

        PreparedStatement preparedStatement = database.prepareSQL("select *  from pdi.type_test");

        ResultSet resultSet = preparedStatement.executeQuery();

        database.disconnect();
        database.disconnect();
        preparedStatement.close();
        resultSet.close();

        Thread.sleep(2000);
//
//        System.out.println(11111);
//        System.out.println(preparedStatement.isClosed());
//        System.out.println(resultSet.isClosed());


    }

    public static DatasourceExplainDTO explainURL(String dbUrl) {
        final Matcher matcher = URL_PATTERN.matcher(dbUrl);
        DatasourceExplainDTO datasourceExplainDTO = new DatasourceExplainDTO();

        if (matcher.find()) {
            datasourceExplainDTO.setHost(matcher.group(1));
            datasourceExplainDTO.setPort(matcher.group(2));
            datasourceExplainDTO.setDb(matcher.group(3));
        }

        return datasourceExplainDTO;
    }
}
*/
