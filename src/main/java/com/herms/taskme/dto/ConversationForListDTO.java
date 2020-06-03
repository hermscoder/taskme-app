package com.herms.taskme.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.herms.taskme.model.Message;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ConversationForListDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;
    private Boolean hasUnreadMessages;
    private List<Message> messagesList;
    private List<UserDTO> userList;
    private Date createdOn;

    public ConversationForListDTO() {
    	messagesList = new ArrayList<>();
    	userList = new ArrayList<>();
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public List<Message> getMessagesList() {
		return messagesList;
	}

	public void setMessagesList(List<Message> messagesList) {
		this.messagesList = messagesList;
	}

	public List<UserDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<UserDTO> userList) {
		this.userList = userList;
	}

	
	public Boolean getHasUnreadMessages() {
		return hasUnreadMessages;
	}

	public void setHasUnreadMessages(Boolean hasUnreadMessages) {
		this.hasUnreadMessages = hasUnreadMessages;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((hasUnreadMessages == null) ? 0 : hasUnreadMessages.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((messagesList == null) ? 0 : messagesList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConversationForListDTO other = (ConversationForListDTO) obj;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (hasUnreadMessages == null) {
			if (other.hasUnreadMessages != null)
				return false;
		} else if (!hasUnreadMessages.equals(other.hasUnreadMessages))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (messagesList == null) {
			if (other.messagesList != null)
				return false;
		} else if (!messagesList.equals(other.messagesList))
			return false;
		return true;
	}
}
