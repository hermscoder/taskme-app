package com.herms.taskme.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "COMMENT")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @Size(min = 1, max = 150)
    @Column(name = "CONTENT", nullable = false)
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_SENDER", referencedColumnName = "ID")
    private User userSender;
    @Column(name = "SENT_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentTime;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "TASK_SOMEONE", referencedColumnName = "ID")
    private TaskSomeone taskSomeone;



    public Comment(){
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public TaskSomeone getTaskSomeone() {
        return taskSomeone;
    }

    public void setTaskSomeone(TaskSomeone taskSomeone) {
        this.taskSomeone = taskSomeone;
    }
}
