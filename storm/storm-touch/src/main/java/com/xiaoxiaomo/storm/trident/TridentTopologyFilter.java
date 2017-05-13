package com.xiaoxiaomo.storm.trident;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseFilter;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.tuple.TridentTuple;

public class TridentTopologyFilter {
	
	public static class MyBolt extends BaseFunction{

		@Override
		public void execute(TridentTuple tuple, TridentCollector collector) {
			Integer value = tuple.getInteger(0);
			System.out.println(value);
		}
		
	}
	
	public static class MyFilter extends BaseFilter{

		/**
		 * 如果返回值为false，表示这个?被过滤?
		 */
		@Override
		public boolean isKeep(TridentTuple tuple) {
			Integer value = tuple.getInteger(0);
			return value%2==0?true:false;
		}
		
	}
	
	
	public static void main(String[] args) {
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 1,new Values(1),new Values(2));
	    spout.setCycle(true);
		TridentTopology tridentTopology = new TridentTopology();
		tridentTopology.newStream("spout_1", spout)
					.each(new Fields("sentence"), new MyFilter())
					.each(new Fields("sentence"), new MyBolt(), new Fields(""));
		
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("trident_topology", new Config(), tridentTopology.build());
	}

}
