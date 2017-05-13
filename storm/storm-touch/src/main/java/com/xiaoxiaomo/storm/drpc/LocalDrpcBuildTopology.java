package com.xiaoxiaomo.storm.drpc;

import backtype.storm.*;
import backtype.storm.drpc.DRPCSpout;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.drpc.ReturnResults;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * DRPC
 * Created by xiaoxiaomo on 2015/6/10.
 */
public class LocalDrpcBuildTopology {
	public static class MyBolt extends BaseRichBolt{
		
		private OutputCollector collector;
		@Override
		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			this.collector = collector;
		}

		/**
		 * 这个tuple中封装两个元素
		 * 第一个元素：请求的id
		 * 第二个元素：请求的参数
		 */
		@Override
		public void execute(Tuple input) {
			String value = input.getString(1);
			value = "hello "+value;
			this.collector.emit(new Values(input.getValue(0),value));
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("id","value"));
		}
	}
	
	public static void main(String[] args) throws Exception {
//		@SuppressWarnings("deprecation")
//		//创建一个线性DRPC拓扑构建器
//		LinearDRPCTopologyBuilder topologyBuilder = new LinearDRPCTopologyBuilder("hello");
//		topologyBuilder.addBolt(new MyBolt());
//
//		LocalCluster localCluster = new LocalCluster();
//		String simpleName = LocalDrpcBuildTopology.class.getSimpleName();
//		ILocalDRPC drpc = new LocalDRPC();
//		localCluster.submitTopology(simpleName, new Config(), topologyBuilder.createLocalTopology(drpc));
//
//		String result = drpc.execute("hello", "storm");
//		System.out.println("客户端调用的结果：" + result);


		TopologyBuilder builder = new TopologyBuilder();
		//开始的Spout
		DRPCSpout drpcSpout = new DRPCSpout("DrpcBuildTopology");
		builder.setSpout("drpc-input", drpcSpout, 5);

		//真正处理的Bolt
		builder.setBolt("cpp", new MyBolt(), 5).noneGrouping("drpc-input");

		//结束的ReturnResults
		builder.setBolt("return", new ReturnResults(),5).noneGrouping("cpp");

		StormSubmitter.submitTopology("DrpcBuildTopology", new Config(), builder.createTopology());

	}

}
