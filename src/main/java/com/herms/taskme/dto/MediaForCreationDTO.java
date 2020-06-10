package com.herms.taskme.dto;

import com.herms.taskme.model.TaskSomeone;

import java.io.File;
import java.util.Date;

public class MediaForCreationDTO extends DataTransferObject {
    private String url;
    private byte[] fileByteArray;
    private String description;
    private Date createdOn;
    private String publicId;
    private String type;
    private TaskSomeone taskSomeone;

    public MediaForCreationDTO(){
        this.createdOn = new Date();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getFileByteArray() {
        return fileByteArray;
    }

    public void setFileByteArray(byte[] fileByteArray) {
        this.fileByteArray = fileByteArray;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TaskSomeone getTaskSomeone() {
        return taskSomeone;
    }

    public void setTaskSomeone(TaskSomeone taskSomeone) {
        this.taskSomeone = taskSomeone;
    }
}
