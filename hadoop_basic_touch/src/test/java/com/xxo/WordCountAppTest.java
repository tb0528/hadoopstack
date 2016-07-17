package com.xxo;

import java.io.IOException;
import java.util.ArrayList;

import com.xxo.hadoop.mr.WordCountApp;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class WordCountAppTest {
	private WordCountApp.WordCountMapper wordCountMapper;
	private WordCountApp.WordCountReducer wordCountReducer;
	private MapDriver<LongWritable, Text, Text, LongWritable>  mapDriver;
	private ReduceDriver<Text, LongWritable, Text, LongWritable> reduceDriver;
	private MapReduceDriver mrDriver;
	
	@Before
	public void before(){
		this.wordCountMapper = new WordCountApp.WordCountMapper();
		this.wordCountReducer = new WordCountApp.WordCountReducer();
		
		this.mapDriver = MapDriver.newMapDriver(wordCountMapper);
		this.reduceDriver = ReduceDriver.newReduceDriver(wordCountReducer);
		this.mrDriver = MapReduceDriver.newMapReduceDriver(wordCountMapper, wordCountReducer);
	}
	
	@Test
	public void testMap() throws IOException {
		//设置输入数据
		this.mapDriver.addInput(new LongWritable(0), new Text("hello	you"));
		this.mapDriver.addInput(new LongWritable(0), new Text("hello	me"));
		this.mapDriver.addOutput(new Text("hello"), new LongWritable(1));
		this.mapDriver.addOutput(new Text("you"), new LongWritable(1));
		this.mapDriver.addOutput(new Text("hello"), new LongWritable(1));
		this.mapDriver.addOutput(new Text("me"), new LongWritable(1));
		this.mapDriver.runTest();
	}
	
	@Test
	public void testReduce() throws IOException{
		ArrayList<LongWritable> values = Lists.newArrayList(new LongWritable(1), new LongWritable(1));
		this.reduceDriver.addInput(new Text("hello"), values);
		
		System.out.println(this.reduceDriver.run());
	}
}
