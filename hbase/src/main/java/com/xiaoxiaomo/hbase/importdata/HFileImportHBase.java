package com.xiaoxiaomo.hbase.importdata;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.KeyValueSortReducer;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.mapreduce.SimpleTotalOrderPartitioner;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaoxiaomo on 2015/6/3.
 */
public class HFileImportHBase {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");


    public static class HFileMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, KeyValue>{

        ImmutableBytesWritable k2 = new ImmutableBytesWritable() ;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] splited = value.toString().split("\t");

            String reversedPhoenunumber = StringUtils.reverse(splited[1]);
            final Date date = new Date(Long.parseLong(splited[0].trim()));
            final String d = dateFormat.format(date);
            k2.set(Bytes.toBytes( reversedPhoenunumber + ":" + d ));


            KeyValue keyValue1 = new KeyValue(splited[1].getBytes(), Bytes.toBytes("f1"), Bytes.toBytes("upn"), System.currentTimeMillis(), splited[6].getBytes());
            KeyValue keyValue2 = new KeyValue(splited[1].getBytes(), Bytes.toBytes("f1"), Bytes.toBytes("dpn"), System.currentTimeMillis(), splited[7].getBytes());
            KeyValue keyValue3 = new KeyValue(splited[1].getBytes(), Bytes.toBytes("f1"), Bytes.toBytes("upl"), System.currentTimeMillis(), splited[8].getBytes());
            KeyValue keyValue4 = new KeyValue(splited[1].getBytes(), Bytes.toBytes("f1"), Bytes.toBytes("dpl"), System.currentTimeMillis(), splited[9].getBytes());

            context.write(k2, keyValue1);
            context.write(k2, keyValue2);
            context.write(k2, keyValue3);
            context.write(k2, keyValue4);

        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, HFileImportHBase.class.getSimpleName());
        job.setJarByClass(HFileImportHBase.class);

        job.setMapperClass(HFileMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(KeyValue.class);

        //使用默认的分区器
        job.setPartitionerClass(SimpleTotalOrderPartitioner.class);
        //使用默认的Reducer类型
        job.setReducerClass(KeyValueSortReducer.class);


        FileInputFormat.setInputPaths(job, args[0]);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));


        HTable hTable = new HTable(conf, TableName.valueOf(args[2]));
        HFileOutputFormat2.configureIncrementalLoad(job,hTable);

//        job.addArchiveToClassPath(new Path(args[3]));



        job.waitForCompletion(true) ;


        ///////////////////////////////////////////////////////////
        LoadIncrementalHFiles loader = new LoadIncrementalHFiles(conf);
        loader.doBulkLoad(new Path(args[1]), hTable);


    }
}
