package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.NamedEntity;

public class University extends NamedEntity {
    private String city;
    private String address;

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

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        University that = (University) o;

        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        return address != null ? address.equals(that.address) : that.address == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}