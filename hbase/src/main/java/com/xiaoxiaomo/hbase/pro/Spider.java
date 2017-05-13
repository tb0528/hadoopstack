package com.xiaoxiaomo.hbase.pro;

import com.xxo.pro.domain.Page;
import com.xxo.pro.download.DownloadJDPageImpl;
import com.xxo.pro.download.Downloadable;
import com.xxo.pro.process.ProcessJDMobileImpl;
import com.xxo.pro.process.Processable;
import com.xxo.pro.repository.QueueRepository;
import com.xxo.pro.repository.RedisRepository;
import com.xxo.pro.repository.Repository;
import com.xxo.pro.store.ConsoleStoreImpl;
import com.xxo.pro.store.Storeable;
import com.xxo.pro.utils.Config;
import com.xxo.pro.utils.SleepUtsils;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网络爬虫
 * Created by xiaoxiaomo on 2015/5/3.
 */
public class Spider {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(ProcessJDMobileImpl.class);

    private Repository repository = new QueueRepository();
    private Downloadable downloadable = new DownloadJDPageImpl();
    private Processable processable ;
    private Storeable storeable = new ConsoleStoreImpl() ;

    public void start() {

        check();
        logger.info("爬虫开始运行......");

        while ( true ) {

            ExecutorService threadPool = Executors.newFixedThreadPool(Config.NTHREAD);
            final String url = repository.poll();

            if( url != null ) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 1. 下载 通过URL下载页面信息
                         */
                        Page page = download(url);

                        /**
                         * 2.  解析 解析的详情/列表页面
                         * 2.1 详情信息： 获取详细信息并通过page返回
                         * 2.2 列表页面： 获取URLs和nextURL通过page返回
                         */
                        Page process = process(page);

                        /**
                         * 3.  存储 只存储详情页面的信息
                         * 3.1 如果是详情页面就存储
                         * 3.2 如果是列表信息放入队列仓库继续下载解析
                         */
                        if (url.startsWith("http://item.jd.com/")) {
                            store(process);
                        } else {
                            storeQueuePool(process);
                        }

                        SleepUtsils.sleep(Config.MILLION_1);
                    }
                });
            } else {
                logger.info("线程休息中.........");
                SleepUtsils.sleep(Config.MILLION_5);
            }
        }

    }

    /**
     * 3.2 列表页面，存储URL到仓库中等待下载解析
     * @param process
     */
    private void storeQueuePool(Page process) {

        List<String> urls = process.getUrls();
        for (String s : urls) {
            logger.debug("--- "+s);
            if( s.startsWith("http://item.jd.com/") ){
                this.repository.add(s);
            }else{
                this.repository.addHeigh(s);
            }
        }
    }

    /**
     * 检查
     */
    private void check() {
        if( repository == null ){
            throw new RuntimeException("repository入口页面为空！");
        }
    }


    /**
     * 入口类
     * @param args
     */
    public static void main(String[] args) {

        Spider spider = new Spider();
        spider.setRepository(new RedisRepository()); //如设置必须在入口页面之前
        spider.setEntryPage("http://list.jd.com/list.html?cat=9987%2C653%2C655&go=0");
        spider.setProcessable(new ProcessJDMobileImpl());
        spider.setStoreable(new ConsoleStoreImpl());
        spider.start();
    }






    public Page download( String url ){
        return downloadable.download(url) ;
    }

    public Page process( Page page ){
        return processable.process(page) ;
    }

    public void store( Page page ){
        this.storeable.store(page);
    }


    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void setDownload( Downloadable downloadable ){
        this.downloadable = downloadable ;
    }

    public void setProcessable(Processable processable) {
        this.processable = processable;
    }

    public void setStoreable(Storeable storeable) {
        this.storeable = storeable;
    }

    /**
     * 设置入口URL
     * @param entryPage
     */
    public void setEntryPage(String entryPage) {
        this.repository.add( entryPage );
    }
}
