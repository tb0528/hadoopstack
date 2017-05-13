package com.xiaoxiaomo.storm.pro;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;

import java.util.Map;

/**
 * Created by xiaoxiaomo on 2015/6/8.
 */
public class MySpout extends BaseRichSpout {

    private SpoutOutputCollector collector ;
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector ;
    }

    @Override
    public void nextTuple() {


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
