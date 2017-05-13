package com.xiaoxiaomo.storm.trident;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.tuple.TridentTuple;

public class TridentTopologyFunc {
	
	public static class MyBolt extends BaseFunction{

		@Override
		public void execute(TridentTuple tuple, TridentCollector collector) {
			Integer value = tuple.getInteger(0);
			System.out.println(value);
		}
		
	}
	
	
	
	public static void main(String[] args) {
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 1,new Values(1));
	    spout.setCycle(false);
		TridentTopology tridentTopology = new TridentTopology();
		tridentTopology.newStream("spout_1", spout)
					.each(new Fields("sentence"), new MyBolt(), new Fields(""));
		
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("trident_topology", new Config(), tridentTopology.build());
	}

}
