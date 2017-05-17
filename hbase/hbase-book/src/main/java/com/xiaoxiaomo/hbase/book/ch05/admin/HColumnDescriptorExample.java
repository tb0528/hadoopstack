package com.xiaoxiaomo.hbase.book.ch05.admin;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Map;

/**
 *
 * 列族中几个重要的属性
 *
 * 1. 最大版本数：getMaxVersions()、setMaxVersions(int maxVersions)
 * 2. 压缩：HBase支持插件式的压缩算法（目前支持：NONE、GZ、LZO、SNAPPY）默认NONE
 *          setCompressionType(Compression.Algorithm.SNAPPY)
 * 3. 块大小：HBase所有的存储文件都被划分成了若干个小存储块，这些小存储快在get或者scan操作时会加载到内存中，它们类似于RDBMS中的存储单元页，
 *          默认是65536K=64KB，setBlocksize(int s)、getBlocksize()。
 *          主要用于高效加载和缓存数据，并不依赖于HDFS的块大小，并且只用于HBase内部。
 * 4. 块缓存：HBase顺序地读取一个数据块到内存缓存中，其读取相邻的数据时就可以在内存中读取而不需要从磁盘中再次读取，有效减少了磁盘I/O的次数，调高了I/O效率
 *          默认true，意味着每次读取的块都会缓存到内存中，但是，如果用户要顺序读取某个特定列族，最好设置为false，从而禁止使用块缓存。
 *          setBlockCacheEnabled(boolean blockCacheEnabled)
 *          还有很多参数都会影响到块缓存的使用，比如scan的过程中众多参数。
 * 5. 生命周期TTL：默认FOREVER setTimeToLive() => Integer.MAX_VALUE
 * 6. in-memory：一种高优先级，在正常的数据读取过程中，快数据被加载到缓存区重并长期驻留在内存中，除非堆压力过大，这个时候才会强制重内存中写在这部分数据。
 *          该参数通常适合数据量较小的列族，例如保存登陆账号密码的用户表。
 * 7. 布隆过滤器：这个模式会增加内存和存储的负担，NONE/ROW/ROWCOL
 * 8. 复制范围：一个高级的复制。
 *
 * Example how to create a HColumnDescriptor in code
 */
public class HColumnDescriptorExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        // vv HColumnDescriptorExample
        HColumnDescriptor desc = new HColumnDescriptor("colfam1")
                .setValue("test-key", "test-value") //METADATA
                .setBloomFilterType(BloomType.ROWCOL);

//        desc.setCompressionType(Compression.Algorithm.SNAPPY);    //压缩格式
        desc.setBlocksize(64);
//        desc.setBlockCacheEnabled();


        System.out.println("Column Descriptor: " + desc);

        System.out.print("Values: ");
        for (Map.Entry<ImmutableBytesWritable, ImmutableBytesWritable>
                entry : desc.getValues().entrySet()) {
            System.out.print(Bytes.toString(entry.getKey().get()) +
                    " -> " + Bytes.toString(entry.getValue().get()) + ", ");
        }
        System.out.println();

        System.out.println("Defaults: " +
                HColumnDescriptor.getDefaultValues());

        System.out.println("Custom: " +
                desc.toStringCustomizedValues());

        System.out.println("Units:");
        System.out.println(HColumnDescriptor.TTL + " -> " +
                desc.getUnit(HColumnDescriptor.TTL));
        System.out.println(HColumnDescriptor.BLOCKSIZE + " -> " +
                desc.getUnit(HColumnDescriptor.BLOCKSIZE));

    }
}
