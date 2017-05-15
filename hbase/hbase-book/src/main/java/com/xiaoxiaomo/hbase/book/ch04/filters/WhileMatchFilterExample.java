package com.xiaoxiaomo.hbase.book.ch04.filters;

// cc WhileMatchFilterExample Example of using a filter to skip entire rows based on another filter's results

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

public class WhileMatchFilterExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");
        System.out.println("Adding rows to table...");
        helper.fillTable("testtable", 1, 10, 1, 2, true, false, "colfam1");

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));
        // vv WhileMatchFilterExample
        Filter filter1 = /*[*/new RowFilter(CompareFilter.CompareOp.NOT_EQUAL,
                new BinaryComparator(Bytes.toBytes("row-05")));/*]*/

        Scan scan = new Scan();
        scan.setFilter(filter1);
        ResultScanner scanner1 = table.getScanner(scan);
        // ^^ WhileMatchFilterExample
        System.out.println("Results of scan #1:");
        int n = 0;
        // vv WhileMatchFilterExample
        for (Result result : scanner1) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
                // ^^ WhileMatchFilterExample
                n++;
                // vv WhileMatchFilterExample
            }
        }
        scanner1.close();

        Filter filter2 = new /*[*/WhileMatchFilter(filter1);/*]*/

        scan.setFilter(filter2);
        ResultScanner scanner2 = table.getScanner(scan);
        // ^^ WhileMatchFilterExample
        System.out.println("Total cell count for scan #1: " + n);
        n = 0;
        System.out.println("Results of scan #2:");
        // vv WhileMatchFilterExample
        for (Result result : scanner2) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
                // ^^ WhileMatchFilterExample
                n++;
                // vv WhileMatchFilterExample
            }
        }
        scanner2.close();
        // ^^ WhileMatchFilterExample
        System.out.println("Total cell count for scan #2: " + n);
    }
}
