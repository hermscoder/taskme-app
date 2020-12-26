package com.herms.taskme.model;

import com.herms.taskme.converter.FrequencyConverter;
import com.herms.taskme.converter.TaskStateConverter;
import com.herms.taskme.enums.FrequencyEnum;
import com.herms.taskme.enums.TaskState;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "TASK_SOMEONE")
public class TaskSomeone implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @Size(min = 1, max = 70)
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Size(min = 1, max = 1000)
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Size(min = 1, max = 150)
    @Column(name = "LOCATION", nullable = false)
    private String location;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_USER", referencedColumnName = "ID")
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "taskSomeone", cascade = CascadeType.ALL)
    private List<Media> mediaList;

    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Convert(converter = FrequencyConverter.class)
    @Column(name = "FREQUENCY", length = 2)
    private FrequencyEnum frequency;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Convert(converter = TaskStateConverter.class)
    @Column(name = "STATE", nullable = true)
    private TaskState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_TASK", referencedColumnName = "ID")
    private TaskSomeone parentTask;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "TASK_PARTICIPANT",
            joinColumns = { @JoinColumn(name = "TASK_SOMEONE", referencedColumnName = "ID") },
            inverseJoinColumns = { @JoinColumn(name = "T_USER", referencedColumnName = "ID") }
    )
    private List<User> participants;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskSomeone> subTasksList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taskSomeone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    public TaskSomeone(){
        mediaList = new ArrayList<>();
        participants = new ArrayList<>();
        subTasksList = new ArrayList<>();
        commentList = new ArrayList<>();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public List<User> getParticipants() {
		return participants;
	}

	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}

    public FrequencyEnum getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyEnum frequencyEnum) {
        this.frequency = frequencyEnum;
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

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public boolean isPeriodic(){
        return frequency != null;
    }

    public TaskSomeone getParentTask() {
        return parentTask;
    }

    public void setParentTask(TaskSomeone parentTask) {
        this.parentTask = parentTask;
    }

    public List<TaskSomeone> getSubTasksList() {
        return subTasksList;
    }

    public void setSubTasksList(List<TaskSomeone> subTasksList) {
        this.subTasksList = subTasksList;
    }

    public boolean isSubTask(){
        return parentTask != null;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskSomeone)) return false;
        TaskSomeone that = (TaskSomeone) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getLocation(), that.getLocation()) &&
                Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getDescription(), getLocation());
    }

    @Override
    public String toString() {
        return "TaskSomeone{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", local='" + location + '\'' +
                '}';
    }
}
