package com.herms.taskme.dto;

import java.io.Serializable;

public class ChangeStateDTO implements Serializable {
    private static final long serialVersionUID = 12512L;

    String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
