package com.xiaoxiaomo.storm.core.biz;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoxiaomo.storm.core.dao.ChartConfigDao;
import com.xiaoxiaomo.storm.core.dao.DataGridDao;

/**
 * 实现业务层 重复代码复用
 */
public abstract class BaseService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	protected ChartConfigDao chartConfigDao;
	
	@Resource
	protected DataGridDao dataGridDao;
	
}
