package com.herms.taskme.dto;

import java.util.Date;

import com.herms.taskme.enums.ApplicationStatus;

public class MsgAndNewApplicationStatus extends DataTransferObject {
	
    private Long taskApplicationId;
    private String newStatusCode;
    private MessageDTO updateStatusMsg;

    
	public MsgAndNewApplicationStatus() {
	}
	
	public Long getTaskApplicationId() {
		return taskApplicationId;
	}
	public void setTaskApplicationId(Long taskApplicationId) {
		this.taskApplicationId = taskApplicationId;
	}
	
	public String getNewStatusCode() {
		return newStatusCode;
	}
	public void setNewStatusCode(String newStatusCode) {
		this.newStatusCode = newStatusCode;
	}
	
	public MessageDTO getUpdateStatusMsg() {
		return updateStatusMsg;
	}
	public void setUpdateStatusMsg(MessageDTO updateStatusMsg) {
		this.updateStatusMsg = updateStatusMsg;
	}
}
