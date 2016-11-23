package model.university;

import model.BaseEntity;

import java.util.Collections;
import java.util.List;

public class Faculty extends BaseEntity {
    private List<Direction> directions;
    private String officePhone;
    private String officeEmail;

    public Faculty(String name, List<Direction> directions, String officePhone, String officeEmail) {
        super(name);
        this.directions = directions;
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
    }

    public Faculty(int id, String name, List<Direction> directions, String officePhone, String officeEmail) {
        super(id, name);
        this.directions = directions;
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
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
