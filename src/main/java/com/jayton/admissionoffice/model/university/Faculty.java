package com.jayton.admissionoffice.model.university;

import com.jayton.admissionoffice.model.BaseEntity;

import java.util.Collections;
import java.util.List;

public class Faculty extends BaseEntity {
    private String officePhone;
    private String officeEmail;
    private String officeAddress;
    private University owner;
    private List<Direction> directions;

    public Faculty() {
    }

    public Faculty(String name, String officePhone, String officeEmail, String officeAddress, University owner) {
        super(name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.owner = owner;
    }

    public Faculty(Long id, String name, String officePhone, String officeEmail, String officeAddress, University owner) {
        super(id, name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.owner = owner;
    }

    public Faculty(String name, String officePhone, String officeEmail, String officeAddress, List<Direction> directions) {
        super(name);
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.directions = directions;
    }

    public Faculty(Long id, String name, String officePhone, String officeEmail, String officeAddress, List<Direction> directions) {
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

    public University getOwner() {
        return owner;
    }

    public void setOwner(University owner) {
        this.owner = owner;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }
}
