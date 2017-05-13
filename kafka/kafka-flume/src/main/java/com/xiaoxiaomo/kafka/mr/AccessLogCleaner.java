package com.xiaoxiaomo.kafka.mr;

import com.xxo.utils.RedisUtils;
import com.xxo.utils.UserAgent;
import com.xxo.utils.UserAgentUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaoxiaomo on 2015/5/20.
 */
public class AccessLogCleaner {

    private static Logger logger = Logger.getLogger(AccessLog.class) ;

    public static void main(String[] args) throws Exception {

        if( args != null && args.length >= 2 ){
            logger.error( "args must be ge 2" );
            System.exit(2);
        }


        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "myAccessLogCleaner");

        //设置job
        job.setJarByClass(AccessLogCleaner.class);
        job.setMapperClass(cleanMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        for (int i = 0; i < args.length -1 ; i++) {
            FileInputFormat.addInputPath(job , new Path(args[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(args[args.length - 1]));

    }




    public static class cleanMapper extends Mapper<LongWritable,Text,NullWritable,Text>{

        private Integer appId=0;
        private Integer userId=0;
        private Integer loginType=0;
        private Integer status=0;
        private Long time=0l;
        private Jedis jedis = null ; //redis中读取ip
        SimpleDateFormat format = null ;
        Text text = null ;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //初始化配置
            jedis = RedisUtils.getJedis();
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //1. 解析每行数据，并存入对象中
            if( value != null && value.toString().length() > 0 ) {
                String[] strs = value.toString().split("\t");
                //过滤错误日志
                if (strs.length == 10) {
                    //转换aappID
                    try {
                        appId = Integer.parseInt(strs[0]);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                    try {
                        userId = Integer.parseInt(strs[3]);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                    try {
                        loginType = Integer.parseInt(strs[4]);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                    try {
                        status = Integer.parseInt(strs[6]);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                    try {
                        time = Long.parseLong(strs[9]);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }

                    AccessLog log = new AccessLog(
                            appId, strs[1], strs[2], userId, loginType, strs[5], status, strs[7], strs[8], time);

                    //2. 读取对象，并解析
                    if( log != null ){
                        //解析 ip
                        String s = jedis.get("ip:" + log.getIp());
                        if( s != null && s.split("\t").length == 3 ){
                            String[] split = s.split("\t");
                            log.setProvince( split[1] );
                            log.setCity( split[2] );
                        }

                        //解析 time
                        log.setDateTime( format.format(new Date(log.getTime())) );

                        //解析 user-agent
                        UserAgent userAgent = UserAgentUtil.getUserAgent(log.getUserAgent());
                        if( userAgent != null )
                            log.setIeType( userAgent.getBrowserType() );

                    }

                    text = new Text(log.toString());
                    context.write(NullWritable.get(),  text );
                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            //做一些清理工作
            format = null ;
            logger = null ;
            jedis.close();
        }
    }
}
