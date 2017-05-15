package com.xiaoxiaomo.hbase.book.ch04.filters;

// cc FuzzyRowFilterExample Example filtering by column prefix

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FuzzyRowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import util.HBaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FuzzyRowFilterExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");
        System.out.println("Adding rows to table...");
        helper.fillTable("testtable", 1, 20, 10, 2, true, "colfam1");

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));
        // vv FuzzyRowFilterExample
        List<Pair<byte[], byte[]>> keys = new ArrayList<Pair<byte[], byte[]>>();
        keys.add(new Pair<byte[], byte[]>(
                Bytes.toBytes("row-?5"), new byte[]{0, 0, 0, 0, 1, 0}));
        Filter filter = new FuzzyRowFilter(keys);

        Scan scan = new Scan()
                .addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("col-5"))
                .setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        // ^^ FuzzyRowFilterExample
        System.out.println("Results of scan:");
        // vv FuzzyRowFilterExample
        for (Result result : scanner) {
            System.out.println(result);
        }
        scanner.close();
        // ^^ FuzzyRowFilterExample
    }
}
