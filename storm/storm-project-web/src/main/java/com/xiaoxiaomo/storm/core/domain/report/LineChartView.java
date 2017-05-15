package com.xiaoxiaomo.storm.core.domain.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * 拼装折线图
 * @author xiaoxiaomo
 *
 */
public class LineChartView extends ChartView {
	
	private String xaxisdata;//x轴的值
	private String yaxislable;//y轴的单位
	
	private Map<String, List<String>> dataMap =  new HashMap<String, List<String>>();
	/**
	 * 
	 * @param name 折线名称
	 * @param dataList 折线对应的数据集
	 */
	public void addData(String name, List<String> dataList){
		dataMap.put(name, dataList);
	}
	
	
	public String getYaxislable() {
		return yaxislable;
	}
	/**
	 * 设置y轴的单位 
	 * @param yaxislable
	 */
	public void setYaxislable(String yaxislable) {
		this.yaxislable = yaxislable;
	}
	
	
	public String getXaxisdata() {
		return "'"+xaxisdata.replaceAll(",", "','")+"'";
	}
	/**
	 * 设置x轴的值，需要一个字符串
	 * 例如"星期一,星期二,星期三,星期四"
	 * @param xAxisData
	 */
	public void setXaxisdata(String xaxisdata) {
		this.xaxisdata = xaxisdata;
	}
	/**
	 * 
	 * @return 获取折线名称
	 */
	public String getLegendData(){
		Object[] keyArray = dataMap.keySet().toArray();
		return "'"+StringUtils.join(keyArray, "','")+"'";
	}
	
	
	public List<String[]> getDataList(){
		ArrayList<String[]> result = new ArrayList<String[]>();
		for(Entry<String, List<String>> entry: dataMap.entrySet()){
			String key = entry.getKey();
			String value = StringUtils.join(entry.getValue().toArray(), ",") ;
			
			result.add(new String[]{key, value});
		}
		return result;
	}
}
