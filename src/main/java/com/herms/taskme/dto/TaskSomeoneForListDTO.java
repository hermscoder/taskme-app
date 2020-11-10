package com.herms.taskme.dto;

import com.herms.taskme.model.Media;
import com.herms.taskme.model.TaskSomeone;

import java.util.*;

public class TaskSomeoneForListDTO extends DataTransferObject {

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
    private String infoUrl;
    private Integer state;

    public TaskSomeoneForListDTO() {
    }

    public TaskSomeoneForListDTO(TaskSomeone taskSomeone) {
        id = taskSomeone.getId();
        title = taskSomeone.getTitle();
        description = taskSomeone.getDescription();
        location = taskSomeone.getLocation();
        user = new UserDTO(taskSomeone.getUser());
        mediaList = taskSomeone.getMediaList();
        createdOn = taskSomeone.getCreatedOn();
        frequency = Objects.isNull(taskSomeone.getFrequency()) ? null :  taskSomeone.getFrequency().getCode();
        startDate = taskSomeone.getStartDate();
        endDate = taskSomeone.getEndDate();
        state = Objects.isNull(taskSomeone.getState()) ? null :  taskSomeone.getState().getCode();
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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
}
