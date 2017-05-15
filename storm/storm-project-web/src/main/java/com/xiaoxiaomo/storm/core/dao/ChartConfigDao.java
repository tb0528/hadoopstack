package com.xiaoxiaomo.storm.core.dao;

import org.springframework.stereotype.Repository;

import com.xiaoxiaomo.storm.core.entity.ChartConfig;


@Repository
public class ChartConfigDao extends BaseDao<ChartConfig>{
	
	public void save(){
		ChartConfig chartConfig = new ChartConfig();
		super.save(chartConfig);
	}
}
