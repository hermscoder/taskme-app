package com.herms.taskme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.herms.taskme.converter.ApplicationStatusConverter;
import com.herms.taskme.enums.ApplicationStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "TASK_APPLICATION")
public class TaskApplication implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_USER", referencedColumnName = "ID")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TASK_SOMEONE", referencedColumnName = "ID")
    private TaskSomeone taskSomeone;
    @Column(name = "CREATED_ON", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic
    @Convert(converter = ApplicationStatusConverter.class)
    private ApplicationStatus status;
    @OneToOne
    @JoinColumn(name = "APPLYING_MESSAGE", referencedColumnName = "ID")
    private Message applyingMessage;
    
    
    public TaskApplication(){
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public TaskSomeone getTaskSomeone() {
		return taskSomeone;
	}
	public void setTaskSomeone(TaskSomeone taskSomeone) {
		this.taskSomeone = taskSomeone;
	}

	public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

	public Message getApplyingMessage() {
		return applyingMessage;
	}
	public void setApplyingMessage(Message applyingMessage) {
		this.applyingMessage = applyingMessage;
	}

	public ApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		TaskApplication other = (TaskApplication) obj;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TaskApplication [id=" + id + ", createdOn=" + createdOn + ", status=" + status + "]";
	}
	
}