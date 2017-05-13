package com.xiaoxiaomo.storm;

import backtype.storm.Config;
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
 * Created by xiaoxiaomo on 2015/6/6.
 */
public class WordCountTopology {

    public static class WCCountSpout extends BaseRichSpout{

        private SpoutOutputCollector spoutOutputCollector;

        /**
         * 初始化操作
         * @param map
         * @param topologyContext
         * @param spoutOutputCollector
         */
        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            this.spoutOutputCollector = spoutOutputCollector;
        }

        /**
         * 监控文件
         */
        public void nextTuple() {
            /**
             * 文件目录
             * 文件后缀
             * 是否递归
             */
            Collection<File> files = FileUtils.listFiles(new File("/tmp/test"), new String[]{"txt"}, false);
            for (File file : files) {
                try {
                    List<String> lines = FileUtils.readLines(file);
                    for (String line : lines) {
                        spoutOutputCollector.emit( new Values(line)) ;
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
    }

    /**
     * 该Bolt 用于切割
     */
    public static class SplitCountBolt extends BaseRichBolt {

        private OutputCollector outputCollector;

        /**
         * 做初始化操作
         * @param map
         * @param topologyContext
         * @param outputCollector
         */
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.outputCollector = outputCollector ;
        }

        public void execute(Tuple tuple) {
            String line = tuple.getStringByField("line");
            String[] split = line.split("\t");
            for (String s : split) {
                outputCollector.emit( new Values(s) );
            }

        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare( new Fields("word") );
        }
    }


    /**
     * 用于统计
     */
    public static class WordCountBolt extends BaseRichBolt {

        private Map<String , Integer> wordMap = null ;

        /**
         * 做初始化操作
         * @param map
         * @param topologyContext
         * @param outputCollector
         */
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
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

        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        }
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(WCCountSpout.class.getSimpleName(), new WCCountSpout());
        builder.setBolt( SplitCountBolt.class.getSimpleName() , new SplitCountBolt() ).shuffleGrouping(WCCountSpout.class.getSimpleName());
        builder.setBolt( WordCountBolt.class.getSimpleName() , new WordCountBolt() ).shuffleGrouping( SplitCountBolt.class.getSimpleName() );


//        LocalCluster cluster = new LocalCluster();
//        cluster.submitTopology(  WordCountTopology.class.getSimpleName(), new Config(), builder.createTopology() );

        StormSubmitter.submitTopology(WordCountTopology.class.getSimpleName(), new Config(), builder.createTopology());

    }

}
