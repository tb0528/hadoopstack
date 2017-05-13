package com.xiaoxiaomo.hbase.pro.process;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFilter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * Created by xiaoxiaomo on 2015/6/3.
 */
public class ParserConfig {


    public static void init( Path inputPath ) throws IOException, ClassNotFoundException, InterruptedException {

        //1. 实例化配置文件
        Configuration conf = new Configuration();

        //2. 设置job
        Job job = Job.getInstance(conf, ParserConfig.class.getSimpleName());

        job.setJarByClass(ParserConfig.class);
        TableMapReduceUtil.addDependencyJars(job);

        job.setMapperClass(ParserMapper.class); //map
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //job.setReducerClass(HBaseReduce.class);//reduce没有输出类型，因为直接输入到HBase中了

        job.setInputFormatClass(SequenceFileInputFilter.class);//默认
        job.setOutputFormatClass(TextOutputFormat.class);//输出到表 格式

        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, new Path("/data/test1")); //test

        job.waitForCompletion(true);
    }


    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        init( new Path(args[0]) );
    }
}
