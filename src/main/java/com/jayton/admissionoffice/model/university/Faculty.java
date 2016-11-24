package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.BaseEntity;

import java.util.Collections;
import java.util.List;

public class Faculty extends BaseEntity {
    private String officePhone;
    private String officeEmail;
    private List<Direction> directions;

    public Faculty(String name, String officePhone, String officeEmail) {
        super(name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
    }

    public Faculty(Long id, String name, String officePhone, String officeEmail) {
        super(id, name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
    }

    public Faculty(String name, String officePhone, String officeEmail, List<Direction> directions) {
        super(name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.directions = directions;
    }

    public Faculty(Long id, String name, String officePhone, String officeEmail, List<Direction> directions) {
        super(id, name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
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
}
