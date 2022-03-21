package com.youngdatafan.dataintegration.core.util.sql;

/**
 * 数据库类型和kettle-database-types.xml 保持一致.
 *
 * @author gavin
 */
public enum DatabaseType {

    /**
     * 华为elk.
     */
    ELK,

    /**
     * mysql.
     */
    MYSQL,

    /**
     * postgresql.
     */
    POSTGRESQL,

    /**
     * oracle.
     */
    ORACLE,

    /**
     * td.
     */
    TERADATA,

    /**
     * sql server.
     */
    MSSQL,

    /**
     * DB2.
     */
    DB2,

    /*
     * clickhouse.
     */
    CLICKHOUSE,

    /*
     * 金山云KDW.
     */
    KDW,

    /**
     * HADOOP_HIVE2.
     */
    HADOOP_HIVE2,

    /*
     * Spark.
     */
    SPARK;
}
