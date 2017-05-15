package com.xiaoxiaomo.hbase.book.ch04.filters;

// cc ColumnPaginationFilterExample Example paginating through columns in a row

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.Filter;
import util.HBaseHelper;

import java.io.IOException;

public class ColumnPaginationFilterExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");
        System.out.println("Adding rows to table...");
        helper.fillTable("testtable", 1, 10, 30, 2, true, "colfam1");

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));
        // vv ColumnPaginationFilterExample
        Filter filter = new ColumnPaginationFilter(5, 15);

        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        // ^^ ColumnPaginationFilterExample
        System.out.println("Results of scan:");
        // vv ColumnPaginationFilterExample
        for (Result result : scanner) {
            System.out.println(result);
        }
        scanner.close();
        // ^^ ColumnPaginationFilterExample
    }
}
