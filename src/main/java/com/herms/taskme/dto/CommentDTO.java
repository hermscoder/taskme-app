package com.herms.taskme.dto;

import com.herms.taskme.model.Comment;

import java.util.Date;

public class CommentDTO  extends DataTransferObject {

    private Long id;
    private String content;
    private UserDTO userSender;
    private Date sentTime;
    private Long taskSomeoneId;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.userSender = new UserDTO(comment.getUserSender());
        this.sentTime = comment.getSentTime();
    }

    public CommentDTO(){

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

    public UserDTO getUserSender() {
        return userSender;
    }

    public void setUserSender(UserDTO userSender) {
        this.userSender = userSender;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public Long getTaskSomeoneId() {
        return taskSomeoneId;
    }

    public void setTaskSomeoneId(Long taskSomeoneId) {
        this.taskSomeoneId = taskSomeoneId;
    }
}
