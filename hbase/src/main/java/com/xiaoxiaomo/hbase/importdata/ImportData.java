package com.xiaoxiaomo.hbase.importdata;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 从hbfs导入数据到HBase
 * Created by xiaoxiaomo on 2015/6/2.
 */
    public class ImportData {


    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");


    /**
     * 从hdfs中导入数据到某张表，并加载相应的jar
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();

        //1. 设置配置文件

        //1.1. 设置zookeeper
        conf.set("hbase.zookeeper.quorum", "xxo04,xxo05,xxo06");

        //1.2. 设置hbase表名称和超时时间
        conf.set(TableOutputFormat.OUTPUT_TABLE, args[1]);
        conf.set("dfs.socket.timeout", "180000");

        //2. 设置job
        Job job = Job.getInstance(conf, ImportData.class.getSimpleName());

        job.setJarByClass(ImportData.class);
        TableMapReduceUtil.addDependencyJars(job);

        job.setMapperClass(HBaseMap.class); //map
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(HBaseReduce.class);//reduce没有输出类型，因为直接输入到HBase中了

        job.setInputFormatClass(TextInputFormat.class);//默认
        job.setOutputFormatClass(TableOutputFormat.class);//输出到表 格式


        FileInputFormat.addInputPath( job ,new Path(args[0]) );

        //上传hive-exec-1.0.0.jar到HDFS
        //作用是把archive送到task运行的节点上，并且设置到classpath中
//        job.addArchiveToClassPath(new Path(args[2]));


        job.waitForCompletion(true);

    }


    /**
     * Mapper
     */
    public static class HBaseMap extends Mapper<LongWritable,Text,Text,Text>{
        Text k2 = new Text() ;
        Text v2 = new Text() ;
        StringBuffer sb = new StringBuffer();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] splited = value.toString().split("\t");
            Date date = new Date(Long.parseLong(splited[0].trim()));
            k2.set(StringUtils.reverse(splited[1]) + dateFormat.format(date));
            sb.append(splited[5]);
            sb.append("\t");
            sb.append(splited[6]);
            sb.append("\t");
            sb.append(splited[7]);
            sb.append("\t");
            sb.append(splited[8]);
            sb.append("\t");

            v2.set( sb.toString() );
            context.write( k2 , v2 );
        }
    }

    /**
     * Reduce
     */
    public static class HBaseReduce extends TableReducer< Text , Text, NullWritable>{
        Put put = null ;

        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer.Context context) throws IOException, InterruptedException {

            for (Text value : values) {
                String[] splited = value.toString().split("\t");
                put = new Put(Bytes.toBytes(key.toString()));
                put.add( Bytes.toBytes("f_1"), Bytes.toBytes("upn"), Bytes.toBytes(splited[0] ) ) ;
                put.add( Bytes.toBytes("f_1"), Bytes.toBytes("dpn"), Bytes.toBytes(splited[1] ) ) ;
                put.add( Bytes.toBytes("f_1"), Bytes.toBytes("upl"), Bytes.toBytes(splited[2] ) ) ;
                put.add( Bytes.toBytes("f_1"), Bytes.toBytes("dpl"), Bytes.toBytes(splited[3] ) ) ;
            }
            context.write( NullWritable.get() , put );
        }
    }




}
