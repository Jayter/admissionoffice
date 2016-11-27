package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.BaseEntity;

import java.util.Collections;
import java.util.List;

public class University extends BaseEntity {
    private String city;
    private String address;
    private List<Faculty> faculties;

    public University() {
    }

    public University(String name, String city, String address) {
        super(name);
        this.city = city;
        this.address = address;
    }

    public University(Long id, String name, String city, String address) {
        super(id, name);
        this.city = city;
        this.address = address;
    }

    public University(String name, String city, String address, List<Faculty> faculties) {
        super(name);
        this.city = city;
        this.faculties = faculties;
        this.address = address;
    }

    public University(Long id, String name, String city, String address, List<Faculty> faculties) {
        super(id, name);
        this.city = city;
        this.faculties = faculties;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        University that = (University) o;

        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        return faculties != null ? faculties.equals(that.faculties) : that.faculties == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (faculties != null ? faculties.hashCode() : 0);
        return result;
    }
}
