package com.xiaoxiaomo.hadoop.mr.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Reduce 分区计算
 * Created by xiaoxiaomo on 2014/5/10.
 */
public class DisSame {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        if( args.length != 3 ){
            throw new IllegalArgumentException("参数异常：InoutPath outputPath ReduceTasks 3 , you is"+args.length ) ;
        }

        Job job = Job.getInstance(new Configuration(), DisSame.class.getName());
        job.setJarByClass(DisSame.class);

        //job.setInputFormatClass();

        //设置Reduce数量和分区类
        job.setNumReduceTasks( Integer.valueOf(args[2]) );
        job.setPartitionerClass(MyPartition.class);

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);

        //2. 调用map

        job.setMapperClass(DisSameMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        //3. 调用reduce
        job.setReducerClass(DisSameReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        //4. 写入数据
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

//        job.setInputFormatClass();
//        job.setOutputFormatClass();
        //5. 执行
        job.waitForCompletion(true) ;

    }

    /**
     * 自定义 Mapper
     */
    static class DisSameMap extends Mapper<LongWritable,Text,Text,NullWritable> {

        Text K2 = new Text() ;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //处理单行数据信息
            String line = value.toString();
            String[] stirs = line.split("\t");
            K2.set(stirs[1]);
            context.write(K2 , NullWritable.get());
        }
    }

    /**
     * 自定义 Reduce
     */
    static class DisSameReduce extends Reducer<Text , NullWritable , Text ,NullWritable > {

        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write( key , NullWritable.get() );
        }
    }

    /**
     * 自定义分区规则
     */
    private static class MyPartition extends Partitioner<Text, NullWritable> {

        @Override
        public int getPartition(Text text, NullWritable nullWritable, int numPartitions) {
            return text.getLength() == 11 ? 0 : 1;
        }
    }
}
