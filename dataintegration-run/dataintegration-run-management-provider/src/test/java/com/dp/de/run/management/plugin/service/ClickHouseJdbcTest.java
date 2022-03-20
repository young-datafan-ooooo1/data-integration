package com.dp.de.run.management.plugin.service;

import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import com.youngdatafan.dataintegration.core.util.sql.DatabaseType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author gavin
 * @since 2020/2/29 12:02 下午
 */
public class ClickHouseJdbcTest {

    @Test
    public void test() throws Exception {
        final Connection connection = DriverManager.getConnection("jdbc:clickhouse://tencent.vincenthsing.top:50812/default"
                , "default", "jRtXD8F8");

        final PreparedStatement preparedStatement = connection.prepareStatement("select * from pdi.type_test where d1 = ?");
        preparedStatement.setDouble(1, 1.10);

        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getBigDecimal(3));
        }

        resultSet.close();
        preparedStatement.clearParameters();
        connection.createStatement();
    }


    @Test
    public void geDatabaseInfo() throws Exception {
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
    public void testInsert() throws Exception {
        final Connection connection = DriverManager.getConnection("jdbc:clickhouse://tencent.vincenthsing.top:50812/default"
                , "default", "jRtXD8F8");

        final PreparedStatement preparedStatement = connection.prepareStatement("insert into pdi.pdi_ccard_model_data(Data_Dt,Cust_Id) values(?,?)");
        preparedStatement.setString(1, "2020-04-18");
        preparedStatement.setString(2, "2020-04-18");

        System.out.println(preparedStatement.executeUpdate());

        preparedStatement.clearParameters();
        connection.createStatement();
    }
}
