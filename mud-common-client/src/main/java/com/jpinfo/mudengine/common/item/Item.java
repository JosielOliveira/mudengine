package com.jpinfo.mudengine.common.item;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jpinfo.mudengine.common.itemClass.ItemClass;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long itemCode;

	private String itemClassCode;
	
	private ItemClass itemClass;
	
	private Integer quantity;

	private Integer curPlaceCode;
	
	private String curWorld;
	
	private Long curOwner;
	
	private Map<String, Integer> attrs;
	
	public Item() {
		this.attrs = new HashMap<String, Integer>();
	}

	public Long getItemCode() {
		return itemCode;
	}

	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemClassCode() {
		return itemClassCode;
	}

	public void setItemClassCode(String itemClassCode) {
		this.itemClassCode = itemClassCode;
	}

	public Integer getCurPlaceCode() {
		return curPlaceCode;
	}

	public void setCurPlaceCode(Integer curPlaceCode) {
		this.curPlaceCode = curPlaceCode;
	}

	public String getCurWorld() {
		return curWorld;
	}

	public void setCurWorld(String curWorld) {
		this.curWorld = curWorld;
	}

	public Map<String, Integer> getAttrs() {
		return attrs;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getCurOwner() {
		return curOwner;
	}

	public void setCurOwner(Long curOwner) {
		this.curOwner = curOwner;
	}

	public ItemClass getItemClass() {
		return itemClass;
	}

	public void setItemClass(ItemClass itemClass) {
		this.itemClass = itemClass;
	}
	
	
	
}