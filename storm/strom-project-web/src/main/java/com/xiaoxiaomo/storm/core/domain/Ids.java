package com.xiaoxiaomo.storm.core.domain;

import java.io.Serializable;

public class Ids implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3643669486997743676L;
	private Integer[] id;

	public Integer[] getId() {
		return id;
	}

	public void setId(Integer[] id) {
		this.id = id;
	}
	
}
