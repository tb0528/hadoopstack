package com.xiaoxiaomo.hbase.book.ch03.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

/**
 *
 * 一个简单的Get操作
 */
public class GetExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseHelper helper = HBaseHelper.getHelper(conf);
        if (!helper.existsTable("testtable")) {
            helper.createTable("testtable", "colfam1");
        }

        Connection connection = helper.getConnection();
        Table table = connection.getTable(TableName.valueOf("testtable"));

        Get get = new Get(Bytes.toBytes("row1"));
        get.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));

        Result result = table.get(get);
        System.out.println("Value: " + Bytes.toString(result.getValue(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"))));

        table.close();
        connection.close();
        helper.close();
    }
}
