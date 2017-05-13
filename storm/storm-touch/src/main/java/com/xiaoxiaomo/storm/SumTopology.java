package com.xiaoxiaomo.storm;

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

import java.util.Map;

/**
 * 求和
 * Created by xiaoxiaomo on 2015/6/6.
 */
public class SumTopology {

    /**
     * 自定义Spout
     */
    public static class SumSpout extends BaseRichSpout {

        private SpoutOutputCollector spoutOutputCollector;
        int i = 0 ;

        /**
         * 该方法只会被调用一次
         * 做一些初始化的操作
         * @param map
         * @param topologyContext
         * @param spoutOutputCollector
         */
        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            this.spoutOutputCollector = spoutOutputCollector;
            i = 0 ;
        }


        /**
         * 会不停的执行，像一个死循环
         */
        public void nextTuple() {
            i++;
            this.spoutOutputCollector.emit( new Values(i));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         *
         * @param outputFieldsDeclarer
         */
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare( new Fields("num"));
        }

    }

    /**
     * 自定义Bolt
     */
    public static class SumBolt extends BaseRichBolt{

        /**
         * 做初始化操作
         * @param map
         * @param topologyContext
         * @param outputCollector
         */
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        }

        /**
         * 循环执行，当对应的spout有输出时，该方法就会被调用一次
         * @param tuple
         */
        public void execute(Tuple tuple) {
            System.out.println("求和："+ tuple.getIntegerByField("num"));

        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        }
    }


    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout_1", new SumSpout(),2);
        builder.setBolt("bolt_1",new SumBolt(),3).setNumTasks(5).shuffleGrouping("spout_1");

        LocalCluster cluster = new LocalCluster();
        Config conf = new Config();

        if( args != null && args.length > 0 ){
            try {
                StormSubmitter.submitTopology( SumTopology.class.getSimpleName() , conf , builder.createTopology() );
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            }

        }

        else {
            cluster.submitTopology( "cluster_1", conf ,builder.createTopology());
        }
    }
}
