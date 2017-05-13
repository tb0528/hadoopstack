package com.xiaoxiaomo.storm.drpc;

import backtype.storm.Config;
import backtype.storm.ILocalDRPC;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;
/**
 * DRPC
 * Created by xiaoxiaomo on 2015/6/10.
 */
public class LocalDrpcTopology {
	public static class MyBolt extends BaseRichBolt{
		
		private OutputCollector collector;
		@Override		public void prepare(Map stormConf, TopologyContext context,

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

		@SuppressWarnings("deprecation")
		public static void main(String[] args) throws Exception {
		//创建一个线性DRPC拓扑构建器
		LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("hello");
		builder.addBolt(new MyBolt());
		
		LocalCluster local = new LocalCluster();
		String simpleName = LocalDrpcTopology.class.getSimpleName();
		ILocalDRPC drpc = new LocalDRPC();
		local.submitTopology(simpleName, new Config(), builder.createLocalTopology(drpc));

		String result = drpc.execute("hello", "storm");
		System.out.println("客户端调用的结果：" + result);
	}

}
