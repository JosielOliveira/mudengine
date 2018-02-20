package com.jpinfo.mudengine.action.dto;

import java.util.ArrayList;
import java.util.List;

import com.jpinfo.mudengine.action.interfaces.ActionTarget;
import com.jpinfo.mudengine.action.utils.ActionMessage;
import com.jpinfo.mudengine.common.action.Action.EnumTargetType;
import com.jpinfo.mudengine.common.item.Item;

public class ItemComposite implements ActionTarget {
	
	private Item innerItem;
	
	private List<ActionMessage> messages;

	public ItemComposite(Item innerItem) {
		this.innerItem = innerItem;
		
		this.messages = new ArrayList<ActionMessage>();
	}


	@Override
	public void addMessage(Long senderCode, String messageKey, String... args) {
		this.messages.add(new ActionMessage(senderCode, this.getItem().getItemCode(), EnumTargetType.ITEM, messageKey, args));
	}
	
	@Override
	public List<ActionMessage> getMessages() {
		
		return this.messages;
	}

	@Override
	public void describeIt(ActionTarget target) {
		
		// TODO Add more information about the item
		target.addMessage(null, "{str:HEREIS}", this.getItem().getItemClass().getDescription());
	}

	public Item getItem() {
		return innerItem;
	}

	public void setItem(Item innerItem) {
		this.innerItem = innerItem;
	}
}
