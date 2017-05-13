package com.xiaoxiaomo.storm.bolt;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.xiaoxiaomo.storm.utils.MyDateUtils;
import com.xiaoxiaomo.storm.utils.MyDbUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoxiaomo on 2015/6/20.
 */
public class LogFilterBolt extends BaseRichBolt{

    private OutputCollector collector ;
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        collector = collector ;
    }


    Pattern pattern = Pattern.compile("当前时间:([0-9]+),URL:http://[a-z0-9]+\\.(.*)/.*,响应时间:([0-9]+)");
    ArrayList<String> arrayList = new ArrayList<String>();
    @Override
    public void execute(Tuple input) {
        try {
            /**
             * 如果每一条日志数据，都在mysql中存在一条结果记录，那么如果每天的数据都存到一个表中，后期这个表中的数据会越来越大。
             * 解决方案：
             * 1：分库分表
             * 2：如果数据可以局部汇总的话，可以吧指定时间之内的数据做局部汇总，这样可以减少数据库中存储的条数
             */
            if(input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)){
                //获取jdbc链接操作数据库，下面会用到batch批处理
                Connection connection = MyDbUtils.getConnection();
                Statement statm = connection.createStatement();
                for (String line : arrayList) {
                    String[] split = line.split("--");
                    String formatDate2 = MyDateUtils.formatDate2(new Date(Long.parseLong(split[2])));
                    statm.addBatch("insert into log(topdomain,usetime,time) values('"+split[0]+"','"+split[1]+"','"+formatDate2+"')");
                }
                //执行这一批数据
                statm.executeBatch();
                System.out.println("批量入库数据："+arrayList.size());
                //清空list中的数据，防止数据重复处理、
                arrayList.clear();
                //关闭数据量链接
                connection.close();
            }else{
                String log = new String(input.getBinaryByField("bytes"));
                Matcher matcher = pattern.matcher(log);
                if(matcher.find()){
                    String current_time = matcher.group(1);
                    String top_domain = matcher.group(2);
                    String use_time = matcher.group(3);
                    arrayList.add(top_domain+"--"+use_time+"--"+current_time);
                }
            }
            this.collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
            this.collector.ack(input);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,5) ;
        return map ;
    }
}
