package com.xiaoxiaomo.hbase.book.ch04.filters;

// cc PrefixFilterExample Example using the prefix based filter

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

public class PrefixFilterExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1", "colfam2");
        System.out.println("Adding rows to table...");
        helper.fillTable("testtable", 1, 10, 10, "colfam1", "colfam2");

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));
        // vv PrefixFilterExample
        Filter filter = new PrefixFilter(Bytes.toBytes("row-1"));

        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        // ^^ PrefixFilterExample
        System.out.println("Results of scan:");
        // vv PrefixFilterExample
        for (Result result : scanner) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
            }
        }
        scanner.close();

        Get get = new Get(Bytes.toBytes("row-5"));
        get.setFilter(filter);
        Result result = table.get(get);
        // ^^ PrefixFilterExample
        System.out.println("Result of get: ");
        // vv PrefixFilterExample
        for (Cell cell : result.rawCells()) {
            System.out.println("Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }
        // ^^ PrefixFilterExample
    }
}
