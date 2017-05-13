package com.xiaoxiaomo.storm.timing;

import backtype.storm.Config;
import backtype.storm.Constants;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 定时，全局和局部
 * 求和
 * Created by xiaoxiaomo on 2015/6/6.
 */
public class TiMingCSumTopology {

    /**
     * 自定义Spout
     *
     */
    public static class SumSpout extends BaseRichSpout {

        private Map map;
        private TopologyContext topologyContext;
        private SpoutOutputCollector spoutOutputCollector;
        int i = 0 ;
        /**
         * 该方法只会被调用一次
         * 做一些初始化的操作
         */
        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            this.map = map ;
            this.topologyContext = topologyContext;
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
                Thread.sleep(1000 );
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

        private Map map;
        private TopologyContext topologyContext;
        private OutputCollector outputCollector;
        private int sum = 0 ;

        /**
         * 做初始化操作
         */
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

            this.map = map ;
            this.topologyContext = topologyContext ;
            this.outputCollector = outputCollector ;
        }

        /**
         * 循环执行，当对应的spout有输出时，该方法就会被调用一次
         * @param tuple
         */
        public void execute(Tuple tuple) {

            if( tuple.getSourceComponent().equals( Constants.SYSTEM_COMPONENT_ID ) ){
                //如果匹配就说明这个tuple是系统级别的tuple,也就意味着定时时间到了，
                System.out.println("定时任务执行了。。。。:" + sum);
            }else{

                Integer num = tuple.getIntegerByField("num");
                sum+=num;
                System.out.println("和为："+sum);
            }

        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        }

        @Override
        public Map<String, Object> getComponentConfiguration() {
            HashMap<String, Object> map = new HashMap<>();
            map.put( Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS ,5 );
            return map ;
        }
    }


    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout_1", new SumSpout());
        builder.setBolt("bolt_1",new SumBolt()).shuffleGrouping("spout_1");

        LocalCluster cluster = new LocalCluster();
        Config conf = new Config();

        if( args != null && args.length > 0 ){
            try {
                StormSubmitter.submitTopology( TiMingCSumTopology.class.getSimpleName() , conf , builder.createTopology() );
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
//    和为：1
//    和为：3
//    和为：6
//    和为：10
//    和为：15
//    定时任务执行了。。。。:15
//    和为：21
//    和为：28
//    和为：36
//    和为：45
//    和为：55
//    定时任务执行了。。。。:55
//    和为：66
//    和为：78
//    和为：91
}
