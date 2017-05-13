package com.xiaoxiaomo.hbase.importdata;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1.主表的手机号码反转，创建的表要预分区，过滤掉非手机号码
 * @author xiaoxiaomo
 *
 */
public class WlanImportDataApp {
	public static SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMddHHmmss");

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		//设置zookeeper
		conf.set("hbase.zookeeper.quorum", "xxo04,xxo05,xxo06");
		//设置hbase表名称
		conf.set(TableOutputFormat.OUTPUT_TABLE, args[1]);
		//将该值改大，防止hbase超时退出
		conf.set("dfs.socket.timeout", "180000");
		Job job = Job.getInstance(conf, WlanImportDataApp.class.getSimpleName());
		job.setJarByClass(WlanImportDataApp.class);
		TableMapReduceUtil.addDependencyJars(job);
		
		FileInputFormat.setInputPaths(job, args[0]);

		job.setMapperClass(WlanImportDataMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setReducerClass(WlanImportDataReducer.class);
		job.setOutputFormatClass(TableOutputFormat.class);

		//上传hive-exec-1.0.0.jar到HDFS
		//作用是把archive送到task运行的节点上，并且设置到classpath中
//		job.addArchiveToClassPath(new Path(args[2]));
		job.waitForCompletion(true);
	}
	
	
	public static class WlanImportDataMapper extends Mapper<LongWritable, Text, Text, Text>{
		Text k2 = new Text();
		Text v2 = new Text();
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String[] splited = line.split("\t");
			
			//这里的StringUtils.reverse(...)引用的是hive-exec-1.0.0.jar
			//设置行键
			String reversedPhoenunumber = StringUtils.reverse(splited[1]);
			final Date date = new Date(Long.parseLong(splited[0].trim()));
			final String dateFormat = dateformat1.format(date);
			k2.set(reversedPhoenunumber+":"+dateFormat);
			v2.set(splited[6]+"\t"+splited[7]+"\t"+splited[8]+"\t"+splited[9]);
			context.write(k2, v2);
		}
	}
	
	public static class WlanImportDataReducer extends TableReducer<Text, Text, NullWritable>{
		@Override
		protected void reduce(Text k2, Iterable<Text> v2s, Reducer.Context context)
				throws IOException, InterruptedException {
			for (Text v2 : v2s) {
				String[] splited = v2.toString().split("\t");
				Put put = new Put(Bytes.toBytes(k2.toString()));
				put.add(Bytes.toBytes("f_1"), Bytes.toBytes("upn"), Bytes.toBytes(splited[0]));
				put.add(Bytes.toBytes("f_1"), Bytes.toBytes("dpn"), Bytes.toBytes(splited[1]));
				put.add(Bytes.toBytes("f_1"), Bytes.toBytes("upl"), Bytes.toBytes(splited[2]));
				put.add(Bytes.toBytes("f_1"), Bytes.toBytes("dpl"), Bytes.toBytes(splited[3]));
				context.write(NullWritable.get(), put);
			}
		}
	}
}
