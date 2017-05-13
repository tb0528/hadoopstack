package com.xiaoxiaomo.hbase.pro.store;

import com.xxo.pro.domain.Page;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaoxiaomo on 2015/4/29.
 */
public class ConsoleStoreImpl implements Storeable {


    private org.slf4j.Logger logger = LoggerFactory.getLogger(ConsoleStoreImpl.class);
    public void store(Page page) {

        logger.info(" 存储数据 {} -- {}", page.getUrl(), page.getProp().get("price"));
    }
}
