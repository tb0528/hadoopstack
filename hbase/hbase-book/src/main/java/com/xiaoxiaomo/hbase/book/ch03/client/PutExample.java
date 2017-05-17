package com.xiaoxiaomo.hbase.book.ch03.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

// ^^ PutExample
// vv PutExample

/**
 *
 *  一个简单的单行插入操作
 */
public class PutExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");


        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));

        Put put = new Put(Bytes.toBytes("row1"));

//        put.setDurability(Durability.SKIP_WAL);     //跳过
//        put.setDurability(Durability.ASYNC_WAL);    //异步
//        put.setDurability(Durability.SYNC_WAL);     //同步，默认

        put.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1")); //  Add a column, whose name is "colfam1:qual1", to the put.
        put.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"), Bytes.toBytes("val2")); // Add another column, whose name is "colfam1:qual2", to the put.

        table.put(put);
        table.close(); // Close table and connection instances to free resources.
        connection.close();
        helper.close();
    }
}
