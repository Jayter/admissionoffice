package model;

public class Subject extends BaseEntity {
    public Subject(String name) {
        super(name);
    }

    public Subject(int id, String name) {
        super(id, name);
    }
}
