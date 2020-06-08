package com.herms.taskme.dto;

import java.util.Date;

import com.herms.taskme.model.Message;
	
public class MessageDTO {

    private Long id;
    private String content;
    private String senderName;
    private Long userSenderId;
    private Date sentTime;
    private Long conversationId;

    public MessageDTO() {
    	
    }

	public MessageDTO(Message message) {
		this.id = message.getId();
		this.content = message.getContent();
		this.senderName = message.getSender().getGivenName() + " " + message.getSender().getFamilyName();
		this.userSenderId = message.getSender().getId();
		this.sentTime = message.getSentTime();
		this.conversationId = message.getConversation().getId();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Long getUserSenderId() {
		return userSenderId;
	}
	public void setUserSenderId(Long userSenderId) {
		this.userSenderId = userSenderId;
	}

	public Date getSentTime() {
		return sentTime;
	}
	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}
	
	public Long getConversationId() {
		return conversationId;
	}
	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	
}	
