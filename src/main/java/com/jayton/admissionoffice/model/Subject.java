package com.jayton.admissionoffice.model;

public class Subject extends BaseEntity {
    public Subject(String name) {
        super(name);
    }

    public Subject(Long id, String name) {
        super(id, name);
    }
}
