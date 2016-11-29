package com.jayton.admissionoffice.model;

public class Subject extends NamedEntity {

    public Subject() {
    }

    public Subject(String name) {
        super(name);
    }

    public Subject(Long id, String name) {
        super(id, name);
    }
}
