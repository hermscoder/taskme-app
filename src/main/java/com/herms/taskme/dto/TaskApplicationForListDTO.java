package com.herms.taskme.dto;

import java.util.Date;

import com.herms.taskme.enums.ApplicationStatus;

public class TaskApplicationForListDTO extends DataTransferObject {
	
    private Long taskApplicationId;
    private UserDTO user;
    private Date createdOn;
    private ApplicationStatus taskApplicationStatus;
    private MessageDTO applyingMessage;

    
	public TaskApplicationForListDTO() {
	}


	public Long getTaskApplicationId() {
		return taskApplicationId;
	}


	public void setTaskApplicationId(Long taskApplicationId) {
		this.taskApplicationId = taskApplicationId;
	}


	public UserDTO getUser() {
		return user;
	}


	public void setUser(UserDTO user) {
		this.user = user;
	}


	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public ApplicationStatus getTaskApplicationStatus() {
		return taskApplicationStatus;
	}


	public void setTaskApplicationStatus(ApplicationStatus taskApplicationStatus) {
		this.taskApplicationStatus = taskApplicationStatus;
	}


	public MessageDTO getApplyingMessage() {
		return applyingMessage;
	}


	public void setApplyingMessage(MessageDTO applyingMessage) {
		this.applyingMessage = applyingMessage;
	}

	
}
