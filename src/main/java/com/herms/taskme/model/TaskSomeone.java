package com.herms.taskme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

//  TODO ADD STATE
//CREATED -> PUBLISHED

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "TASK_PARTICIPANT",
            joinColumns = { @JoinColumn(name = "TASK_SOMEONE", referencedColumnName = "ID") },
            inverseJoinColumns = { @JoinColumn(name = "T_USER", referencedColumnName = "ID") }
    )
    private List<User> participants;
    
    public TaskSomeone(){
        mediaList = new ArrayList<>();
        participants = new ArrayList<>();
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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
