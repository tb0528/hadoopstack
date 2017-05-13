package com.xiaoxiaomo.storm.other.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xiaoxiaomo.storm.other.model.QueryItem;

import com.google.common.collect.Lists;

@RequestMapping("/autoquery")
@Controller
public class AutoqueryController{
	public static final String PAGE_INDEX = "/crm/autoquery/table1";

	@RequestMapping(value= {"/", ""})
	public String defaultIndex(ModelMap modelMap) {
		final ArrayList<Object> queryItems = Lists.newArrayList();
		queryItems.add(new QueryItem("name", QueryItem.STYLE_TEXT, "姓名", "name", ""));
		queryItems.add(new QueryItem("sex", QueryItem.STYLE_SELECT, "性别", "sex", Lists.newArrayList(new QueryItem.QueryItemValue("1", "男"), new QueryItem.QueryItemValue("2", "女"))));
		modelMap.put("queryItems", queryItems);
		
		return PAGE_INDEX;
	}

}
