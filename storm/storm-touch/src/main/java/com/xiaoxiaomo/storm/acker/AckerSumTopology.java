package com.xiaoxiaomo.storm.acker;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
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
import backtype.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 求和
 *
 * Created by xiaoxiaomo on 2015/6/6.
 */
public class AckerSumTopology {

    private static final Logger logger = LoggerFactory.getLogger( AckerSumTopology.class ) ;
    /**
     * 自定义Spout
     *
     */
    public static class SumSpout extends BaseRichSpout {

        private SpoutOutputCollector collector;
        int i = 0 ;
        /**
         * 该方法只会被调用一次
         * 做一些初始化的操作
         * @param map
         * @param context
         * @param collector
         */
        public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
            this.collector = collector;
            i = 0 ;
        }


        /**
         * 会不停的执行，像一个死循环
         */
        public void nextTuple() {
            i++;

            //messageid和tuple是一一对应的
            //可以认为messageid 是tuple里面的数据主键id
            //messageid和tuple关系需要程序员自己去维护
            //注意：只有在发spout中发射tuple的时候带上messageid，才说明开启了消息确认机制
            this.collector.emit( new Values(i) , i );
            Utils.sleep(1000);
        }

        /**
         *
         * @param declarer
         */
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare( new Fields("num"));
        }

        @Override
        public void ack(Object msgId) {
            System.out.println( "消息msgId："+msgId + "，处理成功！" );
        }

        @Override
        public void fail(Object msgId) {
            System.out.println("消息msgId：" + msgId + "，处理失败！");
        }
    }

    /**
     * 自定义Bolt
     */
    public static class SumBolt extends BaseRichBolt{

        private OutputCollector collector;

        /**
         * 做初始化操作
         * @param map
         * @param context
         * @param collector
         */
        public void prepare(Map map, TopologyContext context, OutputCollector collector) {
            this.collector = collector ;
        }

        /**
         * 循环执行，当对应的spout有输出时，该方法就会被调用一次
         * @param tuple
         */
        public void execute(Tuple tuple) {

            try {
                System.out.println("求和：" + tuple.getIntegerByField("num"));
//                if( tuple.getIntegerByField("num") > 10 ){ //test 手动抛出异常
//                    throw new RuntimeException("num > 10!") ;
//                }
//                outputCollector.ack( tuple );
            } catch ( Exception e ){
                collector.fail( tuple);
                logger.error(" execute tuple error! " , e  );
            }

        }

        public void declareOutputFields(OutputFieldsDeclarer declarer) {}
    }


    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout_1", new SumSpout());
        //通过shuffleGrouping，可以指定bolt接收哪个组件发射出来的数据
        builder.setBolt("bolt_1",new SumBolt()).setNumTasks(5).shuffleGrouping("spout_1");

        LocalCluster cluster = new LocalCluster();
        Config conf = new Config();

        //使用.setNumTasks(5) 预留task，方便后期进行rebalance

        //设置超时
        ////在指定的时间内，如果bolt没有确认tuple处理成功，那么系统会默认这个tuple处理失败
        conf.put( Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS , 10 ) ;

        //集群
        if( args != null && args.length > 0 ){
            try {
                StormSubmitter.submitTopology(
                        AckerSumTopology.class.getSimpleName() , conf , builder.createTopology() );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //本地
        else {
            cluster.submitTopology( "cluster_1", conf ,builder.createTopology());
        }
    }


//求和：1
//求和：2
//求和：3
//求和：4
//求和：5
//求和：6
//求和：7
//求和：8
//求和：9
//求和：10
//消息msgId：3，处理失败！
//消息msgId：4，处理失败！
//消息msgId：2，处理失败！
//消息msgId：1，处理失败！
//消息msgId：5，处理失败！
//求和：11
//求和：12
//求和：13
//求和：14
//求和：15
//消息msgId：8，处理失败！
//消息msgId：6，处理失败！
//消息msgId：10，处理失败！
//消息msgId：7，处理失败！
//消息msgId：9，处理失败！
//求和：16
//求和：17
//求和：18
//求和：19
//求和：20
//消息msgId：12，处理失败！
//消息msgId：14，处理失败！
//消息msgId：11，处理失败！
//消息msgId：13，处理失败！
//消息msgId：15，处理失败！
//求和：21
//求和：22
//求和：23
}
