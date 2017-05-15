package com.xiaoxiaomo.storm.other.controller;

import com.xiaoxiaomo.storm.core.entity.CityView;
import com.xiaoxiaomo.storm.core.utils.MyDbUtils2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/map")
public class MapController {
	private static final String PAGE_INDEX = "/map/map";
	
	
	@RequestMapping("")
	private String index(ModelMap modelMap) {
		List<CityView> list = MyDbUtils2.executeSqlObject("select m.* from (select c.city,sum(c.count) as allnum,p.lng,p.lat from city_view c LEFT JOIN `position` p on c.city=p.city GROUP BY c.city   order by allnum desc) as m where m.lng is not null and m.lat is not null");
		int maxnum = 0;
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		for(int i=0;i<list.size();i++){
			CityView cityView = list.get(i);
			if(i>0){
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"name\":\"").append(cityView.getCity()).append("\",");
			Integer count = cityView.getCount();
			sb.append("\"value\":").append(count);
			sb.append("}");
		}
		sb.append("]");
		if(list.size()>0){
			CityView cityView = list.get(0);
			maxnum=cityView.getCount();
		}
		StringBuilder sbtop5 = new StringBuilder();
		int size = 0;
		if(list.size()>=5){
			size=5;
		}else{
			size=list.size();
		}
		sbtop5.append("[");
		for (int i = 0; i <size; i++) {
			CityView cityView = list.get(i);
			if(i>0){
				sbtop5.append(",");
			}
			sbtop5.append("{");
			sbtop5.append("\"name\":\"").append(cityView.getCity()).append("\",");
			Integer count = cityView.getCount();
			sbtop5.append("\"value\":").append(count);
			sbtop5.append("}");
		}
		sbtop5.append("]");
		
		
		modelMap.put("maxnum", maxnum);
		modelMap.put("datastr", sb.toString());
		modelMap.put("top5", sbtop5);
		return PAGE_INDEX;
	}
	
	

}
