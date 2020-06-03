package com.herms.taskme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

@Entity
@Table(name = "CONVERSATION")
public class Conversation implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @Type(type = "yes_no")
    @Column(name = "HAS_UNREAD_MSG", nullable = false)
    private Boolean hasUnreadMessages;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messagesList;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "USER_CONVERSATION",
            joinColumns = { @JoinColumn(name = "CONVERSATION", referencedColumnName = "ID") },
            inverseJoinColumns = { @JoinColumn(name = "T_USER", referencedColumnName = "ID") }
    )
    private List<User> userList;
    @Column(name = "CREATEDON", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    public Conversation(){
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

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
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
		result = prime * result + ((hasUnreadMessages == null) ? 0 : hasUnreadMessages.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Conversation other = (Conversation) obj;
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
		return true;
	}

	
}
