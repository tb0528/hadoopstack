package com.xiaoxiaomo.storm.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xiaoxiaomo.storm.core.biz.BaseService;
import com.xiaoxiaomo.storm.core.domain.Ids;
import com.xiaoxiaomo.storm.core.entity.ChartConfig;

@Service
public class ChartConfigService extends BaseService{

	public void save(ChartConfig chartConfig) {
		chartConfigDao.saveOrUpdate(chartConfig);
	}

	public void delete(ChartConfig chartConfig) {
		chartConfigDao.delete(chartConfig);
	}

	public List<ChartConfig> queryAllChartConfig() {
		return chartConfigDao.findAll();
		
	}


	public void deleteBatch(Ids ids) {
		for (Integer id : ids.getId()) {
			chartConfigDao.delete(id);
		}
	}
	
	public List<ChartConfig> findAll() {
		return chartConfigDao.findAll();
	}

	public ChartConfig getById(Integer id) {
		return chartConfigDao.get(id);
	}


}
