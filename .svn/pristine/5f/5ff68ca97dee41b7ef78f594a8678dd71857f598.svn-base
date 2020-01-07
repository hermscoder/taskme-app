package com.herms.taskme.model;

import java.io.Serializable;
import java.util.Objects;

public class UserPassChange implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String currentPassword;
    private String newPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPassChange)) return false;
        UserPassChange that = (UserPassChange) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getCurrentPassword(), that.getCurrentPassword()) &&
                Objects.equals(getNewPassword(), that.getNewPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getCurrentPassword(), getNewPassword());
    }
}
