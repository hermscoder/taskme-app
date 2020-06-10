package com.herms.taskme.dto;

import java.util.Date;

import com.herms.taskme.enums.ApplicationStatus;

public class TaskApplicationDetailsDTO extends DataTransferObject {
	
	private Long id;
    private UserDTO user;
    private TaskSomeoneDetailsDTO taskSomeone;
    private Date createdOn;
    private ApplicationStatus status;
    private MessageDTO applyingMessage;
    
    public TaskApplicationDetailsDTO() {
    	
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

	public MessageDTO getApplyingMessage() {
		return applyingMessage;
	}
	public void setApplyingMessage(MessageDTO applyingMessage) {
		this.applyingMessage = applyingMessage;
	}
}
