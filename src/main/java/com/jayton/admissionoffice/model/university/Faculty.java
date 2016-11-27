package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.BaseEntity;

import java.util.Collections;
import java.util.List;

public class Faculty extends BaseEntity {
    private String officePhone;
    private String officeEmail;
    private String officeAddress;
    private Long universityId;
    private List<Direction> directions;

    public Faculty() {
    }

    public Faculty(String name, String officePhone, String officeEmail, String officeAddress, Long owner) {
        super(name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.officeAddress = officeAddress;
        this.universityId = owner;
    }

    public Faculty(Long id, String name, String officePhone, String officeEmail, String officeAddress, Long owner) {
        super(id, name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.officeAddress = officeAddress;
        this.universityId = owner;
    }

    public Faculty(String name, String officePhone, String officeEmail, String officeAddress, List<Direction> directions) {
        super(name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.officeAddress = officeAddress;
        this.directions = directions;
    }

    public Faculty(Long id, String name, String officePhone, String officeEmail, String officeAddress, List<Direction> directions) {
        super(id, name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.officeAddress = officeAddress;
        this.directions = directions;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public void setOfficeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public List<Direction> getDirections() {
        return Collections.unmodifiableList(directions);
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Faculty faculty = (Faculty) o;

        if (officePhone != null ? !officePhone.equals(faculty.officePhone) : faculty.officePhone != null) return false;
        if (officeEmail != null ? !officeEmail.equals(faculty.officeEmail) : faculty.officeEmail != null) return false;
        if (officeAddress != null ? !officeAddress.equals(faculty.officeAddress) : faculty.officeAddress != null)
            return false;
        if (universityId != null ? !universityId.equals(faculty.universityId) : faculty.universityId != null)
            return false;
        return directions != null ? directions.equals(faculty.directions) : faculty.directions == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (officePhone != null ? officePhone.hashCode() : 0);
        result = 31 * result + (officeEmail != null ? officeEmail.hashCode() : 0);
        result = 31 * result + (officeAddress != null ? officeAddress.hashCode() : 0);
        result = 31 * result + (universityId != null ? universityId.hashCode() : 0);
        result = 31 * result + (directions != null ? directions.hashCode() : 0);
        return result;
    }
}
