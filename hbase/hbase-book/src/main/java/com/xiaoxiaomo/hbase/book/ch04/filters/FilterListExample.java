package com.xiaoxiaomo.hbase.book.ch04.filters;

// cc FilterListExample Example of using a filter list to combine single purpose filters

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterListExample {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");
        System.out.println("Adding rows to table...");
        helper.fillTable("testtable", 1, 10, 5, 2, true, false, "colfam1");

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("testtable"));
        // vv FilterListExample
        List<Filter> filters = new ArrayList<Filter>();

        Filter filter1 = new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes("row-03")));
        filters.add(filter1);

        Filter filter2 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes("row-06")));
        filters.add(filter2);

        Filter filter3 = new QualifierFilter(CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator("col-0[03]"));
        filters.add(filter3);

        FilterList filterList1 = new FilterList(filters);

        Scan scan = new Scan();
        scan.setFilter(filterList1);
        ResultScanner scanner1 = table.getScanner(scan);
        // ^^ FilterListExample
        System.out.println("Results of scan #1 - MUST_PASS_ALL:");
        int n = 0;
        // vv FilterListExample
        for (Result result : scanner1) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
                // ^^ FilterListExample
                n++;
                // vv FilterListExample
            }
        }
        scanner1.close();

        FilterList filterList2 = new FilterList(
                FilterList.Operator.MUST_PASS_ONE, filters);

        scan.setFilter(filterList2);
        ResultScanner scanner2 = table.getScanner(scan);
        // ^^ FilterListExample
        System.out.println("Total cell count for scan #1: " + n);
        n = 0;
        System.out.println("Results of scan #2 - MUST_PASS_ONE:");
        // vv FilterListExample
        for (Result result : scanner2) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
                // ^^ FilterListExample
                n++;
                // vv FilterListExample
            }
        }
        scanner2.close();
        // ^^ FilterListExample
        System.out.println("Total cell count for scan #2: " + n);
    }
}
