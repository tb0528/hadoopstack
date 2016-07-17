package com.xxo.hadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 通过MapReduce统计单词次数
 * Created by xiaoxiaomo on 2014/5/9.
 */
public class WordCountApp {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance( conf,WordCountApp.class.getSimpleName()) ;
        job.setJarByClass(WordCountApp.class);

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);
        FileInputFormat.setInputDirRecursive(job, true); //递归

        //2. 使用Mapper计算
        job.setMapperClass(WordCountMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //3. 使用Reducer合并计算
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //4. 数据写入
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //5. 执行
        job.waitForCompletion(true) ;
    }

    /**
     * 自定义的Map 需要继承Mapper
     * K1 : 行序号
     * V1 : 行信息
     * K2 : 单词
     * V2 : 次数
     */
    public static class WordCountMapper extends Mapper<LongWritable,Text,Text,LongWritable> {

        Text k2 = new Text() ;
        LongWritable v2 = new LongWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            //1. 获取行信息
            String line = value.toString();

            //2. 获取行的所用单词
            String[] words = line.split("\t");
            for (String word : words) {
                k2.set(word.getBytes()) ; //设置键
                v2.set(1);                //设置值
                context.write(k2,v2);
            }
            
        }
    }

    /**
     * 自定义的Reduce 需要继承Reducer
     * K2 : 字符串
     * V3 : 次数（分组）
     * K3 : 字符串
     * V3 : 次数（统计总的）
     */
    public static class WordCountReducer extends Reducer<Text,LongWritable,Text,LongWritable>{

        LongWritable v3 = new LongWritable() ;
        int sum  = 0 ;
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context)
                throws IOException, InterruptedException {
            sum = 0 ;
            for (LongWritable value : values) {
                sum +=value.get() ;
            }
            v3.set(sum);
            context.write( key , v3 );
        }
    }

}
