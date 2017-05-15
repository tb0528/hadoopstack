package com.xiaoxiaomo.storm.core.domain.report;


public class GaugeChartView extends ChartView {
	private String seriesname;//仪表盘点击指针时候显示的名称
	private String[] datastr = new String[2];
	/**
	 * 
	 * @param name 仪表中监控的数据名称
	 * @param data 数据的百分比 不需要%号 取值范围0-100
	 */
	public void setDatastr(String name, String data){
		datastr[0] = name;
		datastr[1] = data;
	}
	public String[] getDatastr() {
		return datastr;
	}
	public String getSeriesname() {
		return seriesname;
	}
	public void setSeriesname(String seriesname) {
		this.seriesname = seriesname;
	}
	
	
}
