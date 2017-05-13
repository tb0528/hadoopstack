package com.xiaoxiaomo.storm.other.model;

import java.util.ArrayList;
import java.util.List;

public class AutoForm {
	private String id = "";
	private String action ="";
	private List items = new ArrayList();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List getItems() {
		return items;
	}
	public void setItems(List items) {
		this.items = items;
	}

	public void addItem(QueryItem item) {
		this.items.add(item);
	}
	
	public void addItem(String style, String name, String name_cn, String value) {
	}
}
