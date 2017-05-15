package com.xiaoxiaomo.storm.other.model;

import java.util.ArrayList;
import java.util.List;

public class QueryItem {
	public static final String STYLE_HIDDEN="hidden";
	public static final String STYLE_TEXT="text";
	public static final String STYLE_SELECT="select";
	
	private String id = "";
	private String style = "";
	private String name = "";
	private String name_cn = "";
	private String value = "";
	private List<QueryItemValue> values = new ArrayList<QueryItemValue>();
	
	public QueryItem(String id, String style, String name_cn, String name, String value) {
		super();
		this.id = id;
		this.style = style;
		this.name = name;
		this.name_cn = name_cn;
		this.value = value;
	}
	
	public QueryItem(String id, String style, String name, String name_cn, List<QueryItemValue> values) {
		super();
		this.id = id;
		this.style = style;
		this.name = name;
		this.name_cn = name_cn;
		this.values = values;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_cn() {
		return name_cn;
	}
	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<QueryItemValue> getValues() {
		return values;
	}
	public void setValues(List<QueryItemValue> values) {
		this.values = values;
	}
	
	public static class QueryItemValue{
		private String name;
		private String value;
		
		public QueryItemValue(String value, String name) {
			super();
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		
	}

}


