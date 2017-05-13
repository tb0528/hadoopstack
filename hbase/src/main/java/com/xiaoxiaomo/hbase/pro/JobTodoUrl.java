package com.xiaoxiaomo.hbase.pro;

import com.xxo.pro.utils.RedisUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by xiaoxiaomo on 2015/5/4.
 */
public class JobTodoUrl implements Job {

    RedisUtils redisUtils = new RedisUtils() ;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<String> lrange = redisUtils.lrange(RedisUtils.start_url, 0, 1);
        for (String s : lrange) {
            redisUtils.add( RedisUtils.heightkey , s );
        }
        //System.out.println("---------");

    }
}
