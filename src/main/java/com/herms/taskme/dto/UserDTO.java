package com.herms.taskme.dto;

import com.herms.taskme.model.Media;
import com.herms.taskme.model.User;
import com.herms.taskme.util.Converter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class UserDTO extends DataTransferObject  {

    private Long id;

    private String username;

    private String givenName;

    private String familyName;

    private String contact;

    private String address;

    private Date createdOn;

    private Date birthDate;

    private Media profilePhoto;

    private Integer age;

    public UserDTO() {

    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();
        this.contact = user.getContact();
        this.address = user.getAddress();
        this.createdOn = user.getCreatedOn();
        this.birthDate = user.getBirthDate();
        this.profilePhoto = user.getProfilePhoto();

        Period diff = Period.between(Converter.convertToLocalDateViaSqlDate(user.getBirthDate()), LocalDate.now());
        this.age = diff.getYears();
    }

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

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Media getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Media profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO user = (UserDTO) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getGivenName(), user.getGivenName()) &&
                Objects.equals(getFamilyName(), user.getFamilyName()) &&
                Objects.equals(getContact(), user.getContact()) &&
                Objects.equals(getAddress(), user.getAddress());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getGivenName(), getFamilyName(), getContact(), getAddress());
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", createdOn=" + createdOn +
                ", birthDate=" + birthDate +
                ", age=" + age +
                '}';
    }
}
