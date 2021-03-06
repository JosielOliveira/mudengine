package com.jpinfo.mudengine.common.message;

import java.util.List;

import lombok.Data;

@Data
public class Message {

	private String messageDate;
	
	private Long senderCode;
	
	private String senderName;
	
	private String content;
	
	private List<MessageEntity> changedEntities;
}
