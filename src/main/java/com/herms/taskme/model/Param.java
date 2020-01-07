package com.herms.taskme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "T_PARAM")
public class Param implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @Size(min = 1, max = 30)
    @Column(name = "PARAM_CODE", nullable = false, unique = true)
    private String code;
    @Size(min = 1, max = 2000)
    @Column(name = "PARAM_VALUE", nullable = false)
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Param)) return false;
        Param param = (Param) o;
        return Objects.equals(getId(), param.getId()) &&
                Objects.equals(getCode(), param.getCode()) &&
                Objects.equals(getValue(), param.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getValue());
    }
}
