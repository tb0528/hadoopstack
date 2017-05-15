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
import java.util.ArrayList;
import java.util.List;

/**
 *
 *  插入一个错误的列族
 */
public class PutListErrorExample1 {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));

        List<Put> puts = new ArrayList<Put>();



        Put put1 = new Put(Bytes.toBytes("row1"));
        put1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"),
                Bytes.toBytes("val1"));
        puts.add(put1);

        Put put2 = new Put(Bytes.toBytes("row2"));
    /*[*/
        put2.addColumn(Bytes.toBytes("BOGUS"),/*]*/ Bytes.toBytes("qual1"),
                Bytes.toBytes("val2")); // ErrorPut Add put with non existent family to list.
        puts.add(put2);

        Put put3 = new Put(Bytes.toBytes("row2"));
        put3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"),
                Bytes.toBytes("val3"));
        puts.add(put3);

        table.put(puts);

        table.close();
        connection.close();
        helper.close();
    }
}
