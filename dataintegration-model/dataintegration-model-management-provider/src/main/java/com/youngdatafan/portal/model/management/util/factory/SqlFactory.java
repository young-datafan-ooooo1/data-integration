package com.youngdatafan.portal.model.management.util.factory;

import com.youngdatafan.portal.model.management.util.enums.DataSourceTypeEnum;
import com.youngdatafan.portal.model.management.util.factory.sql.SqlJoin;
import com.youngdatafan.portal.model.management.util.factory.sql.clickhouse.ClickhouseSql;
import com.youngdatafan.portal.model.management.util.factory.sql.mysql.MysqlSql;
import com.youngdatafan.portal.model.management.util.factory.sql.oracle.OracleSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/7/2 5:16 下午</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Component
public class SqlFactory {
    @Autowired
    MysqlSql mysqlSql;

    @Autowired
    OracleSql oracleSql;

    @Autowired
    ClickhouseSql clickhouseSql;

    private static SqlFactory instance;

    private SqlFactory() {
    }

    public static synchronized SqlFactory getInstance() {
        if (instance == null) {
            instance = new SqlFactory();
        }
        return instance;
    }

    public SqlJoin getDatasourceEntity(String type) {
        if (type == null) {
            return null;
        }
        if (type.toUpperCase().equalsIgnoreCase(DataSourceTypeEnum.CLICKHOUSE.code())) {
            return clickhouseSql;
        } else if (type.toUpperCase().equalsIgnoreCase(DataSourceTypeEnum.MYSQL.code())) {
            return mysqlSql;
        } else if (type.toUpperCase().equalsIgnoreCase(DataSourceTypeEnum.ORACLE.code())) {
            return oracleSql;
        } else {
            return mysqlSql;
        }
    }
}
