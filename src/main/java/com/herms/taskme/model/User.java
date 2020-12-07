package com.herms.taskme.model;

import com.herms.taskme.util.Converter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(min = 1, max = 50)
    @Column(name = "GIVEN_NAME")
    @NotNull(message = "Given name is a mandatory field, please fill it")
    private String givenName;
    @Size(min = 1, max = 50)
    @Column(name = "FAMILY_NAME")
    @NotNull(message = "Family Name is a mandatory field, please fill it")
    private String familyName;
    @Size(min = 1, max = 12)
    @Column(name = "CONTACT")
    @NotNull(message = "Contact is a mandatory field, please fill it")
    private String contact;
    @Size(min = 1, max = 120)
    @Column(name = "ADDRESS", nullable = false)
    private String address;
    @NotNull(message = "Username is a mandatory field, please fill it")
    @NotEmpty(message = "Username is a mandatory field, please fill it")
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;
    @NotNull(message = "Password is a mandatory field, please fill it")
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PROFILE_PHOTO", referencedColumnName = "ID")
    private Media profilePhoto;
    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;
    @Column(name = "CREATED")
    @Temporal(TemporalType.DATE)
    private Date createdOn;
    @Column(name = "RATE_SUM")
    private Integer rateSum = 0;
    @Column(name = "NUM_RATES")
    private Integer numberOfRates = 0;

    public User(){
        this.rateSum = 0;
        this.numberOfRates = 0;
    }
    
    //constructor for lazy fetched relations
    public User(Long id) {
    	this.id = id;
    }

    public User(Long id, @Size(min = 1, max = 50) String givenName, @Size(min = 1, max = 50) String familyName, @Size(min = 1, max = 12) String contact, @Size(min = 1, max = 120) String address, @Size(min = 1, max = 20) String username, @Size(min = 1, max = 60) String password) {
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.contact = contact;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    public Double getRateAvg(){
        this.rateSum = this.rateSum == null ? 0 : this.rateSum;
        this.numberOfRates = (this.numberOfRates == null || this.numberOfRates == 0) ? 1 : this.numberOfRates;

        return Double.valueOf(this.rateSum)/Double.valueOf(this.numberOfRates);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles(){
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return roles;
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

    public Integer getRateSum() {
        return rateSum;
    }

    public void setRateSum(Integer rateSum) {
        this.rateSum = rateSum;
    }

    public Integer getNumberOfRates() {
        return numberOfRates;
    }

    public void setNumberOfRates(Integer numberOfRates) {
        this.numberOfRates = numberOfRates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
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
        return "User{" +
                "id=" + id +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
