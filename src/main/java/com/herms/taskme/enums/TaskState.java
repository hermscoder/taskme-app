package com.herms.taskme.enums;

public enum TaskState {

    CREATED("CR", "Created"),
    APPLICATIONS_OPEN("AO", "Applications Open"),
    APPLICATIONS_CLOSED("AC", "Applications Closed"),
    STARTED( "ST", "Started"),
    DONE("DO", "Done"),
    CANCELLED("CN", "Cancelled");

    private String code;
    private String description;

    TaskState(String code,String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }
    public static TaskState toEnum(String code){
        for(TaskState task : values()){
            if(task.getCode().equals(code)) {
                return task;
            }
        }
        return null;
    }
}
