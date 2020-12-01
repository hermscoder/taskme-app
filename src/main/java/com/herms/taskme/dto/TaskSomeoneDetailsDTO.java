package com.herms.taskme.dto;

import com.herms.taskme.enums.FrequencyEnum;
import com.herms.taskme.model.Media;
import com.herms.taskme.model.TaskSomeone;

import java.util.Date;
import java.util.List;

public class TaskSomeoneDetailsDTO extends DataTransferObject {
	
    private Long id;
    private String title;
    private String description;
    private String location;
    private UserDTO user;
    private String frequency;
    private Date startDate;
    private Date endDate;
    private List<Media> mediaList;
    private Date createdOn;
    private Boolean ownTask;
    private List<TaskApplicationForListDTO> taskApplicants;
    private Boolean alreadyApplied;
    private List<UserDTO> participants;
    private Integer state;
    private Integer nextState;
    private Integer previousState;
    private Boolean isSubTask;
    private List<TaskSomeoneDetailsDTO> subTasks;

    public TaskSomeoneDetailsDTO() {
    }

    public TaskSomeoneDetailsDTO(TaskSomeone taskSomeone) {
        id = taskSomeone.getId();
        title = taskSomeone.getTitle();
        description = taskSomeone.getDescription();
        location = taskSomeone.getLocation();
        user = new UserDTO(taskSomeone.getUser());
        mediaList = taskSomeone.getMediaList();
        createdOn = taskSomeone.getCreatedOn();
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }
    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

	public Boolean getOwnTask() {
		return ownTask;
	}
	public void setOwnTask(Boolean ownTask) {
		this.ownTask = ownTask;
	}

	public List<TaskApplicationForListDTO> getTaskApplicants() {
		return taskApplicants;
	}
	public void setTaskApplicants(List<TaskApplicationForListDTO> taskApplicants) {
		this.taskApplicants = taskApplicants;
	}

	public Boolean getAlreadyApplied() {
		return alreadyApplied;
	}
	public void setAlreadyApplied(Boolean alreadyApplied) {
		this.alreadyApplied = alreadyApplied;
	}

    public List<UserDTO> getParticipants() {
        return participants;
    }
    public void setParticipants(List<UserDTO> participants) {
        this.participants = participants;
    }

    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPreviousState() {
        return previousState;
    }
    public void setPreviousState(Integer previousState) {
        this.previousState = previousState;
    }

    public Integer getNextState() {
        return nextState;
    }
    public void setNextState(Integer nextState) {
        this.nextState = nextState;
    }

    public Boolean getIsSubTask() {
        return isSubTask;
    }

    public void setIsSubTask(Boolean subTask) {
        isSubTask = subTask;
    }

    public List<TaskSomeoneDetailsDTO> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<TaskSomeoneDetailsDTO> subTasks) {
        this.subTasks = subTasks;
    }
}
