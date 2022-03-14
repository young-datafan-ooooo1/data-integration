package com.dp.de.run.management.plugin.service;

/**
 * @author gavin
 * @since 2020/4/28 5:45 下午
 */
public class ClickHouseDDlReplaceTest {
    public static void main(String[] args) {
        String s = "CREATE TABLE \n" +
                "(\n" +
                "  plan_id Nullable(String)\n" +
                ", task_id Nullable(String)\n" +
                ", job_id Nullable(String)\n" +
                ", instance_id Nullable(String)\n" +
                ", start_time Nullable(DATETIME)\n" +
                ", end_time Nullable(DATETIME)\n" +
                ", batch_date DATE\n" +
                ", status Nullable(String)\n" +
                ", return_value Nullable(String)\n" +
                ")\n" +
                "ENGINE = MergeTree()  ORDER BY (batch_date);";

        System.out.println(s.replaceFirst("start_time Nullable\\((\\w+)\\)", "start_time $1"));
    }
}
