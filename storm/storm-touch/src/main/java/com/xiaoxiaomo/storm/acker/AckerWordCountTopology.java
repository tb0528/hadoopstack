package com.xiaoxiaomo.storm.acker;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控一个目录中的文件
 * 统计单词次数
 * Created by xiaoxiaomo on 2015/6/7.
 */
public class AckerWordCountTopology {

    /**
     * Spout
     */
    public static class WCCountSpout extends BaseRichSpout{
        private Map map;
        private TopologyContext topologyContext;
        private SpoutOutputCollector spoutOutputCollector;
        int i = 0 ;

        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            this.map = map ;
            this.topologyContext = topologyContext;
            this.spoutOutputCollector = spoutOutputCollector;
        }

        public void nextTuple() {
            Collection<File> files = FileUtils.listFiles(new File("d://test"), new String[]{"txt"}, false);
            for (File file : files) {
                i++;
                try {
                    List<String> lines = FileUtils.readLines(file);
                    for (String line : lines) {
                        spoutOutputCollector.emit( new Values(line) , i ) ;//开启acker
                    }

                    //修改一下文件名，防止再次获取该文件
                    FileUtils.moveFile( file , new File(file.getAbsolutePath()+ System.currentTimeMillis()) );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare( new Fields("line"));
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
     * 该Bolt 用于切割
     */
    public static class SplitCountBolt extends BaseRichBolt {

        private Map map;
        private TopologyContext topologyContext;
        private OutputCollector outputCollector;

        /**
         * 做初始化操作
         */
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.map = map ;
            this.topologyContext = topologyContext ;
            this.outputCollector = outputCollector ;
        }

        public void execute(Tuple tuple) {
            String line = tuple.getStringByField("line");
            String[] split = line.split("\t");
            for (String s : split) {
                outputCollector.emit( tuple ,new Values(s) );
            }
            this.outputCollector.ack( tuple );
        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare( new Fields("word") );
        }
    }


    /**
     * 用于统计
     */
    public static class WordCountBolt extends BaseRichBolt {

        private Map map;
        private TopologyContext topologyContext;
        private OutputCollector outputCollector;
        private Map<String , Integer> wordMap = null ;

        /**
         * 做初始化操作
         */
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.map = map ;
            this.topologyContext = topologyContext ;
            this.outputCollector = outputCollector ;
            wordMap = new HashMap<String, Integer>() ;
        }

        public void execute(Tuple tuple) {
            String word = tuple.getStringByField("word");
            Integer integer = wordMap.get(word);

            //统计单词
            if( integer == null ){
                integer = 0;
            }
            integer ++ ;
            wordMap.put(word, integer);

            ////////把结果打印出来//////////////
            for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
                System.out.println( entry );
            }

            try {
                if( integer > 2 ){
                    throw new RuntimeException("手动自造异常！");
                }
                this.outputCollector.ack( tuple );
            } catch ( Exception e )  {
                this.outputCollector.fail( tuple );
            }

        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        }
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(WCCountSpout.class.getSimpleName(), new WCCountSpout());
        builder.setBolt( SplitCountBolt.class.getSimpleName() , new SplitCountBolt() ).shuffleGrouping(WCCountSpout.class.getSimpleName());
        builder.setBolt( WordCountBolt.class.getSimpleName() , new WordCountBolt() ).shuffleGrouping( SplitCountBolt.class.getSimpleName() );


        LocalCluster cluster = new LocalCluster();
        Config config = new Config();
        config.setNumAckers(2);//设置acker数量
        cluster.submitTopology(  AckerWordCountTopology.class.getSimpleName(), config, builder.createTopology() );

        //StormSubmitter.submitTopology(AckerWordCountTopology.class.getSimpleName(), new Config(), builder.createTopology());

    }

}
