package com.herms.taskme.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationDTO extends DataTransferObject {

    private Long id;
    private Boolean hasUnreadMessages;
    private List<MessageDTO> messagesList;
    private Map<Long, UserDTO> userMap;
    private Date createdOn;
    private List<UserDTO> participants;

    public ConversationDTO() {
    	messagesList = new ArrayList<>();
    	userMap = new HashMap<>();
    	participants = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

	public List<MessageDTO> getMessagesList() {
		return messagesList;
	}
	public void setMessagesList(List<MessageDTO> messagesList) {
		this.messagesList = messagesList;
	}
	
	public Map<Long, UserDTO> getUserMap() {
		return userMap;
	}
	public void setUserMap(Map<Long, UserDTO> userMap) {
		this.userMap = userMap;
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

	public List<UserDTO> getParticipants() {
		return participants;
	}
	public void setParticipants(List<UserDTO> participants) {
		this.participants = participants;
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
		ConversationDTO other = (ConversationDTO) obj;
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
