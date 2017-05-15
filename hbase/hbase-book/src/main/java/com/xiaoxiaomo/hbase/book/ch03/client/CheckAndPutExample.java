package com.xiaoxiaomo.hbase.book.ch03.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

/**
 *  原子性插入操作 using the atomic compare-and-set operations.
 *
 *  检查相关信息，并决定是否修改数据操作，常用于账户结余，状态转换等。
 *
 *  注意：下面的示例4会抛出异常
 *      因为HBase提供的compare-and-set操作，只能检查和修改同一行数据，与其他的许多操作一样，这个操作只提供同一行数据的原子性保证。
 *      检查和修改分别针对不同行数据时会抛出异常。
 *
 *      这个操作十分强大，尤其是在分布式系统中，且有多个独立的客户端同时操作数据时。
 *      通过这个方法，HBase与其他复杂的设计结构区分了开来，提供不同客户端可以并发修改数据的功能。
 *
 *
 *
 */
public class CheckAndPutExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));

        // 用例
        Put put1 = new Put(Bytes.toBytes("row1"));
        put1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"));

        Put put2 = new Put(Bytes.toBytes("row1"));
        put2.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"), Bytes.toBytes("val2"));

        Put put3 = new Put(Bytes.toBytes("row2"));
        put3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val3"));

        //1. 参数为null，即检查到不存在就添加put1,返回true
        boolean res1 = table.checkAndPut(Bytes.toBytes("row1"),
                Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), null, put1);
        System.out.println(res1);

        //2. 再次添加，即检查到不存在就添加put1,但是已经存在。所以就不会添加返回false
        boolean res2 = table.checkAndPut(Bytes.toBytes("row1"),
                Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), null, put1);
        System.out.println(res2);


        //3. 检查put1是否存在，存在就添加put2，返回true
        boolean res3 = table.checkAndPut(Bytes.toBytes("row1"),
                Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"),
                Bytes.toBytes("val1"), put2);
        System.out.println(res3);


        //4. 这里会抛出异常：org.apache.hadoop.hbase.DoNotRetryIOException: Action's getRow must match the passed row
        boolean res4 = table.checkAndPut(Bytes.toBytes("row1"),
                Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"), put3);


        table.close();
        connection.close();
        helper.close();
    }
}
