package com.herms.taskme.dto;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.herms.taskme.converter.ApplicationStatusConverter;
import com.herms.taskme.enums.ApplicationStatus;
import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;

public class TaskApplicationForListDTO {
	
    private Long id;
    private UserDTO user;
    private TaskSomeoneDetailsDTO taskSomeone;
    private Date createdOn;
    private ApplicationStatus status;
    
	public TaskApplicationForListDTO(TaskApplication addTaskApplication) {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public TaskSomeoneDetailsDTO getTaskSomeone() {
		return taskSomeone;
	}

	public void setTaskSomeone(TaskSomeoneDetailsDTO taskSomeone) {
		this.taskSomeone = taskSomeone;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
}
