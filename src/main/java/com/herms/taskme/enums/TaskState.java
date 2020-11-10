package com.herms.taskme.enums;

public enum TaskState {

    CREATED(10, "Created"),
    APPLICATIONS_OPEN(20, "Applications Open"),
    APPLICATIONS_CLOSED(30, "Applications Closed"),
    STARTED( 40, "Started"),
    DONE(50, "Done"),
    CANCELLED(0, "Cancelled");

    private Integer code;
    private String description;

    TaskState(Integer code,String description){
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }
    public static TaskState toEnum(Integer code){
        for(TaskState task : values()){
            if(task.getCode().equals(code)) {
                return task;
            }
        }
        return null;
    }
}
