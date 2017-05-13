package com.xiaoxiaomo.storm.core.domain.report;

public abstract class ChartView {
	public String text;//主标题
	
	/**
	 * 
	 * @param text 主标题
	 * @param subText 副标题
	 */
	public void setTitle(String text){
		this.text = text;
	}
	public String getText() {
		return text;
	}
}
