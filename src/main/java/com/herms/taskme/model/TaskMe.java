package com.herms.taskme.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TASK_ME")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TaskMe.findAll", query = "SELECT taskMe FROM TaskMe taskMe")
        , @NamedQuery(name = "TaskMe.findByUser", query = "SELECT taskMe FROM TaskMe taskMe WHERE taskMe.user = :user")})
public class TaskMe implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 1, max = 150)
    @Column(name = "LOCATION", nullable = false)
    private String location;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "WORKSHEET")
    private Worksheet worksheet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_USER")
    private User user;


    public TaskMe(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Worksheet getWorksheet() {
        return worksheet;
    }

    public void setWorksheet(Worksheet worksheet) {
        this.worksheet = worksheet;
    }
    @XmlTransient
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskMe)) return false;
        TaskMe taskMe = (TaskMe) o;
        return Objects.equals(getId(), taskMe.getId()) &&
                Objects.equals(getLocation(), taskMe.getLocation()) &&
                Objects.equals(getWorksheet(), taskMe.getWorksheet()) &&
                Objects.equals(getUser(), taskMe.getUser());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getLocation());
    }
}
