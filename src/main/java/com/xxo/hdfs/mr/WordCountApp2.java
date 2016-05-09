package com.xxo.hdfs.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 1.打成jar，指定入口类
 * 2.启动yarn和historyserver
 *   start-yarn.sh
 *   mr-jobhistory-daemon.sh start historyserver
 * 3.hadoop jar WordCountApp.jar   /hello   /out2  
 * @author 吴超
 *
 */
public class WordCountApp2 {

	public static void main(String[] args) throws Exception{
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, WordCountApp2.class.getSimpleName());
		job.setJarByClass(WordCountApp2.class);
		
		//数据来自哪里
		FileInputFormat.setInputPaths(job, args[0]);
		
		//使用我的mapper
		job.setMapperClass(WordCountMapper.class);
		//指明<k2,v2>类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		
		//使用我的reducer
		job.setReducerClass(WordCountReducer.class);
		//指明<k3,v3>类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		//数据写到哪里
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);
	}

	
	public static class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
		Text k2 = new Text();
		LongWritable v2 = new LongWritable();
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			System.out.println("map函数v1的值"+line);
			
			String[] splited = line.split("\t");
			for (String word : splited) {
				k2.set(word);
				v2.set(1);
				System.out.println("       产生的<k2,v2>的值<"+word+",1>");
				context.write(k2, v2);
			}
		}
	}
	
	public static class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable>{
		LongWritable v3 = new LongWritable();
		@Override
		protected void reduce(Text k2, Iterable<LongWritable> v2s,
				Context context) throws IOException, InterruptedException {
			System.out.println("reduce接收的k2的值"+k2.toString());
			long sum = 0;
			for (LongWritable times : v2s) {
				System.out.println("      reduce接收的k2("+k2.toString()+")对应的v2的值"+times.get());
				sum += times.get();
			}
			v3.set(sum);
			System.out.println("输出的<k3,v3>的值<"+k2.toString()+","+sum+">");
			context.write(k2, v3);
		}
	}
}
