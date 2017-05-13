package com.xiaoxiaomo.hbase.pro;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 * URL调度：负责定时某个时间点给爬虫添加种子URL
 *
 * Created by xiaoxiaomo on 2015/5/4.
 */
public class URLManager {


    public static void main(String[] args) {
        try {

            //1. 获取一个调度器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //2. 开启调度器
            scheduler.start();

            //3. 调度任务
            scheduler.scheduleJob(
                    new JobDetail(JobTodoUrl.class.getSimpleName(),Scheduler.DEFAULT_GROUP,JobTodoUrl.class),
                    new CronTrigger(JobTodoUrl.class.getSimpleName(),Scheduler.DEFAULT_GROUP,"*/1 * * * * ?"));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
