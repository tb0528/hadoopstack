package com.xiaoxiaomo.storm.core.domain.web;

import java.io.Serializable;

import org.hibernate.criterion.DetachedCriteria;

public class PageVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3386577029166675615L;
	private String order;	//可选值：desc、asc
	private String sort;	//排序字段
	private int page; // 页码
	private int rows; // 每页记录条数

	// 查询条件
	// 传统写法 拼接SQL
	private DetachedCriteria detachedCriteria;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public DetachedCriteria getDetachedCriteria() {
		return detachedCriteria;
	}

	public void setDetachedCriteria(DetachedCriteria detachedCriteria) {
		this.detachedCriteria = detachedCriteria;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}