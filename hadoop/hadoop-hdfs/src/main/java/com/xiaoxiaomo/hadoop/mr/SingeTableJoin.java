package com.xiaoxiaomo.hadoop.mr;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 这是一个单表关联的例子
 * 一、问题：在面试宜人贷的时候面试官就出了一道题，要求如下：
 * 输入： |   输出：
 * A B   |    A C
 * B C   |    D F
 * D E   |
 * E F   |
 *
 * 二、思路
 * 1. 在map端输出正序、倒序，kv
 * A B --->  <A,2 B>  <B,1 A>
 * B C --->  <B,2 C>  <C,1 B>
 *
 * 2. 在reduce端得到数据
 * <A,2 B>
 * <B,1 A,2 C>
 * <C,1 B>
 *
 * 3. 输出
 * <A C>
 * Created by xiaoxiaomo on 2016/8/10.
 */
public class SingeTableJoin {

    private static Logger logger = LoggerFactory.getLogger(SingeTableJoin.class) ;

    /**
     * Map类
     */
    public static class SingeTableMap extends Mapper<LongWritable,Text,Text,Text>{

        Text key1 = new Text() ;
        Text value1 = new Text() ;
        Text key2 = new Text() ;
        Text value2 = new Text() ;

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            if( value == null || "".equals(value.toString().trim()) ){
                logger.warn( "该行数据为空！" );
                return;
            }

            //1. 切分处理
            String[] split = value.toString().split(" ");
            if( split.length  != 2 ){
                logger.warn( "数据格式异常，暂不支持！" );
                return;
            }

            //2. 组装写出数据
            key1.set( split[0] );
            value1.set( 2 + " " + split[1]);

            key2.set( split[1] );
            value2.set( 1 + " " + split[0]);

            context.write( key1 , value1 );
            context.write( key2 , value2 );
        }
    }


    /**
     * Reduece
     */
    public static class SingeTableReduce extends Reducer<Text ,Text ,Text ,Text>{

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            List<String> left = new ArrayList<String>();//左表
            List<String> right = new ArrayList<String>();//右边

            for (Text value : values) {
                String[] lines = value.toString().split(" "); //<B,2 A,1 C>
                if( lines.length != 2 ) continue; ;
                if( "1".equals(lines[0]) ) left.add(lines[1]) ;
                else if( "2".equals(lines[0]) ) right.add(lines[1]);

            }


            //left 和 right数组求笛卡尔积
            if(left.size() != 0 && right.size() != 0){
                for(int i=0;i<left.size();i++){
                    for(int j=0;j<right.size();j++){
                        context.write(new Text(left.get(i)), new Text(right.get(j)));
                    }
                }
            }


        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance( conf,SingeTableJoin.class.getSimpleName()) ;
        job.setJarByClass(SingeTableJoin.class);

        //1. 数据来源
        FileInputFormat.setInputPaths(job, args[0]);

        //2. 使用Mapper计算
        job.setMapperClass(SingeTableMap.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //3. 使用Reducer合并计算
        job.setReducerClass(SingeTableReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //4. 数据写入
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //5. 执行
        job.waitForCompletion(true) ;
    }
}
