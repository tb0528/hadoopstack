package com.xxo.hadoop.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 获取最大值
 * Created by xiaoxiaomo on 2014/5/10.
 */
public class SortKeyMax {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration(), SortKeyMax.class.getSimpleName());
        job.setJarByClass(SortKeyMax.class);

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);

        //2. 调用map

        job.setMapperClass(SortMap.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(LongWritable.class);

        //3. 调用reduce
        job.setReducerClass(SortReduce.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(LongWritable.class);

        //4. 写入数据
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //5. 执行
        job.waitForCompletion(true) ;

    }

    /**
     * 自定义 Mapper
     */
    static class SortMap extends Mapper<LongWritable,Text,LongWritable,LongWritable>{

        LongWritable k2 = new LongWritable() ;
        LongWritable v2 = new LongWritable() ;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //处理单行数据信息
            String line = value.toString();
            String[] stirs = line.split("\t");
            k2.set(Long.parseLong(stirs[0])) ;
            v2.set(Long.parseLong(stirs[1])) ;
            context.write( k2 , v2 );
        }
    }

    /**
     * 自定义 Reduce
     */
    static class SortReduce extends Reducer<LongWritable , LongWritable , LongWritable ,LongWritable >{

        LongWritable k3 = new LongWritable() ;
        @Override
        protected void reduce(LongWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            Long max = Long.MIN_VALUE ;
            for (LongWritable value : values) {
                if( max < value.get() ){
                    max = value.get() ;
                }
            }
            k3.set(max);
            context.write(key, k3);
        }
    }
}
