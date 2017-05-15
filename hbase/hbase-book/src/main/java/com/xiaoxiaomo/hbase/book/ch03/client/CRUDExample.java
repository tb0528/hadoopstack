package com.xiaoxiaomo.hbase.book.ch03.client;

// cc CRUDExample Example application using all of the basic access methods (v1.0 and later)

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

public class CRUDExample {

    public static void main(String[] args) throws IOException {

        // 加载配置
        //1. HBaseConfiguration.create()
        //2. HBaseConfiguration.create(Configuration that) 自己指定的优先级高
        //3. 调用任何一个静态的create()方法，代码都会尝试使用当前的JAVA CLASSPATH 来加载两个配置文件：hbase-default.xml、hbase-site.xml
        //4. 当然在调用HTable实例之前也可以使用代码任意的修改配置
        Configuration conf = HBaseConfiguration.create();
        HBaseHelper helper = HBaseHelper.getHelper(conf);
        try (

//                Connection connection = ConnectionFactory.createConnection(conf);
                Connection connection = helper.getConnection();

                Table table = connection.getTable(TableName.valueOf("testtable"));
        ) {

            /** drop create table */
            helper.dropTable("testtable");
            helper.createTable("testtable", "colfam1", "colfam2");


            /** put */
            /** Bytes工具类挺好用的，其实内部实现很简单*/
            /** Put(byte[] row, long ts) 通过ts时间戳将数据存储为一个特定的版本（数据的版本化） */
            Put put = new Put(Bytes.toBytes("row1"));
            put.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"));
            put.addColumn(Bytes.toBytes("colfam2"), Bytes.toBytes("qual2"), Bytes.toBytes("val2"));
            table.put(put);

            /** scan */
            Scan scan = new Scan();
//            scan.setMaxVersions(1);//默认查出最新版本的数据
            ResultScanner scanner = table.getScanner(scan);
            for (Result result2 : scanner) {
                while (result2.advance())
                    System.out.println("Cell: " + result2.current());
            }

            /** get */
            Get get = new Get(Bytes.toBytes("row1"));
            get.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
            Result result = table.get(get);
            System.out.println("Get result: " + result);
            byte[] val = result.getValue(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
            System.out.println("Value only: " + Bytes.toString(val));

            /** delete */
            Delete delete = new Delete(Bytes.toBytes("row1"));
            delete.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
            table.delete(delete);

            /** scan2 */
            Scan scan2 = new Scan();
            ResultScanner scanner2 = table.getScanner(scan2);
            for (Result result2 : scanner2) {
                System.out.println("Scan: " + result2);
            }

        }
        helper.close();
    }
}
