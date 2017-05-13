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

public class TridentTopologyMeger {
	
	public static class MyBolt extends BaseFunction{

		@Override
		public void execute(TridentTuple tuple, TridentCollector collector) {
			Integer value = tuple.getInteger(0);
			System.out.println(value);
		}
		
	}
	
	
	
	public static void main(String[] args) {
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 1,new Values(1),new Values(2));
	    spout.setCycle(false);
	    
	    FixedBatchSpout spout1 = new FixedBatchSpout(new Fields("sentence"), 1,new Values(3),new Values(4));
	    spout1.setCycle(false);
	    
		TridentTopology tridentTopology = new TridentTopology();
		Stream stream_1 = tridentTopology.newStream("spout_1", spout);
		Stream stream_2 = tridentTopology.newStream("spout_2", spout1);
		
		
		
		tridentTopology.merge(stream_1,stream_2)
					.each(new Fields("sentence"), new MyBolt(), new Fields(""));
		
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("trident_topology", new Config(), tridentTopology.build());
	}

}
