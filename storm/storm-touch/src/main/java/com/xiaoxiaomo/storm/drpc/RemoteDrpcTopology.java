package com.xiaoxiaomo.storm.drpc;

import java.util.Map;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class RemoteDrpcTopology {
	
	
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
	
	
	public static void main(String[] args) {
		@SuppressWarnings("deprecation")
		LinearDRPCTopologyBuilder topologyBuilder = new LinearDRPCTopologyBuilder("hello");//创建一个线性DRPC拓扑构建器
		topologyBuilder.addBolt(new MyBolt());
		
		String simpleName = RemoteDrpcTopology.class.getSimpleName();
		try {
			StormSubmitter.submitTopology(simpleName, new Config(), topologyBuilder.createRemoteTopology());
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
		}
		
	}

}
