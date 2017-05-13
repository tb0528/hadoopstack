package com.xiaoxiaomo.storm.core.domain.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;


public class PieChartView extends ChartView {
	
	private Map<String, String> dataMap =  new HashMap<String, String>();
	/**
	 * 
	 * @param name 饼图各个块的名称
	 * @param dataList 每个块对应的数据
	 */
	public void addData(String name, String data){
		dataMap.put(name, data);
	}
	/**
	 * 
	 * @return 获取饼图每个块的名称
	 */
	public String getLegendData(){
		Object[] keyArray = dataMap.keySet().toArray();
		return "'"+StringUtils.join(keyArray, "','")+"'";
	}

	public List<String[]> getDataList(){
		ArrayList<String[]> result = new ArrayList<String[]>();
		for(Entry<String, String> entry: dataMap.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			
			result.add(new String[]{key, value});
		}
		return result;
	}
	
}
