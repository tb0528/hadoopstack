package com.xiaoxiaomo.storm.count;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


/**
 * 
 * 改造：
 * 使用并行度进行改造
 * 获取下面指标：
 * 1:获取所有单词的个数
 * 2:获取所有单词出现的总次数
 * 
 * 单词计数
 * 在spout中需要监控指定目录下所有文件的变化，当发现又新文件的时候，解析这个文件中的所有行的数据，
 * 		获取的是一行一行的，然后把一行行数据发射出去
 * 在splitbolt中对spout发射出来的一行数据进行切割，获取到很多的单词，再把每一个单词发射出去
 * 
 * 在countbolt中对splitbolt发射出来的单词进行计数(获取所有的单词，保存到一个map中)
 * 
 * @author xiaoxiaomo
 *
 */
public class WordCountTopology2 {
	
	public static class MySpout extends BaseRichSpout{
		
		private Map conf;
		private TopologyContext context;
		private SpoutOutputCollector collector;
		@Override
		public void open(Map conf, TopologyContext context,
				SpoutOutputCollector collector) {
			this.conf = conf;
			this.context = context;
			this.collector = collector;
		}
		
		@Override
		public void nextTuple() {
			//读取指定目录下面的所有文件
			/**
			 * directory：表示要监控的目录
			 * extensions：文件名后缀
			 * recursive：知否进行递归获取
			 */
			Collection<File> listFiles = FileUtils.listFiles(new File("d:\\test"), new String[]{"txt"}, true);
			//迭代每个文件
			for (File file : listFiles) {
				try {
					//获取每个文件的所有行
					List<String> readLines = FileUtils.readLines(file);
					//把文件的内容一行一行发射出去
					for (String line : readLines) {
						this.collector.emit(new Values(line));
					}
					//注意：文件读取完成之后需要改名字
					FileUtils.moveFile(file, new File(file.getAbsolutePath()+System.currentTimeMillis()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("line"));
		}
		
	}
	
	/**
	 * 对spout发射出来的数据进行切割
	 * @author xiaoxiaomo
	 *
	 */
	public static class SplitBolt extends BaseRichBolt{
		
		private Map stormConf; 
		private TopologyContext context;
		private OutputCollector collector;
		@Override
		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			this.stormConf = stormConf;
			this.context = context;
			this.collector = collector;
		}

		@Override
		public void execute(Tuple input) {
			//获取一行数据
			String line = input.getStringByField("line");
			//切割
			String[] split = line.split("\t");
			//把每个单词发射出去
			for (String word : split) {
				this.collector.emit(new Values(word));
			}
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare( new Fields("word"));
		}
		
	}
	
	/**
	 * 局部汇总的bolt
	 * 对splitbolt发射出来的单词进行汇总统计
	 * @author xiaoxiaomo
	 *
	 */
	public static class JuBuCountBolt extends BaseRichBolt{
		
		private Map stormConf; 
		private TopologyContext context;
		private OutputCollector collector;
		@Override
		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			this.stormConf = stormConf;
			this.context = context;
			this.collector = collector;
		}

		HashMap<String, Integer> hashMap = new HashMap<String,Integer>();
		@Override
		public void execute(Tuple input) {
			//获取一个单词
			String word = input.getStringByField("word");
			//汇总统计
			Integer integer = hashMap.get(word);
			if(integer==null){
				integer=0;
			}
			integer++;
			hashMap.put(word, integer);
			for (Entry<String, Integer> entry : hashMap.entrySet()) {
				this.collector.emit(new Values(entry.getKey(),entry.getValue()));
			}
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("key","value"));
		}
		
	}
	
	
	/**
	 * 全局汇总的bolt
	 * @author xiaoxiaomo
	 *
	 */
	public static class GlobalCountBolt extends BaseRichBolt{
		
		private Map stormConf; 
		private TopologyContext context;
		private OutputCollector collector;
		@Override
		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			this.stormConf = stormConf;
			this.context = context;
			this.collector = collector;
		}

		HashMap<String, Integer> hashMap = new HashMap<String,Integer>();
		@Override
		public void execute(Tuple input) {
			String key = input.getStringByField("key");
			Integer value = input.getIntegerByField("value");
			hashMap.put(key, value);
			
			//所有单词的个数
			System.out.println("总单词个数："+hashMap.size());
			//所有单词出现的总次数
			int sum = 0;
			for (Entry<String, Integer> entry : hashMap.entrySet()) {
				sum+=entry.getValue();
			}
			System.out.println("所有单词出现的总次数："+sum);
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
		}
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		String SPOUT_ID = MySpout.class.getSimpleName();
		String BOLT_1 = SplitBolt.class.getSimpleName();
		String BOLT_2 = JuBuCountBolt.class.getSimpleName();
		String BOLT_3 = GlobalCountBolt.class.getSimpleName();
		
		
		topologyBuilder.setSpout(SPOUT_ID, new MySpout());
		
		topologyBuilder.setBolt(BOLT_1, new SplitBolt(),3).shuffleGrouping(SPOUT_ID);
		//局部汇总
		topologyBuilder.setBolt(BOLT_2, new JuBuCountBolt(),3).fieldsGrouping(BOLT_1, new Fields("word"));
		//全局汇总
		topologyBuilder.setBolt(BOLT_3, new GlobalCountBolt()).globalGrouping(BOLT_2);
		
		
		LocalCluster localCluster = new LocalCluster();
		String simpleName = WordCountTopology2.class.getSimpleName();
		localCluster.submitTopology(simpleName, new Config(), topologyBuilder.createTopology());
	}
	
	

}
