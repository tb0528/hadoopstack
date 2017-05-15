package com.xiaoxiaomo.hbase.book.ch04.filters;

// cc ColumnPrefixFilterExample Example filtering by column prefix

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

public class ColumnPrefixFilterExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");
        System.out.println("Adding rows to table...");
        helper.fillTable("testtable", 1, 10, 30, 0, true, "colfam1");

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));
        // vv ColumnPrefixFilterExample
        Filter filter = new ColumnPrefixFilter(Bytes.toBytes("col-1"));

        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        // ^^ ColumnPrefixFilterExample
        System.out.println("Results of scan:");
        // vv ColumnPrefixFilterExample
        for (Result result : scanner) {
            System.out.println(result);
        }
        scanner.close();
        // ^^ ColumnPrefixFilterExample
    }
}
