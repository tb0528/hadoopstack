package com.xxo.hdfs.mr.array;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 解析手机流量，并统计信息
 * 使用 ArrayWritable
 * Created by xiaoxiaomo on 2014/5/10.
 */
public class TrafficArrayWritable {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration(), TrafficArrayWritable.class.getName());
        job.setJarByClass(TrafficArrayWritable.class);

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);

        //2. 调用map

        job.setMapperClass(TrafficMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(ArrayWritable.class);

        //3. 调用reduce
        job.setReducerClass(TrafficReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(ArrayWritable.class);

        //4. 写入数据
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //5. 执行
        job.waitForCompletion(true) ;

    }

    /**
     * 自定义 Mapper
     */
    static class TrafficMap extends Mapper<LongWritable,Text,Text,ArrayWritable>{
        ArrayWritable arrayWritable =new ArrayWritable(LongWritable.class);
        Text K2 = new Text() ;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //处理单行数据信息
            String line = value.toString();
            String[] stirs = line.split("\t");
            K2.set(stirs[1]);
            Writable[] values = {new Text(stirs[6]), new Text(stirs[7]), new Text(stirs[8]), new Text(stirs[9])};
            arrayWritable.set(values);
            context.write(K2 , arrayWritable);
        }
    }

    /**
     * 自定义 Reduce
     */
    static class TrafficReduce extends Reducer<Text , ArrayWritable , Text ,ArrayWritable >{

        ArrayWritable arrayWritable =new ArrayWritable(LongWritable.class);
        @Override
        protected void reduce(Text key, Iterable<ArrayWritable> values, Context context) throws IOException, InterruptedException {
            //统计流量
            long upPackNum = 0;
            long downPackNum = 0 ;
            long upPayLoad = 0;
            long downPayLoad = 0;
            for (ArrayWritable value : values) {
                LongWritable[] writables = (LongWritable[]) value.get();
                upPackNum = writables[0].get() ;
                downPackNum = writables[1].get() ;
                upPayLoad = writables[2].get() ;
                downPayLoad = writables[3].get() ;
            }

            Writable[] values1 = {new Text(upPackNum + ""), new Text(downPackNum + ""), new Text(upPayLoad + ""), new Text(downPayLoad + "")};
            arrayWritable.set(values1);
            context.write(key, arrayWritable);
        }
    }

}
