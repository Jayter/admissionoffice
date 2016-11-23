package model.university;

import model.BaseEntity;

import java.util.Collections;
import java.util.List;

public class University extends BaseEntity {
    private String city;
    private List<Faculty> faculties;

    public University(String name, String city, List<Faculty> faculties) {
        super(name);
        this.city = city;
        this.faculties = faculties;
    }

    public University(int id, String name, String city, List<Faculty> faculties) {
        super(id, name);
        this.city = city;
        this.faculties = faculties;
    }

    public String getCity() {
        return city;
    }

    public List<Faculty> getFaculties() {
        return Collections.unmodifiableList(faculties);
    }
}
