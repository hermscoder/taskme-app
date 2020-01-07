package com.herms.taskme.exception;

public class UsernameAlreadyExistException extends Exception {
    private String field;

    public UsernameAlreadyExistException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
