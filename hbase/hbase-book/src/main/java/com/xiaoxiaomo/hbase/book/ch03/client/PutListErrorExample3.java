package com.xiaoxiaomo.hbase.book.ch03.client;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 捕获RetriesExhaustedWithDetailsException客户端报告的远程错误
 *
 * 客户检查有多少个操作失败，错误原因以及重试次数。
 * 注意对于列族错误，服务端的重试次数为1（见 NoSuchColumnFamilyException:1 time）,因为这是一个不可恢复的错误类型。
 */
public class PutListErrorExample3 {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        try (
                HBaseHelper helper = HBaseHelper.getHelper(conf);
                Connection connection = ConnectionFactory.createConnection(conf);
                Table table = connection.getTable(TableName.valueOf("testtable"));
        ) {
            helper.dropTable("testtable");
            helper.createTable("testtable", "colfam1");
            List<Put> puts = new ArrayList<Put>();

            Put put1 = new Put(Bytes.toBytes("row1"));
            put1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"),
                    Bytes.toBytes("val1"));
            puts.add(put1);
            Put put2 = new Put(Bytes.toBytes("row2"));
            put2.addColumn(Bytes.toBytes("BOGUS"), Bytes.toBytes("qual1"),
                    Bytes.toBytes("val2"));
            puts.add(put2);
            Put put3 = new Put(Bytes.toBytes("row2"));
            put3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"),
                    Bytes.toBytes("val3"));
            puts.add(put3);


            try {
                table.put(puts);
            } catch (RetriesExhaustedWithDetailsException e) {
                int numErrors = e.getNumExceptions();
                System.out.println("Number of exceptions: " + numErrors);
                for (int n = 0; n < numErrors; n++) {
                    System.out.println("Cause[" + n + "]: " + e.getCause(n));
                    System.out.println("Hostname[" + n + "]: " + e.getHostnamePort(n));
                    System.out.println("Row[" + n + "]: " + e.getRow(n)); //access to the failed operation.
                }
                System.out.println("Cluster issues: " + e.mayHaveClusterIssues());
                System.out.println("Description: " + e.getExhaustiveDescription());
            }

        }
    }
}
