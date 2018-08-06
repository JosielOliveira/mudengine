package com.jpinfo.mudengine.common.itemclass;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;


/**
 * The persistent class for the mud_item_class database table.
 * 
 */
@Data
public class ItemClass implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String itemClassCode;
	
	private String itemClassName;

	private float size;

	private float weight;
	
	private String description;

	private Map<String, Integer> attrs;

	public ItemClass() {
		this.attrs = new HashMap<>();
	}
}