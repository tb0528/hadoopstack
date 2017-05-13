package com.xiaoxiaomo.storm.group;

import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
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
 * 需求：
 * 数字累加
 * spout：产生1,2,3,4 
 * bolt：汇总求和
 * @author xiaoxiaomo
 * 
 * 在远程storm集群运行
 *
 */
public class RemoteTopologyAllGrouping {
	/**
	 * 自定义的spout需要继承baserichspout
	 * @author xiaoxiaomo
	 *
	 */
	public static class MySpout extends BaseRichSpout{
		
		private Map conf; 
		private TopologyContext context;
		private SpoutOutputCollector collector;
		/**
		 * 是一个初始化方法
		 * 这个方法只会被调用一次
		 * 例子：如果需要通过这个spout从mysql中读取数据
		 * 	那么获取jdbc，加载驱动的初始化代码，建议放在open方法内
		 * 
		 * Map conf：认识是一个配置参数，里面保存的是一个配置信息
		 * TopologyContext context：上下文，里面可以保证topology的一些信息
		 * SpoutOutputCollector collector：主要负责向外面发射数据
		 * 
		 */
		@Override
		public void open(Map conf, TopologyContext context,
				SpoutOutputCollector collector) {
			this.conf = conf;
			this.context = context;
			this.collector = collector;
		}

		
		int i = 0;
		/**
		 * 这个方法是一个死循环，类似于while(true)
		 * 这个方法会别一直不停的调用
		 */
		@Override
		public void nextTuple() {
			i++;
			System.err.println("spout发射："+i);
			this.collector.emit(new Values(i));//负责把数据发射出去
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 声明输出字段
		 * 注意：主要在nextTuple方法中发射数据了，那么declareOutputFields中必须要声明输出字段
		 */
		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			//fields对象中封装的参数和values中的参数一一对应，后面想要获取tuple中的元素的话，需要使用field中指定的字段名称
			declarer.declare(new Fields("num"));
		}
		
	}
	
	
	/**
	 * 自定义的bolt需要继承BaseRichBolt
	 * @author xiaoxiaomo
	 *
	 */
	public static class SumBolt extends BaseRichBolt{
		
		private Map stormConf;
		private TopologyContext context;
		private OutputCollector collector;
		/**
		 * 初始化方法，只会执行一次，类似于spout中的open方法
		 */
		@Override
		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			this.stormConf = stormConf;
			this.context = context;
			this.collector = collector;
		}

		int sum = 0;
		/**
		 * 这个方法也会执行多次，只要spout中向外面发射一个tuple，这个方法就会被调用一次
		 */
		@Override
		public void execute(Tuple input) {
			//input.getInteger(0);//根据角标获取数据
			Integer value = input.getIntegerByField("num");
			//sum+=value;
			System.out.println("线程id："+Thread.currentThread().getId()+"------------,值为："+value);
			//因为这个bolt是最后一个bolt，所以不需要调用emit向外发射数据了
		}
		
		/**
		 * 因为execute方法中没有向外发射数据，所以这里面不需要实现
		 */
		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			
		}
		
	}
	
	/**
	 * 1：同一个topology内部，组件的id不能一样
	 * 2：组件的id不能以__开头
	 * @param args
	 */
	public static void main(String[] args) {
		//首先获取一个topology的构建器
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		topologyBuilder.setSpout("spout_1", new MySpout());//在setspout或者setbolt方法中指定并行度的参数
		//通过shuffleGrouping，可以指定bolt接收哪个组件发射出来的数据
		//使用.setNumTasks(5) 预留task，方便后期进行rebalance
		topologyBuilder.setBolt("bolt_1", new SumBolt(),3).allGrouping("spout_1");
		
		Config config = new Config();
		//config.setNumWorkers(2);//用两个worker 来运行这个topology里面的组件
		//创建一个本地集群
		if(args.length==0){
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("sum_topology", config, topologyBuilder.createTopology());
		}else{
			String simpleName = RemoteTopologyAllGrouping.class.getSimpleName();
			try {
				//把topology提交到真正的集群运行
				StormSubmitter.submitTopology(simpleName, config, topologyBuilder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	

}
