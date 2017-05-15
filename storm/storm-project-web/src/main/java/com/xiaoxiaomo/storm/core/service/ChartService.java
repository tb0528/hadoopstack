package com.xiaoxiaomo.storm.core.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.xiaoxiaomo.storm.core.biz.BaseService;
import com.xiaoxiaomo.storm.core.domain.report.LineChartView;
import com.xiaoxiaomo.storm.core.entity.ChartConfig;
import com.xiaoxiaomo.storm.core.utils.MyDbUtils;

@Service
public class ChartService extends BaseService{

	/**
	 * 根据传过来的配置信息集合 组装前台树的json字符串
	 * @param chartConfigList
	 * @return
	 */
	public String installJson(List<ChartConfig> chartConfigList) {
		StringBuffer sb = new StringBuffer();
		sb.append("[{ ");
		sb.append("\"id\":0,");
		sb.append("\"text\":\"我的图表\",");
		sb.append("\"children\":[");
		for (int i=0;i<chartConfigList.size();i++) {
			ChartConfig chartConfig = chartConfigList.get(i);
			if(i!=0){
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"id\":"+chartConfig.getId()+",");
			sb.append("\"text\":\""+chartConfig.getText()+"\",");
			sb.append("\"attributes\":{");
			sb.append("\"style\":\""+chartConfig.getStyle()+"\"");
			sb.append("}");
			sb.append("}");
		}
		sb.append("]");
		sb.append("}]");
		return sb.toString();
	}

	public LineChartView getLineChartViewById(Integer id) {
		ChartConfig chartConfig = chartConfigDao.get(id);
		String ysql = chartConfig.getYsql();
		
		LineChartView lineChartView = new LineChartView();
		JSONArray jsonArray = new JSONArray(ysql);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			String text = (String) object.get("title");
			String sql = (String) object.get("sql");
			List<String> list = MyDbUtils.executeSql(sql);
			lineChartView.addData(text, list);
		}
		
		String xsql = chartConfig.getXsql();
		List<String> list = MyDbUtils.executeSql(xsql);
		String xdata = StringUtils.join(list, ",");
		lineChartView.setXaxisdata(xdata);
		
		lineChartView.setTitle(chartConfig.getText());
		lineChartView.setYaxislable(chartConfig.getYlabel());
		return lineChartView;
	}
	
	public boolean deleteChartConfig(String ids) {
		boolean flag = true;
		ids = ids.substring(1, ids.length());
		ids = ids.substring(0, ids.length()-1);
		ids = ids.replaceAll(",", ",");
		JdbcTemplate jdbcTemplate = chartConfigDao.getJdbcTemplate();
		int num = jdbcTemplate.update("DELETE FROM chart_config WHERE ID IN ("+ids+")");
		if(num<=0){
			flag = false;
		}
		return flag;
	}


}
