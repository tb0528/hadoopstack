package com.xxo.hdfs.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 获取手机号，并排序
 * 默认字典排序（升序）
 * Created by xiaoxiaomo on 2014/5/10.
 */
public class SortMobileSimple {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration(), SortMobileSimple.class.getSimpleName());
        job.setJarByClass(SortMobileSimple.class);

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);

        //2. 调用map

        job.setMapperClass(SortMap.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(NullWritable.class);

        //3. 调用reduce
        job.setReducerClass(SortReduce.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(NullWritable.class);

        //4. 写入数据
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //5. 执行
        job.waitForCompletion(true) ;

    }

    /**
     * 自定义 Mapper
     */
    static class SortMap extends Mapper<LongWritable,Text,LongWritable,NullWritable>{

        LongWritable K2 = new LongWritable() ;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //处理单行数据信息
            String line = value.toString();
            String[] stirs = line.split("\t");
            K2.set(Long.valueOf(stirs[1]));
            context.write(K2 , NullWritable.get());
        }
    }

    /**
     * 自定义 Reduce
     */
    static class SortReduce extends Reducer<LongWritable , NullWritable , LongWritable ,NullWritable >{

        @Override
        protected void reduce(LongWritable key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write( key , NullWritable.get() );
        }
    }

}
