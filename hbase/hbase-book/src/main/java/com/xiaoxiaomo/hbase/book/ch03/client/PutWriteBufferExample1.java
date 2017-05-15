package com.xiaoxiaomo.hbase.book.ch03.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import util.HBaseHelper;

import java.io.IOException;

/**
 *
 *   using the client-side write buffer
 *
 *  【每一个Put操作实际上都是一个RPC操作，它将客户端数据传到服务器然后返回】
 *   这只是和小数据量的操作，如果有个应用需要每秒存储上千行数据到HBase表中就不适合。
 *
 *  减少独立RPC调用的关键是限制往返时间（round-trip time）,一般情况下在LRN网络中大概要花1毫秒，这意味着1秒钟只能完成1000次RPC往返相应。
 *  当然另一个重要因素就是消息大小，如果通过网络发送的请求内容较大，时间主要话费到数据传递上，这样就没有对比性。
 *
 *  比如，像计数器这样的操作如果合并后批量提交，往返次数就减少了，性能自然会提升
 *
 *  【配置】：
 *      hbase-site.xml  hbase.client.write.buffer=>20971520(2MB)
 *
 *  【访问客户端写缓冲区的内容】：
 *      ArrayList<Put> getWriteBuffer() 是线程不安全，并且访问时数据有可能在写入
 *
 *  【注意】：
 *      一个更大的全重去需要客户端和服务器端消耗更多的内存，因为服务器端也需要先将数据写入到服务器的写缓冲区中，然后再处理。
 *      另一方面，确实减少了RPC请求次数。估算服务器的内存：
 *      hbase.client.write.buffer*hbase.regionserver.handler.count*region服务的数量
 *
 *  再次提到往返时间，如果用户只存储大单元格，客户端缓冲区的作用就不大了，因为传输时间占用了大部分请求时间，
 *  在这种情况下，建议最好不要增加客户端缓冲区大小。
 *
 *
 */
public class PutWriteBufferExample1 {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HBaseHelper helper = HBaseHelper.getHelper(conf);
        helper.dropTable("testtable");
        helper.createTable("testtable", "colfam1");
        Connection connection = helper.getConnection();

        // put 操作
        BufferedMutator mut = connection.getBufferedMutator(TableName.valueOf("testtable")); // Get a mutator instance for the table.

        Put put1 = new Put(Bytes.toBytes("row1"));
        put1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"));
        mut.mutate(put1); // Store some rows with columns into HBase.

        Put put2 = new Put(Bytes.toBytes("row2"));
        put2.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val2"));
        mut.mutate(put2);

        Put put3 = new Put(Bytes.toBytes("row3"));
        put3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val3"));
        mut.mutate(put3);


        // 我们查看一下，flush()前后flush后的数据
        Table table = connection.getTable(TableName.valueOf("testtable"));
        Get get = new Get(Bytes.toBytes("row1"));
        Result res1 = table.get(get);
        System.out.println("Result: " + res1); // this will print "Result: keyvalues=NONE".

        mut.flush(); // 强制刷新，触发一次RPC调用

        Result res2 = table.get(get);
        System.out.println("Result: " + res2); // the row is persisted and can be loaded.

        mut.close();
        table.close();
        connection.close();

        helper.close();
    }
}
