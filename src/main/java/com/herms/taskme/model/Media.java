package com.herms.taskme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "MEDIA")
public class Media implements Serializable{

    private static final long serialVersionUID = 1L;

    public static final String MEDIA_TYPE_IMG = "1";
    public static final String MEDIA_TYPE_VIDEO = "2";
    public static final String MEDIA_TYPE_AUDIO = "3";
    public static final String MEDIA_TYPE_DOC = "4";


//    @GeneratedValue(generator = "SQ_MEDIA")
//    @SequenceGenerator(name = "SQ_MEDIA", sequenceName = "SQ_MEDIA", allocationSize = 1)
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @Size(min = 1, max = 2)
    @Column(name = "TYPE", nullable = false)
    private String type;
    @Size(min = 1, max = 150)
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Size(min = 1, max = 250)
    @Column(name = "URL", nullable = false)
    private String url;
    @Column(name = "PUBLIC_ID")
    private String publicId;
    @Column(name = "CREATED")
    @Temporal(TemporalType.DATE)
    private Date createdOn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_SOMEONE", referencedColumnName = "ID")
    private TaskSomeone taskSomeone;

    public Media(){

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @JsonIgnore
    public TaskSomeone getTaskSomeone() {
        return taskSomeone;
    }

    public void setTaskSomeone(TaskSomeone taskSomeone) {
        this.taskSomeone = taskSomeone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Media)) return false;
        Media media = (Media) o;
        return Objects.equals(getId(), media.getId()) &&
                Objects.equals(getType(), media.getType()) &&
                Objects.equals(getDescription(), media.getDescription()) &&
                Objects.equals(getUrl(), media.getUrl());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getType(), getDescription(), getUrl());
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
