package com.xiaoxiaomo.storm.trident;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseAggregator;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.spout.IBatchSpout;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.tuple.TridentTuple;

public class TridentTopologyWordCount {
	
	public static class MySpout implements IBatchSpout {

	    Fields fields;
	    HashMap<Long, List<List<Object>>> batches = new HashMap<Long, List<List<Object>>>();
	    
	    public MySpout(Fields fields) {
	        this.fields = fields;
	    }
	    
	    
	    @Override
	    public void open(Map conf, TopologyContext context) {
	    }

	    @Override
	    public void emitBatch(long batchId, TridentCollector collector) {
	        List<List<Object>> batch = this.batches.get(batchId);
	        if(batch == null){
	            batch = new ArrayList<List<Object>>();
	            Collection<File> listFiles = FileUtils.listFiles(new File("d:\\test"), new String[]{"txt"}, true);
	            for (File file : listFiles) {
	            	List<String> readLines;
					try {
						readLines = FileUtils.readLines(file);
						for (String line : readLines) {
							batch.add(new Values(line));
						}
						FileUtils.moveFile(file, new File(file.getAbsolutePath()+System.currentTimeMillis()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
	            this.batches.put(batchId, batch);
	        }
	        for(List<Object> list : batch){
	            collector.emit(list);
	        }
	    }

	    @Override
	    public void ack(long batchId) {
	        this.batches.remove(batchId);
	    }

	    @Override
	    public void close() {
	    }

	    @Override
	    public Map getComponentConfiguration() {
	        Config conf = new Config();
	        conf.setMaxTaskParallelism(1);
	        return conf;
	    }

	    @Override
	    public Fields getOutputFields() {
	        return fields;
	    }
	    
	}
	
	public static class MySplitBolt extends BaseFunction{

		@Override
		public void execute(TridentTuple tuple, TridentCollector collector) {
			String value = tuple.getString(0);
			String[] split = value.split("\t");
			for (String word : split) {
				collector.emit(new Values(word));
			}
		}
	}
	
	public static class MyAgg extends BaseAggregator<Map<String, Integer>>{

		@Override
		public Map<String, Integer> init(Object batchId,
				TridentCollector collector) {
			return new HashMap<String,Integer>();
		}
		
		@Override
		public void aggregate(Map<String, Integer> val, TridentTuple tuple,
				TridentCollector collector) {
			String word = tuple.getString(0);
			/*Integer integer = val.get(word);
			if(integer==null){
				integer=0;
			}
			integer++;
			val.put(word, integer);*/
			val.put(word, MapUtils.getIntValue(val, word, 0)+1);
		}

		@Override
		public void complete(Map<String, Integer> val,
				TridentCollector collector) {
			collector.emit(new Values(val));
		}

		
	}
	
	
	public static class GlobalCount extends BaseFunction{
		
		
		
		HashMap<String, Integer> globalMap = new HashMap<String,Integer>();
		@Override
		public void execute(TridentTuple tuple, TridentCollector collector) {
			Map<String, Integer> map = (Map<String, Integer>)tuple.get(0);
			for (Entry<String, Integer> entry : map.entrySet()) {
				String key = entry.getKey();
				Integer value = entry.getValue();
				globalMap.put(key, MapUtils.getIntValue(globalMap, key,0)+value);
			}
			System.err.println("==================================");
			Utils.sleep(1000);
			
			for (Entry<String, Integer> entry : globalMap.entrySet()) {
				System.out.println(entry);
			}
		}
	}
	
	
	public static void main(String[] args) {
		TridentTopology tridentTopology = new TridentTopology();
		tridentTopology.newStream("spout_1", new MySpout(new Fields("sentence")))
					.each(new Fields("sentence"), new MySplitBolt(), new Fields("word"))
					.groupBy(new Fields("word"))
					.aggregate(new Fields("word"), new MyAgg(), new Fields("map"))//对一批中的数据进行局部聚?
					.each(new Fields("map"), new GlobalCount(), new Fields(""));
		
		LocalCluster localCluster = new LocalCluster();
		Config config = new Config();
		
		
		localCluster.submitTopology("trident_topology", config, tridentTopology.build());
	}

}
