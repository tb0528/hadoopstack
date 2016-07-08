package com.xxo.hdfs.excel;

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
 * 获取手机号，并排序
 * 默认字典排序（升序）
 * Created by xiaoxiaomo on 2014/5/10.
 */
public class ReadUserInfoByExcel {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration(), ReadUserInfoByExcel.class.getSimpleName());
        job.setJarByClass(ReadUserInfoByExcel.class);

        job.setInputFormatClass(ExcelInputFormat.class); //自定义输入格式化

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);
//        FileInputFormat.setInputDirRecursive();

        //2. 调用map
        job.setMapperClass(SortMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //3. 调用reduce
        job.setReducerClass(SortReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //4. 写入数据
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //5. 执行
        job.waitForCompletion(true) ;

    }

    /**
     * 自定义 Mapper
     */
    static class SortMap extends Mapper<LongWritable,Text,Text,Text>{

        Text K2 = new Text() ;
        Text V2 = new Text() ;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //处理单行数据信息
            String line = value.toString();
            String[] split = line.split("\t");
            K2.set(split[4]);
            V2.set(split[4]);
            context.write( K2 , V2 );

            System.out.println("Map 读取完毕！");
        }
    }

    /**
     * 自定义 Reduce
     */
    static class SortReduce extends Reducer<Text , Text , Text ,Text >{

        Text V3 = new Text() ;
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            StringBuffer sb = new StringBuffer() ;
            int count = 0 ;
            for (Text value : values) {
                count ++ ;
                sb.append( value ).append(",") ;
            }
            V3.set( " 访问总页面:  "+ count + sb.toString() );
            context.write( key, V3 );
        }
    }

}
