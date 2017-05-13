package com.xiaoxiaomo.storm.count;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Values;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 实时统计流量信息
 * Created by xiaoxiaomo on 2015/6/7.
 */
public class ContPvAndUvTopology {

    public static class CountPVUVSpout extends BaseRichSpout{

        private Map conf;
        private TopologyContext context;
        private SpoutOutputCollector collector;
        BufferedReader reader  ;

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            this.conf = conf;
            this.context = context;
            this.collector = collector;
            try {
                reader = new BufferedReader(new FileReader(new File("D:\\test\\access_2013_05_30.log")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        /**
         * 监控文件
         */
        @Override
        public void nextTuple() {
            try {
                String s = reader.readLine();
                String[] split = s.split(" ");
                collector.emit( new Values( split[0] ) ) ;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {


        }



    }

}
