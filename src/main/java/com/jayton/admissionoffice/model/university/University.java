package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.BaseEntity;

import java.util.Collections;
import java.util.List;

public class University extends BaseEntity {
    private String city;
    private List<Faculty> faculties;

    public University() {
    }

    public University(String name, String city) {
        super(name);
        this.city = city;
    }

    public University(Long id, String name, String city) {
        super(id, name);
        this.city = city;
    }

    public University(String name, String city, List<Faculty> faculties) {
        super(name);
        this.city = city;
        this.faculties = faculties;
    }

    public University(Long id, String name, String city, List<Faculty> faculties) {
        super(id, name);
        this.city = city;
        this.faculties = faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public List<Faculty> getFaculties() {
        return Collections.unmodifiableList(faculties);
    }
}
