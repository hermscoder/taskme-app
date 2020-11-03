package com.herms.taskme.enums;

public enum TaskState {

    CREATED("Created","CR"),
    APPLICATIONS_OPEN("Applications Open","AO"),
    APPLICATIONS_CLOSED("Applications Closed", "AC"),
    STARTED("Started", "ST"),
    DONE("Done","DO"),
    CANCELLED("Cancelled", "CN");

    private String code;
    private String description;

    TaskState(String code,String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }
    public TaskState toEnum(String code){
        for(TaskState task : values()){
            if(task.getCode().equals(code)) {
                return task;
            }
        }
        return null;
    }
}
