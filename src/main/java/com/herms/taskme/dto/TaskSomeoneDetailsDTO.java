package com.herms.taskme.dto;

import com.herms.taskme.model.Media;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TaskSomeoneDetailsDTO {

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

    private Long id;

    private String title;

    private String description;

    private String location;

    private UserDTO user;

    private List<Media> mediaList;

    private Date createdOn;

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
}
